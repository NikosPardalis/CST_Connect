package com.cst.connect;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class TeachersList extends SherlockActivity {

	ArrayList<HashMap<String, String>> infoList;
	Intent intent;

	ListAdapter adapter;
	String[] teachers, emails;
	Context context = this;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Εκπαιδευτικό Προσωπικό");

		teachers = getResources().getStringArray(R.array.Teachers);
		emails = getResources().getStringArray(R.array.Emails);

		infoList = new ArrayList<HashMap<String, String>>();
		infoList.clear();

		for (int i = 0; i < teachers.length; i++) {

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("teachers", teachers[i]);
			map.put("emails", emails[i]);
			infoList.add(map);
		}
		ListView lv = (ListView) findViewById(android.R.id.list);

		adapter = new SimpleAdapter(context, infoList,
				R.layout.teachers_list_item, new String[] { "teachers",
						"emails" }, new int[] { R.id.names, R.id.mails}) {

			@SuppressWarnings("unchecked")
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				if (convertView == null) {
					LayoutInflater infalInflater = (LayoutInflater) context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = infalInflater.inflate(
							R.layout.teachers_list_item, null);
				}

				((TextView) convertView.findViewById(R.id.names))
						.setText((String) ((HashMap<String, String>) getItem(position))
								.get("teachers"));
				((TextView) convertView.findViewById(R.id.mails))
						.setText((String) ((HashMap<String, String>) getItem(position))
								.get("emails"));

				return convertView;
			}

		};

		lv.setAdapter(adapter);

		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (isOnline()) {
					Intent intent = new Intent("com.cst.connect.Anakoinoseis");
					intent.putExtra("RSS",
							"http://www.cs.teilar.gr/CS/rss.jsp?id="
									+ emails[position]);
					intent.putExtra("title", teachers[position]);
					startActivity(intent);

				} else {

					Toast.makeText(
							TeachersList.this,
							"Δεν υπάρχει διαθέσιμη σύνδεση, παρακαλώ συνδεθείτε για να συνεχίσετε.",
							Toast.LENGTH_LONG).show();
				}
			}
		});

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
