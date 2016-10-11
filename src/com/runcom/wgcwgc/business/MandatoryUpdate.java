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

	// �������Զ���View������ʵ��һ����Ĭ�ϵĹ��췽��
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

		// ���ò���ʾ�Ի��������
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTitle("����");
		// ���öԻ�����ʾ�ĸ������ļ�

		TextView textView = (TextView) findViewById(R.id.textView_business_dialog);
		textView.setText(mesg);
		setContentView(R.layout.business_dialog);
		// �Ի���Ҳ����ͨ����Դid�ҵ������ļ��е�������Ӷ����õ������
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
