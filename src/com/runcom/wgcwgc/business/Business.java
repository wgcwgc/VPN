package com.runcom.wgcwgc.business;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.runcom.wgcwgc.R;
import com.runcom.wgcwgc.main.MainActivity;
import com.runcom.wgcwgc.md5.MD5;
import com.runcom.wgcwgc.position.Coordinate;
import com.runcom.wgcwgc.position.Coordinate_1;
import com.runcom.wgcwgc.web.SSLSocketFactoryEx;

public class Business extends Activity
{

	public Svrlist svrlist;
	private Intent intent;
	// private String contents;
	// private TextView textView;
	String app;

	private String login , mesg , uid , expire , freetime , flow , score ,
	        coupon , type , email , session;
	private Long result;

	// private Button button_getsvrlist , button_getproducts;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business);

		ActionBar actionbar = getActionBar();
		// ��ʾ���ؼ�ͷĬ���ǲ���ʾ��
		actionbar.setDisplayHomeAsUpEnabled(false);
		// ��ʾ���ķ��ؼ�ͷ�����ҷ��ؼ�ͷ��titleһ�����ã����ؼ�ͷ������ʾ
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayUseLogoEnabled(true);
		// ��ʾ����
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);

		new GetThread_cheakNewVersion().start();

		Toast.makeText(this ,"��¼�ɹ�������" ,Toast.LENGTH_LONG).show();

		intent = getIntent();

		login = intent.getStringExtra("login");
		actionbar.setTitle(login);
		// actionbar.set
		result = intent.getLongExtra("result" , -1);
		mesg = intent.getStringExtra("mesg");
		uid = intent.getStringExtra("uid");
		expire = intent.getStringExtra("expire");
		actionbar.setSubtitle("����ʱ�䣺" + expire);
		freetime = intent.getStringExtra("freetime");
		flow = intent.getStringExtra("flow");
		score = intent.getStringExtra("score");
		coupon = intent.getStringExtra("coupon");
		type = intent.getStringExtra("type");
		email = intent.getStringExtra("email");
		session = intent.getStringExtra("session");

		// contents = "uid:\t" + uid + "\nexpire:\t" + expire + "\nfreetime:\t"
		// + freetime + "\nflow:\t" + flow + "\nscore:\t" + score +
		// "\ncoupon:\t" + coupon + "\ntype:\t" + type + "\nemaile:\t" + email +
		// "\nsession:\t" + session + "\n";

		// contents = "login:\t" + intent.getStringExtra("login") + "\n";
		// contents += "reslut:\t" + intent.getLongExtra("result" , -1) + "\n";
		// contents += "mesg:\t" + intent.getStringExtra("mesg") + "\n";
		// contents += "uid:\t" + intent.getStringExtra("uid") + "\n";
		// contents += "expire:\t" + intent.getStringExtra("expire") + "\n";
		// contents += "freetime:\t" + intent.getStringExtra("freetime") + "\n";
		// contents += "flow:\t" + intent.getStringExtra("flow") + "\n";
		// contents += "score:\t" + intent.getStringExtra("score") + "\n";
		// contents += "coupon:\t" + intent.getStringExtra("coupon") + "\n";
		// contents += "type:\t" + intent.getStringExtra("type") + "\n";
		// contents += "email:\t" + intent.getStringExtra("email") + "\n";
		// contents += "session:\t" + intent.getStringExtra("session") + "\n";

		// getproducts(uid ,type);
		// getconfig(uid);
		// getsvrlist(uid , type);
		// textView = (TextView) findViewById(R.id.textView_business);
		// textView.setText(contents);

	}

	public void getsvrlist(View view )
	{
		Intent intent = new Intent();
		intent.putExtra("uid" ,uid);
		intent.putExtra("type" ,type);
		intent.setClass(Business.this ,Getsvrlist.class);
		startActivity(intent);
		// getsvrlist(uid ,type);

	}

	public void getproducts(View view )
	{

		Intent intent = new Intent();
		intent.putExtra("uid" ,uid);
		intent.putExtra("type" ,type);
		intent.setClass(Business.this ,Getproducts.class);
		startActivity(intent);
	}

	@SuppressWarnings("unused")
	private void getconfig(String uid )
	{
		GetThread_getconfig getThread_getconfig = new GetThread_getconfig(uid);
		getThread_getconfig.start();
	}

	class GetThread_getconfig extends Thread
	{

		String uid;

		public GetThread_getconfig()
		{

		}

		public GetThread_getconfig(String uid)
		{
			this.uid = uid;

		}

		@SuppressLint("DefaultLocale")
		@Override
		public void run()
		{
			// Log.d("LOG" ,"getsvrlist beginning,,,");
			PackageManager packageManager = Business.this.getPackageManager();
			PackageInfo packageInfo = null;
			try
			{
				packageInfo = packageManager.getPackageInfo(Business.this.getPackageName() ,0);
				int labelRes = packageInfo.applicationInfo.labelRes;
				app = Business.this.getResources().getString(labelRes);
			}
			catch(NameNotFoundException e)
			{
				Log.d("LOG" ,"Business_getconfig_serverJudge_package_exception:\n" + e.toString());
			}
			String build = "57";
			String dev = android.provider.Settings.Secure.getString(Business.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
			String lang = Locale.getDefault().getLanguage();
			if(lang.contains("zh"))
			{
				lang = "zh-Hans";
			}
			int market = 2;
			String os = Build.VERSION.RELEASE;
			int term = 0;
			String ver = packageInfo.versionName;

			// ��HttpClient�������󣬷�Ϊ�岽
			// HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();// getNewHttpClient

			dev = android.provider.Settings.Secure.getString(Business.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);

			String signValu = "tuoyouvpn" + app + build + dev + lang + market + os + term + uid + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			// Log.d("LOG" ,signValu);
			String url = "https://a.redvpn.cn:8443/interface/getconfig.php?app=" + app + "&build=" + build + "&uid=" + uid + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			// �ڶ�����������������Ķ���,�����Ƿ��ʵķ�������ַ
			Log.d("LOG" ,"Business_getconfig_url:\n" + url);
			HttpGet httpGet = new HttpGet(url);
			try
			{
				// Log.d("LOG" , "start1,,,");
				// ��������ִ�����󣬻�ȡ��������������Ӧ����
				HttpResponse response = httpClient.execute(httpGet);
				// System.out.println("test00");
				// ���Ĳ��������Ӧ��״̬�Ƿ����������״̬���ֵ��200��ʾ����
				if(response.getStatusLine().getStatusCode() == 200)
				{
					// Log.d("LOG" , "start2,,,");
					// ���岽������Ӧ������ȡ�����ݣ��ŵ�entity����
					// System.out.println("test01");
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					// String json_result = reader.readLine();
					// JSONObject jsonObject = new JSONObject(json_result);

					String line = "";
					String returnLine = "";
					while((line = reader.readLine()) != null)
					{
						// Log.d("LOG" , "start3,,,");
						returnLine += line;
						System.out.println("*" + line + "*\n");
					}
					// Log.d("LOG" , "start4,,,");
					JSONObject jsonObject = new JSONObject(returnLine);
					// String result = jsonObject.getString("result");
					Long result = jsonObject.getLong("result");
					final String mesg = jsonObject.getString("mesg");
					JSONObject jsonObject_config = jsonObject.getJSONObject("config");

					// JSONArray config_list_Array =
					// jsonObject.getJSONArray("config");
					// int leng = config_list_Array.length();

					// Log.d("LOG" , "result:" + result + "\nmesg:" + mesg +
					// "\njsonObject_config:" + jsonObject_config);
					if(result == 0)
					{
						Log.d("LOG" ,"Business_getconfig_result:\n" + "result:" + result + "\nmesg:" + mesg + "\njsonObject_config:" + jsonObject_config);
						// List < String > config_list = new ArrayList < String
						// >();
						// for(int i = 0 ; i < config_list_Array.length() ; i ++
						// )
						// {
						//
						// config_list.add(config_list_Array.getString(i));
						// Log.d("LOG" ,config_list.get(i));
						// }

						// JSONObject jsonObject2 = new
						// JSONObject(products_list.get(0).toString());
						// Log.d("LOG" , "id:" + jsonObject2.getString("id"));

						// Log.d("LOG" ,"Business_getconfig_response:\n" +
						// "result:" + result + "\nmesg:" + mesg + "\nconfig:" +
						// config_list_Array + "\nleng:" + leng);

					}

				}
			}
			catch(Exception e)
			{
				// Log.d("LOG"
				// ,"Business_getconfig_GetThread_submit_http_bug:\n" +
				// e.toString());
				e.printStackTrace();
			}

		}

	}

	@SuppressWarnings("unused")
	private void getproducts(String uid , String type )
	{
		GetThread_getproducts getThread_getproducts = new GetThread_getproducts(uid , type);
		getThread_getproducts.start();
	}

	class GetThread_getproducts extends Thread
	{

		String uid;
		String type;

		public GetThread_getproducts()
		{

		}

		public GetThread_getproducts(String uid , String type)
		{
			this.uid = uid;
			this.type = type;

		}

		@SuppressLint("DefaultLocale")
		@Override
		public void run()
		{
			// Log.d("LOG" ,"getsvrlist beginning,,,");
			PackageManager packageManager = Business.this.getPackageManager();
			PackageInfo packageInfo = null;
			try
			{
				packageInfo = packageManager.getPackageInfo(Business.this.getPackageName() ,0);
				int labelRes = packageInfo.applicationInfo.labelRes;
				app = Business.this.getResources().getString(labelRes);
			}
			catch(NameNotFoundException e)
			{
				Log.d("LOG" ,"Business_getproducts_serverJudge_package_exception:\n" + e.toString());
			}
			String build = "57";
			String dev = android.provider.Settings.Secure.getString(Business.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
			String lang = Locale.getDefault().getLanguage();
			if(lang.contains("zh"))
			{
				lang = "zh-Hans";
			}
			int market = 2;
			String os = Build.VERSION.RELEASE;
			int term = 0;
			String ver = packageInfo.versionName;

			// ��HttpClient�������󣬷�Ϊ�岽
			// HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();// getNewHttpClient

			dev = android.provider.Settings.Secure.getString(Business.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);

			String signValu = "tuoyouvpn" + app + build + dev + lang + market + os + term + uid + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			// Log.d("LOG" ,signValu);
			String url = "https://a.redvpn.cn:8443/interface/getproducts.php?app=" + app + "&build=" + build + "&uid=" + uid + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			// �ڶ�����������������Ķ���,�����Ƿ��ʵķ�������ַ
			Log.d("LOG" ,"Business_getproducts_url:\n" + url);
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
					// String json_result = reader.readLine();
					// JSONObject jsonObject = new JSONObject(json_result);

					String line = "";
					String returnLine = "";
					while((line = reader.readLine()) != null)
					{
						returnLine += line;
						System.out.println("*" + line + "*\n");
					}
					JSONObject jsonObject = new JSONObject(returnLine);
					// String result = jsonObject.getString("result");
					Long result = jsonObject.getLong("result");
					final String mesg = jsonObject.getString("mesg");
					JSONArray products_list_Array = jsonObject.getJSONArray("products");
					int leng = products_list_Array.length();

					if(result == 0)
					{
						List < String > products_list = new ArrayList < String >();
						for(int i = 0 ; i < products_list_Array.length() ; i ++ )
						{

							products_list.add(products_list_Array.getString(i));
							Log.d("LOG" ,products_list.get(i));
						}

						// JSONObject jsonObject2 = new
						// JSONObject(products_list.get(0).toString());
						// Log.d("LOG" , "id:" + jsonObject2.getString("id"));

						Log.d("LOG" ,"Business_getproducts_response:\n" + "result:" + result + "\nmesg:" + mesg + "\nproducts:" + products_list_Array + "\nleng:" + leng);

					}

				}
			}
			catch(Exception e)
			{
				Log.d("LOG" ,"Business_getproducts_GetThread_submit_http_bug:\n" + e.toString());
				e.printStackTrace();
			}

		}

	}

	@SuppressWarnings("unused")
	private void getsvrlist(String uid , String type )
	{
		GetThread_getsvrlist getThread_getsvrlist = new GetThread_getsvrlist(uid , type);
		getThread_getsvrlist.start();

	}

	class GetThread_getsvrlist extends Thread
	{

		String uid;
		String type;

		public GetThread_getsvrlist()
		{

		}

		public GetThread_getsvrlist(String uid , String type)
		{
			this.uid = uid;
			this.type = type;

		}

		@SuppressLint("DefaultLocale")
		@Override
		public void run()
		{
			// Log.d("LOG" ,"getsvrlist beginning,,,");
			PackageManager packageManager = Business.this.getPackageManager();
			PackageInfo packageInfo = null;
			try
			{
				packageInfo = packageManager.getPackageInfo(Business.this.getPackageName() ,0);
				int labelRes = packageInfo.applicationInfo.labelRes;
				app = Business.this.getResources().getString(labelRes);
			}
			catch(NameNotFoundException e)
			{
				Log.d("LOG" ,"Business_getsvrlist_serverJudge_package_exception:\n" + e.toString());
			}
			String build = "57";
			String dev = android.provider.Settings.Secure.getString(Business.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
			String lang = Locale.getDefault().getLanguage();
			if(lang.contains("zh"))
			{
				lang = "zh-Hans";
			}
			int market = 2;
			String os = Build.VERSION.RELEASE;
			int term = 0;
			String ver = packageInfo.versionName;

			// ��HttpClient�������󣬷�Ϊ�岽
			// HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();// getNewHttpClient

			dev = android.provider.Settings.Secure.getString(Business.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);

			String signValu = "tuoyouvpn" + app + build + dev + lang + market + os + term + type + uid + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			// Log.d("LOG" ,signValu);
			String url = "https://a.redvpn.cn:8443/interface/getsvrlist.php?app=" + app + "&build=" + build + "&uid=" + uid + "&type=" + type + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			// �ڶ�����������������Ķ���,�����Ƿ��ʵķ�������ַ
			Log.d("LOG" ,"Business_getsvrlist_url:\n" + url);
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
					// String json_result = reader.readLine();
					// JSONObject jsonObject = new JSONObject(json_result);

					String line = "";
					String returnLine = "";
					while((line = reader.readLine()) != null)
					{
						returnLine += line;
						System.out.println("*" + line + "*\n");
					}
					JSONObject jsonObject = new JSONObject(returnLine);
					// String result = jsonObject.getString("result");
					Long result = jsonObject.getLong("result");
					final String mesg = jsonObject.getString("mesg");
					JSONArray svrlistArray = jsonObject.getJSONArray("svrlist");
					int leng = svrlistArray.length();

					if(result == 0)
					{
						svrlist = new Svrlist();

						List < String > server_list = new ArrayList < String >();
						for(int i = 0 ; i < leng ; i ++ )
						{
							Log.d("LOG" ,"mesg:" + mesg + "\n");
							server_list.add(svrlistArray.getString(i));
							JSONObject jsonObject_content = new JSONObject(server_list.get(i).toString());
							svrlist.setId(jsonObject_content.getString("id"));
							// svrlist.setName(jsonObject_content.getString("name"));
							// svrlist.setAddr(jsonObject_content.getString("addr"));
							// svrlist.setType(jsonObject_content.getString("type"));
							// svrlist.setProtocol(jsonObject_content.getString("protocol"));
							// svrlist.setArea(jsonObject_content.getString("area"));
							// svrlist.setPrior(jsonObject_content.getString("prior"));

							// Log.d("LOGG" ,server_list.get(i));
						}

						// Log.d("LOG" ,"Business_getsvrlist_response:\n" +
						// "result:" + result + "\nmesg:" + mesg +
						// "\nsvrlistArray:" + svrlistArray + "\nleng:" + leng);

						// Log.d("LOG" ,"id:" + jsonObject2.getString("id"));

					}

				}
			}
			catch(Exception e)
			{
				Log.d("LOG" ,"Business_getsvrlist_GetThread_submit_http_bug:\n" + e.toString());
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
					Toast.makeText(this ,"overflow չ����ʾitemͼ���쳣" ,Toast.LENGTH_LONG).show();
				}
			}
		}

		return super.onMenuOpened(featureId ,menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu )
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.business ,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item )
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id)
		{
			case R.id.business_personInfromation:
				getPersonInfromation();
				break;

			case R.id.business_bind:
				bind();
				break;

			case R.id.business_coupon:
				coupon();
				break;

			case R.id.business_bonus:
				bonus();
				break;

			// case R.id.business_accept_bonus:
			// acceptBonus();
			// break;

			case R.id.business_opinion:
				opinion();
				break;

			case R.id.business_cheakNewVersion:
				cheakNewVersion();
				break;

			case R.id.business_aboutUs:
				aboutUs();
				break;

			case R.id.business_exit:
				exit();
				break;

			case R.id.business_settings:
				setting();
				break;

			case android.R.id.home:
				// actionbar�����ͼ��ĵ���¼�����
				// finish();
				Toast.makeText(this ,"������һ��" ,Toast.LENGTH_LONG).show();
				onBackPressed();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("unused")
	private void acceptBonus()
	{
		Intent intent = new Intent();
		intent.putExtra("login" ,login);
		intent.putExtra("uid" ,uid);
		intent.setClass(Business.this ,AcceptBonus.class);
		startActivity(intent);
	}

	private void bonus()
	{
		Intent intent = new Intent();
		intent.putExtra("login" ,login);
		intent.putExtra("uid" ,uid);
		intent.putExtra("type" ,type);
		intent.setClass(Business.this ,GetBonusList.class);
		startActivity(intent);
	}

	private void coupon()
	{
		Intent intent = new Intent();
		intent.putExtra("login" ,login);
		intent.putExtra("uid" ,uid);
		intent.putExtra("coupon" ,coupon);
		intent.setClass(Business.this ,Usecoupon.class);
		startActivity(intent);

	}

	private void exit()
	{
		Business.this.finish();
		intent = new Intent();
		intent.setClass(this ,MainActivity.class);
		startActivity(intent);
	}

	private void getPersonInfromation()
	{
		intent = new Intent();
		intent.putExtra("login" ,login);
		intent.putExtra("result" ,result);
		intent.putExtra("mesg" ,mesg);
		intent.putExtra("uid" ,uid);
		intent.putExtra("expire" ,expire);
		intent.putExtra("freetime" ,freetime);
		intent.putExtra("flow" ,flow);
		intent.putExtra("score" ,score);
		intent.putExtra("coupon" ,coupon);
		intent.putExtra("type" ,type);
		intent.putExtra("email" ,email);
		intent.putExtra("session" ,session);

		intent.setClass(this ,PersonInformation.class);
		startActivity(intent);
	}

	private void bind()
	{
		Intent intent = new Intent();
		intent.setClass(Business.this ,Bind.class);
		startActivity(intent);
	}

	private void opinion()
	{
		// TODO Auto-generated method stub
		Toast.makeText(this ,"This is opinion" ,Toast.LENGTH_LONG).show();
	}

	@SuppressLint("DefaultLocale")
	private void cheakNewVersion()
	{

		GetThread_cheakNewVersion getThread_cheakNewVersion = new GetThread_cheakNewVersion();
		getThread_cheakNewVersion.start();

		// PostThread postThread = new PostThread();
		// postThread.start();

	}

	public String toURLEncoded(String paramString )
	{
		if(paramString == null || paramString.equals(""))
		{
			// LogD("toURLEncoded error:"+paramString);
			Log.d("LOG" ,"toURLEncoded error:" + paramString);
			return "";
		}

		try
		{
			String str = new String(paramString.getBytes() , "UTF-8");
			str = URLEncoder.encode(str ,"UTF-8");
			Log.d("LOG" ,"toURLEncoded:" + str);
			return str;
		}
		catch(Exception localException)
		{
			// LogE("toURLEncoded error:"+paramString, localException);
			Log.d("LOG" ,"toURLEncoded error:" + paramString);
		}

		return "";
	}

	// ���̣߳�ʹ��POST����������������û��������������
	class PostThread extends Thread
	{

		String username;
		String password;

		public PostThread()
		{

		}

		public PostThread(String username , String password)
		{
			this.username = username;
			this.password = password;
		}

		@SuppressLint("DefaultLocale")
		@Override
		public void run()
		{

			PackageManager packageManager = Business.this.getPackageManager();
			PackageInfo packageInfo = null;
			try
			{
				packageInfo = packageManager.getPackageInfo(Business.this.getPackageName() ,0);
				int labelRes = packageInfo.applicationInfo.labelRes;
				app = Business.this.getResources().getString(labelRes);
			}
			catch(NameNotFoundException e)
			{
				Log.d("LOG" ,"serverJudge_package exception:" + e.toString());
			}
			String build = "57";
			String dev = android.provider.Settings.Secure.getString(Business.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
			String lang = Locale.getDefault().getLanguage();
			int market = 2;
			String os = Build.VERSION.RELEASE;
			int term = 0;
			String ver = packageInfo.versionName;
			// ver = "0." + ver;

			// ��HttpClient�������󣬷�Ϊ�岽
			// HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();
			// getNewHttpClient

			dev = android.provider.Settings.Secure.getString(Business.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);

			String signValu = "tuoyouvpn" + app + build + dev + lang + market + os + term + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			String url = "https://a.redvpn.cn:8443/interface/getver.php?app=" + app + "&build=" + build + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			// �ڶ�����������������Ķ���,�����Ƿ��ʵķ�������ַ
			Log.d("LOG" ,url);

			// HttpClient httpClient = new DefaultHttpClient();
			// String url = "http://172.16.1.31:8080/test.jsp";
			// �ڶ���������ʹ��POST�������������
			HttpPost httpPost = new HttpPost(url);
			// NameValuePair���������һ����Ҫ�����������ļ�ֵ��
			NameValuePair pair1 = new BasicNameValuePair("app" , app);
			NameValuePair pair2 = new BasicNameValuePair("build" , build);
			NameValuePair pair3 = new BasicNameValuePair("dev" , dev);
			NameValuePair pair4 = new BasicNameValuePair("lang" , lang);
			NameValuePair pair5 = new BasicNameValuePair("market" , market + "");
			NameValuePair pair6 = new BasicNameValuePair("os" , os);
			NameValuePair pair7 = new BasicNameValuePair("term" , term + "");
			NameValuePair pair8 = new BasicNameValuePair("ver" , ver);
			// ��׼���õļ�ֵ�Զ��������һ��List����
			ArrayList < NameValuePair > pairs = new ArrayList < NameValuePair >();
			pairs.add(pair1);
			pairs.add(pair2);
			pairs.add(pair3);
			pairs.add(pair4);
			pairs.add(pair5);
			pairs.add(pair6);
			pairs.add(pair7);
			pairs.add(pair8);
			try
			{
				// ��������������Ķ���ע�⣬�������壩
				HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
				// ����������������������
				httpPost.setEntity(requestEntity);
				// ִ���������
				try
				{
					// ��������ִ��������󣬻�ȡ��������������Ӧ����
					HttpResponse response = httpClient.execute(httpPost);
					// ���Ĳ��������Ӧ��״̬�Ƿ����������״̬���ֵ��200��ʾ����
					if(200 == response.getStatusLine().getStatusCode())
					{
						// ���岽������Ӧ������ȡ�����ݣ��ŵ�entity����
						HttpEntity entity = response.getEntity();
						BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
						String json_result = reader.readLine();

						JSONObject jsonObject = new JSONObject(json_result);
						Log.d("LOG" ,jsonObject + "***");

						// Long result = jsonObject.getLong("result");
						// final String mesg = jsonObject.getString("mesg");
						// String min = jsonObject.getString("min");
						// String latest = jsonObject.getString("latest");
						// final String install =
						// jsonObject.getString("install");
						// String content = jsonObject.getString("content");

						Log.d("LOG" ,json_result);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Log.d("LOG" ,"POST:�����������쳣01������" + httpClient.execute(httpPost).getStatusLine().getStatusCode());
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Log.d("LOG" ,"POST:�����������쳣02������");
			}

		}
	}

	class GetThread_cheakNewVersion extends Thread
	{

		public GetThread_cheakNewVersion()
		{

		}

		@SuppressLint("DefaultLocale")
		@Override
		public void run()
		{
			PackageManager packageManager = Business.this.getPackageManager();
			PackageInfo packageInfo = null;
			try
			{
				packageInfo = packageManager.getPackageInfo(Business.this.getPackageName() ,0);
				int labelRes = packageInfo.applicationInfo.labelRes;
				app = Business.this.getResources().getString(labelRes);
			}
			catch(NameNotFoundException e)
			{
				Log.d("LOG" ,"Business_getver_serverJudge_package_exception_response:\n" + e.toString());
			}
			String build = "57";
			String dev = android.provider.Settings.Secure.getString(Business.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
			String lang = Locale.getDefault().getLanguage();
			if(lang.contains("zh"))
			{
				lang = "zh-Hans";
			}
			int market = 2;
			String os = Build.VERSION.RELEASE;
			int term = 0;
			String ver = packageInfo.versionName;

			// ��HttpClient�������󣬷�Ϊ�岽
			// HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();// getNewHttpClient

			dev = android.provider.Settings.Secure.getString(Business.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);

			String signValu = "tuoyouvpn" + app + build + dev + lang + market + os + term + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			String url = "https://a.redvpn.cn:8443/interface/getver.php?app=" + app + "&build=" + build + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			// �ڶ�����������������Ķ���,�����Ƿ��ʵķ�������ַ
			Log.d("LOG" ,"Business_getver_url:\n" + url);
			HttpGet httpGet = new HttpGet(url);
			try
			{
				// ��������ִ�����󣬻�ȡ��������������Ӧ����
				HttpResponse response = httpClient.execute(httpGet);
				// ���Ĳ��������Ӧ��״̬�Ƿ����������״̬���ֵ��200��ʾ����
				if(response.getStatusLine().getStatusCode() == 200)
				{
					// Log.d("LOG" ,"test00");
					// ���岽������Ӧ������ȡ�����ݣ��ŵ�entity����
					HttpEntity entity = response.getEntity();
					// Log.d("LOG" ,"test01");

					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					// Log.d("LOG" ,"test02");
					// String jString = reader.toString();
					// Log.d("LOG" ,jString);
					// String json_result = reader.readLine();
					String line = "";
					String returnLine = "";
					while((line = reader.readLine()) != null)
					{
						returnLine += line;
						System.out.println("*" + line + "*\n");
					}

					// JSONObject jsonObject = new JSONObject(json_result);
					JSONObject jsonObject = new JSONObject(returnLine);

					Log.d("LOG" ,"Business_getver_response:\n" + returnLine);

					Long result = jsonObject.getLong("result");
					final String mesg = jsonObject.getString("mesg");
					String min = jsonObject.getString("min");
					String latest = jsonObject.getString("latest");
					final String install = jsonObject.getString("install");
					final String content = jsonObject.getString("content");

					String [] ver_string = ver.split("\\.");
					String [] min_string = min.split("\\.");
					String [] latest_string = latest.split("\\.");

					// Log.d("LOG" ,"Business_getver_response_version:\nmin:" +
					// min + "\nlatest:" + latest);

					Long ver_first = Long.valueOf(ver_string[0]);
					Long ver_second = Long.valueOf(ver_string[1]);

					Long min_first = Long.valueOf(min_string[0]);// + 1;
					Long min_second = Long.valueOf(min_string[1]);

					Long latest_first = Long.valueOf(latest_string[0]);
					Long latest_second = Long.valueOf(latest_string[1]);

					if(result == 0)
					{

						if(ver_first < min_first || (ver_first == min_first && ver_second < min_second))
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
											// ǿ�Ƹ���
											Builder alertDialog = new AlertDialog.Builder(Business.this);
											alertDialog.setTitle("ǿ�Ƹ���");
											alertDialog.setMessage(content);
											// alertDialog.setCancelable(false);
											alertDialog.setCancelable(true);
											alertDialog.setPositiveButton("ȷ��" ,new DialogInterface.OnClickListener()
											{
												@Override
												public void onClick(DialogInterface dialog , int which )
												{
													Intent intent_cheackNewVersion = new Intent(Intent.ACTION_VIEW);
													intent_cheackNewVersion.setData(Uri.parse(install));
													startActivity(intent_cheackNewVersion);
													finish();
												}
											}).show();
										}

									});
								}
							}.start();
						}
						else
							if((ver_first >= latest_first && ver_second >= latest_second))
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
												Toast.makeText(Business.this ,"�Ѹ��������°�" ,Toast.LENGTH_LONG).show();
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
												AlertDialog.Builder builder = new AlertDialog.Builder(Business.this);
												builder.setTitle("����˵��");
												builder.setMessage(content);

												builder.setCancelable(false);
												// ȷ����ť
												builder.setPositiveButton("ȷ��" ,new DialogInterface.OnClickListener()
												{
													@Override
													public void onClick(DialogInterface dialog , int which )
													{
														Intent intent_cheackNewVersion = new Intent(Intent.ACTION_VIEW);
														intent_cheackNewVersion.setData(Uri.parse(install));
														startActivity(intent_cheackNewVersion);
														finish();
													}

												});
												// ȡ����ť
												builder.setNegativeButton("ȡ��" ,new DialogInterface.OnClickListener()
												{
													@Override
													public void onClick(DialogInterface dialog , int which )
													{
														Toast.makeText(Business.this ,"��ȡ�����£�����" ,Toast.LENGTH_SHORT).show();
													}
												});

												builder.show();

											}

										});
									}
								}.start();

							}

					}
					else
					{
						Toast.makeText(Business.this ,mesg ,Toast.LENGTH_LONG).show();

					}

				}
				else
				{
					Toast.makeText(Business.this ,"�����쳣������" ,Toast.LENGTH_LONG).show();
				}
			}
			catch(Exception e)
			{
				Log.d("LOG" ,"Business_getver_GetThread_http_bug:\n" + e.toString());
				e.printStackTrace();
			}
		}
	}

	private void aboutUs()
	{
		// TODO Auto-generated method stub
		// Toast.makeText(this ,"This is aboutUs" ,Toast.LENGTH_LONG).show();
		intent = new Intent();
		intent.setClass(this ,Coordinate_1.class);
		startActivity(intent);
	}

	private void setting()
	{
		// TODO Auto-generated method stub
		// Toast.makeText(this ,"This is setting" ,Toast.LENGTH_LONG).show();
		intent = new Intent();
		intent.setClass(this ,Coordinate.class);
		startActivity(intent);

	}

	// �����ڰ����ؼ������˳�����
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode , KeyEvent event )
	{
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if((System.currentTimeMillis() - exitTime) > 2000)
			{
				Toast.makeText(getApplicationContext() ,"�ٰ�һ���˳�����" ,Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			}
			else
			{
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode ,event);
	}

}
