package com.recruitmentclient.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.recruitmentapp.R;
import com.recruitmentapp.R.id;
import com.recruitmentclient.control.Constant;
import com.recruitmentclient.control.HttpRequestThread;
import com.recruitmentclient.model.Company;
import com.recruitmentclient.model.Seeker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener
{

	HttpRequestThread httpRequestThread;
	// EditText of LoginActivity
	private EditText login_username_ed;
	private EditText login_password_ed;
	// identity Radio Button
	private RadioButton seeker_rd;
	private RadioButton company_rd;

	// Remember Password and auto login CheckBox
	private CheckBox rmpsw_cb;
	private CheckBox autologin_ck;

	// Login and Sign up Button
	private Button login_bt;
	private Button signup_bt;
	// 登录信息记录
	private String username;
	private String password;
	private int identity = Constant.SEEKER;
	// 登录模式标志
	private boolean rm_flag = false;
	private boolean auto_flag = false;

	private ProgressDialog progressDialog;

	public static SharedPreferences SP = null;
	Handler handler;

	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		HandlerThread handlerThread = new HandlerThread("LoginThread");
		handlerThread.start();
		handler = new Handler(handlerThread.getLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				progressDialog.dismiss();
				System.out.println(msg.what);
				switch (msg.what)
				{
				case Constant.FAIL:
					this.removeCallbacks(httpRequestThread);
					Toast.makeText(LoginActivity.this,
							"Username doesn't exists or wrong password!",
							Toast.LENGTH_LONG).show();
					break;

				case Constant.SUCCESS:
					this.removeCallbacks(httpRequestThread);
					if (rm_flag)
					{
						rememberMe(username, password, identity, auto_flag);
					}
					Intent intent1 = null;
					if (identity == Constant.SEEKER)
					{
						Seeker seeker = (Seeker) msg.obj;
						intent1 = new Intent(LoginActivity.this,
								SeekerMainActivity.class);
						intent1.putExtra("username", username);
						Bundle bundle = new Bundle();
						bundle.putSerializable("seeker", seeker);
						intent1.putExtra("seeker", bundle);
						startActivity(intent1);
						LoginActivity.this.finish();
					} else if (identity == Constant.COMPANY)
					{
						Company company = (Company)msg.obj;
						System.out.println(company + "login");
						intent1 = new Intent(LoginActivity.this,
								ComMainActivity.class);
						intent1.putExtra("username", username);
						Bundle bundle = new Bundle();
						bundle.putSerializable("company", company);
						intent1.putExtra("company", bundle);
						startActivity(intent1);
						LoginActivity.this.finish();
					}

					break;
				}
			}
		};
		SP = getPreferences(MODE_PRIVATE);

		login_username_ed = (EditText) findViewById(R.id.login_username_ed);
		login_password_ed = (EditText) findViewById(R.id.login_password_ed);

		seeker_rd = (RadioButton) findViewById(R.id.seeker_rd);
		company_rd = (RadioButton) findViewById(R.id.company_rd);

		seeker_rd.setOnCheckedChangeListener(this);
		company_rd.setOnCheckedChangeListener(this);

		rmpsw_cb = (CheckBox) findViewById(R.id.rempas_cb);
		autologin_ck = (CheckBox) findViewById(R.id.autologin_cb);
		//
		rmpsw_cb.setOnCheckedChangeListener(this);
		autologin_ck.setOnCheckedChangeListener(this);

		login_bt = (Button) findViewById(R.id.login_bt);
		signup_bt = (Button) findViewById(R.id.signup_bt);
		login_bt.setOnClickListener(this);
		signup_bt.setOnClickListener(this);

		checkIfRem();
		if (checkIfAutoLogin())
		{
			login();
		}
	}

	private boolean checkInput()
	{
		if (login_username_ed.getText().toString().trim().equals(""))
		{
			Toast.makeText(this, "Please Enter Username", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (login_password_ed.getText().toString().trim().equals(""))
		{
			Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		username = login_username_ed.getText().toString().trim();
		password = login_password_ed.getText().toString().trim();

		return true;
	}

	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.login_bt:
			if (!checkInput())
			{
				return;
			}
			login();
			break;

		case R.id.signup_bt:
			final Intent intent = new Intent(this, SignUpActivity.class);
			AlertDialog.Builder builder = new Builder(LoginActivity.this);
			builder.setTitle("You are a:");
			builder.setPositiveButton("Seeker",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							// flag 0 for seeker
							intent.addFlags(0);
							startActivity(intent);
						}
					});
			builder.setNegativeButton("Company",
					new DialogInterface.OnClickListener()
					{

						public void onClick(DialogInterface arg0, int arg1)
						{
							// flag 1 for company
							intent.addFlags(1);
							startActivity(intent);
						}
					});
			builder.show();
			break;
		}
	}

	// Remember Password
	private void rememberMe(String username, String pwd, int identity,
			boolean autologin)
	{
		SharedPreferences.Editor editor = SP.edit();
		editor.putString("username", username);
		editor.putString("password", pwd);
		editor.putInt("identity", identity);
		editor.putBoolean("autologin", autologin);
		editor.commit();
	}

	public void clearCount()
	{
		SharedPreferences.Editor editor = SP.edit();
		editor.clear();
		editor.commit();
	}

	// Check whether remmembered pass word
	private void checkIfRem()
	{
		username = SP.getString("username", null);
		password = SP.getString("password", null);
		identity = SP.getInt("identity", Constant.SEEKER);
		if (username != null && password != null)
		{
			login_username_ed.setText(username);
			login_password_ed.setText(password);
			if (identity == Constant.SEEKER)
			{
				seeker_rd.setChecked(true);
			} else if (identity == Constant.COMPANY)
			{
				company_rd.setChecked(true);
			}
			rmpsw_cb.setChecked(true);
		}
	}

	// check whether autoLogin
	private boolean checkIfAutoLogin()
	{
		SharedPreferences sp = getPreferences(MODE_PRIVATE);
		return sp.getBoolean("autologin", false);
	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		Constant.exit(this);
	}

	private void login()
	{
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(null);
		progressDialog.setTitle("Wait!");
		progressDialog.setMessage("Login……");
		progressDialog.show();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("identity", identity + ""));
		httpRequestThread = new HttpRequestThread(handler,
				Constant.HTTP_ACTION_LOGIN, identity, params);
		handler.post(httpRequestThread);

	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		// TODO Auto-generated method stub
		switch (buttonView.getId())
		{
		case R.id.seeker_rd:
			if (isChecked)
			{
				identity = Constant.SEEKER;
			}
			break;

		case R.id.company_rd:
			if (isChecked)
			{
				identity = Constant.COMPANY;
			}
			break;
		case R.id.rempas_cb:
			if (isChecked)
			{
				rm_flag = true;
				autologin_ck.setClickable(true);
			} else
			{
				rm_flag = false;
				auto_flag = false;
				autologin_ck.setChecked(false);
				autologin_ck.setClickable(false);
				clearCount();
			}
			break;
		case R.id.autologin_cb:
			if (isChecked)
			{
				auto_flag = true;
			} else
			{
				auto_flag = false;
			}
			break;
		}
	}
}