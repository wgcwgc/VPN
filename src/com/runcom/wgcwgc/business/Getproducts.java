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

public class Getproducts extends Activity
{
	public Intent intent;
	public String uid , type;
	public String app;

	public ListView listView;

	public List < ProductsList > productsList_list;
	public ProductsList productsList;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business_getproducts_listview);

		intent = getIntent();
		uid = intent.getStringExtra("uid");
		type = intent.getStringExtra("type");

		listView = (ListView) findViewById(R.id.business_main_getproducts_listView);
		getproducts(uid ,type);

	}

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
			PackageManager packageManager = Getproducts.this.getPackageManager();
			PackageInfo packageInfo = null;
			try
			{
				packageInfo = packageManager.getPackageInfo(Getproducts.this.getPackageName() ,0);
				int labelRes = packageInfo.applicationInfo.labelRes;
				app = Getproducts.this.getResources().getString(labelRes);
			}
			catch(NameNotFoundException e)
			{
				// Log.d("LOG"
				// ,"Getproducts_getproducts_serverJudge_package_exception:\n" +
				// e.toString());
				e.printStackTrace();
			}
			String build = "57";
			String dev = android.provider.Settings.Secure.getString(Getproducts.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
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

			dev = android.provider.Settings.Secure.getString(Getproducts.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);

			String signValu = "tuoyouvpn" + app + build + dev + lang + market + os + term + uid + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			// Log.d("LOG" ,signValu);
			final String url = "https://a.redvpn.cn:8443/interface/getproducts.php?app=" + app + "&build=" + build + "&uid=" + uid + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			// 第二步：创建代表请求的对象,参数是访问的服务器地址
			// Log.d("LOG" ,"Getproducts_getproducts_url:\n" + url);
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
					JSONArray products_list_Array = jsonObject.getJSONArray("products");
					int leng = products_list_Array.length();

					if(result == 0)
					{

						productsList_list = new ArrayList < ProductsList >();
						List < String > products_list = new ArrayList < String >();
						for(int i = 0 ; i < leng ; i ++ )
						{

							productsList = new ProductsList();
							products_list.add(products_list_Array.getString(i));
							// Log.d("LOG" ,products_list.get(i));
							JSONObject jsonObject_content = new JSONObject(products_list.get(i).toString());

							// new Thread()
							// {
							// public void run()
							// {
							// runOnUiThread(new Runnable()
							// {
							// @Override
							// public void run()
							// {
							// Log.d("LOG" ,"http请求执行了" +
							// jsonObject_content.toString());
							// }
							//
							// });
							// }
							// }.start();

							String id = jsonObject_content.getString("id");
							String name = jsonObject_content.getString("name");
							String desc = jsonObject_content.getString("desc");
							String type = jsonObject_content.getString("type");
							String price = jsonObject_content.getString("price");
							String productcode = jsonObject_content.getString("productcode");
							String market_products = jsonObject_content.getString("market");
							String paymethod = jsonObject_content.getString("payMethod");

							productsList.setId(id);
							productsList.setName(name);
							productsList.setDesc(desc);
							productsList.setProductcode(productcode);
							productsList.setMarket(market_products);
							productsList.setPaymethod(paymethod);

							String price_hint = "";
							if(lang == "zh-Hans" || lang.contains("zh"))
							{
								price_hint = "¥" + price;
							}
							else
								if(lang == "en" || lang.contains("en"))
								{
									price_hint = "$" + price;

								}
							productsList.setPrice(price_hint);

							String type_hint = "";
							if(type.contains("0") || type == "0")
							{
								type_hint = "一次性";
							}
							else
								if(type.contains("1") || type == "1")
								{

									type_hint = "按天数";
								}
								else
									if(type.contains("2") || type == "2")
									{

										type_hint = "包月（自动续订）";
									}
									else
										if(type.contains("3") || type == "3")
										{

											type_hint = "包年";
										}
										else
											if(type.contains("4") || type == "4")
											{

												type_hint = "流量包";
											}

							productsList.setType(type_hint);

							productsList_list.add(productsList);

						}

						// JSONObject jsonObject2 = new
						// JSONObject(products_list.get(0).toString());
						// Log.d("LOG" , "id:" + jsonObject2.getString("id"));

						// Log.d("LOG" ,"Getproducts_getproducts_response:\n" +
						// "result:" + result + "\nmesg:" + mesg + "\nproducts:"
						// + products_list_Array + "\nleng:" + leng);

						new Thread()
						{
							public void run()
							{
								runOnUiThread(new Runnable()
								{
									@Override
									public void run()
									{
										// Log.d("LOG" ,"productsList_list:\n" +
										// productsList_list.toString());
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
				// ,"Getproducts_getproducts_GetThread_submit_http_bug:\n" +
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
			return productsList_list.size();
		}

		@Override
		public Object getItem(int position )
		{
			return productsList_list.get(position);
		}

		@Override
		public long getItemId(int position )
		{
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position , View convertView , ViewGroup parent )
		{
			Holder holder;
			if(convertView == null)
			{
				getLayoutInflater();
				convertView = LayoutInflater.from(Getproducts.this).inflate(R.layout.business_productslist ,null);
				holder = new Holder();

				holder.id = (TextView) convertView.findViewById(R.id.business_main_getproducts_id);
				holder.name = (TextView) convertView.findViewById(R.id.business_main_getproducts_name);
				holder.type = (TextView) convertView.findViewById(R.id.business_main_getproducts_type);
				holder.price = (TextView) convertView.findViewById(R.id.business_main_getproducts_price);
				convertView.setTag(holder);
			}
			else
			{
				holder = (Holder) convertView.getTag();
			}

			holder.id.setText(productsList_list.get(position).getId());
			holder.name.setText(productsList_list.get(position).getName());
			holder.type.setText(productsList_list.get(position).getType());
			holder.price.setText(productsList_list.get(position).getPrice());

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
									MyUtil.showMsg(Getproducts.this ,getItem(position).toString() ,delayTime);

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
			TextView id , show , type , name , price;
		}
	}
}
