/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.recruitmentclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.recruitmentapp.R;
import com.recruitmentapp.R.layout;
import com.recruitmentclient.control.Constant;
import com.recruitmentclient.control.HttpRequestThread;
import com.recruitmentclient.model.Applicant;
import com.recruitmentclient.model.Company;
import com.recruitmentclient.model.Job;
import com.recruitmentclient.model.Seeker;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Choreographer.FrameCallback;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This example illustrates a common usage of the DrawerLayout widget in the
 * Android support library.
 * <p/>
 * <p>
 * When a navigation (left) drawer is present, the host activity should detect
 * presses of the action bar's Up affordance as a signal to open and close the
 * navigation drawer. The ActionBarDrawerToggle facilitates this behavior. Items
 * within the drawer should fall into one of two categories:
 * </p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic
 * policies as list or tab navigation in that a view switch does not create
 * navigation history. This pattern should only be used at the root activity of
 * a task, leaving some form of Up navigation active for activities further down
 * the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an
 * alternate parent for Up navigation. This allows a user to jump across an
 * app's navigation hierarchy at will. The application should treat this as it
 * treats Up navigation from a different task, replacing the current task stack
 * using TaskStackBuilder or similar. This is the only form of navigation drawer
 * that should be used outside of the root activity of a task.</li>
 * </ul>
 * <p/>
 * <p>
 * Right side drawers should be used for actions, not navigation. This follows
 * the pattern established by the Action Bar that navigation should be to the
 * left and actions to the right. An action should be an operation performed on
 * the current contents of the window, for example enabling or disabling a data
 * overlay on top of the current content.
 * </p>
 */
public class ComMainActivity extends Activity
{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] Titles = new String[]
	{ "Released", "Applicant", "Provide", "Profile" };

	private String userName;
	private static Company company;
	private static List<Job> jobs;
	private static List<Applicant> applicants;

	private static Handler handler;
	private static HttpRequestThread httpRequestThread;
	private static ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		HandlerThread handlerThread = new HandlerThread("MainThread");
		handlerThread.start();
		handler = new Handler(handlerThread.getLooper())
		{
			public void handleMessage(Message msg)
			{
				// TODO Auto-generated method stub
				switch (msg.what)
				{
				case Constant.COM_RELEASE:
					progressDialog.cancel();
					handler.removeCallbacks(httpRequestThread);
					jobs = (ArrayList<Job>) msg.obj;
					Fragment fragment1 = new ReleaseedJobFragment();
					FragmentManager fragmentManager1 = getFragmentManager();
					fragmentManager1.beginTransaction()
							.replace(R.id.content_frame, fragment1).commit();

					break;

				case Constant.COM_APPLICANT:
					progressDialog.cancel();
					handler.removeCallbacks(httpRequestThread);
					applicants = (ArrayList<Applicant>) msg.obj;
					Fragment fragment = new AppliersFragment();
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.content_frame, fragment).commit();

					break;

				case Constant.COM_ADD:
					progressDialog.cancel();
					handler.removeCallbacks(httpRequestThread);
					Toast.makeText(ComMainActivity.this, "Submited!",
							Toast.LENGTH_SHORT).show();
					break;
				}

			}
		};
		Intent intent = getIntent();
		userName = intent.getStringExtra("username");
		company = new Company();
		Bundle bundle = intent.getBundleExtra("company");
		company = (Company) bundle.get("company");
		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> userMap = new HashMap<String, String>();
		userMap.put("title", userName);
		userMap.put("count", "");

		Map<String, String> recommendMap = new HashMap<String, String>();
		recommendMap.put("title", "Released");
		recommendMap.put("count", "");

		Map<String, String> appliedMap = new HashMap<String, String>();
		appliedMap.put("title", "Applicant");
		appliedMap.put("count", "");

		Map<String, String> provideMap = new HashMap<String, String>();
		provideMap.put("title", "Provide");
		provideMap.put("count", "");

		list.add(recommendMap);
		list.add(appliedMap);
		list.add(provideMap);
		list.add(userMap);

		SimpleAdapter listAdapter = new SimpleAdapter(ComMainActivity.this,
				list, R.layout.seeker_drawer_item, new String[]
				{ "title", "count" }, new int[]
				{ R.id.seeker_drawer_item_recommend,
						R.id.seeker_drawer_item_count });
		// Set the adapter for the list view
		mDrawerList.setAdapter(listAdapter);
		// set up the drawer's list view with items and click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		)
		{
			public void onDrawerClosed(View view)
			{
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView)
			{
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null)
		{
			selectItem(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}
		// Handle action buttons
		switch (item.getItemId())
		{
		case 0:
			// create intent to perform web search for this planet
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			// catch event that there's no activity to handle intent
			if (intent.resolveActivity(getPackageManager()) != null)
			{
				startActivity(intent);
			} else
			{
				Toast.makeText(this, R.string.app_not_available,
						Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			selectItem(position);
		}
	}

	private void selectItem(int position)
	{
		// update the main content by replacing fragments
		Fragment fragment = null;
		if (position == 0)
		{
			String com_id = company.getId();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("condition", com_id));
			params.add(new BasicNameValuePair("mode",
					Constant.SEARCH_MODE_COMPANY_ID + ""));
			httpRequestThread = new HttpRequestThread(handler,
					Constant.HTTP_ACTION_JOB_SEARCH, Constant.COMPANY, params);
			handler.post(httpRequestThread);
			progressDialog = new ProgressDialog(ComMainActivity.this);
			progressDialog.setTitle("Wait!");
			progressDialog.setMessage("Updating……");
			progressDialog.show();
			mDrawerList.setItemChecked(position, true);
			setTitle(Titles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else if (position == 1)
		{
			String com_id = company.getId();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("com_id", com_id));
			httpRequestThread = new HttpRequestThread(handler,
					Constant.HTTP_ACTION_GET_APPLIER, Constant.COMPANY, params);
			handler.post(httpRequestThread);
			progressDialog = new ProgressDialog(ComMainActivity.this);
			progressDialog.setTitle("Wait!");
			progressDialog.setMessage("Updating……");
			progressDialog.show();
			mDrawerList.setItemChecked(position, true);
			setTitle(Titles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);

		} else if (position == 2)
		{
			fragment = new ProvideFragment();
			FragmentManager manager = getFragmentManager();
			manager.beginTransaction().replace(R.id.content_frame, fragment)
					.commit();
			mDrawerList.setItemChecked(position, true);
			setTitle(Titles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}

		else if (position == 3)
		{
			fragment = new UserInfoFragment();
			FragmentManager manager = getFragmentManager();
			manager.beginTransaction().replace(R.id.content_frame, fragment)
					.commit();
			mDrawerList.setItemChecked(position, true);
			setTitle(Titles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}

	@Override
	public void setTitle(CharSequence title)
	{
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public static class ReleaseedJobFragment extends ListFragment
	{
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			// TODO Auto-generated method stub
			View rootView = inflater.inflate(R.layout.job_list, container,
					false);
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			for (Iterator<Job> iterator = jobs.iterator(); iterator.hasNext();)
			{
				Job job = (Job) iterator.next();
				Map<String, String> map = new HashMap<String, String>();
				map.put("position", job.getPosition());
				map.put("location", job.getLocation());
				map.put("salary", job.getSalary());
				list.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
					R.layout.release_item, new String[]
					{ "position", "location", "salary" }, new int[]
					{ R.id.release_item_position, R.id.release_item_location,
							R.id.release_item_salary });
			setListAdapter(adapter);
			return rootView;
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id)
		{
			// TODO Auto-generated method stub

			// TODO Auto-generated method stub
			final Job job = jobs.get(position);
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			View rootView = LayoutInflater.from(getActivity()).inflate(
					R.layout.job_update, null);
			final EditText positionEditText = (EditText) rootView
					.findViewById(R.id.job_update_position_ed);
			final EditText skillEditText = (EditText) rootView
					.findViewById(R.id.job_update_skill_ed);
			final EditText salaryEditText = (EditText) rootView
					.findViewById(R.id.job_update_salary_ed);
			positionEditText.setText(job.getPosition());
			skillEditText.setText(job.getSkill());
			salaryEditText.setText(job.getSalary());
			builder.setView(rootView);
			builder.setTitle("Job:");
			builder.setPositiveButton("Update",
					new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							String position = positionEditText.getText()
									.toString().trim();
							String salary = salaryEditText.getText().toString()
									.trim();
							String skill = skillEditText.getText().toString()
									.trim();
							if (position.equals("") || salary.equals("")
									|| skill.equals(""))
							{
								Toast.makeText(getActivity(),
										"Fill all the blank",
										Toast.LENGTH_SHORT).show();
								return;
							}
							// TODO Auto-generated method stub
							progressDialog = new ProgressDialog(getActivity());
							progressDialog.setTitle("Wait!");
							progressDialog.setMessage("Updating……");
							progressDialog.show();
							List<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("company_id",
									company.getId()));
							params.add(new BasicNameValuePair("job_id", job
									.getJob_id()));
							params.add(new BasicNameValuePair("skill", skill));
							params.add(new BasicNameValuePair("salary", salary));
							params.add(new BasicNameValuePair("position",
									position));
							httpRequestThread = new HttpRequestThread(handler,
									Constant.HTTP_ACTION_UPDATE, Constant.JOB,
									params);
							handler.post(httpRequestThread);
						}
					});
			builder.setNeutralButton("Delete",
					new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							progressDialog = new ProgressDialog(getActivity());
							progressDialog.setTitle("Wait!");
							progressDialog.setMessage("Deleting……");
							progressDialog.show();
							List<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("job_id", job
									.getJob_id()));
							params.add(new BasicNameValuePair("company_id",
									company.getId()));
							httpRequestThread = new HttpRequestThread(handler,
									Constant.HTTP_ACTION_DELETE, Constant.JOB,
									params);
							handler.post(httpRequestThread);
						}
					});
			builder.setNegativeButton("Cancel", null);
			builder.show();

		}
	}

	public static class AppliersFragment extends ListFragment
	{
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			// TODO Auto-generated method stub
			View rootView = inflater.inflate(R.layout.job_list, container,
					false);
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			for (Iterator<Applicant> iterator = applicants.iterator(); iterator
					.hasNext();)
			{
				Applicant applier = (Applicant) iterator.next();
				Map<String, String> map = new HashMap<String, String>();
				map.put("position", applier.getPosition());
				int gender = applier.getGender();
				if (gender == Constant.MALE)
				{
					map.put("gender", "Male");
				} else
				{
					map.put("gender", "Female");
				}
				map.put("name", applier.getName());
				map.put("major", applier.getMajor());
				list.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
					R.layout.applier_item, new String[]
					{ "position", "name", "gender", "major" }, new int[]
					{ R.id.applier_item_position, R.id.applier_item_name,
							R.id.applier_item_gender, R.id.applier_item_major });
			setListAdapter(adapter);
			return rootView;
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id)
		{
			// TODO Auto-generated method stub
			final Applicant applicant = applicants.get(position);
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			View rootView = LayoutInflater.from(getActivity()).inflate(
					R.layout.applicant_info, null);
			TextView nameTextView = (TextView) rootView
					.findViewById(R.id.info_applicant_name);
			TextView majorTextView = (TextView) rootView
					.findViewById(R.id.info_applicant_major);
			TextView skillTextView = (TextView) rootView
					.findViewById(R.id.info_applicant_skill);
			TextView phoneTextView = (TextView) rootView
					.findViewById(R.id.info_applicant_phone);
			TextView schoolTextView = (TextView) rootView
					.findViewById(R.id.info_applicant_school);
			nameTextView.setText(applicant.getName());
			skillTextView.setText(applicant.getSkill());
			majorTextView.setText(applicant.getMajor());
			schoolTextView.setText(applicant.getSchool());
			phoneTextView.setText(applicant.getPhone());
			builder.setView(rootView);
			builder.setTitle(applicant.getPosition());
			builder.setPositiveButton("Call",
					new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							Uri uri = Uri.parse("tel:" + applicant.getPhone());
							Intent intent = new Intent(Intent.ACTION_DIAL, uri);
							startActivity(intent);
						}
					});
			builder.setNegativeButton("Cancel", null);
			builder.show();
		}
	}

	public static class ProvideFragment extends Fragment
	{
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			// TODO Auto-generated method stub
			View rootView = inflater.inflate(R.layout.job_provide, container,
					false);
			final EditText positionEditText = (EditText) rootView
					.findViewById(R.id.job_provide_position_ed);
			final EditText skillEditText = (EditText) rootView
					.findViewById(R.id.job_provide_skill_ed);
			final EditText salaryEditText = (EditText) rootView
					.findViewById(R.id.job_provide_salary_ed);
			Button submitButton = (Button) rootView
					.findViewById(R.id.job_provide_submit_bt);

			submitButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					String position = positionEditText.getText().toString()
							.trim();
					String skill = skillEditText.getText().toString().trim();
					String salary = salaryEditText.getText().toString().trim();
					// TODO Auto-generated method stub
					if (position.equals("") || skill.equals("")
							|| salary.equals(""))
					{
						Toast.makeText(getActivity(), "Fill all the blank",
								Toast.LENGTH_SHORT).show();
						return;
					}
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("position", position));
					params.add(new BasicNameValuePair("company_id", company
							.getId()));
					params.add(new BasicNameValuePair("skill", skill));
					params.add(new BasicNameValuePair("salary", salary));
					progressDialog = new ProgressDialog(getActivity());
					progressDialog.setTitle("Wait!");
					progressDialog.setMessage("Submitting……");
					progressDialog.show();
					httpRequestThread = new HttpRequestThread(handler,
							Constant.HTTP_ACTION_ADD_JOB, Constant.JOB, params);
					handler.post(httpRequestThread);
				}
			});
			return rootView;
		}
	}

	/**
	 * Fragment that appears in the "content_frame", shows a planet
	 */
	public static class UserInfoFragment extends Fragment
	{
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.info_com, container,
					false);

			TextView name = (TextView) rootView
					.findViewById(R.id.info_com_name);
			TextView phone = (TextView) rootView
					.findViewById(R.id.info_com_phone);
			TextView field = (TextView) rootView
					.findViewById(R.id.info_com_field);
			TextView location = (TextView) rootView
					.findViewById(R.id.info_com_location);
			Button update = (Button) rootView.findViewById(R.id.info_update_bt);
			name.setText(company.getName());
			phone.setText(company.getPhone());
			location.setText(company.getLocation());
			field.setText(company.getField());
			update.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{

				}
			});
			return rootView;
		}
	}
}
