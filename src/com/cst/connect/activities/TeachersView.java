package com.cst.connect.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.cst.connect.R;
import com.viewpagerindicator.TitlePageIndicator;

public class TeachersView extends SherlockActivity {

	AlertDialog ep = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_layout);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Ανακοινώσεις Εκπαιδευτικών");

		ViewPagerAdapter adapter = new ViewPagerAdapter(this);
		ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
		TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);
	}

	class ViewPagerAdapter extends PagerAdapter {
		private final String[] titles = new String[] { "Μόνιμοι", "Συνεργάτες" };
		private final Context context;
		private int[] scrollPosition = new int[titles.length];

		String[] monimoi_names = getResources().getStringArray(
				R.array.monimoi_teachers_names);
		String[] monimoi_emails = getResources().getStringArray(
				R.array.monimoi_teachers_emails);
		String[] sunergates_names = getResources().getStringArray(
				R.array.sunergates_names);
		String[] sunergates_emails = getResources().getStringArray(
				R.array.sunergates_emails);

		public ViewPagerAdapter(Context context) {
			this.context = context;
			for (int i = 0; i < titles.length; i++) {
				scrollPosition[i] = 0;
			}
		}

		@Override
		public String getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Object instantiateItem(View pager, int position) {

			ListView v = new ListView(context);

			switch (position) {

			case 0:
				SimpleAdapter monimoi = new SimpleAdapter(context,
						monimoi_list(), R.layout.teachers_list_item,
						new String[] { "Όνομα", "Emails" }, new int[] {
								R.id.names, R.id.mails }) {

					@SuppressWarnings("unchecked")
					@Override
					public View getView(int groupPosition, View convertView,
							ViewGroup parent) {

						if (convertView == null) {
							LayoutInflater infalInflater = (LayoutInflater) context
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							convertView = infalInflater.inflate(
									R.layout.teachers_list_item, null);
						}

						((TextView) convertView.findViewById(R.id.names))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Όνομα"));
						((TextView) convertView.findViewById(R.id.mails))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Emails"));

						return convertView;
					}

				};

				v.setAdapter(monimoi);

				v.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							final int position, long id) {

						if (isOnline()) {
							Intent intent = new Intent(getBaseContext(),
									com.cst.connect.activities.Rss.class);
							intent.putExtra("RSS",
									"http://www.cs.teilar.gr/CS/rss.jsp?id="
											+ monimoi_emails[position]);
							// intent.putExtra("title",
							// monimoi_names[position]);
							intent.putExtra("position", position);
							intent.putExtra("title", "teachers");
							startActivity(intent);

						} else {

							Toast.makeText(
									TeachersView.this,
									"Δεν υπάρχει διαθέσιμη σύνδεση, παρακαλώ συνδεθείτε για να συνεχίσετε.",
									Toast.LENGTH_LONG).show();
						}

					}
				});

				break;

			case 1:

				SimpleAdapter sunergates = new SimpleAdapter(context,
						sunergates_list(), R.layout.teachers_list_item,
						new String[] { "Όνομα", "Emails" }, new int[] {
								R.id.names, R.id.mails })

				{

					@SuppressWarnings("unchecked")
					@Override
					public View getView(int groupPosition, View convertView,
							ViewGroup parent) {

						if (convertView == null) {
							LayoutInflater infalInflater = (LayoutInflater) context
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							convertView = infalInflater.inflate(
									R.layout.teachers_list_item, null);
						}

						((TextView) convertView.findViewById(R.id.names))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Όνομα"));
						((TextView) convertView.findViewById(R.id.mails))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Emails"));

						return convertView;
					}

				};

				v.setAdapter(sunergates);

				v.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							final int position, long id) {

						if (isOnline()) {
							Intent intent = new Intent(getBaseContext(),
									com.cst.connect.activities.Rss.class);
							intent.putExtra("RSS",
									"http://www.cs.teilar.gr/CS/rss.jsp?id="
											+ sunergates_emails[position]);
							intent.putExtra("title", "teachers");
							intent.putExtra("position", position + 17);
							// intent.putExtra("title",
							// sunergates_names[position]);
							startActivity(intent);

						} else {

							Toast.makeText(
									TeachersView.this,
									"Δεν υπάρχει διαθέσιμη σύνδεση, παρακαλώ συνδεθείτε για να συνεχίσετε.",
									Toast.LENGTH_LONG).show();
						}

					}
				});
				break;

			default:
				break;

			}
			((ViewPager) pager).addView(v, 0);
			return v;
		}

		private List<HashMap<String, Object>> monimoi_list() {
			ArrayList<HashMap<String, Object>> monimoi = new ArrayList<HashMap<String, Object>>();

			for (int i = 0; i < monimoi_names.length; i++) {
				HashMap<String, Object> m = new HashMap<String, Object>();

				m.put("Όνομα", monimoi_names[i]);
				m.put("Emails", monimoi_emails[i]);

				monimoi.add(m);
			}
			return (List<HashMap<String, Object>>) monimoi;
		}

		private List<HashMap<String, Object>> sunergates_list() {
			ArrayList<HashMap<String, Object>> sunergates = new ArrayList<HashMap<String, Object>>();

			for (int i = 0; i < sunergates_names.length; i++) {
				HashMap<String, Object> m = new HashMap<String, Object>();

				m.put("Όνομα", sunergates_names[i]);
				m.put("Emails", sunergates_emails[i]);
				sunergates.add(m);
			}
			return (List<HashMap<String, Object>>) sunergates;
		}

		@Override
		public void destroyItem(View pager, int position, Object view) {
			((ViewPager) pager).removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void finishUpdate(View view) {
		}

		@Override
		public void restoreState(Parcelable p, ClassLoader c) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View view) {
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();

		}
		return true;
	}

	private Boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnected())
			return true;

		return false;
	}

}
