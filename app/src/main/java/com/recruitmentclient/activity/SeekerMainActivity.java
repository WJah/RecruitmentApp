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
import com.recruitmentclient.model.Job;
import com.recruitmentclient.model.Seeker;

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

/*
 */
public class SeekerMainActivity extends Activity
{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] Titles = new String[]
	{ "Reconmend", "Search", "Profile" };

	private String userName;
	private static Seeker seeker;

	Handler handler;
	HttpRequestThread httpRequestThread;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		HandlerThread handlerThread = new HandlerThread("MainThread");
		handlerThread.start();
		handler = new Handler(handlerThread.getLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				// TODO Auto-generated method stub
				progressDialog.cancel();
				List<Job> jobs = (ArrayList<Job>) msg.obj;
				Fragment fragment = new CompanyListFragment();
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("jobs",
						(ArrayList<? extends Parcelable>) jobs);
				fragment.setArguments(bundle);
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();
			}
		};
		Intent intent = getIntent();
		userName = intent.getStringExtra("username");
		seeker = new Seeker();
		Bundle bundle = intent.getBundleExtra("seeker");
		seeker = (Seeker) bundle.get("seeker");
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
		recommendMap.put("title", "Recomend");
		recommendMap.put("count", "");

		Map<String, String> appliedMap = new HashMap<String, String>();
		appliedMap.put("title", "Search");
		appliedMap.put("count", "");

		list.add(recommendMap);
		list.add(appliedMap);
		list.add(userMap);

		SimpleAdapter listAdapter = new SimpleAdapter(SeekerMainActivity.this, list,
				R.layout.seeker_drawer_item, new String[]
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
			// Get recommend Jobs
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("condition", seeker.getSkill()));
			// According seeker's skill to recommend job
			params.add(new BasicNameValuePair("mode",
					Constant.SEARCH_MODE_SKILL + ""));
			httpRequestThread = new HttpRequestThread(handler,
					Constant.HTTP_ACTION_JOB_SEARCH, Constant.SEEKER, params);
			progressDialog = new ProgressDialog(this);
			progressDialog.setTitle("Wait!");
			progressDialog.setMessage("Searching……");
			progressDialog.show();
			handler.post(httpRequestThread);
			// fragment = new CompanyListFragment();

		} else if (position == 1)
		{
			fragment = new SearchFragment();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			setTitle(Titles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);

		} else if (position == 2)
		{
			Bundle args = new Bundle();
			fragment = new UserInfoFragment();
			args.putSerializable("seeker", seeker);
			fragment.setArguments(args);
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

			// update selected item and title, then close the drawer
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

	public static class SearchFragment extends Fragment implements
			OnClickListener
	{
		EditText companySearchEditText;
		EditText jobSearchEditText;
		EditText jobSalaEditText;
		EditText avgSalaEditText;

		HttpRequestThread httpRequestThread;
		Handler handler;
		ProgressDialog progressDialog;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.search_seeker, container,
					false);
			companySearchEditText = (EditText) rootView
					.findViewById(R.id.seeker_search_company_ed);
			Button companySearchButton = (Button) rootView
					.findViewById(R.id.seeker_search_company_bt);
			companySearchButton.setOnClickListener(this);

			jobSearchEditText = (EditText) rootView
					.findViewById(R.id.seeker_search_job_ed);
			Button jobSearchButton = (Button) rootView
					.findViewById(R.id.seeker_search_job_bt);
			jobSearchButton.setOnClickListener(this);

			jobSalaEditText = (EditText) rootView
					.findViewById(R.id.seeker_search_company_sala_ed);
			Button jobSalaButton = (Button) rootView
					.findViewById(R.id.seeker_search_job_salary_bt);

			jobSalaButton.setOnClickListener(this);

			avgSalaEditText = (EditText) rootView
					.findViewById(R.id.seeker_search_avg_sala_ed);
			Button avgSalaButton = (Button) rootView
					.findViewById(R.id.seeker_search_avg_salary_bt);
			avgSalaButton.setOnClickListener(this);

			HandlerThread handlerThread = new HandlerThread("SearchHandler");
			handlerThread.start();
			handler = new Handler(handlerThread.getLooper())
			{
				@Override
				public void handleMessage(Message msg)
				{
					// TODO Auto-generated method stub
					progressDialog.cancel();
					List<Job> jobs = (ArrayList<Job>) msg.obj;
					Fragment fragment = new CompanyListFragment();
					Bundle bundle = new Bundle();
					bundle.putParcelableArrayList("jobs",
							(ArrayList<? extends Parcelable>) jobs);
					bundle.putSerializable("seeker", seeker);
					fragment.setArguments(bundle);
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.content_frame, fragment).commit();
				}
			};
			return rootView;
		}

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			switch (v.getId())
			{
			case R.id.seeker_search_company_bt:
				String condition1 = companySearchEditText.getText().toString()
						.trim();
				if (condition1.equals(""))
				{
					return;
				}
				List<NameValuePair> params1 = new ArrayList<NameValuePair>();
				params1.add(new BasicNameValuePair("condition", condition1));
				params1.add(new BasicNameValuePair("mode",
						Constant.SEARCH_MODE_COMPANY_NAME + ""));
				httpRequestThread = new HttpRequestThread(handler,
						Constant.HTTP_ACTION_JOB_SEARCH, Constant.SEEKER,
						params1);
				showProgress();
				handler.post(httpRequestThread);
				break;

			case R.id.seeker_search_job_bt:
				String condition2 = jobSearchEditText.getText().toString()
						.trim();
				if (condition2.equals(""))
				{
					return;
				}
				List<NameValuePair> params2 = new ArrayList<NameValuePair>();
				params2.add(new BasicNameValuePair("condition", condition2));
				params2.add(new BasicNameValuePair("mode",
						Constant.SEARCH_MODE_JOB_POSITION + ""));
				httpRequestThread = new HttpRequestThread(handler,
						Constant.HTTP_ACTION_JOB_SEARCH, Constant.SEEKER,
						params2);
				showProgress();
				handler.post(httpRequestThread);

				break;

			case R.id.seeker_search_job_salary_bt:
				String condition3 = jobSalaEditText.getText().toString().trim();
				if (condition3.equals(""))
				{
					return;
				}
				List<NameValuePair> params3 = new ArrayList<NameValuePair>();
				params3.add(new BasicNameValuePair("condition", condition3));
				params3.add(new BasicNameValuePair("mode",
						Constant.SEARCH_MODE_JOB_SALARY + ""));
				httpRequestThread = new HttpRequestThread(handler,
						Constant.HTTP_ACTION_JOB_SEARCH, Constant.SEEKER,
						params3);
				showProgress();
				handler.post(httpRequestThread);
				break;

			case R.id.seeker_search_avg_salary_bt:
				String condition4 = avgSalaEditText.getText().toString().trim();
				if (condition4.equals(""))
				{
					return;
				}
				List<NameValuePair> params4 = new ArrayList<NameValuePair>();
				params4.add(new BasicNameValuePair("condition", condition4));
				params4.add(new BasicNameValuePair("mode",
						Constant.SEARCH_MODE_AVG_SALARY + ""));
				httpRequestThread = new HttpRequestThread(handler,
						Constant.HTTP_ACTION_JOB_SEARCH, Constant.SEEKER,
						params4);
				showProgress();
				handler.post(httpRequestThread);

				break;
			}
		}

		private void showProgress()
		{
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setTitle("Wait!");
			progressDialog.setMessage("Searching……");
			progressDialog.show();
		}
	}

	public static class CompanyListFragment extends ListFragment
	{
		Context context;
		List<Job> jobs;
		HttpRequestThread httpRequestThread;
		HandlerThread handlerThread;
		Handler handler;
		ProgressDialog progressDialog;

		@Override
		public void onActivityCreated(Bundle savedInstanceState)
		{
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			// TODO Auto-generated method stub
			View rootView = inflater.inflate(R.layout.job_list, container,
					false);
			handlerThread = new HandlerThread("CompanyList");
			handlerThread.start();
			handler = new Handler(handlerThread.getLooper())
			{
				public void handleMessage(Message msg)
				{
					progressDialog.cancel();
					this.removeCallbacks(httpRequestThread);
					Toast.makeText(getActivity(), "Applied Success!",
							Toast.LENGTH_SHORT).show();
				};
			};
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			jobs = (List<Job>) getArguments().get("jobs");
			for (Iterator<Job> iterator = jobs.iterator(); iterator.hasNext();)
			{
				Job job = (Job) iterator.next();
				Map<String, String> map = new HashMap<String, String>();
				map.put("position", job.getPosition());
				map.put("company", job.getName());
				map.put("location", job.getLocation());
				map.put("salary", job.getSalary());
				list.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
					R.layout.job_item, new String[]
					{ "position", "company", "location", "salary" }, new int[]
					{ R.id.job_item_position, R.id.job_item_company,
							R.id.job_item_location, R.id.job_item_salary });
			setListAdapter(adapter);

			return rootView;
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id)
		{
			// TODO Auto-generated method stub
			Job job = jobs.get(position);
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			View rootView = LayoutInflater.from(getActivity()).inflate(
					R.layout.job_info, null);
			TextView nameTextView = (TextView) rootView
					.findViewById(R.id.info_job_company);
			TextView salaryTextView = (TextView) rootView
					.findViewById(R.id.info_job_salary);
			TextView skillTextView = (TextView) rootView
					.findViewById(R.id.info_job_skill);
			TextView locaTextView = (TextView) rootView
					.findViewById(R.id.info_job_location);
			TextView posiTextView = (TextView) rootView
					.findViewById(R.id.info_job_position);
			TextView phoneTextView = (TextView) rootView
					.findViewById(R.id.info_seeker_phone);
			nameTextView.setText(job.getName());
			salaryTextView.setText(job.getSalary());
			skillTextView.setText(job.getSkill());
			locaTextView.setText(job.getLocation());
			posiTextView.setText(job.getPosition());
			phoneTextView.setText(job.getPhone());
			final String job_id = job.getJob_id();
			System.out.println(job_id);
			builder.setView(rootView);
			builder.setTitle("Job:");
			builder.setPositiveButton("Apply",
					new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							progressDialog = new ProgressDialog(getActivity());
							progressDialog.setTitle("Wait!");
							progressDialog.setMessage("Applying……");
							progressDialog.show();
							List<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("job_id", job_id
									+ ""));
							params.add(new BasicNameValuePair("seekers", seeker
									.getUsername()));
							httpRequestThread = new HttpRequestThread(handler,
									Constant.HTTP_ACTION_UPDATE,
									Constant.SEEKER, params);
							handler.post(httpRequestThread);
						}
					});
			builder.setNegativeButton("Cancel", null);
			builder.show();
		}
	}

	/**
	 * Fragment that appears in the "content_frame", shows a planet
	 */
	public static class UserInfoFragment extends Fragment
	{
		public static final String ARG_PLANET_NUMBER = "planet_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.info_seeker, container,
					false);
			Seeker seeker = (Seeker) this.getArguments().get("seeker");
			TextView name = (TextView) rootView
					.findViewById(R.id.info_seeker_name);
			TextView phone = (TextView) rootView
					.findViewById(R.id.info_seeker_phone);
			TextView school = (TextView) rootView
					.findViewById(R.id.info_seeker_school);
			TextView major = (TextView) rootView
					.findViewById(R.id.info_seeker_major);
			TextView skill = (TextView) rootView
					.findViewById(R.id.info_seeker_skill);
			Button update = (Button) rootView.findViewById(R.id.info_update_bt);
			name.setText(seeker.getName());
			phone.setText(seeker.getPhone());
			school.setText(seeker.getSchool());
			major.setText(seeker.getMajor());
			skill.setText(seeker.getSkill());
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
