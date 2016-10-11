package com.runcom.wgcwgc.configure;

import java.io.DataOutputStream;
import java.util.Locale;

import android.content.Context;
import android.os.Build;
import android.util.Log;

public class GetConfigure extends Thread
{

	public Context context;

	public GetConfigure()
	{

	}

	public GetConfigure(Context context)
	{
		this.context = context;
	}

	public void run()
	{
		// Log.d("LOG" ,"�豸�ͺţ�" + android.os.Build.MODEL);// �豸�ͺ� 1503-A01
		// Log.d("LOG" ,"�豸id��devΨһʶ���룩��" +
		// android.provider.Settings.Secure.getString(context.getContentResolver()
		// ,android.provider.Settings.Secure.ANDROID_ID));// 2b8b2d1541a790dd

		// TelephonyManager tm = (TelephonyManager)
		// context.getSystemService(Context.TELEPHONY_SERVICE);

		// Log.d("LOG" ,"version release �豸ϵͳ�汾�ţ�" + Build.VERSION.RELEASE);//
		// 6.0
		// Log.d("LOG" ,"version sdk �豸sdk�汾��" + Build.VERSION.SDK);// 23
		// Log.d("LOG" ,"\nDeviceId(IMEI)�豸IMEI��" + tm.getDeviceId());//
		// IMEI.861483030594770
		// Log.d("LOG" ,"\nDeviceSoftwareVersion IMEI sv ����汾��" +
		// tm.getDeviceSoftwareVersion());// IMEI.SV.����汾78
		// Log.d("LOG" ,"\nLine1Number �ֻ����룺" + tm.getLine1Number()); // �ֻ���
		// Log.d("LOG" ,"\nNetworkCountryIso �����ṩ�̹��ң�" +
		// tm.getNetworkCountryIso());// cn
		// Log.d("LOG" ,"\nNetworkOperator ������Ӫ�̴��룺" +
		// tm.getNetworkOperator());// 46001
		// Log.d("LOG" ,"\nNetworkOperatorName ������Ӫ�����ƣ�" +
		// tm.getNetworkOperatorName());// ������Ӫ������Mi.Mobile
		// Log.d("LOG" ,"\nNetworkType �������ͣ�" + tm.getNetworkType());// 13
		// Log.d("LOG" ,"\nPhoneType �ֻ����ͣ�" + tm.getPhoneType());// 1
		// Log.d("LOG" ,"\nSimCountryIso Sim���ṩ���Ҵ��룺" +
		// tm.getSimCountryIso());// cn
		// Log.d("LOG" ,"\nSimOperator Sim����Ӫ�̴��룺" + tm.getSimOperator());//
		// 46009
		// Log.d("LOG" ,"\nSimOperatorName Sim����Ӫ�����ƣ�" +
		// tm.getSimOperatorName());// С���ƶ�
		// Log.d("LOG" ,"\nSimSerialNumber Sim�����кţ�" +
		// tm.getSimSerialNumber());// 89860116842300087449
		// Log.d("LOG" ,"\nSimState Sim��״̬��" + tm.getSimState());// 5
		// Log.d("LOG" ,"\nSubscriberId(IMSI) ��Ӫ����ϸ���ƣ�" +
		// tm.getSubscriberId());// 460095879800933

		String dev = android.provider.Settings.Secure.getString(context.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
		Configure getConfigure_set = new Configure();
		// Log.d("LOG" ,"getconfigure.java ִ����");
		getConfigure_set.setOs(Build.VERSION.RELEASE);
		getConfigure_set.setDev(dev);
		if(getRootAhth())
		{
			getConfigure_set.setIsbreak(1);
			// Log.d("LOG" ,"�ѻ�ȡroot");
		}
		else
		{
			getConfigure_set.setIsbreak(0);
			// Log.d("LOG" ,"δ��ȡroot");
		}

		getConfigure_set.setLang(Locale.getDefault().getLanguage());
		// Log.d("LOG" ,"��ǰϵͳ���ԣ�" + Locale.getDefault().getLanguage());
		// Log.d("LOG" ,"��ǰ���ң�" + Locale.getDefault().getCountry());

		getConfigure_set.setMarket(2);

	}

	// �жϻ��� Android�Ƿ��Ѿ�root�����Ƿ��ȡrootȨ��
	public boolean getRootAhth()
	{
		Process process = null;
		DataOutputStream os = null;
		try
		{
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes("exit\n");
			os.flush();
			int exitValue = process.waitFor();
			if(exitValue == 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			Log.d("*** DEBUG ***" ,"Unexpected error - Here is what I know: " + e.getMessage());
			return false;
		}
		finally
		{
			try
			{
				if(os != null)
				{
					os.close();
				}
				process.destroy();
			}
			catch(Exception e)
			{
				Log.d("*** DEBUG ***" ,"Unexpected error - Here is what I know: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
