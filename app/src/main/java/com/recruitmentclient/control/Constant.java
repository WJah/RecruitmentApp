package com.recruitmentclient.control;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

public class Constant
{
	public final static String URL = "http://10.0.2.2:8080/RecruitmentServer/";

	// Result Flag
	public final static int FAIL = 0;
	public final static int SUCCESS = 1;

	public final static int MALE = 1;
	public final static int FEMALE = 0;

	// Identity Flag
	public final static int SEEKER = 1;
	public final static int COMPANY = 0;
	public final static int JOB = 2;

	public final static int COM_RELEASE = SUCCESS;
	public final static int COM_ADD = 0;
	public final static int COM_APPLICANT = 2;

	public final static int HTTP_ACTION_SIGNUP = 0;
	public final static int HTTP_ACTION_CHECKUNI = 1;
	public final static int HTTP_ACTION_LOGIN = 2;
	public final static int HTTP_ACTION_JOB_SEARCH = 3;
	public final static int HTTP_ACTION_UPDATE = 4;
	public final static int HTTP_ACTION_GET_APPLIER = 5;
	public final static int HTTP_ACTION_ADD_JOB = 6;
	public final static int HTTP_ACTION_DELETE = 7;

	public final static int SEARCH_MODE_COMPANY_NAME = 0;
	public final static int SEARCH_MODE_JOB_POSITION = 1;
	public final static int SEARCH_MODE_AVG_SALARY = 2;
	public final static int SEARCH_MODE_JOB_SALARY = 3;
	public final static int SEARCH_MODE_SKILL = 4;
	public final static int SEARCH_MODE_COMPANY_ID = 5;

	public static void exit(final Activity activity)
	{
		Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Exit?");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				System.exit(0);
			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener()
				{

					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method
						// stub

					}
				});
		builder.show();
	}

}
