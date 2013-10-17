package com.cst.connect.helper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.cst.connect.R;

public class DownloadFileAsync extends AsyncTask<String, String, String> {

	Context mContext;

	public DownloadFileAsync(Context context) {
		this.mContext = context;
	}

	static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	RssHelper helper = new RssHelper(mContext);
	String sdcard = Environment.getExternalStorageDirectory().getPath();
	private static ProgressDialog mProgressDialog;
	Notification notification;
	int mNotificationId = 001;
	File downloadsDirectory;
	String filename;
	private volatile boolean running = true;

	@Override
	protected void onPreExecute() {
		onCreateDialog(DIALOG_DOWNLOAD_PROGRESS);

	}

	@Override
	protected void onCancelled() {
		running = false;

	}

	@Override
	protected String doInBackground(String... aurl) {
		int count;

		try {

			URL url = new URL(aurl[0]);
			filename = new String(aurl[1]);
			URLConnection conexion = url.openConnection();
			conexion.connect();

			int lenghtOfFile = conexion.getContentLength();
			downloadsDirectory = new File(sdcard + "/CST Connect Downloads/");

			if (!downloadsDirectory.exists()) {
				downloadsDirectory.mkdir();
			}

			InputStream input = new BufferedInputStream(url.openStream());
			OutputStream output = new FileOutputStream(sdcard
					+ "/CST Connect Downloads/" + filename);

			byte data[] = new byte[1024];
			long total = 0;

			while ((count = input.read(data)) != -1) {
				if (!isCancelled() && running != false) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				} else {
					File file = new File(sdcard + "/CST Connect Downloads/"
							+ filename);
					boolean deleted = file.delete();
					break;
				}
			}
			output.flush();
			output.close();
			input.close();

		} catch (Exception e) {
		}
		return null;
	}

	protected void onProgressUpdate(String... progress) {
		mProgressDialog.setProgress(Integer.parseInt(progress[0]));

	}

	@Override
	protected void onPostExecute(String result) {
		mProgressDialog.dismiss();
		createNotification();
		mNotificationId++;

	}

	public void createNotification() {

		int icon = R.drawable.notification_icon;
		CharSequence tickerText = "Λήψη αρχείου ολοκληρώθηκε";
		CharSequence contentTitle = filename;
		CharSequence contentText = "Κάντε κλικ για άνοιγμα του αρχείου.";

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				mContext).setSmallIcon(icon).setTicker(tickerText)
				.setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true);

		Intent notificationIntent = new Intent();

		try {
			notificationIntent.setAction(android.content.Intent.ACTION_VIEW);
		} catch (ActivityNotFoundException e) {
			System.err.println("Caught IOException: " + e.getMessage());
			Toast.makeText(mContext, "Δεν υπάρχει διαθέσιμη εφαρμογή",
					Toast.LENGTH_SHORT).show();
		}
		File file = new File(sdcard + "/CST Connect Downloads/" + filename);
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		String ext = file.getName().substring(file.getName().indexOf(".") + 1);
		String type = mime.getMimeTypeFromExtension(ext);

		notificationIntent.setDataAndType(Uri.fromFile(file), type);
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
				notificationIntent, 0);

		mBuilder.setContentTitle(contentTitle).setContentText(contentText)
				.setContentIntent(contentIntent);

		NotificationManager mNotifyMgr = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);

		mNotifyMgr.notify(mNotificationId, mBuilder.build());

	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setMessage("Λήψη αρχείου...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(true);
			mProgressDialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					cancel(true);
				}
			});

			mProgressDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					cancel(true);
				}
			});
			mProgressDialog.show();

			return mProgressDialog;
		default:
			return null;
		}
	}
}