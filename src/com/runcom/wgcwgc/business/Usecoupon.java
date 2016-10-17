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

public class Usecoupon extends Activity
{
	private EditText editText;

	@SuppressWarnings("unused")
	private String login , uid , coupon;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business_usecoupon);

		ActionBar actionbar = getActionBar();
		// 显示返回箭头默认是不显示的
		actionbar.setDisplayHomeAsUpEnabled(false);
		// 显示左侧的返回箭头，并且返回箭头和title一起设置，返回箭头才能显示
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayUseLogoEnabled(true);
		// 显示标题
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setTitle(" 领取奖励 ");

	}

	public void getCoupon(View view )
	{
		editText = (EditText) findViewById(R.id.business_usecoupon_editText);
		coupon = editText.getText().toString();

		Intent intent = getIntent();
		login = intent.getStringExtra("name");
		uid = intent.getStringExtra("uid");
		useCoupon(uid ,coupon);

	}

	private void useCoupon(String uid , String coupon )
	{
		GetThread_useCoupon getThread_useCoupon = new GetThread_useCoupon(uid , coupon);
		getThread_useCoupon.start();
	}

	class GetThread_useCoupon extends Thread
	{
		String uid;
		String coupon;

		public GetThread_useCoupon()
		{

		}

		public GetThread_useCoupon(String uid , String coupon)
		{
			this.uid = uid;
			this.coupon = coupon;
		}

		@SuppressLint("DefaultLocale")
		@Override
		public void run()
		{
			String app = null;
			PackageManager packageManager = Usecoupon.this.getPackageManager();
			PackageInfo packageInfo = null;
			try
			{
				packageInfo = packageManager.getPackageInfo(Usecoupon.this.getPackageName() ,0);
				int labelRes = packageInfo.applicationInfo.labelRes;
				app = Usecoupon.this.getResources().getString(labelRes);
			}
			catch(NameNotFoundException e)
			{
				Log.d("LOG" ,"serverJudge_package exception:" + e.toString());
			}
			String build = "57";
			String dev = android.provider.Settings.Secure.getString(Usecoupon.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
			String lang = Locale.getDefault().getLanguage();
			int market = 2;
			String os = Build.VERSION.RELEASE;
			int term = 0;
			String ver = packageInfo.versionName;

			// 用HttpClient发送请求，分为五步
			// HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();// getNewHttpClient

			dev = android.provider.Settings.Secure.getString(Usecoupon.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);

			String signValu = "tuoyouvpn" + app + build + coupon + dev + lang + market + os + term + uid + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			// Log.d("LOG" ,signValu);
			String url = "https://a.redvpn.cn:8443/interface/usecoupon.php?app=" + app + "&uid=" + uid + "&coupon=" + coupon + "&build=" + build + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			// 第二步：创建代表请求的对象,参数是访问的服务器地址
			// url = toURLEncoded(url);
			// System.out.println(url);
			Log.d("LOG" ,"Usecoupon_usecoupon_url:\n" + url);
			// System.out.println(new
			// MD5().md5("runcom8888123@abc.comxyz9.3.26666").toUpperCase());
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

					Log.d("LOG" ,"Usecoupon_Usecoupon_reponse:\n" + json_result);
					// Log.d("LOG" ,result.toString());
					if(result == 0)
					{

						new Thread()
						{
							public void run()
							{
								runOnUiThread(new Runnable()
								{
									@Override
									public void run()
									{
										// 更新UI
										Toast.makeText(Usecoupon.this ,mesg ,Toast.LENGTH_LONG).show();
									}

								});
							}
						}.start();

					}
					else
					{
						new Thread()
						{
							public void run()
							{
								runOnUiThread(new Runnable()
								{

									@Override
									public void run()
									{
										Toast.makeText(Usecoupon.this ,mesg ,Toast.LENGTH_LONG).show();
									}

								});
							}
						}.start();
					}

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
