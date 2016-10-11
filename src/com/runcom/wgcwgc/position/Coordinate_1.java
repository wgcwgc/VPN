package com.runcom.wgcwgc.position;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.R;

@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")
public class Coordinate_1 extends Activity
{

	private TextView postionView;

	private LocationManager locationManager;
	private String locationProvider;

	public static final int SHOW_LOCATION = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coordinate);

		// ��ȡ��ʾ����λ����Ϣ��TextView
		postionView = (TextView) findViewById(R.id.positionView);
		// ��ȡ����λ�ù�����
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// ��ȡ���п��õ�λ���ṩ��
		List < String > providers = locationManager.getProviders(true);
		if(providers.contains(LocationManager.GPS_PROVIDER))
		{
			// �����GPS
			locationProvider = LocationManager.GPS_PROVIDER;
		}
		else
			if(providers.contains(LocationManager.NETWORK_PROVIDER))
			{
				// �����Network
				locationProvider = LocationManager.NETWORK_PROVIDER;
			}
			else
				if(providers.contains(LocationManager.PASSIVE_PROVIDER))
				{
					// �����PASSIVE_PROVIDER
					locationProvider = LocationManager.PASSIVE_PROVIDER;
				}
				else
				{
					Toast.makeText(this ,"û�п��õ�λ���ṩ��" ,Toast.LENGTH_SHORT).show();
					return;
				}
		// ��ȡLocation
		Location location = locationManager.getLastKnownLocation(locationProvider);

		if(location != null)
		{
			// ��Ϊ��,��ʾ����λ�þ�γ��

			showLocation(location);
		}
		else
		{
			Toast.makeText(this ,"locationΪ��" ,Toast.LENGTH_SHORT).show();
		}
		// ���ӵ���λ�ñ仯
		locationManager.requestLocationUpdates(locationProvider ,3000 ,1 ,locationListener);

	}

	private Handler handler = new Handler()
	{
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg )
		{
			switch(msg.what)
			{
				case SHOW_LOCATION:
					String position = (String) msg.obj;
					postionView.setText(position);
					break;
				default:
					break;
			}
		}
	};

	/**
	 * ��ʾ����λ�þ��Ⱥ�γ����Ϣ
	 * 
	 * @param location
	 */
	private void showLocation(final Location location )
	{
		/*
		 * String locationStr = "ά�ȣ�" + location.getLatitude() +"\n" + "���ȣ�" +
		 * location.getLongitude(); postionView.setText(locationStr);
		 */
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					// ��װ����������Ľӿ�λ��
					StringBuilder url = new StringBuilder();
					url.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
					url.append(location.getLatitude()).append(",");
					url.append(location.getLongitude());
					url.append("&sensor=false");
					@SuppressWarnings("resource")
					HttpClient client = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(url.toString());
					httpGet.addHeader("Accept-Language" ,"zh-CN");
					HttpResponse response = client.execute(httpGet);
					if(response.getStatusLine().getStatusCode() == 200)
					{
						HttpEntity entity = response.getEntity();
						String res = EntityUtils.toString(entity);
						// ����
						JSONObject jsonObject = new JSONObject(res);
						// ��ȡresults�ڵ��µ�λ����Ϣ
						JSONArray resultArray = jsonObject.getJSONArray("results");
						if(resultArray.length() > 0)
						{
							JSONObject obj = resultArray.getJSONObject(0);
							// ȡ����ʽ�����λ������
							String address = obj.getString("formatted_address");

							Message msg = new Message();
							msg.what = SHOW_LOCATION;
							msg.obj = address;
							handler.sendMessage(msg);
						}
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * LocationListern������ ����������λ���ṩ��������λ�ñ仯��ʱ������λ�ñ仯�ľ�������LocationListener������
	 */

	LocationListener locationListener = new LocationListener()
	{

		@Override
		public void onStatusChanged(String provider , int status , Bundle arg2 )
		{

		}

		@Override
		public void onProviderEnabled(String provider )
		{

		}

		@Override
		public void onProviderDisabled(String provider )
		{

		}

		@Override
		public void onLocationChanged(Location location )
		{
			// ���λ�÷����仯,������ʾ
			showLocation(location);

		}
	};

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if(locationManager != null)
		{
			// �Ƴ�������
			locationManager.removeUpdates(locationListener);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu )
	{
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main ,menu);
		return true;
	}

}
