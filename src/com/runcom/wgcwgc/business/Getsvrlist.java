package com.runcom.wgcwgc.business;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.runcom.wgcwgc.R;
import com.runcom.wgcwgc.md5.MD5;
import com.runcom.wgcwgc.util.MyUtil;
import com.runcom.wgcwgc.web.SSLSocketFactoryEx;

public class Getsvrlist extends Activity
{
	public Intent intent;
	public String uid , type;
	public String app;

	public ListView listView;

	public List < Svrlist > svrList_list;
	public Svrlist svrlist;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business_getsvrlist_listview);

		intent = getIntent();
		uid = intent.getStringExtra("uid");
		type = intent.getStringExtra("type");

		listView = (ListView) findViewById(R.id.business_main_getsvrlist_listView);
		getsvrlist(uid ,type);
		// try
		// {
		// Thread.sleep(7000);
		// }
		// catch(InterruptedException e)
		// {
		// e.printStackTrace();
		// }
		// listView.setAdapter(new MyBaseAdapter());

	}

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
			PackageManager packageManager = Getsvrlist.this.getPackageManager();
			PackageInfo packageInfo = null;
			try
			{
				packageInfo = packageManager.getPackageInfo(Getsvrlist.this.getPackageName() ,0);
				int labelRes = packageInfo.applicationInfo.labelRes;
				app = Getsvrlist.this.getResources().getString(labelRes);
			}
			catch(NameNotFoundException e)
			{
				// Log.d("LOG"
				// ,"Getsvrlist_getsvrlist_serverJudge_package_exception:\n" +
				// e.toString());
				e.printStackTrace();
			}
			String build = "57";
			String dev = android.provider.Settings.Secure.getString(Getsvrlist.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
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

			dev = android.provider.Settings.Secure.getString(Getsvrlist.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);

			String signValu = "tuoyouvpn" + app + build + dev + lang + market + os + term + type + uid + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			String url = "https://a.redvpn.cn:8443/interface/getsvrlist.php?app=" + app + "&build=" + build + "&uid=" + uid + "&type=" + type + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			// 第二步：创建代表请求的对象,参数是访问的服务器地址
			// Log.d("LOG" ,"Getsvrlist_getsvrlist_url:\n" + url);
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
					JSONObject jsonObject = new JSONObject(returnLine);
					// String result = jsonObject.getString("result");
					Long result = jsonObject.getLong("result");
					// final String mesg = jsonObject.getString("mesg");
					JSONArray svrlistArray = jsonObject.getJSONArray("svrlist");
					int leng = svrlistArray.length();

					if(result == 0)
					{

						svrList_list = new ArrayList < Svrlist >();
						List < String > server_list = new ArrayList < String >();
						for(int i = 0 ; i < leng ; i ++ )
						{
							svrlist = new Svrlist();
							// Log.d("LOG" ,"mesg:" + mesg + "\n");
							server_list.add(svrlistArray.getString(i));
							JSONObject jsonObject_content = new JSONObject(server_list.get(i).toString());

							String id = jsonObject_content.getString("id");
							String name = jsonObject_content.getString("name");
							String addr = jsonObject_content.getString("addr");
							String type = jsonObject_content.getString("type");
							String protocol = jsonObject_content.getString("protocol");
							String area = jsonObject_content.getString("area");
							String prior = jsonObject_content.getString("prior");

							svrlist.setId(id);
							svrlist.setName(name);
							svrlist.setAddr(addr);
							svrlist.setProtocol(protocol);
							svrlist.setArea(area);
							svrlist.setPrior(prior);

							String type_hint = "";
							if(type.contains("0") || type == "0")
							{
								type_hint = "免费";
//								type = "免费";
							}
							else
								if(type.contains("1") || type == "1")
								{

									type_hint = "收费";
//									type = "收费";
								}

							svrlist.setType(type_hint);
//							String contents = id + "\t" + name + "\t" + type_hint + "\t" + area;
//							svrlist.setShow(contents);
							svrList_list.add(svrlist);

						}
						// Log.d("LOG" ,"svrList.toString():" +
						// svrList_list.toString());

						// Log.d("LOG" ,"Getsvrlist_getsvrlist_response:\n" +
						// "result:" + result + "\nmesg:" + mesg +
						// "\nsvrlistArray:" + svrlistArray + "\nleng:" + leng);

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
										listView.setAdapter(new MyBaseAdapter());
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
				// ,"Getsvrlist_getsvrlist_GetThread_submit_http_bug:\n" +
				// e.toString());
				e.printStackTrace();
			}

		}

	}

	class MyBaseAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return svrList_list.size();
		}

		@Override
		public Object getItem(int position )
		{
			return svrList_list.get(position);
		}

		@Override
		public long getItemId(int position )
		{
			return position;
		}

		/**
		 * 
		 * 匹配到相应视图中 在getView()方法中,给convert
		 * View设置setTag()，可以将position或者view设置完成后进去，然后在on
		 * Click方法中通过getTag()获得所设置的值，就完成获取任务了。
		 */
		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position , View convertView , ViewGroup parent )
		{

			Holder holder;
			if(convertView == null)
			{
				getLayoutInflater();
				convertView = LayoutInflater.from(Getsvrlist.this).inflate(R.layout.business_svrlist ,null);
				holder = new Holder();
				// holder.userName = (TextView)
				// convertView.findViewById(R.id.userName);
				// holder.userPhoneNumber = (TextView)
				// convertView.findViewById(R.id.userPhone);
				// holder.otherInformation = (TextView)
				// convertView.findViewById(R.id.other);

//				holder.show = (TextView) convertView.findViewById(R.id.business_main_getsvrlist_textView_id);
				holder.id = (TextView) convertView.findViewById(R.id.business_main_getsvrlist_textView_id);
				holder.name = (TextView) convertView.findViewById(R.id.business_main_getsvrlist_textView_name);
				holder.type = (TextView) convertView.findViewById(R.id.business_main_getsvrlist_textView_type);
				
				convertView.setTag(holder);
			}
			else
			{
				holder = (Holder) convertView.getTag();
			}
			// holder.userName.setText(addressList.get(position).getUserName());
			// holder.userPhoneNumber.setText(addressList.get(position).getPhoneNumber());
			// holder.otherInformation.setText(addressList.get(position).getOtherImportantInformation());

			holder.id.setText(svrList_list.get(position).getId());
			holder.name.setText(svrList_list.get(position).getName());
			holder.type.setText(svrList_list.get(position).getType());

			// TextView textView = (TextView)
			// convertView.findViewById(R.id.userName);
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
									// Log.d("LOG" ,"svrList_list:\n" +
									// svrList_list.toString());

									int delay = getItem(position).toString().length();
									int delayTime = delay / 17 + 1;
									MyUtil.showMsg(Getsvrlist.this ,getItem(position).toString() ,delayTime);

								}

							});
						}
					}.start();
				}
			});

			// final ImageView imageView = (ImageView)
			// convertView.findViewById(R.id.more);
			// imageView.setOnClickListener(new View.OnClickListener()
			// {
			// @Override
			// public void onClick(View v )
			// {
			// int delay = getItem(position).toString().length();
			// int delayTime = delay / 17 + 1;
			// MyUtil.showMsg(Getsvrlist.this ,delay + "\n" + delayTime + "\n" +
			// getItem(position).toString() ,delayTime);
			// }
			// });

			return convertView;
		}

		class Holder
		{
			TextView id , show , type , name;
		}

	}
}
