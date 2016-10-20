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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.R;
import com.runcom.wgcwgc.md5.MD5;
import com.runcom.wgcwgc.util.MyUtil;
import com.runcom.wgcwgc.web.SSLSocketFactoryEx;

public class GetBonusList extends Activity
{
	public Intent intent;
	public String uid , type , id;
	public String app;

	public ListView listView;

	public List < BonusList > bonusList_list;
	public BonusList bonuslist;
	Button button;

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
		GetThread_getbonuslist getThread_getbonuslist = new GetThread_getbonuslist(uid , type);
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

			String signValu = "tuoyouvpn" + app + build + dev + lang + market + os + term + uid + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			String url = "https://a.redvpn.cn:8443/interface/getbonuslist.php?app=" + app + "&build=" + build + "&uid=" + uid + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
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
						final List < String > bonus_list = new ArrayList < String >();
						for(int i = 0 ; i < leng ; i ++ )
						{
							bonuslist = new BonusList();
							bonus_list.add(BonusListArray.getString(i));
							JSONObject jsonObject_content = new JSONObject(bonus_list.get(i).toString());

							id = jsonObject_content.getString("id");
							String title = jsonObject_content.getString("title");
							String reason = jsonObject_content.getString("reason");
							String type = jsonObject_content.getString("type");
							String value = jsonObject_content.getString("value");
							String bonusdate = jsonObject_content.getString("bonusdate");
							String hasgot = jsonObject_content.getString("hasgot");
							String gotdate = jsonObject_content.getString("gotdate");

							bonuslist.setId(id);
							bonuslist.setTitle(title);
							bonuslist.setReason(reason);
							bonuslist.setType(type);
							bonuslist.setValue(value);
							bonuslist.setBonusdate(bonusdate);
							bonuslist.setHasgot(hasgot);
							bonuslist.setGotdate(gotdate);

							bonusList_list.add(bonuslist);

						}

						new Thread()
						{
							public void run()
							{
								runOnUiThread(new Runnable()
								{
									@Override
									public void run()
									{
										// Log.d("LOG"
										// ,"GetBonusList_getbonuslist_response:\n"
										// + bonusList_list.toString());
										if(bonus_list.isEmpty())
										{
											MyUtil.showMsg(GetBonusList.this ,"您还没有可领取的奖励！" ,5);
										}
										else
										{
											listView.setAdapter(new MyBaseAdapter());
										}
									}

								});
							}
						}.start();

					}

				}
			}
			catch(Exception e)
			{
				Log.d("LOG" ,"GetBonusList_getBonusList_GetThread_submit_http_bug:\n" + e.toString());
				e.printStackTrace();
			}

		}

	}

	void acceptbonus(String uid , String id )
	{
		GetBonusList_acceptBonus getBonusList_acceptBonus = new GetBonusList_acceptBonus(uid , id);
		getBonusList_acceptBonus.start();

	}

	class GetBonusList_acceptBonus extends Thread
	{
		String uid;
		String id;

		public GetBonusList_acceptBonus()
		{

		}

		public GetBonusList_acceptBonus(String uid , String id)
		{
			this.uid = uid;
			this.id = id;
		}

		@SuppressLint("DefaultLocale")
		@Override
		public void run()
		{
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

			HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();// getNewHttpClient

			dev = android.provider.Settings.Secure.getString(GetBonusList.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);

			String signValu = "tuoyouvpn" + app + build + dev + id + lang + market + os + term + uid + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			String url = "https://a.redvpn.cn:8443/interface/acceptbonus.php?app=" + app + "&id=" + id + "&build=" + build + "&uid=" + uid + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			// 第二步：创建代表请求的对象,参数是访问的服务器地址
			Log.d("LOG" ,"GetBonusList_acceptbonus_url:\n" + url);
			HttpGet httpGet = new HttpGet(url);

			try
			{
				// 第三步：执行请求，获取服务器发还的相应对象
				HttpResponse response = httpClient.execute(httpGet);
				// 第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
				if(response.getStatusLine().getStatusCode() == 200)
				{
					// 第五步：从相应对象当中取出数据，放到entity当中
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));

					String line = "";
					String returnLine = "";
					while((line = reader.readLine()) != null)
					{
						returnLine += line;
					}

					Log.d("LOG" ,"GetBonuslist_acceptbonus_response:\n" + returnLine);
					JSONObject jsonObject = new JSONObject(returnLine);
					Long result = jsonObject.getLong("result");
					final String mesg = jsonObject.getString("mesg");

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
										button.setText("已领取");
										button.setEnabled(false);
										MyUtil.showMsg(GetBonusList.this ,mesg ,5);
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
										MyUtil.showMsg(GetBonusList.this ,mesg ,5);
									}

								});
							}
						}.start();
					}

				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	class MyBaseAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			return bonusList_list.size();
		}

		@Override
		public Object getItem(int position )
		{
			return bonusList_list.get(position);
		}

		@Override
		public long getItemId(int position )
		{
			return position;
		}

		@SuppressLint(
		{ "InflateParams", "DefaultLocale" })
		@Override
		public View getView(final int position , View convertView , ViewGroup parent )
		{

			Holder holder;
			if(convertView == null)
			{
				getLayoutInflater();
				convertView = LayoutInflater.from(GetBonusList.this).inflate(R.layout.business_bonuslist ,null);
				holder = new Holder();
				holder.id = (TextView) convertView.findViewById(R.id.business_main_getbonuslist_textView_id);
				holder.title = (TextView) convertView.findViewById(R.id.business_main_getbonuslist_textView_title);
				// holder.reason = (TextView)
				// convertView.findViewById(R.id.business_main_getbonuslist_textView_reason);

				convertView.setTag(holder);
			}
			else
			{
				holder = (Holder) convertView.getTag();
			}

			holder.id.setText(bonusList_list.get(position).getId());
			holder.title.setText(bonusList_list.get(position).getTitle());
			// holder.reason.setText(bonusList_list.get(position).getReason());

			button = (Button) convertView.findViewById(R.id.business_main_getbonuslist_button);
			if(bonusList_list.get(position).getHasgot().contains("1") || bonusList_list.get(position).getHasgot() == "1")
			{
				button.setText("已领取");
				button.setEnabled(false);
			}
			else
			{
				// \(^o^)/YES!
			}

			button.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v )
				{
					acceptbonus(uid ,id);
				}

			});

			convertView.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View view )
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
									int delay = getItem(position).toString().length();
									int delayTime = delay / 17 + 1;
									MyUtil.showMsg(GetBonusList.this ,getItem(position).toString() ,delayTime);

								}

							});
						}
					}.start();
				}
			});

			return convertView;
		}

		class Holder
		{
			TextView id , show , type , title , reason;
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
