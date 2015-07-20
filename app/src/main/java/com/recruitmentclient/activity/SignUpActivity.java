package com.recruitmentclient.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.recruitmentapp.R;
import com.recruitmentclient.control.Constant;
import com.recruitmentclient.control.HttpRequestThread;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView.OnEditorActionListener;

public class SignUpActivity extends Activity
{
	Intent intent;
	ProgressDialog progressDialog;
	Handler handler;
	HttpRequestThread httpThread;
	boolean isUsernameUnique = true;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		HandlerThread handlerThread = new HandlerThread("HttpThread");
		// To handle http request from HttpRequestThread
		handlerThread.start();
		handler = new Handler(handlerThread.getLooper())
		{
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case Constant.HTTP_ACTION_SIGNUP:
					progressDialog.cancel();
					handler.removeCallbacks(httpThread);
					Toast.makeText(SignUpActivity.this, "Sign Up Success!",
							Toast.LENGTH_SHORT).show();
					SignUpActivity.this.finish();// Back to LoginActivity
					break;

				case Constant.HTTP_ACTION_CHECKUNI:
					handler.removeCallbacks(httpThread);
					if (msg.arg1 == 0)
					{
						isUsernameUnique = true;
						Toast.makeText(SignUpActivity.this,
								"You can use this Username!",
								Toast.LENGTH_SHORT).show();
					} else if (msg.arg1 == 1)
					{
						isUsernameUnique = false;
						Toast.makeText(SignUpActivity.this,
								"Username Exists,Try Another!",
								Toast.LENGTH_SHORT).show();
					}
					break;
				}

			};
		};
		// Entry of Seeker SignUp View
		intent = getIntent();
		// get flag from intent, 0 for seeker, 1 for company
		int flag = intent.getFlags();
		if (flag == 0)
		{
			gotoSeekerSignUpView();
		} else
		{
			gotoCompanySignUpView();
		}

	}

	int gender = 0;

	// Entry of Seeker SignUp View
	protected void gotoSeekerSignUpView()
	{
		setContentView(R.layout.signup_seeker);

		// Get input content
		final EditText usernameEditText = (EditText) findViewById(R.id.se_signup_username_ed);
		final EditText passwordEditText = (EditText) findViewById(R.id.se_signup_password_ed);
		final EditText nameEditText = (EditText) findViewById(R.id.se_signup_name_ed);
		RadioButton maleRadioButton = (RadioButton) findViewById(R.id.se_male_rd);
		final EditText schoolEditText = (EditText) findViewById(R.id.se_sign_school_ed);
		final EditText majorEditText = (EditText) findViewById(R.id.se_sign_major_ed);
		final EditText skillEditText = (EditText) findViewById(R.id.se_signup_skill_ed);
		final EditText phoneEditText = (EditText) findViewById(R.id.se_signup_phone_ed);
		Button submitButton = (Button) findViewById(R.id.se_sign_submit_bt);
		Button backButton = (Button) findViewById(R.id.se_signup_back_bt);
		RadioGroup radioGroup = (RadioGroup) maleRadioButton.getParent();
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				// TODO Auto-generated method stub
				if (checkedId == R.id.se_female_rd)
				{
					gender = 1;
				} else if (checkedId == R.id.se_male_rd)
				{
					gender = 0;
				}
			}
		});

		backButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				// back to LoginActivity
				SignUpActivity.this.finish();
			}
		});

		submitButton.setOnClickListener(new OnClickListener()
		{

			public void onClick(View arg0)
			{
				String username = usernameEditText.getText().toString().trim();
				String password = passwordEditText.getText().toString().trim();
				String name = nameEditText.getText().toString().trim();
				String school = schoolEditText.getText().toString().trim();
				String major = majorEditText.getText().toString().trim();
				String skill = skillEditText.getText().toString().trim();
				String phone = phoneEditText.getText().toString().trim();

				if (username.equals("") || password.equals("")
						|| name.equals("") || school.equals("")
						|| major.equals("") || skill.equals("")
						|| phone.equals(""))
				{

					// ensure none blank input and uniq username
					Toast.makeText(SignUpActivity.this,
							"Please fill blank all!", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (!isUsernameUnique)
				{
					Toast.makeText(SignUpActivity.this,
							"Username Exists,Try Another!", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				// submit sign up request
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs
						.add(new BasicNameValuePair("username", username));
				nameValuePairs
						.add(new BasicNameValuePair("password", password));
				nameValuePairs.add(new BasicNameValuePair("name", name));
				nameValuePairs.add(new BasicNameValuePair("school", school));
				nameValuePairs.add(new BasicNameValuePair("major", major));
				nameValuePairs.add(new BasicNameValuePair("skill", skill));
				nameValuePairs
						.add(new BasicNameValuePair("gender", gender + ""));
				nameValuePairs.add(new BasicNameValuePair("phone", phone));
				progressDialog = new ProgressDialog(SignUpActivity.this);
				progressDialog.setTitle("Wait");
				progressDialog.setMessage("Submitting……");
				progressDialog.show();
				httpThread = new HttpRequestThread(handler,
						Constant.HTTP_ACTION_SIGNUP, Constant.SEEKER,
						nameValuePairs);
				handler.post(httpThread);
			}
		});

		usernameEditText.setOnEditorActionListener(new OnEditorActionListener()
		{

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2)
			{
				// TODO Auto-generated method stub
				if (arg1 == EditorInfo.IME_ACTION_DONE)
				{
					String username = usernameEditText.getText().toString()
							.trim();
					checkUniq(username, Constant.SEEKER);
				}
				return false;
			}
		});

		usernameEditText.setOnFocusChangeListener(new OnFocusChangeListener()
		{

			@Override
			public void onFocusChange(View arg0, boolean arg1)
			{
				// TODO Auto-generated method stub
				if (!arg1)
				{
					String username = usernameEditText.getText().toString()
							.trim();
					checkUniq(username, Constant.SEEKER);
				}
			}
		});

	}

	EditText usernameEditText;

	// Entry of Seeker SignUp View
	private void gotoCompanySignUpView()
	{
		setContentView(R.layout.signup_company);

		// Get input content
		usernameEditText = (EditText) findViewById(R.id.com_signup_username_ed);
		final EditText passwordEditText = (EditText) findViewById(R.id.com_signup_password_ed);
		final EditText nameEditText = (EditText) findViewById(R.id.com_signup_name_ed);
		final EditText fieldEditText = (EditText) findViewById(R.id.com_signup_field_ed);
		final EditText locationEditText = (EditText) findViewById(R.id.com_signup_location_ed);
		final EditText phoneEditText = (EditText) findViewById(R.id.com_signup_phone_ed);
		Button submitButton = (Button) findViewById(R.id.com_signup_submit_bt);
		Button backButton = (Button) findViewById(R.id.com_signup_back_bt);

		backButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				// back to LoginActivity
				SignUpActivity.this.finish();
			}
		});

		submitButton.setOnClickListener(new OnClickListener()
		{

			public void onClick(View arg0)
			{
				String username = usernameEditText.getText().toString().trim();
				String password = passwordEditText.getText().toString().trim();
				String name = nameEditText.getText().toString().trim();
				String field = fieldEditText.getText().toString().trim();
				String location = locationEditText.getText().toString().trim();
				String phone = phoneEditText.getText().toString().trim();
				if (username.equals("") || password.equals("")
						|| name.equals("") || field.equals("")
						|| location.equals("") || phone.equals(""))
				{

					// ensure none blank input and uniq username
					Toast.makeText(SignUpActivity.this,
							"Please fill blank all!", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (!isUsernameUnique)
				{
					Toast.makeText(SignUpActivity.this,
							"Username Exists,Try Another!", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				// submit sign up request
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs
						.add(new BasicNameValuePair("username", username));
				nameValuePairs
						.add(new BasicNameValuePair("password", password));
				nameValuePairs.add(new BasicNameValuePair("name", name));
				nameValuePairs.add(new BasicNameValuePair("field", field));
				nameValuePairs
						.add(new BasicNameValuePair("location", location));
				nameValuePairs.add(new BasicNameValuePair("phone", phone));
				progressDialog = new ProgressDialog(SignUpActivity.this);
				progressDialog.setTitle("Wait");
				progressDialog.setMessage("Submitting……");
				progressDialog.show();
				httpThread = new HttpRequestThread(handler,
						Constant.HTTP_ACTION_SIGNUP, Constant.COMPANY,
						nameValuePairs);
				handler.post(httpThread);
			}
		});

		usernameEditText.setOnEditorActionListener(new OnEditorActionListener()
		{

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2)
			{
				// TODO Auto-generated method stub
				if (arg1 == EditorInfo.IME_ACTION_DONE)
				{
					String username = usernameEditText.getText().toString()
							.trim();
					checkUniq(username, Constant.COMPANY);
				}
				return false;
			}
		});

		usernameEditText.setOnFocusChangeListener(new OnFocusChangeListener()
		{

			@Override
			public void onFocusChange(View arg0, boolean arg1)
			{
				// TODO Auto-generated method stub
				if (!arg1)
				{
					String username = usernameEditText.getText().toString()
							.trim();
					checkUniq(username, Constant.COMPANY);
				}
			}
		});

	}

	private void checkUniq(String username, int identity)
	{
		{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			NameValuePair param = new BasicNameValuePair("username", username);
			NameValuePair param1 = new BasicNameValuePair("identity", identity
					+ "");
			params.add(param);
			params.add(param1);
			httpThread = new HttpRequestThread(handler,
					Constant.HTTP_ACTION_CHECKUNI, Constant.COMPANY, params);
			handler.post(httpThread);
		}

	}
}
