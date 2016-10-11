package com.runcom.wgcwgc.position;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.R;

public class Coordinate extends Activity
{
	private TextView postionView;

	private LocationManager locationManager;
	private String locationProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coordinate);

		// 获取显示地理位置信息的TextView
		postionView = (TextView) findViewById(R.id.positionView);
		// 获取地理位置管理器
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// 获取所有可用的位置提供器
		List < String > providers = locationManager.getProviders(true);
		if(providers.contains(LocationManager.GPS_PROVIDER))
		{
			// 如果是GPS
			locationProvider = LocationManager.GPS_PROVIDER;
		}
		else
			if(providers.contains(LocationManager.NETWORK_PROVIDER))
			{
				// 如果是Network
				locationProvider = LocationManager.NETWORK_PROVIDER;
			}
			else
				if(providers.contains(LocationManager.PASSIVE_PROVIDER))
				{
					// 如果是PASSIVE_PROVIDER
					locationProvider = LocationManager.PASSIVE_PROVIDER;
				}
				else
				{
					Toast.makeText(this ,"没有可用的位置提供器" ,Toast.LENGTH_SHORT).show();
					return;
				}
		// 获取Location
		Location location = locationManager.getLastKnownLocation(locationProvider);
		if(location != null)
		{
			// 不为空,显示地理位置经纬度
			showLocation(location);
		}
		// 监视地理位置变化
		locationManager.requestLocationUpdates(locationProvider ,3000 ,1 ,locationListener);

	}

	/**
	 * 显示地理位置经度和纬度信息
	 * 
	 * @param location
	 */
	private void showLocation(Location location )
	{
		String locationStr = "维度：" + location.getLatitude() + "\n" + "经度：" + location.getLongitude();
		postionView.setText(locationStr);
	}

	/**
	 * LocationListern监听器 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
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
			// 如果位置发生变化,重新显示
			showLocation(location);

		}
	};

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if(locationManager != null)
		{
			// 移除监听器
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
