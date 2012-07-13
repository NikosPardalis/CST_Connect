package com.cst.connect;

import java.io.File;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainScreen extends SherlockActivity {

	Context context = this;
	Intent intent, mobile;
	AlertDialog.Builder builder = null;
	AlertDialog alertDialog = null;
	ChangeLog cl;

	private static final String PRIVATE_PREF = "myapp";
	private static final String VERSION_KEY = "version_number";
	private TextView tv;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (checkDate() && (getResources().getConfiguration().orientation == 1)) {
			setContentView(R.layout.main_plus_text);
		} else {
			setContentView(R.layout.main);
		}

		cl = new ChangeLog(this);
		if (cl.firstRun())
			cl.getLogDialog().show();
		// init();

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));
		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				switch (position) {
				case 0:
					try {

						CharSequence[] items = { "Τμήματος",
								"Εκπαιδευτικού Προσωπικού", "Δημόσια Νέα",
								"Ημερολόγιο" };
						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);
						builder.setTitle("Ανακοινώσεις");
						builder.setItems(items,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {
										if (item == 0) {
											if (isOnline()) {

												intent = new Intent(
														"com.cst.connect.Anakoinoseis");
												intent.putExtra("RSS",
														"http://www.cs.teilar.gr/CS/rss.jsp");//
												intent.putExtra("title",
														"Ανακοινώσεις Τμήματος");
												startActivity(intent);

											} else {
												Toast.makeText(
														MainScreen.this,
														R.string.DataFailureWarning,
														Toast.LENGTH_LONG)
														.show();
											}

										} else if (item == 1) {
											// Intent intent = new Intent(
											// "com.cst.connect.TeachersList");
											Intent intent = new Intent(
													"com.cst.connect.TeachersView");
											intent.putExtra("ComingFrom",
													"teachers");
											startActivity(intent);
										} else if (item == 2) {

											if (isOnline()) {
												intent = new Intent(
														"com.cst.connect.Anakoinoseis");
												intent.putExtra("RSS",
														"http://www.cs.teilar.gr/CS/rss.jsp?id=cs-news@teilar.gr");
												intent.putExtra("title",
														"Δημόσια Νέα");
												startActivity(intent);

											} else {

												Toast.makeText(
														MainScreen.this,
														R.string.DataFailureWarning,
														Toast.LENGTH_LONG)
														.show();
											}

										} else if (item == 3) {

											if (isOnline()) {
												intent = new Intent(
														"com.cst.connect.Anakoinoseis");
												intent.putExtra("RSS",
														"http://www.cs.teilar.gr/CS/rss.jsp?id=cs-calendar%40teilar.gr&Mynews=0#1");
												// intent.putExtra("RSS",
												// "http://www.teilar.gr/rss_ekp_news_xml.php?tid=2");
												intent.putExtra("title",
														"Ημερολόγιο");
												startActivity(intent);

											} else {

												Toast.makeText(
														MainScreen.this,
														R.string.DataFailureWarning,
														Toast.LENGTH_LONG)
														.show();
											}
										}
									}

								});

						AlertDialog alert = builder.create();
						alert.show();

					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
					break;

				case 1:
					try {
						Intent intent = new Intent("com.cst.connect.CSMapView");
						startActivity(intent);

					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
					break;

				case 2:
					try {
						CharSequence[] items = { "Πρόγραμμα Μαθημάτων",
								"Κατανομή σε Εξάμηνα", "Βαθμολογίες",
								"Δηλώσεις", "Πρόγραμμα Εξεταστικής" };
						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);
						builder.setTitle("Μαθήματα");
						builder.setItems(items,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {

										if (item == 0) {
											Intent intent = new Intent(
													"com.cst.connect.Programma");
											startActivity(intent);
										} else if (item == 1) {
											Intent intent = new Intent(
													"com.cst.connect.Mathimata");
											startActivity(intent);
										} else if (item == 2) {

											if (isOnline()) {
												mobile = new Intent(
														"com.cst.connect.MobileWebView");
												mobile.putExtra("Link",
														"https://dionysos.teilar.gr/unistudent/stud_CResults.asp?studPg=1&mnuid=mnu3&");
												startActivity(mobile);
											} else {

												Toast.makeText(
														MainScreen.this,
														R.string.DataFailureWarning,
														Toast.LENGTH_LONG)
														.show();
											}
										} else if (item == 3) {

											if (isOnline()) {
												mobile = new Intent(
														"com.cst.connect.MobileWebView");
												mobile.putExtra(
														"Link",
														"https://dionysos.teilar.gr/unistudent/stud_vClasses.asp?studPg=1&mnuid=diloseis;showDil&");
												startActivity(mobile);
											} else {

												Toast.makeText(
														MainScreen.this,
														R.string.DataFailureWarning,
														Toast.LENGTH_LONG)
														.show();
											}
										} else if (item == 4) {
											mobile = new Intent(
													"com.cst.connect.MobileWebView");
											mobile.putExtra("Link",
													"exetastiki");
											startActivity(mobile);
										}
									}
								});

						AlertDialog alert = builder.create();
						alert.show();

					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
					break;

				case 3:
					try {
						CharSequence[] items = { "Τμήματος",
								"Εκπαιδευτικού Προσωπικού" };
						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);
						builder.setTitle("Πληροφορίες");
						builder.setItems(items,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {

										if (item == 0) {
											Intent intent = new Intent(
													"com.cst.connect.Plirofories");
											startActivity(intent);
										} else {
											Intent intent = new Intent(
													"com.cst.connect.Ekpaideutikoi");
											startActivity(intent);
										}
									}
								});

						AlertDialog alert = builder.create();
						alert.show();

					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
					break;
				case 4:
					try {

						Intent intent = new Intent("com.cst.connect.Links");
						startActivity(intent);

					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
					break;
				case 5:
					try {

						CharSequence[] items = { "CS Mobile", "Webmail" };
						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);
						builder.setTitle("Mobile Sites");
						builder.setItems(items,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {

										if (item == 0) {
											if (isOnline()) {
												mobile = new Intent(
														"com.cst.connect.MobileWebView");
												mobile.putExtra("Link",
														"http://www.cs.teilar.gr/m");
												startActivity(mobile);
											} else {

												Toast.makeText(
														MainScreen.this,
														R.string.DataFailureWarning,
														Toast.LENGTH_LONG)
														.show();
											}
										} else if (item == 1) {
											if (isOnline()) {
												mobile = new Intent(
														"com.cst.connect.MobileWebView");
												mobile.putExtra("Link",
														"https://myweb.teilar.gr/src/login.php");
												startActivity(mobile);
											} else {

												Toast.makeText(
														MainScreen.this,
														R.string.DataFailureWarning,
														Toast.LENGTH_LONG)
														.show();
											}
										}
									}
								});

						AlertDialog alert = builder.create();
						alert.show();

					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
					break;

				}
			}
		});

		if (checkDate() && getResources().getConfiguration().orientation == 1) {

			tv = (TextView) this.findViewById(R.id.marquee);
			tv.setSelected(true); // Set focus to the textview
			tv.setText("Εξεταστική Περίοδος: 3 - 21 Σεπτεμβρίου 2012. Καλή επιτυχία!");
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent exetastiki = new Intent(
							"com.cst.connect.MobileWebView");
					exetastiki.putExtra("Link", "exetastiki");
					startActivity(exetastiki);
				}
			});
		}

	}

	private boolean checkDate() {

		Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);

		// Οι μήνες πάνε -1 ~> Ιανουάριος == 0

		if ((year == 2012) && (month == 8 && day >= 3)
				&& (month == 8 && day <= 21)) {

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getSupportMenuInflater().inflate(R.menu.menu_main, menu);

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.folder:

			Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
			fileIntent.setType("file/*");
			// File file = new
			// File(Environment.getExternalStorageDirectory().getPath()+"/CST Connect Downloads/");
			// fileIntent.setData(Uri.fromFile(file));
			startActivity(fileIntent);

			// Toast.makeText(this, "File Browser coming soon....! ",
			// Toast.LENGTH_SHORT).show();
			break;

		case R.id.about:

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.custom_dialog,
					(ViewGroup) findViewById(android.R.id.list), false);

			builder = new AlertDialog.Builder(context);
			builder.setView(layout);
			String versionName = null;
			try {
				versionName = getPackageManager().getPackageInfo(
						getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

			builder.setTitle("CST Connect v" + versionName);

			builder.setIcon(android.R.drawable.ic_menu_info_details);
			TextView text = (TextView) layout.findViewById(R.id.dialog_text);

			text.setText(R.string.appInfo);

			builder.setPositiveButton("Feedback",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Intent emailIntent = new Intent(Intent.ACTION_SEND);
							emailIntent.setType("html/text");
							emailIntent.putExtra(Intent.EXTRA_EMAIL,
									new String[] { "nikos.pard@gmail.com" });
							emailIntent.putExtra(Intent.EXTRA_SUBJECT,
									"CST Connect Feedback");
							startActivity(Intent.createChooser(emailIntent,
									"Αποστολή Email με..."));

						}
					});

			builder.setNeutralButton("Site Εφαρμογής",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Intent siteIntent = new Intent(Intent.ACTION_VIEW);
							siteIntent.setData(Uri
									.parse("http://cst-connect.blogspot.com/"));
							startActivity(Intent.createChooser(siteIntent,
									"Ανοιγμα σελίδας με..."));

						}
					});

			builder.setNegativeButton("ChangeLog",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// alertDialog.dismiss();
							cl.getFullLogDialog().show();
						}
					});

			alertDialog = builder.create();
			alertDialog.show();
		}
		return true;
	}

	private void init() {
		SharedPreferences sharedPref = getSharedPreferences(PRIVATE_PREF,
				Context.MODE_PRIVATE);
		int currentVersionNumber = 0;

		int savedVersionNumber = sharedPref.getInt(VERSION_KEY, 0);

		try {
			PackageInfo pi = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			currentVersionNumber = pi.versionCode;
		} catch (Exception e) {
		}

		if (currentVersionNumber > savedVersionNumber) {
			showDialog();

			Editor editor = sharedPref.edit();

			editor.putInt(VERSION_KEY, currentVersionNumber);
			editor.commit();
		}
	}

	private void showDialog() {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.custom_dialog,
				(ViewGroup) findViewById(android.R.id.list), false);

		builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		builder.setMessage("");
		builder.setNegativeButton("Κλείσιμο",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						alertDialog.dismiss();
					}
				});
		alertDialog = builder.create();
		alertDialog.show();
	}

	private Boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnected())
			return true;

		return false;
	}

}
