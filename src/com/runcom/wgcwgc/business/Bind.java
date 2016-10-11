package com.runcom.wgcwgc.business;

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
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.runcom.wgcwgc.R;
import com.runcom.wgcwgc.md5.MD5;
import com.runcom.wgcwgc.web.SSLSocketFactoryEx;

public class Bind extends Activity
{
	private EditText editText_login , editText_password , editText_oldPassword ,
	        editText_email;
	// private Button button_submit;

	private String login , password , email , oldPassword;
	String app;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bind);

		ActionBar actionbar = getActionBar();
		// ��ʾ���ؼ�ͷĬ���ǲ���ʾ��
		actionbar.setDisplayHomeAsUpEnabled(false);
		// ��ʾ���ķ��ؼ�ͷ�����ҷ��ؼ�ͷ��titleһ�����ã����ؼ�ͷ������ʾ
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayUseLogoEnabled(true);
		// ��ʾ����
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setTitle(" �� ");
		
		editText_login = (EditText) findViewById(R.id.bind_login);
		editText_password = (EditText) findViewById(R.id.bind_password);
		editText_oldPassword = (EditText) findViewById(R.id.bind_oldPassword);
		editText_email = (EditText) findViewById(R.id.bind_email);

	}

	public void submit(View view )
	{
		login = editText_login.getText().toString();
		password = editText_password.getText().toString();
		email = editText_email.getText().toString();
		oldPassword = editText_oldPassword.getText().toString();

		GetThread_submit getThread_submit = new GetThread_submit(login , password , oldPassword , email);
		getThread_submit.start();

	}

	class GetThread_submit extends Thread
	{

		String account;
		String password;
		String oldPassword;
		String email;

		public GetThread_submit(String account , String password , String oldPassword , String email)
		{
			this.account = account;
			this.password = password;
			this.email = email;
			this.oldPassword = oldPassword;
		}

		@SuppressLint("DefaultLocale")
		@Override
		public void run()
		{
			PackageManager packageManager = Bind.this.getPackageManager();
			PackageInfo packageInfo = null;
			try
			{
				packageInfo = packageManager.getPackageInfo(Bind.this.getPackageName() ,0);
				int labelRes = packageInfo.applicationInfo.labelRes;
				app = Bind.this.getResources().getString(labelRes);
			}
			catch(NameNotFoundException e)
			{
				Log.d("LOG" ,"serverJudge_package exception:" + e.toString());
			}
			String build = "57";
			String dev = android.provider.Settings.Secure.getString(Bind.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
			String lang = Locale.getDefault().getLanguage();
			int market = 2;
			String os = Build.VERSION.RELEASE;
			int term = 0;
			String ver = packageInfo.versionName;

			// ��HttpClient�������󣬷�Ϊ�岽
			// HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();// getNewHttpClient

			// https://a.redvpn.cn:8443/interface/
			// *************************************************************************************************************************************

			// term = 0;
			// os = Build.VERSION.RELEASE;
			dev = android.provider.Settings.Secure.getString(Bind.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);

			String signValu = "tuoyouvpn" + app + build + dev + email + lang + account + market + oldPassword + os + password + term + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			// Log.d("LOG" ,signValu);
			String url = "https://a.redvpn.cn:8443/interface/dobind.php?login=" + account + "&pass=" + password + "&email=" + email + "&app=" + app + "&oldpass=" + oldPassword + "&build=" + build + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			// �ڶ�����������������Ķ���,�����Ƿ��ʵķ�������ַ
			// url = toURLEncoded(url);
			// System.out.println(url);
			Log.d("LOG" ,"Bind:\n" + url);
			// System.out.println(new
			// MD5().md5("runcom8888123@abc.comxyz9.3.26666").toUpperCase());
			HttpGet httpGet = new HttpGet(url);
			try
			{
				// ��������ִ�����󣬻�ȡ��������������Ӧ����
				HttpResponse response = httpClient.execute(httpGet);
				// System.out.println("test00");
				// ���Ĳ��������Ӧ��״̬�Ƿ����������״̬���ֵ��200��ʾ����
				if(response.getStatusLine().getStatusCode() == 200)
				{
					// ���岽������Ӧ������ȡ�����ݣ��ŵ�entity����
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

					Log.d("LOG" ,"Bind_result:\n" + json_result);
					// Log.d("LOG" ,result.toString());
					if(result == 0)
					{

						new Thread()
						{
							public void run()
							{
								// ����Ǻ�ʱ���������֮�����UI��
								runOnUiThread(new Runnable()
								{

									@Override
									public void run()
									{
										// ����UI
										// textView_hint.setText(" ");
										// String contents =
										// "����ϸ�˶���Ϣ\n�˺ţ�\n\t\t\t\t" + login +
										// "\n���룺\n\t\t\t\t" + password +
										// "\n���䣺\n\t\t\t\t" + email;
										// System.out.println(contents);
										// Toast.makeText(Bind.this ,contents
										// ,Toast.LENGTH_LONG).show();

										Toast.makeText(Bind.this ,"��ϲ���� " + "\n\t\t\t\t�޸ĳɹ�������" ,Toast.LENGTH_LONG).show();
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
								// ����Ǻ�ʱ���������֮�����UI��
								runOnUiThread(new Runnable()
								{

									@Override
									public void run()
									{
										// ����UI
										// textView_hint.setText(mesg);
										Toast.makeText(Bind.this ,mesg ,Toast.LENGTH_LONG).show();
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
					// Toast.makeText(Register.this ,"��ϲ��\n\tע��ɹ�������"
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
					Toast.makeText(this ,"overflow չ����ʾitemͼ���쳣" ,Toast.LENGTH_LONG).show();
				}
			}
		}

		return super.onMenuOpened(featureId ,menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu )
	{
//		getMenuInflater().inflate(R.menu.main ,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item )
	{

		switch(item.getItemId())
		{
			case android.R.id.home:
				// actionbar�����ͼ��ĵ���¼�����
				// finish();
				Toast.makeText(this ,"������һ��" ,Toast.LENGTH_LONG).show();
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
				Toast.makeText(this ,"������ҳ" ,Toast.LENGTH_LONG).show();
				return true;

		}
		return super.onOptionsItemSelected(item);
	}

}
