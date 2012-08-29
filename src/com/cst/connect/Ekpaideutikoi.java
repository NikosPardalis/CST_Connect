package com.cst.connect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitleProvider;

public class Ekpaideutikoi extends SherlockActivity {

	AlertDialog ep = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_layout);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Εκπαιδευτικό Προσωπικό");

		ViewPagerAdapter adapter = new ViewPagerAdapter(this);
		ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
		TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);
	}

	class ViewPagerAdapter extends PagerAdapter implements TitleProvider {
		private final String[] titles = new String[] { "Μόνιμοι", "Συνεργάτες" };
		private final Context context;

		public ViewPagerAdapter(Context context) {
			this.context = context;
		}

		@Override
		public String getTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Object instantiateItem(View pager, int position) {

			final String[] monimoi_names = getResources().getStringArray(
					R.array.monimoi_names);
			final String[] monimoi_emails = getResources().getStringArray(
					R.array.monimoi_emails);
			final String[] monimoi_epikoinonia = getResources().getStringArray(
					R.array.monimoi_epikoinonia);

			final String[] sunergates_names = getResources().getStringArray(
					R.array.sunergates_names);
			final String[] sunergates_emails = getResources().getStringArray(
					R.array.sunergates_emails);
			final String[] sunergates_epikoinonia = getResources()
					.getStringArray(R.array.sunergates_epikoinonia);

			ListView v = new ListView(context);

			switch (position) {

			case 0:
				SimpleAdapter monimoi = new SimpleAdapter(
						context,
						monimoi_list(),
						R.layout.monimoi_list_item,
						new String[] { "Όνομα", "Πτυχία", "Τίτλος" },
						new int[] { R.id.teacher_name,
								R.id.teacher_qualification, R.id.teacher_title }) {

					@SuppressWarnings("unchecked")
					@Override
					public View getView(int groupPosition, View convertView,
							ViewGroup parent) {

						if (convertView == null) {
							LayoutInflater infalInflater = (LayoutInflater) context
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							convertView = infalInflater.inflate(
									R.layout.monimoi_list_item, null);
						}

						((TextView) convertView.findViewById(R.id.teacher_name))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Όνομα"));
						((TextView) convertView
								.findViewById(R.id.teacher_qualification))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Πτυχία"));

						((TextView) convertView
								.findViewById(R.id.teacher_title))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τίτλος"));

						((ImageView) convertView
								.findViewById(R.id.monimoi_icon))
								.setImageDrawable((Drawable) ((HashMap<?, ?>) getItem(groupPosition))
										.get("Εικόνα"));

						return convertView;
					}

				};

				v.setAdapter(monimoi);

				v.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							final int position, long id) {

						if (monimoi_epikoinonia[position].contains("null")) {
							CharSequence[] items = { "Ανακοινώσεις",
									"Αποστολή Email" };
							AlertDialog.Builder builder = new AlertDialog.Builder(
									context);
							builder.setTitle(monimoi_names[position]);
							builder.setItems(items,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int item) {

											if (item == 0) {
												Intent intent = new Intent(getBaseContext(), com.cst.connect.Anakoinoseis.class);
												intent.putExtra(
														"RSS",
														"http://www.cs.teilar.gr/CS/rss.jsp?id="
																+ monimoi_emails[position]);
												intent.putExtra("title",
														monimoi_names[position]);
												startActivity(intent);

											} else if (item == 1) {
												Intent emailIntent = new Intent(
														android.content.Intent.ACTION_SEND);
												emailIntent
														.setType("html/text");
												startActivity(Intent
														.createChooser(
																emailIntent,
																"Αποστολή Email με..."));

											}
										}
									});
							AlertDialog alert = builder.create();
							alert.show();

						} else {

							CharSequence[] items = { "Ανακοινώσεις",
									"Αποστολή Email", "Ωρες Επικοινωνίας" };
							AlertDialog.Builder builder = new AlertDialog.Builder(
									context);
							final AlertDialog.Builder epikoinonia_builder = new AlertDialog.Builder(
									context);
							builder.setTitle(monimoi_names[position]);
							builder.setItems(items,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int item) {

											if (item == 0) {
												Intent intent = new Intent(getBaseContext(), com.cst.connect.Anakoinoseis.class);
												intent.putExtra(
														"RSS",
														"http://www.cs.teilar.gr/CS/rss.jsp?id="
																+ monimoi_emails[position]);
												intent.putExtra("title",
														monimoi_names[position]);
//												intent.putExtra("title", "teachers");
												intent.putExtra("position", position);
												startActivity(intent);

											} else if (item == 1) {
												Intent emailIntent = new Intent(
														android.content.Intent.ACTION_SEND);

												emailIntent
														.putExtra(
																android.content.Intent.EXTRA_EMAIL,
																new String[] { monimoi_emails[position] });
												emailIntent
														.setType("html/text");
												emailIntent.putExtra("position", position);
												startActivity(Intent
														.createChooser(
																emailIntent,
																"Αποστολή Email με..."));

											} else if (item == 2) {

												epikoinonia_builder
														.setTitle(monimoi_names[position]);
												epikoinonia_builder
														.setMessage(monimoi_epikoinonia[position]);
												epikoinonia_builder
														.setNeutralButton(
																"Κλείσιμο",
																new DialogInterface.OnClickListener() {
																	public void onClick(
																			DialogInterface dialog,
																			int id) {

																		ep.dismiss();
																	}

																});
												ep = epikoinonia_builder
														.create();
												ep.show();

											}
										}
									});

							AlertDialog alert = builder.create();
							alert.show();

						}

					}
				});

				break;

			case 1:

				SimpleAdapter sunergates = new SimpleAdapter(context,
						sunergates_list(), R.layout.sunergates_list_item,
						new String[] { "Όνομα" },
						new int[] { R.id.sunergates_names })

				{

					@SuppressWarnings("unchecked")
					@Override
					public View getView(int groupPosition, View convertView,
							ViewGroup parent) {

						if (convertView == null) {
							LayoutInflater infalInflater = (LayoutInflater) context
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							convertView = infalInflater.inflate(
									R.layout.sunergates_list_item, null);
						}

						((TextView) convertView
								.findViewById(R.id.sunergates_names))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Όνομα"));

						return convertView;
					}

				};

				v.setAdapter(sunergates);

				v.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							final int position, long id) {

						if (sunergates_epikoinonia[position].contains("null")) {
							CharSequence[] items = { "Ανακοινώσεις",
									"Αποστολή Email" };
							AlertDialog.Builder builder = new AlertDialog.Builder(
									context);
							builder.setTitle(sunergates_names[position]);
							builder.setItems(items,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int item) {

											if (item == 0) {
												Intent intent = new Intent(getBaseContext(), com.cst.connect.Anakoinoseis.class);
												intent.putExtra(
														"RSS",
														"http://www.cs.teilar.gr/CS/rss.jsp?id="
																+ sunergates_emails[position]);
												intent.putExtra(
														"title",
														sunergates_names[position]);
												intent.putExtra("position", position+17);
												intent.putExtra("title", "teachers");
												startActivity(intent);

											} else if (item == 1) {
												Intent emailIntent = new Intent(
														android.content.Intent.ACTION_SEND);
												emailIntent
														.putExtra(
																android.content.Intent.EXTRA_EMAIL,
																new String[] { sunergates_emails[position] });
												emailIntent
														.setType("html/text");
												startActivity(Intent
														.createChooser(
																emailIntent,
																"Αποστολή Email με..."));

											}
										}
									});
							AlertDialog alert = builder.create();
							alert.show();

						} else {

							CharSequence[] items = { "Ανακοινώσεις",
									"Αποστολή Email", "Ωρες Επικοινωνίας" };
							AlertDialog.Builder builder = new AlertDialog.Builder(
									context);
							final AlertDialog.Builder epikoinonia_builder = new AlertDialog.Builder(
									context);
							builder.setTitle(sunergates_names[position]);
							builder.setItems(items,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int item) {

											if (item == 0) {
												Intent intent = new Intent(getBaseContext(), com.cst.connect.Anakoinoseis.class);
												intent.putExtra(
														"RSS",
														"http://www.cs.teilar.gr/CS/rss.jsp?id="
																+ sunergates_emails[position]);
												intent.putExtra(
														"title",
														sunergates_names[position]);
												startActivity(intent);

											} else if (item == 1) {
												Intent emailIntent = new Intent(
														android.content.Intent.ACTION_SEND);
												emailIntent
														.putExtra(
																android.content.Intent.EXTRA_EMAIL,
																sunergates_emails[position]);
												emailIntent
														.setType("html/text");
												startActivity(Intent
														.createChooser(
																emailIntent,
																"Αποστολή Email με..."));

											} else if (item == 2) {

												epikoinonia_builder
														.setTitle(sunergates_names[position]);
												epikoinonia_builder
														.setMessage(sunergates_epikoinonia[position]);
												epikoinonia_builder
														.setNeutralButton(
																"Κλείσιμο",
																new DialogInterface.OnClickListener() {
																	public void onClick(
																			DialogInterface dialog,
																			int id) {
																		ep.dismiss();
																	}
																});

												ep = epikoinonia_builder
														.create();
												ep.show();
											}
										}
									});

							AlertDialog alert = builder.create();
							alert.show();

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
			String[] monimoi_names = getResources().getStringArray(
					R.array.monimoi_names);
			String[] monimoi_qualifications = getResources().getStringArray(
					R.array.monimoi_qualifications);
			String[] monimoi_title = getResources().getStringArray(
					R.array.monimoi_title);
			String[] monimoi_images = getResources().getStringArray(
					R.array.monimoi_images);

			for (int i = 0; i < monimoi_names.length; i++) {
				HashMap<String, Object> m = new HashMap<String, Object>();

				m.put("Όνομα", monimoi_names[i]);
				m.put("Πτυχία", monimoi_qualifications[i]);
				m.put("Τίτλος", monimoi_title[i]);
				m.put("Εικόνα",
						getResources().getDrawable(
								getResources().getIdentifier(monimoi_images[i],
										null, null)));

				monimoi.add(m);
			}
			return (List<HashMap<String, Object>>) monimoi;
		}

		private List<HashMap<String, Object>> sunergates_list() {
			ArrayList<HashMap<String, Object>> sunergates = new ArrayList<HashMap<String, Object>>();
			String[] sunergates_names = getResources().getStringArray(
					R.array.sunergates_names);
			for (int i = 0; i < sunergates_names.length; i++) {
				HashMap<String, Object> m = new HashMap<String, Object>();

				m.put("Όνομα", sunergates_names[i]);

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
