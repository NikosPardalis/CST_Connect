package com.cst.connect.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
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
import com.cst.connect.R;
import com.cst.connect.helper.AsyncResponse;
import com.cst.connect.helper.AsyncTextProvider;
import com.cst.connect.helper.ChangeLog;
import com.cst.connect.helper.ImageAdapter;
import com.cst.connect.helper.RssHelper;

public class MainScreen extends SherlockActivity implements AsyncResponse {

	Context context = this;
	Intent intent, mobile;
	AlertDialog.Builder builder = null;
	AlertDialog alertDialog = null;
	RssHelper helper = new RssHelper(context);

	ChangeLog cl;
	// private SlidingMenu slidingMenu;

	private static final String PRIVATE_PREF = "myapp";
	private static final String VERSION_KEY = "version_number";
	private TextView tv;
	String message = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Ελεγχος ημερομηνίας για προβολή ανάλογου layout */

		if (getResources().getConfiguration().orientation == 1) {
			setContentView(R.layout.main_plus_text);
			checkTextProvider();
		} else {
			setContentView(R.layout.main);

		}

		/* SlidingMenu config */

		// slidingMenu = new SlidingMenu(context);
		// slidingMenu.setMode(SlidingMenu.LEFT);
		// slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		// slidingMenu.setShadowDrawable(R.drawable.shadow);
		// slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// slidingMenu.setFadeDegree(0.35f);
		// slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// slidingMenu.setMenu(R.layout.slidingmenu);

		/* Changelog init */

		cl = new ChangeLog(this);
		if (cl.firstRun())
			cl.getLogDialog().show();

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
											if (helper.isOnline()) {

												intent = new Intent(
														getBaseContext(),
														Rss.class);
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
											Intent intent = new Intent(
													getBaseContext(),
													TeachersView.class);
											intent.putExtra("ComingFrom",
													"teachers");
											startActivity(intent);
										} else if (item == 2) {

											if (helper.isOnline()) {
												intent = new Intent(
														getBaseContext(),
														Rss.class);
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

											if (helper.isOnline()) {
												intent = new Intent(
														getBaseContext(),
														Rss.class);
												intent.putExtra("RSS",
														"http://www.cs.teilar.gr/CS/rss.jsp?id=cs-calendar%40teilar.gr&Mynews=0#1");
												// intent.putExtra("RSS",
												// "http://www.teilar.gr/rss_ekp_news_xml.php?tid=2");
												// intent.putExtra(
												// "RSS",
												// "https://cid-b848b0ca6a4b68be.calendar.live.com/calendar/private/ef4777fa-126d-42c6-9b6f-cb8871a20dc2/22608a24-fbfc-48ea-ab2a-523edc9b98ad/calendar.xml");
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
						Intent intent = new Intent(getBaseContext(), Map.class);
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
													getBaseContext(),
													ImageDisplayer.class);
											intent.putExtra("title",
													"programma");
											startActivity(intent);
										} else if (item == 1) {
											Intent intent = new Intent(
													getBaseContext(),
													Mathimata.class);
											startActivity(intent);
										} else if (item == 2) {

											if (helper.isOnline()) {
												mobile = new Intent(
														getBaseContext(),
														MobileWebView.class);
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

											if (helper.isOnline()) {
												mobile = new Intent(
														getBaseContext(),
														MobileWebView.class);
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
										}
										/* Uncoment for exetastiki */
										else if (item == 4) {
											mobile = new Intent(
													getBaseContext(),
													ImageDisplayer.class);
											mobile.putExtra("title",
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
													getBaseContext(),
													Plirofories.class);
											startActivity(intent);
										} else {
											Intent intent = new Intent(
													getBaseContext(),
													Ekpaideutikoi.class);
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

						// mobile = new Intent(
						// "MobileWebView");
						// mobile.putExtra(
						// "Link",
						// "http://www.cs.teilar.gr/m/#anakep.jsp?ts=1342262884122");
						// startActivity(mobile);

						Intent intent = new Intent(getBaseContext(),
								Links.class);
						// Intent intent = new Intent(getBaseContext(),
						// Calendar.class);
						// Intent intent = new Intent(getBaseContext(),
						// ImageHelper.class);
						startActivity(intent);

						// startActivity(new
						// Intent(Intent.ACTION_VIEW).setDataAndType(null,
						// CalendarActivity.MIME_TYPE));

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
											if (helper.isOnline()) {
												mobile = new Intent(
														getBaseContext(),
														MobileWebView.class);
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
											if (helper.isOnline()) {
												mobile = new Intent(
														getBaseContext(),
														MobileWebView.class);
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

		if (getResources().getConfiguration().orientation == 1) {

			tv = (TextView) this.findViewById(R.id.marquee);
			tv.setSelected(true); // Set focus to the textview
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent exetastiki = new Intent(getBaseContext(),
							ImageDisplayer.class);
					exetastiki.putExtra("title", "exetastiki");
					startActivity(exetastiki);
				}
			});
		}

	}

	private boolean checkTextProvider() {

		if (helper.isOnline()) {
			new AsyncTextProvider(this)
					.execute("https://sites.google.com/site/cstconnectbackend/");
			if (message.length() > 5) {
				return true;
			}
		}
		return false;

	}

	public void processResult(String output) {
		message = output;
		tv.setText(message);
		Log.d("Message", message);

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

		case android.R.id.home:
			// this.slidingMenu.toggle();

		case R.id.folder:

			Intent fileIntent = new Intent(getBaseContext(), FileBrowser.class);
			// File file = new
			// File(Environment.getExternalStorageDirectory().getPath()+"/CST Connect Downloads/");
			// fileIntent.setData(Uri.fromFile(file));
			startActivity(fileIntent);

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

		default:
			return super.onOptionsItemSelected(item);
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

	// @Override
	// public void onBackPressed() {
	// if (slidingMenu.isMenuShowing()) {
	// slidingMenu.toggle();
	// } else {
	// super.onBackPressed();
	// }
	// }
	//
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_MENU) {
	// this.slidingMenu.toggle();
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }
}
