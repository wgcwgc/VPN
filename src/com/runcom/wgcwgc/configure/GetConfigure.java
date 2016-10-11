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
		// Log.d("LOG" ,"设备型号：" + android.os.Build.MODEL);// 设备型号 1503-A01
		// Log.d("LOG" ,"设备id（dev唯一识别码）：" +
		// android.provider.Settings.Secure.getString(context.getContentResolver()
		// ,android.provider.Settings.Secure.ANDROID_ID));// 2b8b2d1541a790dd

		// TelephonyManager tm = (TelephonyManager)
		// context.getSystemService(Context.TELEPHONY_SERVICE);

		// Log.d("LOG" ,"version release 设备系统版本号：" + Build.VERSION.RELEASE);//
		// 6.0
		// Log.d("LOG" ,"version sdk 设备sdk版本：" + Build.VERSION.SDK);// 23
		// Log.d("LOG" ,"\nDeviceId(IMEI)设备IMEI：" + tm.getDeviceId());//
		// IMEI.861483030594770
		// Log.d("LOG" ,"\nDeviceSoftwareVersion IMEI sv 软件版本：" +
		// tm.getDeviceSoftwareVersion());// IMEI.SV.软件版本78
		// Log.d("LOG" ,"\nLine1Number 手机号码：" + tm.getLine1Number()); // 手机号
		// Log.d("LOG" ,"\nNetworkCountryIso 网络提供商国家：" +
		// tm.getNetworkCountryIso());// cn
		// Log.d("LOG" ,"\nNetworkOperator 网络运营商代码：" +
		// tm.getNetworkOperator());// 46001
		// Log.d("LOG" ,"\nNetworkOperatorName 网络运营商名称：" +
		// tm.getNetworkOperatorName());// 网络运营商名称Mi.Mobile
		// Log.d("LOG" ,"\nNetworkType 网络类型：" + tm.getNetworkType());// 13
		// Log.d("LOG" ,"\nPhoneType 手机类型：" + tm.getPhoneType());// 1
		// Log.d("LOG" ,"\nSimCountryIso Sim卡提供国家代码：" +
		// tm.getSimCountryIso());// cn
		// Log.d("LOG" ,"\nSimOperator Sim卡运营商代码：" + tm.getSimOperator());//
		// 46009
		// Log.d("LOG" ,"\nSimOperatorName Sim卡运营商名称：" +
		// tm.getSimOperatorName());// 小米移动
		// Log.d("LOG" ,"\nSimSerialNumber Sim卡序列号：" +
		// tm.getSimSerialNumber());// 89860116842300087449
		// Log.d("LOG" ,"\nSimState Sim卡状态：" + tm.getSimState());// 5
		// Log.d("LOG" ,"\nSubscriberId(IMSI) 运营商详细名称：" +
		// tm.getSubscriberId());// 460095879800933

		String dev = android.provider.Settings.Secure.getString(context.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
		Configure getConfigure_set = new Configure();
		// Log.d("LOG" ,"getconfigure.java 执行了");
		getConfigure_set.setOs(Build.VERSION.RELEASE);
		getConfigure_set.setDev(dev);
		if(getRootAhth())
		{
			getConfigure_set.setIsbreak(1);
			// Log.d("LOG" ,"已获取root");
		}
		else
		{
			getConfigure_set.setIsbreak(0);
			// Log.d("LOG" ,"未获取root");
		}

		getConfigure_set.setLang(Locale.getDefault().getLanguage());
		// Log.d("LOG" ,"当前系统语言：" + Locale.getDefault().getLanguage());
		// Log.d("LOG" ,"当前国家：" + Locale.getDefault().getCountry());

		getConfigure_set.setMarket(2);

	}

	// 判断机器 Android是否已经root，即是否获取root权限
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
