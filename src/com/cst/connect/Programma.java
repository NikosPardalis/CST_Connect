package com.cst.connect;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class Programma extends SherlockActivity implements
		ActionBar.OnNavigationListener {

	WebView webView;
	String data;

	Context context = this;
	AlertDialog.Builder builder = null;
	AlertDialog alertDialog = null;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private static ProgressDialog mProgressDialog;
	private static int LIST_UPDATE_NOTIFICATION = 1;
	private static boolean FILE_EXISTS = false;
	File file, downloadsDirectory, download;
	String downloadsDirectoryPath = "/sdcard/CST Connect Downloads/";
	String filename;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// setTheme(SampleList.THEME); //Used for theme switching in samples
		super.onCreate(savedInstanceState);

		setContentView(R.layout.webview);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		if (getResources().getConfiguration().orientation == 1) {
			actionBar.setDisplayShowTitleEnabled(false);
		} else if (getResources().getConfiguration().orientation == 2) {
			actionBar.setTitle("Πρόγραμμα Μαθημάτων");
		}	
	
		
		Context context = getSupportActionBar().getThemedContext();
		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(
				context, R.array.Examina_xeimerino_list,
				R.layout.sherlock_spinner_item);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getSupportActionBar().setListNavigationCallbacks(list, this);

		// Intent sender = getIntent();
		// String url = sender.getExtras().getString("Link");
		// if (url.contains("exetastiki"))
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setBuiltInZoomControls(true);

		webView.getSettings().setUseWideViewPort(true);

		if (getResources().getConfiguration().orientation == 1) {
			webView.setInitialScale(getScale());
			Toast.makeText(
					this,
					"Για καλύτερη προβολή περιστρέψτε την συσκευή σας οριζόντια.Double Tap για Zoom In/Out.",
					Toast.LENGTH_SHORT).show();
		} else if (getResources().getConfiguration().orientation == 2) {
			webView.getSettings().setLoadWithOverviewMode(true);
		}

	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		switch (itemPosition) {
		case 0:

			data = "<body>" + "<img src=\"xeimerino_examino_1.png\"/></body>";

			webView.loadDataWithBaseURL("file:///android_asset/", data,
					"text/html", "utf-8", null);
			break;

		case 1:

			data = "<body>" + "<img src=\"xeimerino_examino_3.png\"/></body>";

			webView.loadDataWithBaseURL("file:///android_asset/", data,
					"text/html", "utf-8", null);
			break;
		case 2:

			data = "<body>" + "<img src=\"xeimerino_examino_5.png\"/></body>";

			webView.loadDataWithBaseURL("file:///android_asset/", data,
					"text/html", "utf-8", null);
			break;
		case 3:

			data = "<body>" + "<img src=\"xeimerino_examino_7.png\"/></body>";

			webView.loadDataWithBaseURL("file:///android_asset/", data,
					"text/html", "utf-8", null);
			break;
		}

		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		super.onCreateOptionsMenu(menu);
//		getSupportMenuInflater()
//				.inflate(R.menu.menu_programma_mathimaton, menu);
//
//		return true;
//
//	}
//
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
//		case R.id.download_schedule:
//
//			LayoutInflater inflater_schedule = (LayoutInflater) context
//					.getSystemService(LAYOUT_INFLATER_SERVICE);
//			View layout_schedule = inflater_schedule.inflate(
//					R.layout.custom_dialog,
//					(ViewGroup) findViewById(android.R.id.list), false);
//
//			builder = new AlertDialog.Builder(context);
//			builder.setView(layout_schedule);
//
//			builder.setTitle("Ωρολόγιο Πρόγραμμα Μαθημάτων");
//
//			filename = "pr-ab57be23.doc";
//			download = new File(downloadsDirectoryPath + filename);
//
//			if (download.exists()) {
//				FILE_EXISTS = true;
//			} else {
//				FILE_EXISTS = false;
//			}
//
//			if (!FILE_EXISTS) {
//
//				builder.setMessage("Πρόκειται να γίνει λήψη του αρχείου "
//						+ filename + ". Συνέχεια;");
//				builder.setPositiveButton("Λήψη Αρχείου",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog, int id) {
//								if (isOnline()) {
//									startDownload();
//
//								} else {
//
//									Toast.makeText(Programma.this,
//											R.string.DataFailureWarning,
//											Toast.LENGTH_LONG).show();
//								}
//
//							}
//
//							private void startDownload() {
//								new DownloadFileAsync()
//										.execute("http://www.teilar.gr/dbData/TmimataNews/pr-ab57be23.doc");
//							}
//						});
//				FILE_EXISTS = true;
//
//			} else if (FILE_EXISTS) {
//				builder.setMessage("Έχετε ήδη κατεβάσει το Ωρολόγιο πρόγραμμα μαθημάτων.Κάντε κλικ στο κουμπί για άνοιγμα του αρχείου.");
//				builder.setPositiveButton("’νοιγμα Αρχείου",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog, int id) {
//
//								openFile();
//
//							}
//
//						});
//			}
//
//			builder.setNeutralButton("Κλείσιμο",
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int id) {
//							alertDialog.dismiss();
//						}
//					});
//
//			alertDialog = builder.create();
//			alertDialog.show();
//			break;
		}
		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private int getScale() {
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		int width = display.getWidth();
		Double val = new Double(width) / new Double(799);
		val = val * 100d;
		return val.intValue();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage("Λήψη αρχείου...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}

	private Boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnected())
			return true;

		return false;
	}

	public void openFile() {
		Intent notificationIntent = new Intent();
		notificationIntent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(downloadsDirectoryPath + filename);
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		String ext = file.getName().substring(file.getName().indexOf(".") + 1);
		String type = mime.getMimeTypeFromExtension(ext);
		notificationIntent.setDataAndType(Uri.fromFile(file), type);
		startActivity(notificationIntent);

	}

	public void createNotification() {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		int icon = R.drawable.notification_icon;
		CharSequence tickerText = "Λήψη αρχείου ολοκληρώθηκε";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		CharSequence contentTitle = filename;
		CharSequence contentText = "Κάντε κλικ για άνοιγμα του αρχείου.";

		Intent notificationIntent = new Intent();
		notificationIntent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(downloadsDirectoryPath + filename);
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		String ext = file.getName().substring(file.getName().indexOf(".") + 1);
		String type = mime.getMimeTypeFromExtension(ext);
		notificationIntent.setDataAndType(Uri.fromFile(file), type);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(this, contentTitle, contentText,
				contentIntent);

		mNotificationManager.notify(LIST_UPDATE_NOTIFICATION, notification);

	}

	class DownloadFileAsync extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected String doInBackground(String... aurl) {
			int count;

			try {

				URL url = new URL(aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();

				downloadsDirectory = new File(downloadsDirectoryPath);

				if (!downloadsDirectory.exists()) {
					downloadsDirectory.mkdir();
				}

				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(
						downloadsDirectoryPath + filename);

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
			}
			return null;
		}

		protected void onProgressUpdate(String... progress) {
			// Log.d("ANDRO_ASYNC", progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String unused) {
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			createNotification();
			LIST_UPDATE_NOTIFICATION++;
		}
	}
}