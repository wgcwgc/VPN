package com.runcom.wgcwgc.register;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.R;
import com.runcom.wgcwgc.md5.MD5;
import com.runcom.wgcwgc.web.SSLSocketFactoryEx;

public class Register_02 extends Activity
{
	private Intent intent;

	private EditText editText_login;
	private EditText editText_password;
	private EditText editText_password_resure;
	// private Button button_submit;
	private TextView textView_hint;

	private String email;
	private String chkcode;
	private String login;
	private String password;
	private String password_resure;

	String app;
	String ver;
	String build;

	int term = 0;
	String os = Build.VERSION.RELEASE;
	String dev;
	// private int isbreak;
	String lang = Locale.getDefault().getLanguage();
	int market = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_02);

		editText_login = (EditText) findViewById(R.id.bind_login);
		editText_password = (EditText) findViewById(R.id.register_02_password);
		editText_password_resure = (EditText) findViewById(R.id.bind_oldPassword);

		textView_hint = (TextView) findViewById(R.id.register_02_hint);

		// button_submit = (Button) findViewById(R.id.register_02_submitButton);

		intent = getIntent();

		email = intent.getStringExtra("email");
		chkcode = intent.getStringExtra("chkcode");

		login = editText_login.getText().toString();
		password = editText_password.getText().toString();
		password_resure = editText_password_resure.getText().toString();

	}

	private void judgeLoginAndPassword(String login , String password , String password_resure )
	{
		if(password.equals(password_resure) && judgeIsDigitAndLetter(login))
		{
			if(password.length() > 3 && password.length() < 11)
			{
				serverJudge(login ,password);
			}
		}
		else
		{
			textView_hint.setText("�û����������벻���ϱ�׼������");
		}

	}

	private void serverJudge(String login , String password )
	{
		GetThread getThread = new GetThread(login , password);
		getThread.start();
	}

	@SuppressLint("DefaultLocale")
	private boolean judgeIsDigitAndLetter(String login )
	{
		char [] loginArray = login.trim().toLowerCase().toCharArray();
		char temp;
		int length = loginArray.length;

		for(int i = 0 ; i < length ; i ++ )
		{
			temp = loginArray[i];
			if(temp >= 'a' && temp <= 'z' || temp >= '0' && temp <= '9')
			{
			}
			else
			{
				return false;
			}

		}
		return true;
	}

	class GetThread extends Thread
	{

		String account;
		String password;

		public GetThread(String account , String password)
		{
			this.account = account;
			this.password = password;
		}

		@SuppressLint("DefaultLocale")
		@Override
		public void run()
		{
			// ��HttpClient�������󣬷�Ϊ�岽
			// HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();// getNewHttpClient

			// https://a.redvpn.cn:8443/interface/
			// *************************************************************************************************************************************

			// term = 0;
			// os = Build.VERSION.RELEASE;
			dev = android.provider.Settings.Secure.getString(Register_02.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);

			String signValu = "tuoyouvpn" + app + build + chkcode + dev + email + lang + account + market + os + password + term + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			// Log.d("LOG" ,signValu);
			String url = "https://a.redvpn.cn:8443/interface/newUser.php?login=" + account + "&pass=" + password + "&chkcode=" + chkcode + "&email=" + email + "&app=" + app + "&build=" + build + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			// �ڶ�����������������Ķ���,�����Ƿ��ʵķ�������ַ
			// url = toURLEncoded(url);
			// System.out.println(url);
			Log.d("LOG" ,url);
			// System.out.println(new
			// MD5().md5("runcom8888123@abc.comxyz9.3.26666").toUpperCase());
			HttpGet httpGet = new HttpGet(url);
			try
			{
				// ��������ִ�����󣬻�ȡ��������������Ӧ����
				HttpResponse response = httpClient.execute(httpGet);
				// System.out.println("test00");
				// ���Ĳ��������Ӧ��״̬�Ƿ����������״̬���ֵ��200��ʾ����
				if(response.getStatusLine().getStatusCode() == 200)
				{
					// ���岽������Ӧ������ȡ�����ݣ��ŵ�entity����
					// System.out.println("test01");
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String json_result = reader.readLine();
					// JSONObject jsonObject = new JSONObject(json_result);

					// String result = jsonObject.getString("result");
					// Long result = jsonObject.getLong("result");
					// String mesg = jsonObject.getString("mesg");
					// String uid = jsonObject.getString("uid");
					// String expire = jsonObject.getString("expire");
					// String freetime = jsonObject.getString("freetime");
					// String flow = jsonObject.getString("flow");
					// String score = jsonObject.getString("score");
					// String coupon = jsonObject.getString("coupon");
					// String type = jsonObject.getString("type");
					// String email = jsonObject.getString("email");
					// String session = jsonObject.getString("session");

					Log.d("LOG" ,json_result);
					// Log.d("LOG" ,result.toString());

					// System.out.println(result);
					// Toast.makeText(MainActivity.this ,result
					// ,Toast.LENGTH_LONG).show();
					// Intent intent = new Intent();
					// intent.setClass(Register_02.this ,Business.class);
					// intent.putExtra("login" ,account);
					// intent.putExtra("pass" ,password);
					//
					// intent.putExtra("result" ,result);
					// intent.putExtra("mesg" ,mesg);
					// intent.putExtra("uid" ,uid);
					// intent.putExtra("expire" ,expire);
					// intent.putExtra("freetime" ,freetime);
					// intent.putExtra("flow" ,flow);
					// intent.putExtra("score" ,score);
					// intent.putExtra("coupon" ,coupon);
					// intent.putExtra("type" ,type);
					// intent.putExtra("email" ,email);
					// intent.putExtra("session" ,session);

					// startActivity(intent);
					Toast.makeText(Register_02.this ,"��ϲ��\n\tע��ɹ�������" ,Toast.LENGTH_LONG).show();
					finish();
					// System.out.println("test02");

				}
			}
			catch(Exception e)
			{
				Log.d("LOG" ,"http_bug");
			}

		}

	}

	public void submitButtonClick_02(View view )
	{
		judgeLoginAndPassword(login ,password ,password_resure);
	}

}
