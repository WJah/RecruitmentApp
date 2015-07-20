package com.recruitmentclient.control;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xml.sax.SAXException;

import com.recruitmentclient.activity.LoginActivity;
import com.recruitmentclient.model.Applicant;
import com.recruitmentclient.model.Company;
import com.recruitmentclient.model.Job;
import com.recruitmentclient.model.Seeker;

import android.R.bool;
import android.R.integer;
import android.R.menu;
import android.R.string;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract.Constants;

/**
 * 
 * @author Administrator This thread is to do http request to server
 */
public class HttpRequestThread extends Thread
{
	Handler handler = null;
	int action = 0;
	List<NameValuePair> params;
	int identity = 0;// to identify seeker and company

	public HttpRequestThread(Handler handler, int action, int identity,
			List<NameValuePair> params)
	{
		this.handler = handler;
		this.action = action;
		this.params = params;
		this.identity = identity;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		super.run();
		switch (action)
		{
		case Constant.HTTP_ACTION_SIGNUP:
			signUp(identity);
			break;

		case Constant.HTTP_ACTION_CHECKUNI:
			checkUniq();
			break;

		case Constant.HTTP_ACTION_LOGIN:
			login(identity);
			break;

		case Constant.HTTP_ACTION_JOB_SEARCH:
			jobSearch();
			break;

		case Constant.HTTP_ACTION_UPDATE:
			update();
			break;

		case Constant.HTTP_ACTION_GET_APPLIER:
			getApplicant();
			break;

		case Constant.HTTP_ACTION_ADD_JOB:
			addJob();
			break;
			
		case Constant.HTTP_ACTION_DELETE:
			delete();
			break;
		}
	}

	public void addJob()
	{
		String signUpURL = Constant.URL + "signup";
		HttpPost request = new HttpPost(signUpURL);
		try
		{
			NameValuePair idNameValuePair = new BasicNameValuePair("identity",
					identity + "");
			params.add(idNameValuePair);
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				handler.sendEmptyMessage(Constant.COM_ADD);
			}
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getApplicant()
	{
		String signUpURL = Constant.URL + "getapplier";
		HttpPost request = new HttpPost(signUpURL);
		try
		{
			NameValuePair idNameValuePair = new BasicNameValuePair("identity",
					identity + "");
			params.add(idNameValuePair);
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				String result = EntityUtils.toString(response.getEntity())
						.trim();
				System.out.println(result + "result");
				List<Applicant> applicants = ParseXML.parseToAppliers(result);
				Message message = new Message();
				message.what = Constant.COM_APPLICANT;
				message.obj = applicants;
				// If login success , server return .xml file
				handler.sendMessage(message);
			}
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void delete()
	{
		String deleteURL = Constant.URL + "delete";
		HttpPost request = new HttpPost(deleteURL);
		try
		{
			NameValuePair idNameValuePair = new BasicNameValuePair("identity",
					identity + "");
			params.add(idNameValuePair);
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				String result = EntityUtils.toString(response.getEntity())
						.trim();
				System.out.println(result + "result");
				// If login success , server return .xml file
				handler.sendEmptyMessage(Constant.FAIL);
			}
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void update()
	{
		String updateURL = Constant.URL + "update";
		HttpPost request = new HttpPost(updateURL);
		try
		{
			NameValuePair idNameValuePair = new BasicNameValuePair("identity",
					identity + "");
			params.add(idNameValuePair);
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				String result = EntityUtils.toString(response.getEntity())
						.trim();
				System.out.println(result + "result");
				// If login success , server return .xml file
				boolean success = result.length() > 5 ? true : false;
				if (success)
				{
				} else if (!success)
				{
					handler.sendEmptyMessage(Constant.FAIL);
				}
			}
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void jobSearch()
	{
		String signUpURL = Constant.URL + "jobsearch";
		HttpPost request = new HttpPost(signUpURL);
		try
		{
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				String result = EntityUtils.toString(response.getEntity())
						.trim();
				System.out.println(result + "result");
				// If login success , server return .xml file
				boolean success = result.length() > 5 ? true : false;
				if (success)
				{
					List<Job> jobs = ParseXML.parseToJobs(result);
					System.out.println("parse " + jobs);
					Message message = new Message();
					message.obj = jobs;
					message.what = Constant.SUCCESS;
					handler.sendMessage(message);
				} else if (!success)
				{
					handler.sendEmptyMessage(Constant.FAIL);
				}
			}
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void login(int identity)
	{
		String signUpURL = Constant.URL + "login";
		HttpPost request = new HttpPost(signUpURL);
		try
		{
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				String result = EntityUtils.toString(response.getEntity())
						.trim();
				System.out.println(result + "result");
				// If login success , server return .xml file
				boolean success = result.length() > 5 ? true : false;
				if (success)
				{
					if (identity == Constant.SEEKER)
					{
						Seeker seeker = ParseXML.parseToSeeker(result
								.substring(4));
						System.out.println("parse " + seeker);
						Message message = new Message();
						message.obj = seeker;
						message.what = Constant.SUCCESS;
						handler.sendMessage(message);
					} else if (identity == Constant.COMPANY)
					{
						Company company = ParseXML.parseToCompany(result
								.substring(4));
						System.out.println("parse " + company);
						Message message = new Message();
						message.obj = company;
						message.what = Constant.SUCCESS;
						handler.sendMessage(message);
					}

				} else if (!success)
				{
					handler.sendEmptyMessage(Constant.FAIL);
				}
			}
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String signUp(int identity)
	{

		String signUpURL = Constant.URL + "signup";// sign
													// up
													// request
													// url
		HttpPost request = new HttpPost(signUpURL);
		try
		{
			NameValuePair idNameValuePair = new BasicNameValuePair("identity",
					identity + "");
			params.add(idNameValuePair);
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			System.out.println(response.getStatusLine().getReasonPhrase());
			System.out.println("signresponse:"
					+ response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				handler.sendEmptyMessage(Constant.HTTP_ACTION_SIGNUP);
			}
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean checkUniq()
	{
		String signUpURL = Constant.URL + "checkuniq";
		HttpPost request = new HttpPost(signUpURL);
		try
		{
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				Message message = new Message();
				message.what = Constant.HTTP_ACTION_CHECKUNI;
				String a = EntityUtils.toString(response.getEntity());
				if (a.equals("true"))
				{
					message.arg1 = 0;
				} else if (a.equals("false"))
				{
					message.arg1 = 1;
				}
				handler.sendMessage(message);
			}
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
