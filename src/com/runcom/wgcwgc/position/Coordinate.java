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
		// ���ӵ���λ�ñ仯
		locationManager.requestLocationUpdates(locationProvider ,3000 ,1 ,locationListener);

	}

	/**
	 * ��ʾ����λ�þ��Ⱥ�γ����Ϣ
	 * 
	 * @param location
	 */
	private void showLocation(Location location )
	{
		String locationStr = "ά�ȣ�" + location.getLatitude() + "\n" + "���ȣ�" + location.getLongitude();
		postionView.setText(locationStr);
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
