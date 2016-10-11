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
		// ��ʾ���ؼ�ͷĬ���ǲ���ʾ��
		actionbar.setDisplayHomeAsUpEnabled(false);
		// ��ʾ���ķ��ؼ�ͷ�����ҷ��ؼ�ͷ��titleһ�����ã����ؼ�ͷ������ʾ
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setDisplayUseLogoEnabled(true);
		// ��ʾ����
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setTitle(" ������Ϣ ");

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
					Toast.makeText(this ,"overflow չ����ʾitemͼ���쳣" ,Toast.LENGTH_LONG).show();
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
				// actionbar�����ͼ��ĵ���¼�����
				// finish();
				Toast.makeText(this ,"������һ��" ,Toast.LENGTH_LONG).show();
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
				Toast.makeText(this ,"������ҳ" ,Toast.LENGTH_LONG).show();
				return true;

		}
		return super.onOptionsItemSelected(item);
	}

}
