package com.runcom.wgcwgc.register;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.R;
import com.runcom.wgcwgc.judge.Judge;
import com.runcom.wgcwgc.md5.MD5;
import com.runcom.wgcwgc.web.SSLSocketFactoryEx;

public class Register extends Activity
{

	private EditText editText_login , editText_password ,
	        editText_password_resure , editText_email , editText_chkcode;
	private String login , password , password_resure , email , chkcode;
	private TextView textView_hint;
	// private Button button_submit;

	String app;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_backups);

		ActionBar actionbar = getActionBar();
		// 显示返回箭头默认是不显示的
		actionbar.setDisplayHomeAsUpEnabled(false);
		// 显示左侧的返回箭头，并且返回箭头和title一起设置，返回箭头才能显示
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayUseLogoEnabled(true);
		// 显示标题
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setTitle(" 注册 ");

		editText_login = (EditText) findViewById(R.id.register_login);
		editText_password = (EditText) findViewById(R.id.register_password);
		editText_password_resure = (EditText) findViewById(R.id.register_password_resure);
		editText_email = (EditText) findViewById(R.id.register_email);
		editText_chkcode = (EditText) findViewById(R.id.register_chkcode);
		textView_hint = (TextView) findViewById(R.id.register_hint);
		// button_chkcode = (Button) findViewById(R.id.register_chkcodeButton);
		// button_submit = (Button) findViewById(R.id.register_submitButton);

		// button_submit.setEnabled(false);
	}

	public void submitButtonClick(View view )
	{
		login = editText_login.getText().toString();
		password = editText_password.getText().toString();
		password_resure = editText_password_resure.getText().toString();
		email = editText_email.getText().toString();
		chkcode = editText_chkcode.getText().toString();

		judgeLoginAndPassword(login ,password ,password_resure);

		// String contents = "请仔细核对信息\n账号：\n\t\t\t\t" + login +
		// "\n密码：\n\t\t\t\t" + password + "\n邮箱：\n\t\t\t\t" + email;
		// // System.out.println(contents);
		// Toast.makeText(this ,contents ,Toast.LENGTH_LONG).show();

		// if(true)
		// {
		// // System.out.println("恭喜您：注册成功！！！");
		// Toast.makeText(this ,"恭喜您： " + "\n\t\t\t\t注册成功！！！"
		// ,Toast.LENGTH_LONG).show();
		// finish();
		// }

	}

	private void judgeLoginAndPassword(String login , String password , String password_resure )
	{
		if(password.equals(password_resure) && judgeIsDigitAndLetter(login))
		{
			if(password.length() > 3 && password.length() < 11)
			{
				serverJudge_loginAndPassword(login ,password);
			}
			else
			{
				textView_hint.setText("密码不符合规定！！！");
			}
		}
		else
		{
			textView_hint.setText("密码不一致或者用户名含有非法字符！！！");
		}

	}

	@SuppressLint("DefaultLocale")
	private boolean judgeIsDigitAndLetter(String login )
	{
		char [] loginArray = login.trim().toLowerCase().toCharArray();
		char temp;
		int length = loginArray.length;

		for(int i = 0 ; i < length ; i ++ )
		{
			temp = loginArray[i];
			if(temp >= 'a' && temp <= 'z' || temp >= '0' && temp <= '9')
			{
			}
			else
			{
				return false;
			}

		}
		return true;
	}

	private void serverJudge_loginAndPassword(String login , String password )
	{
		GetThread_submit getThread_submit = new GetThread_submit(login , password);
		getThread_submit.start();
	}

	class GetThread_submit extends Thread
	{

		String account;
		String password;

		public GetThread_submit(String account , String password)
		{
			this.account = account;
			this.password = password;
		}

		@SuppressLint("DefaultLocale")
		@Override
		public void run()
		{
			PackageManager packageManager = Register.this.getPackageManager();
			PackageInfo packageInfo = null;
			try
			{
				packageInfo = packageManager.getPackageInfo(Register.this.getPackageName() ,0);
				int labelRes = packageInfo.applicationInfo.labelRes;
				app = Register.this.getResources().getString(labelRes);
			}
			catch(NameNotFoundException e)
			{
				Log.d("LOG" ,"serverJudge_package exception:" + e.toString());
			}
			String build = "57";
			String dev = android.provider.Settings.Secure.getString(Register.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
			String lang = Locale.getDefault().getLanguage();
			int market = 2;
			String os = Build.VERSION.RELEASE;
			int term = 0;
			String ver = packageInfo.versionName;

			// 用HttpClient发送请求，分为五步
			// HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();// getNewHttpClient

			dev = android.provider.Settings.Secure.getString(Register.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);

			String signValu = "tuoyouvpn" + app + build + chkcode + dev + email + lang + account + market + os + password + term + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			// Log.d("LOG" ,signValu);
			String url = "https://a.redvpn.cn:8443/interface/newUser.php?login=" + account + "&pass=" + password + "&chkcode=" + chkcode + "&email=" + email + "&app=" + app + "&build=" + build + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			// 第二步：创建代表请求的对象,参数是访问的服务器地址
			Log.d("LOG" ,url);
			HttpGet httpGet = new HttpGet(url);
			try
			{
				// 第三步：执行请求，获取服务器发还的相应对象
				HttpResponse response = httpClient.execute(httpGet);
				// System.out.println("test00");
				// 第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
				if(response.getStatusLine().getStatusCode() == 200)
				{
					// 第五步：从相应对象当中取出数据，放到entity当中
					// System.out.println("test01");
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String json_result = reader.readLine();
					JSONObject jsonObject = new JSONObject(json_result);

					// String result = jsonObject.getString("result");
					Long result = jsonObject.getLong("result");
					final String mesg = jsonObject.getString("mesg");
					// String uid = jsonObject.getString("uid");
					// String expire = jsonObject.getString("expire");
					// String freetime = jsonObject.getString("freetime");
					// String flow = jsonObject.getString("flow");
					// String score = jsonObject.getString("score");
					// String coupon = jsonObject.getString("coupon");
					// String type = jsonObject.getString("type");
					// String email = jsonObject.getString("email");
					// String session = jsonObject.getString("session");

					Log.d("LOG" ,json_result);
					// Log.d("LOG" ,result.toString());
					if(result == 0)
					{

						new Thread()
						{
							public void run()
							{
								// 这儿是耗时操作，完成之后更新UI；
								runOnUiThread(new Runnable()
								{

									@Override
									public void run()
									{
										// 更新UI
										textView_hint.setText(" ");
										String contents = "请仔细核对信息\n账号：\n\t\t\t\t" + login + "\n密码：\n\t\t\t\t" + password + "\n邮箱：\n\t\t\t\t" + email;
										// System.out.println(contents);
										Toast.makeText(Register.this ,contents ,Toast.LENGTH_LONG).show();

										Toast.makeText(Register.this ,"恭喜您： " + "\n\t\t\t\t注册成功！！！" ,Toast.LENGTH_LONG).show();
										finish();
									}

								});
							}
						}.start();
						// Toast.makeText(Register.this ,mesg
						// ,Toast.LENGTH_LONG).show();
						// Log.d("LOG" ,mesg);
						// System.out.println(mesg);
						// button_submit.setEnabled(true);

					}
					else
					{
						new Thread()
						{
							public void run()
							{
								// 这儿是耗时操作，完成之后更新UI；
								runOnUiThread(new Runnable()
								{

									@Override
									public void run()
									{
										// 更新UI
										textView_hint.setText(mesg);

									}

								});
							}
						}.start();
					}
					// System.out.println(result);
					// Toast.makeText(MainActivity.this ,result
					// ,Toast.LENGTH_LONG).show();
					// Intent intent = new Intent();
					// intent.setClass(Register.this ,Business.class);
					// intent.putExtra("login" ,account);
					// intent.putExtra("pass" ,password);
					//
					// intent.putExtra("result" ,result);
					// intent.putExtra("mesg" ,mesg);
					// intent.putExtra("uid" ,uid);
					// intent.putExtra("expire" ,expire);
					// intent.putExtra("freetime" ,freetime);
					// intent.putExtra("flow" ,flow);
					// intent.putExtra("score" ,score);
					// intent.putExtra("coupon" ,coupon);
					// intent.putExtra("type" ,type);
					// intent.putExtra("email" ,email);
					// intent.putExtra("session" ,session);

					// startActivity(intent);
					// Toast.makeText(Register.this ,"恭喜您\n\t注册成功！！！"
					// ,Toast.LENGTH_LONG).show();
					// finish();
					// System.out.println("test02");

				}
			}
			catch(Exception e)
			{
				Log.d("LOG" ,"GetThread_submit_http_bug");
			}

		}

	}

	public void chkcodeButtonClick(View view )
	{
		// button_submit.setEnabled(true);
		email = editText_email.getText().toString();
		if(localJudge_email(email))
		{
			serverJudge_email(email);
		}
		else
		{
			textView_hint.setText("邮箱格式不正确！！！");
		}

	}

	private void serverJudge_email(String email )
	{
		GetThread_email getThread_email = new GetThread_email(email);
		getThread_email.start();
	}

	private boolean localJudge_email(String email )
	{
		return new Judge().isEmail(email);
	}

	class GetThread_email extends Thread
	{
		String email;

		public GetThread_email(String email)
		{
			this.email = email;
		}

		@SuppressLint("DefaultLocale")
		@Override
		public void run()
		{
			PackageManager packageManager = Register.this.getPackageManager();
			PackageInfo packageInfo = null;
			try
			{
				packageInfo = packageManager.getPackageInfo(Register.this.getPackageName() ,0);
				int labelRes = packageInfo.applicationInfo.labelRes;
				app = Register.this.getResources().getString(labelRes);
			}
			catch(NameNotFoundException e)
			{
				Log.d("LOG" ,"GetyThread_email_serverJudge_package exception:" + e.toString());
			}
			String build = "57";
			String dev = android.provider.Settings.Secure.getString(Register.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
			String lang = Locale.getDefault().getLanguage();
			int market = 2;
			String os = Build.VERSION.RELEASE;
			int term = 0;
			String ver = packageInfo.versionName;

			String signValu = "tuoyouvpn" + app + build + dev + email + lang + market + os + term + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			String url = "https://a.redvpn.cn:8443/interface/emailCheck.php?email=" + email + "&app=" + app + "&build=" + build + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			Log.d("LOG" ,"GetThread_email:" + url);

			HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();
			HttpGet httpGet = new HttpGet(url);
			try
			{
				// Log.d("LOG" ,"test00");

				// 第三步：执行请求，获取服务器发还的相应对象
				HttpResponse response = httpClient.execute(httpGet);
				// 第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
				if(response.getStatusLine().getStatusCode() == 200)
				{
					// 第五步：从相应对象当中取出数据，放到entity当中
					// Log.d("LOG" ,"test01");
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String json_result = reader.readLine();
					JSONObject jsonObject = new JSONObject(json_result);

					// String result = jsonObject.getString("result");
					Long result = jsonObject.getLong("result");
					final String mesg = jsonObject.getString("mesg");
					// String uid = jsonObject.getString("uid");
					// String expire = jsonObject.getString("expire");
					// String freetime = jsonObject.getString("freetime");
					// String flow = jsonObject.getString("flow");
					// String score = jsonObject.getString("score");
					// String coupon = jsonObject.getString("coupon");
					// String type = jsonObject.getString("type");
					// String session = jsonObject.getString("session");
					Log.d("LOG" ,json_result);
					if(result == 0)
					{

						new Thread()
						{
							public void run()
							{
								// 这儿是耗时操作，完成之后更新UI；
								runOnUiThread(new Runnable()
								{

									@Override
									public void run()
									{
										// 更新UI
										textView_hint.setText("我们已经向您的邮箱 " + email + " 发送了一条验证码，请注意查收！！！");

									}

								});
							}
						}.start();
						// Toast.makeText(Register.this ,mesg
						// ,Toast.LENGTH_LONG).show();
						// Log.d("LOG" ,mesg);
						// System.out.println(mesg);
						// button_submit.setEnabled(true);

					}
					else
					{
						// System.out.println(mesg);
						new Thread()
						{
							public void run()
							{
								// 这儿是耗时操作，完成之后更新UI；
								runOnUiThread(new Runnable()
								{

									@Override
									public void run()
									{
										// 更新UI
										// imageView.setImageBitmap(bitmap);
										textView_hint.setText(mesg);
									}

								});
							}
						}.start();
						// Toast.makeText(Register.this ,mesg
						// ,Toast.LENGTH_LONG).show();
						// Log.d("LOG" ,mesg);
						// button_submit.setEnabled(false);
					}
					// Log.d("LOG" ,"result:" + result + "\nmesg:" + mesg);
					// finish();
					// System.out.println("test02");
				}
			}
			catch(Exception e)
			{
				// Log.d("LOG" ,"serverJudge_http exception:" + e.toString());
				e.printStackTrace();
			}
		}

	}

	@Override
	public boolean onMenuOpened(int featureId , Menu menu )
	{

		if(featureId == Window.FEATURE_ACTION_BAR && menu != null)
		{
			if(menu.getClass().getSimpleName().equals("MenuBuilder"))
			{
				try
				{
					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible" ,Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu ,true);
				}
				catch(Exception e)
				{
					Toast.makeText(this ,"overflow 展开显示item图标异常" ,Toast.LENGTH_LONG).show();
				}
			}
		}

		return super.onMenuOpened(featureId ,menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu )
	{
		// getMenuInflater().inflate(R.menu.main ,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item )
	{

		switch(item.getItemId())
		{
			case android.R.id.home:
				// actionbar的左侧图标的点击事件处理
				// finish();
				Toast.makeText(this ,"返回上一级" ,Toast.LENGTH_LONG).show();
				onBackPressed();
				break;

			case R.id.action_settings:
				Intent upIntent = NavUtils.getParentActivityIntent(this);
				if(NavUtils.shouldUpRecreateTask(this ,upIntent))
				{
					TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
				}
				else
				{
					upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					NavUtils.navigateUpTo(this ,upIntent);
				}
				Toast.makeText(this ,"返回首页" ,Toast.LENGTH_LONG).show();
				return true;

		}
		return super.onOptionsItemSelected(item);
	}

}
