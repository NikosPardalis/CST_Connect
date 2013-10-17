package com.cst.connect.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cst.connect.R;
import com.cst.connect.helper.AsyncTaskListener;
import com.cst.connect.helper.DownloadFileAsync;
import com.cst.connect.helper.FeedsDbAdapter;
import com.cst.connect.helper.RssAsync;
import com.cst.connect.helper.RssHelper;
import com.cst.connect.helper.RssParser;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Rss extends SherlockActivity implements OnNavigationListener,
		AsyncTaskListener {

	ArrayList<HashMap<String, String>> localist;

	ProgressDialog dialog;
	SimpleAdapter adapter;
	ListView lv;
	AlertDialog.Builder builder = null;
	AlertDialog alertDialog = null;
	Context mContext = this;
	boolean flag, clik;
	static boolean UTF8 = false;
	static HttpPost httpPost;
	String rss, actionTitle, message, hyperlink;
	String sdcard = Environment.getExternalStorageDirectory().getPath();
	// PullToRefreshListView pullToRefreshView;
	private static boolean FILE_EXISTS = false;
	ActionBar actionBar;
	Menu myActionBarMenu;
	ContentValues test;
	FeedsDbAdapter mDbHelper = new FeedsDbAdapter(this);
	private Menu optionsMenu;

	private RssHelper helper = new RssHelper(mContext);
	private RssParser parser = new RssParser(mContext);
	private RssAsync async;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		localist = new ArrayList<HashMap<String, String>>();

		Intent sender = getIntent();
		rss = sender.getExtras().getString("RSS");
		actionTitle = sender.getExtras().getString("title");
		int position = sender.getExtras().getInt("position");

		Context context = getSupportActionBar().getThemedContext();
		ArrayAdapter<CharSequence> teachers_list = ArrayAdapter
				.createFromResource(context, R.array.Teachers_sorted,
						R.layout.sherlock_spinner_item);
		teachers_list
				.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		if (!actionTitle.contains("teachers")) {
			actionBar.setTitle(actionTitle);
		}

		if (actionTitle.contains("teachers")) {
			actionBar.setDisplayShowTitleEnabled(false);
			getSupportActionBar().setNavigationMode(
					ActionBar.NAVIGATION_MODE_LIST);
			getSupportActionBar().setListNavigationCallbacks(teachers_list,
					this);

			int[] indexes = getResources().getIntArray(R.array.indexes);
			Log.i("Position_Int", Integer.toString(position));
			Log.i("Sorted_Pos_Int", Integer.toString(indexes[position]));
			actionBar.setSelectedNavigationItem(indexes[position] - 1);

		}

		mDbHelper.open();

		httpPost = new HttpPost(rss);
		async = new RssAsync(this);

		if (!actionTitle.contains("teachers")) {

			async.execute(httpPost);

		}

		// pullToRefreshView = (PullToRefreshListView)
		// findViewById(R.id.pull_to_refresh_listview);
		// pullToRefreshView.setOnRefreshListener(new OnRefreshListener() {
		// @Override
		// public void onRefresh() {
		// new GetDataTask().execute();
		// }
		// });

	}

	private void listViewSetup() {

		adapter = new SimpleAdapter(this, localist, R.layout.rss_list_item,
				new String[] { "title", "pubDate", "link", "description" },
				new int[] { R.id.title, R.id.pubDate, R.id.attached_image }) {

			@SuppressWarnings("unchecked")
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				if (convertView == null) {
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = vi.inflate(R.layout.rss_list_item, null);
				}
				((TextView) convertView.findViewById(R.id.title))
						.setText((String) ((Map<String, String>) getItem(position))
								.get("title"));

				final HashMap<String, String> e = (HashMap<String, String>) adapter
						.getItem(position);

				((TextView) convertView.findViewById(R.id.pubDate))
						.setText(helper.convertDate(e.get("pubDate")));

				if (helper.attachedFile(e.get("description"))) {
					((ImageView) convertView.findViewById(R.id.attached_image))
							.setImageResource(R.drawable.attached);
				} else {
					((ImageView) convertView.findViewById(R.id.attached_image))
							.setImageDrawable(null);
				}

				return convertView;
			}

		};

		lv = (ListView) findViewById(android.R.id.list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				@SuppressWarnings("unchecked")
				final HashMap<String, String> o = (HashMap<String, String>) lv
						.getItemAtPosition(position);

				message = o.get("description");

				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.custom_dialog,
						(ViewGroup) findViewById(android.R.id.list), false);

				View title_layout = inflater.inflate(R.layout.custom_title,
						null);

				TextView title = (TextView) title_layout
						.findViewById(R.id.dialog_title);
				title.setText(Html.fromHtml("<a href=" + o.get("link") + ">"
						+ o.get("title") + "</a>"));
				title.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(o.get("link")));
						startActivity(intent);
					}
				});

				TextView text = (TextView) layout
						.findViewById(R.id.dialog_text);

				text.setText(helper.xmlCleansing(message, rss));
				Linkify.addLinks(text, Linkify.ALL);
				// Log.d(null, message);
				text.setSingleLine(false);

				builder = new AlertDialog.Builder(mContext);
				builder.setView(layout);
				builder.setCustomTitle(title_layout);

				builder.setNeutralButton("Κλείσιμο",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								alertDialog.dismiss();
							}
						});

				if (helper.hasAttachment(message, rss)) {

					File download = new File(Environment
							.getExternalStorageDirectory().getPath()
							+ "/CST Connect Downloads/" + helper.getFilename());
					if (download.exists()) {
						FILE_EXISTS = true;
					} else {
						FILE_EXISTS = false;
					}
					if (!FILE_EXISTS) {

						builder.setPositiveButton("Λήψη Αρχείου",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										if (helper.isOnline()) {
											new DownloadFileAsync(mContext).execute(
													helper.getUrl(),
													helper.getFilename());

										} else {
											Toast.makeText(
													Rss.this,
													R.string.DataFailureWarning,
													Toast.LENGTH_LONG).show();
										}
									}

								});
					} else if (FILE_EXISTS) {
						builder.setPositiveButton("’νοιγμα Αρχείου",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										openFile();
									}
								});
					}

				}

				alertDialog = builder.create();
				alertDialog.show();

			}

		});
	}

	public void openFile() {
		Intent notificationIntent = new Intent();
		notificationIntent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(sdcard + "/CST Connect Downloads/"
				+ helper.getFilename());
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		String ext = file.getName().substring(file.getName().indexOf(".") + 1);
		String type = mime.getMimeTypeFromExtension(ext);
		notificationIntent.setDataAndType(Uri.fromFile(file), type);

		try {
			startActivity(notificationIntent);
		} catch (ActivityNotFoundException e) {
			System.err.println("Caught IOException: " + e.getMessage());
			Toast.makeText(this, "Δεν υπάρχει διαθέσιμη εφαρμογή",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onTaskComplete(ArrayList<HashMap<String, String>> result) {
		// this method is called after completion of asynctask
		if (!result.isEmpty()) {
			localist = result;
			listViewSetup();

		} else {
			if (adapter != null) {
				localist.clear();
				adapter.notifyDataSetChanged();
			}
			Toast.makeText(Rss.this, "Δεν υπάρχουν διαθέσιμες ανακοινώσεις",
					Toast.LENGTH_SHORT).show();
		}

		setRefreshActionButtonState(false);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		this.optionsMenu = menu;
		getSupportMenuInflater().inflate(R.menu.menu_refresh, menu);
		async = new RssAsync(this);
		setRefreshActionButtonState(true);
		return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:		
			finish();

			break;

		case R.id.refresh:
			if (helper.isOnline()) {
				setRefreshActionButtonState(true);
				new RssAsync(this).execute(httpPost);
			} else {
				Toast.makeText(Rss.this, R.string.DataFailureWarning,
						Toast.LENGTH_LONG).show();
			}
			break;
		}
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	// @Override
	// protected void onResume() {
	// // super.onResume();
	// // mDbHelper.open();
	// }

	// @Override
	// protected void onPause() {
	// // super.onPause();
	// // mDbHelper.close();
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {

		String[] teachers_emails = getResources().getStringArray(
				R.array.Emails_sorted);

		rss = "http://www.cs.teilar.gr/CS/rss.jsp?id="
				+ teachers_emails[itemPosition];

		httpPost = new HttpPost(rss);
		setRefreshActionButtonState(true);
		async = new RssAsync(this);
		async.execute(httpPost);

		return true;
	}

	public void setRefreshActionButtonState(final boolean refreshing) {
		if (optionsMenu != null) {
			final MenuItem refreshItem = optionsMenu.findItem(R.id.refresh);
			if (refreshItem != null) {
				if (refreshing) {
					refreshItem
							.setActionView(R.layout.abs_indeterminate_progress);
				} else {
					refreshItem.setActionView(null);
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		async.cancel(true);
		 super.onBackPressed();
	}
}