package com.runcom.wgcwgc.business;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.runcom.wgcwgc.R;

public class MandatoryUpdate extends Dialog
{

	private String mesg;
	private String install;
	private Context context;

	// 类似于自定义View，必须实现一个非默认的构造方法
	protected MandatoryUpdate(Context context , String mesg , String install)
	{
		super(context);
		this.context = context;
		this.mesg = mesg;
		this.install = install;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);

		// 设置不显示对话框标题栏
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTitle("更新");
		// 设置对话框显示哪个布局文件

		TextView textView = (TextView) findViewById(R.id.textView_business_dialog);
		textView.setText(mesg);
		setContentView(R.layout.business_dialog);
		// 对话框也可以通过资源id找到布局文件中的组件，从而设置点击侦听
		Button button_ok = (Button) findViewById(R.id.button_business_dialog);
		button_ok.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v )
			{
				Intent intent_cheackNewVersion = new Intent(Intent.ACTION_VIEW);
				intent_cheackNewVersion.setData(Uri.parse(install));
				context.startActivity(intent_cheackNewVersion);
				dismiss();
			}
		});
	}
}
