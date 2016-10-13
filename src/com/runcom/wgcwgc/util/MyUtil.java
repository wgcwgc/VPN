package com.runcom.wgcwgc.util;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class MyUtil
{
	/**
	 * 使用Toast显示提示信息
	 * 
	 * @param context
	 * @param text
	 */
	public static void showMsg(Context context , String text , int time )
	{
		{
			// Toast.makeText(context ,text ,Toast.LENGTH_SHORT).show();
		}

		{
			// Toast toast = Toast.makeText(context ,text ,Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.CENTER ,0 ,0);
			// // toast.setMargin((float) 0.01 ,(float) 0.01);
			// toast.show();
			// execToast(toast);
		}

		{
			// Toast toast = Toast.makeText(context ,text ,Toast.LENGTH_LONG);
			// // toast.setGravity(Gravity.CENTER ,0 ,0);
			// toast.setMargin((float) 0.05 ,(float) 0.07);
			// toast.show();
			// execToast(toast);
		}

		{
			Toast toast = Toast.makeText(context ,text ,Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER ,0 ,0);
			showMyToast(toast ,time * 1000);
			toast.show();
		}
	}

	// private static void execToast(final Toast toast )
	// {
	// new Timer().schedule(new TimerTask()
	// {
	// @Override
	// public void run()
	// {
	// toast.show();
	// }
	// } ,1000);
	//
	// }

	public static void showMyToast(final Toast toast , final int time )
	{
		final Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				toast.show();
			}
		} ,0 ,3000);
		new Timer().schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				toast.cancel();
				timer.cancel();
			}
		} ,time);
	}

}
