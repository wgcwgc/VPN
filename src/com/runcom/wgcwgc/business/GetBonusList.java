package com.runcom.wgcwgc.business;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
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
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.runcom.wgcwgc.R;
import com.runcom.wgcwgc.md5.MD5;
import com.runcom.wgcwgc.web.SSLSocketFactoryEx;

public class GetBonusList extends Activity
{
	public Intent intent;
	public String uid , type;
	public String app;

	public ListView listView;

	public List < BonusList > bonusList_list;
	public BonusList bonuslist;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business_getbonuslist_listview);

		ActionBar actionbar = getActionBar();
		// 显示返回箭头默认是不显示的
		actionbar.setDisplayHomeAsUpEnabled(false);
		// 显示左侧的返回箭头，并且返回箭头和title一起设置，返回箭头才能显示
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayUseLogoEnabled(true);
		// 显示标题
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setTitle(" 奖励列表 ");

		intent = getIntent();
		uid = intent.getStringExtra("uid");
		type = intent.getStringExtra("type");

		listView = (ListView) findViewById(R.id.business_main_getbonuslist_listView);
		getBonusList(uid ,type);
	}

	private void getBonusList(String uid , String type )
	{
		GetThread_getbonuslist getThread_getbonuslist = new GetThread_getbonuslist();
		getThread_getbonuslist.start();
	}

	class GetThread_getbonuslist extends Thread
	{
		String uid;
		String type;

		public GetThread_getbonuslist()
		{

		}

		public GetThread_getbonuslist(String uid , String type)
		{
			this.uid = uid;
			this.type = type;
		}

		@SuppressLint("DefaultLocale")
		@Override
		public void run()
		{
			// TODO Auto-generated method stub

			PackageManager packageManager = GetBonusList.this.getPackageManager();
			PackageInfo packageInfo = null;
			try
			{
				packageInfo = packageManager.getPackageInfo(GetBonusList.this.getPackageName() ,0);
				int labelRes = packageInfo.applicationInfo.labelRes;
				app = GetBonusList.this.getResources().getString(labelRes);
			}
			catch(NameNotFoundException e)
			{
				// Log.d("LOG"
				// ,"GetBonusList_getBonusList_serverJudge_package_exception:\n"
				// +
				// e.toString());
				e.printStackTrace();
			}
			String build = "57";
			String dev = android.provider.Settings.Secure.getString(GetBonusList.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
			String lang = Locale.getDefault().getLanguage();
			if(lang.contains("zh"))
			{
				lang = "zh-Hans";
			}
			int market = 2;
			String os = Build.VERSION.RELEASE;
			int term = 0;
			String ver = packageInfo.versionName;

			// 用HttpClient发送请求，分为五步
			// HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();// getNewHttpClient

			dev = android.provider.Settings.Secure.getString(GetBonusList.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);

			String signValu = "tuoyouvpn" + app + build + dev + lang + market + os + term + type + uid + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			String url = "https://a.redvpn.cn:8443/interface/getbonuslist.php?app=" + app + "&build=" + build + "&uid=" + uid + "&type=" + type + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			// 第二步：创建代表请求的对象,参数是访问的服务器地址
			Log.d("LOG" ,"GetBonusList_getbonuslist_url:\n" + url);
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
					// String json_result = reader.readLine();
					// JSONObject jsonObject = new JSONObject(json_result);

					String line = "";
					String returnLine = "";
					while((line = reader.readLine()) != null)
					{
						returnLine += line;
						// System.out.println("*" + line + "*\n");
					}
					Log.d("LOG" ,"GetBonuslist_getbonuslist_response:\n" + returnLine);
					JSONObject jsonObject = new JSONObject(returnLine);
					// String result = jsonObject.getString("result");
					Long result = jsonObject.getLong("result");
					// final String mesg = jsonObject.getString("mesg");
					JSONArray BonusListArray = jsonObject.getJSONArray("bonus");
					int leng = BonusListArray.length();

					if(result == 0)
					{

						bonusList_list = new ArrayList < BonusList >();
						List < String > bonus_list = new ArrayList < String >();
						for(int i = 0 ; i < leng ; i ++ )
						{
							 bonuslist = new BonusList();
//							 Log.d("LOG" ,"mesg:" + mesg + "\n");
							 bonus_list.add(BonusListArray.getString(i));
							 JSONObject jsonObject_content = new JSONObject(bonus_list.get(i).toString());

							String id = jsonObject_content.getString("id");
							String title = jsonObject_content.getString("name");
							String addr = jsonObject_content.getString("addr");
							String type = jsonObject_content.getString("type");
							String protocol = jsonObject_content.getString("protocol");
							String area = jsonObject_content.getString("area");
							String prior = jsonObject_content.getString("prior");

							 bonuslist.setId(id);
							 bonuslist.setTitle(title);
							 bonuslist.setReason(addr);
							 bonuslist.setType(protocol);
							 bonuslist.setValue(area);
							 bonuslist.setBonusdate(prior);
							 bonuslist.setHasgot(prior);
							 bonuslist.setGotdate(prior);

							@SuppressWarnings("unused")
							String type_hint = "";
							if(type.contains("0") || type == "0")
							{
								type_hint = "免费";
								// type = "免费";
							}
							else
								if(type.contains("1") || type == "1")
								{

									type_hint = "收费";
									// type = "收费";
								}

							// BonusList.setType(type_hint);
							// svrList_list.add(BonusList);

						}
						// Log.d("LOG" ,"svrList.toString():" +
						// svrList_list.toString());

						// Log.d("LOG" ,"GetBonusList_getBonusList_response:\n"
						// +
						// "result:" + result + "\nmesg:" + mesg +
						// "\nBonusListArray:" + BonusListArray + "\nleng:" +
						// leng);

						// Log.d("LOG" ,"id:" + jsonObject2.getString("id"));

						new Thread()
						{
							public void run()
							{
								runOnUiThread(new Runnable()
								{
									@Override
									public void run()
									{
										// Log.d("LOG" ,"svrList_list:\n" +
										// svrList_list.toString());
										// listView.setAdapter(new
										// MyBaseAdapter());
									}

								});
							}
						}.start();

					}

				}
			}
			catch(Exception e)
			{
				// Log.d("LOG"
				// ,"GetBonusList_getBonusList_GetThread_submit_http_bug:\n" +
				// e.toString());
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
