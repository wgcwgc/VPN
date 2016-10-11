package com.runcom.wgcwgc.business;

import java.lang.reflect.Method;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.R;

public class PersonInformation extends Activity
{
	private String login , mesg , uid , expire , freetime , flow , score ,
	        coupon , type , email , session;
	private Long result;
	private Intent intent;
	private TextView textView;
	private String contents;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business_personinformation);

		ActionBar actionbar = getActionBar();
		// 显示返回箭头默认是不显示的
		actionbar.setDisplayHomeAsUpEnabled(false);
		// 显示左侧的返回箭头，并且返回箭头和title一起设置，返回箭头才能显示
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayUseLogoEnabled(true);
		// 显示标题
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setTitle(" 个人信息 ");

		intent = getIntent();

		login = intent.getStringExtra("login");
		result = intent.getLongExtra("result" , -1);
		mesg = intent.getStringExtra("mesg");
		uid = intent.getStringExtra("uid");
		expire = intent.getStringExtra("expire");
		freetime = intent.getStringExtra("freetime");
		flow = intent.getStringExtra("flow");
		score = intent.getStringExtra("score");
		coupon = intent.getStringExtra("coupon");
		type = intent.getStringExtra("type");
		email = intent.getStringExtra("email");
		session = intent.getStringExtra("session");

		contents = "login:\t" + login + "\nresult:\t" + result + "\nmesg:\t" + mesg + "\nuid:\t" + uid + "\nexpire:\t" + expire + "\nfreetime:\t" + freetime + "\nflow:\t" + flow + "\nscore:\t" + score + "\ncoupon:\t" + coupon + "\ntype:\t" + type + "\nemaile:\t" + email + "\nsession:\t" + session + "\n";

		textView = (TextView) findViewById(R.id.textView_business_personinformation);
		textView.setText(contents);

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
