package com.runcom.wgcwgc.register;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.R;
import com.runcom.wgcwgc.md5.MD5;
import com.runcom.wgcwgc.web.SSLSocketFactoryEx;

public class Register_01 extends Activity
{
	private EditText editText_email;
	private EditText editText_chkcode;
	private TextView textView_hint;

	@SuppressWarnings("unused")
	private Button button_chkcode;
	private Button button_submit;

	String app;
	String email;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_01);

		editText_chkcode = (EditText) findViewById(R.id.register_chkcode);
		textView_hint = (TextView) findViewById(R.id.register_01_hint);
		button_chkcode = (Button) findViewById(R.id.register_chkcodeButton);
		button_submit = (Button) findViewById(R.id.register_submitButton);

		// button_chkcode.setEnabled(false);
		button_submit.setEnabled(false);
		editText_chkcode.setEnabled(false);
	}

	public void chkcodeButtonClick_01(View view )
	{
		editText_email = (EditText) findViewById(R.id.register_01_email);
		email = editText_email.getText().toString();
		if(judge(email))
		{
			editText_chkcode.setEnabled(true);
			// button_chkcode.setEnabled(false);
//			button_submit.setEnabled(true);
			serverJudge(email);
		}
		else
		{
			textView_hint.setText("邮箱格式不正确或者填写错误！！！");
		}
	}

	@SuppressLint("DefaultLocale")
	private void serverJudge(String email )
	{

		GetThread getThread = new GetThread(email);
		getThread.start();

	}

	class GetThread extends Thread
	{
		String email;

		public GetThread(String email)
		{
			this.email = email;
		}

		@SuppressLint("DefaultLocale")
		@Override
		public void run()
		{
			PackageManager packageManager = Register_01.this.getPackageManager();
			PackageInfo packageInfo = null;
			try
			{
				packageInfo = packageManager.getPackageInfo(Register_01.this.getPackageName() ,0);
				int labelRes = packageInfo.applicationInfo.labelRes;
				app = Register_01.this.getResources().getString(labelRes);
			}
			catch(NameNotFoundException e)
			{
				Log.d("LOG" ,"serverJudge_package exception:" + e.toString());
			}
			String build = "57";
			String dev = android.provider.Settings.Secure.getString(Register_01.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
			String lang = Locale.getDefault().getLanguage();
			int market = 2;
			String os = Build.VERSION.RELEASE;
			int term = 0;
			String ver = packageInfo.versionName;

			String signValu = "tuoyouvpn" + app + build + dev + email + lang + market + os + term + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			String url = "https://a.redvpn.cn:8443/interface/emailCheck.php?email=" + email + "&app=" + app + "&build=" + build + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			Log.d("LOG" ,url);

			HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();
			HttpGet httpGet = new HttpGet(url);
			try
			{
				// Log.d("LOG" ,"test00");

				// 第三步：执行请求，获取服务器发还的相应对象
				HttpResponse response = httpClient.execute(httpGet);
				// 第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
				if(response.getStatusLine().getStatusCode() == 200)
				{
					// 第五步：从相应对象当中取出数据，放到entity当中
					// Log.d("LOG" ,"test01");
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String json_result = reader.readLine();
					JSONObject jsonObject = new JSONObject(json_result);

					// String result = jsonObject.getString("result");
					Long result = jsonObject.getLong("result");
					String mesg = jsonObject.getString("mesg");
					// String uid = jsonObject.getString("uid");
					// String expire = jsonObject.getString("expire");
					// String freetime = jsonObject.getString("freetime");
					// String flow = jsonObject.getString("flow");
					// String score = jsonObject.getString("score");
					// String coupon = jsonObject.getString("coupon");
					// String type = jsonObject.getString("type");
					// String session = jsonObject.getString("session");
					Log.d("LOG" ,json_result);
					if(result == 0)
					{
						textView_hint.setText("我们已经向您的邮箱发送了一条验证码，请注意查收！！！");
						Toast.makeText(Register_01.this ,mesg ,Toast.LENGTH_LONG).show();
						button_submit.setEnabled(true);
					}
					else
					{
						textView_hint.setText(mesg);
						Toast.makeText(Register_01.this ,mesg ,Toast.LENGTH_LONG).show();
						button_submit.setEnabled(false);
					}
					// Log.d("LOG" ,"result:" + result + "\nmesg:" + mesg);
					// finish();
					// System.out.println("test02");
				}
			}
			catch(Exception e)
			{
//				Log.d("LOG" ,"serverJudge_http exception:" + e.toString());
				 e.printStackTrace();
			}
		}

	}

	private boolean judge(String email )
	{

		return true;
	}

	public void submitButtonClick_01(View view )
	{
		Intent intent = new Intent();
		intent.setClass(Register_01.this ,Register_02.class);
		intent.putExtra("email" ,email);
		intent.putExtra("chkcode" ,editText_chkcode.getText().toString());
		startActivity(intent);

	}
}
