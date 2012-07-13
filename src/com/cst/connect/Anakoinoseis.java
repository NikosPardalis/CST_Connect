package com.cst.connect;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
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
import com.actionbarsherlock.view.Window;

public class Anakoinoseis extends SherlockActivity implements
		OnNavigationListener {

	static ArrayList<HashMap<String, String>> localist, templist;

	ProgressDialog dialog;
	SimpleAdapter adapter;
	static String url, filename;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	AlertDialog.Builder builder = null;
	AlertDialog alertDialog = null;
	private static ProgressDialog mProgressDialog;
	Context mContext = this;
	private static int LIST_UPDATE_NOTIFICATION = 1;
	boolean flag, clik;
	static boolean UTF8 = false;
	static HttpPost httpPost;
	String rss, actionTitle, message, hyperlink;
	// PullToRefreshListView pullToRefreshView;
	private static boolean FILE_EXISTS = false;
	ActionBar actionBar;
	public static boolean CHILDREN_NULL;
	Menu myActionBarMenu;
	GetDataTask gdt;
	ContentValues test;
	FeedsDbAdapter mDbHelper = new FeedsDbAdapter(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.list);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);		
		
		localist = new ArrayList<HashMap<String, String>>();

		Intent sender = getIntent();
		rss = sender.getExtras().getString("RSS");
		actionTitle = sender.getExtras().getString("title");
		int position =  sender.getExtras().getInt("position");
		

		Context context = getSupportActionBar().getThemedContext();
		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(
				context, R.array.Teachers, R.layout.sherlock_spinner_item);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		if (!actionTitle.contains("teachers")) {
			actionBar.setTitle(actionTitle);
		}

		if (actionTitle.contains("teachers")) {
			actionBar.setDisplayShowTitleEnabled(false);
			getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			getSupportActionBar().setListNavigationCallbacks(list, this);
			actionBar.setSelectedNavigationItem(position);

		} 
		
		mDbHelper.open();

		httpPost = new HttpPost(rss);

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
						.setText(convertDate(e.get("pubDate")));

				if (attachedFile(e.get("description"))) {
					((ImageView) convertView.findViewById(R.id.attached_image))
							.setImageResource(R.drawable.attached);
				} else {
					((ImageView) convertView.findViewById(R.id.attached_image))
							.setImageDrawable(null);
				}

				return convertView;
			}

		};

		final ListView lv = (ListView) findViewById(android.R.id.list);
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

				text.setText(XMLcleansing(message, rss));
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

				if (flag == true) {

					File download = new File(Environment.getExternalStorageDirectory().getPath() +"/CST Connect Downloads/"
							+ filename);
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
										if (isOnline()) {
											startDownload();

										} else {
											Toast.makeText(
													Anakoinoseis.this,
													R.string.DataFailureWarning,
													Toast.LENGTH_LONG).show();
										}
									}

									private void startDownload() {
										new DownloadFileAsync().execute(url);
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

		// dialog = ProgressDialog.show(Anakoinoseis.this, "", "Loading");
		if (!actionTitle.contains("teachers")) {
			new GetDataTask().execute();	
		}
		
		if (CHILDREN_NULL) {
			Toast.makeText(Anakoinoseis.this,
					"Δεν υπάρχουν διαθέσιμες ανακοινώσεις", Toast.LENGTH_LONG)
					.show();
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

	public boolean attachedFile(String message) {

		if (message.contains("title='Επισυναπτόμενο αρχείο'>")) {
			return true;
		}

		return false;
	}

	public String XMLcleansing(String message, String rss) {

		if (rss.equals("http://www.cs.teilar.gr/CS/rss.jsp")) {

			/* Έλεγχος επισυναπτόμενου αρχείου */
			if (message.contains("<BR />")) {

				int start = message.indexOf("HREF='") + 6;
				int start_file = message.indexOf("News/") + 5;
				int end = message.indexOf("' title");
				filename = message.substring(start_file, end);
				url = message.substring(start, end);

				String pattern = "(?i)(<BR />)(.+?)(</A>)";
				message = message.replaceAll(pattern, "");

				flag = true;

			} else {
				flag = false;
			}
			/* Έλεγχος επισυναπτόμενου αρχείου */

		} else if (rss.contains("http://www.cs.teilar.gr/CS/rss.jsp?id=")) {

			Pattern stulianos = Pattern
					.compile("(?i)(?s)(normal)(.+?)(0400\\;\\})");
			Matcher mstulianos = stulianos.matcher(message);
			message = mstulianos.replaceAll("");

			Pattern vlachos1 = Pattern
					.compile("(?i)(?s)(normal)(.+?)(latin\\;\\})");
			Matcher mvlachos1 = vlachos1.matcher(message);
			message = mvlachos1.replaceAll("");

			Pattern vlachos2 = Pattern
					.compile("(?i)(?s)(normal)(.+?)(-US\\;\\})");
			Matcher mvlachos2 = vlachos2.matcher(message);
			message = mvlachos2.replaceAll("");

			Pattern vlachos3 = Pattern
					.compile("(?i)(?s)(normal)(.+?)(JA\\;\\})");
			Matcher mvlachos3 = vlachos3.matcher(message);
			message = mvlachos3.replaceAll("");

			Pattern vlachos4 = Pattern
					.compile("(?i)(?s)(function)(.+?)(src\\;\\})");
			Matcher mvlachos4 = vlachos4.matcher(message);
			message = mvlachos4.replaceAll("");

			Pattern ventzas = Pattern
					.compile("(?i)(?s)(normal)(.+?)(Roman\"\\;\\})");
			Matcher mventzas = ventzas.matcher(message);
			message = mventzas.replaceAll("");

			/* Έλεγχος επισυναπτόμενου αρχείου */
			if (message.contains("<BR />")) {

				int start = message.indexOf("HREF='") + 6;
				int start_file = message.indexOf("Ann/") + 4;
				int end = message.indexOf("' title");
				filename = message.substring(start_file, end);
				url = message.substring(start, end);
				String pattern = "(?i)(<BR />)(.+?)(</A>)";
				message = message.replaceAll(pattern, "");
				flag = true;

			} else {
				flag = false;
			}
			/* Έλεγχος επισυναπτόμενου αρχείου */
		}

		if (message.length() == 0) {
			message = message.replaceAll("", "Δεν υπάρχει κείμενο ανακοίνωσης");
		}

		message = Jsoup.parse(message.replaceAll("\n", "br2nl")).text();
		message = message.replaceAll("(br2nl)++", "\n")
				.replaceAll("(br2nl)++", "\n").trim();

		return message;

	}

	private String convertDate(String pubDate) {

		DateFormat inputFormat = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss", Locale.US);
		DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date convertedDate = null;
		try {
			convertedDate = inputFormat.parse(pubDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return outputFormat.format(convertedDate);

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
		File file = new File("/sdcard/CST Connect Downloads/" + filename);
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

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setMessage("Λήψη αρχείου...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}

	public void openFile() {
		Intent notificationIntent = new Intent();
		notificationIntent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File("/sdcard/CST Connect Downloads/" + filename);
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		String ext = file.getName().substring(file.getName().indexOf(".") + 1);
		String type = mime.getMimeTypeFromExtension(ext);
		notificationIntent.setDataAndType(Uri.fromFile(file), type);
		startActivity(notificationIntent);

	}

	private class GetDataTask extends AsyncTask<Void, Void, Integer> {
		Boolean ZERO_FLAG = false;

		@Override
		protected void onPreExecute() {
			setSupportProgressBarIndeterminateVisibility(true);
			localist.clear();
			// myActionBarMenu.findItem(R.id.refresh).setVisible(false);
		}

		@Override
		protected Integer doInBackground(Void... params) {

			String xml = ParseXMLmethods.getXML();
			Document doc = ParseXMLmethods.XMLfromString(xml);

			if (doc != null) {
				NodeList children = doc.getElementsByTagName("item");
				ZERO_FLAG = false;
				if (children.getLength() == 0) {
					ZERO_FLAG = true;
					publishProgress();
				}
				if (!isCancelled()) {

					for (int i = 0; i < children.getLength(); i++) {

						HashMap<String, String> map = new HashMap<String, String>();

						Element e = (Element) children.item(i);
						map.put("title", ParseXMLmethods.getValue(e, "title"));
						map.put("pubDate",
								ParseXMLmethods.getValue(e, "pubDate"));
						map.put("link", ParseXMLmethods.getValue(e, "link"));
						map.put("description",
								ParseXMLmethods.getValue(e, "description"));
						localist.add(map);

						// String title = ParseXMLmethods.getValue(e, "title");
						// String pubdate = ParseXMLmethods.getValue(e,
						// "pubDate");
						// String description = ParseXMLmethods.getValue(e,
						// "description");
						// String link = ParseXMLmethods.getValue(e, "link");
						//
						// mDbHelper.createAnakoinosi(title, pubdate,
						// description,
						// link);
						if (isCancelled())
							break;
					}
				}
				return 1;

			} else {
				ZERO_FLAG = true;
				publishProgress();
				return null;
			}

		}

		@Override
		protected void onCancelled() {
			Log.d("onCancelled", "true");
			// localist.clear();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			if (ZERO_FLAG == true) {
				Toast.makeText(mContext,
						"Δεν υπάρχουν διαθέσιμες ανακοινώσεις.",
						Toast.LENGTH_LONG).show();
			}
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Integer result) {

			adapter.notifyDataSetChanged();
			setSupportProgressBarIndeterminateVisibility(false);

			// dialog.dismiss();
			// pullToRefreshView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	public static class ParseXMLmethods {

		public final static Document XMLfromString(String xml) {

			Document doc = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				// if (UTF8) {
				/* Κώδικας για ανακοινωσεις teilar */
				// ByteArrayInputStream encXML = new ByteArrayInputStream(
				// xml.getBytes("ISO-8859-1"));
				// doc = db.parse(encXML);
				// } else {
				/* Κώδικας για ανακοινωσεις cs.teilar */
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(xml));
				doc = db.parse(is);
				// }

			} catch (ParserConfigurationException e) {
				System.out.println("XML parse error: " + e.getMessage());
				return null;
			} catch (SAXException e) {
				System.out.println("Wrong XML file structure: "
						+ e.getMessage());
				return null;
			} catch (IOException e) {
				System.out.println("I/O exeption: " + e.getMessage());
				return null;
			}
			return doc;

		}

		public static String getElementValue(Node elem) {
			Node kid;

			if (elem != null) {
				if (elem.hasChildNodes()) {
					for (kid = elem.getFirstChild(); kid != null;) {
						Log.d("node Value", kid.getNodeValue());
						return kid.getNodeValue();
					}
				}
			}

			return "";
		}

		public static String getXML() {

			String line = null;
			try {

				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				line = EntityUtils.toString(httpEntity);

			} catch (Exception e) {
				line = "Internet Connection Error >> " + e.getMessage();
			}
			return line;
		}

		public static String getValue(Element item, String str) {
			NodeList n = item.getElementsByTagName(str);
			return ParseXMLmethods.getElementValue(n.item(0));
		}

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
				File downloadsDirectory = new File(
						"/sdcard/CST Connect Downloads/");

				if (!downloadsDirectory.exists()) {
					downloadsDirectory.mkdir();
				}

				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(
						"/sdcard/CST Connect Downloads/" + filename);

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
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String unused) {
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			createNotification();
			LIST_UPDATE_NOTIFICATION++;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		myActionBarMenu = menu;
		getSupportMenuInflater().inflate(R.menu.menu_refresh, menu);

		return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			new GetDataTask().cancel(true);
			Log.d("cancel", "true");
			finish();

			break;

		case R.id.refresh:
			if (isOnline()) {
				new GetDataTask().execute();
			} else {
				Toast.makeText(Anakoinoseis.this, R.string.DataFailureWarning,
						Toast.LENGTH_LONG).show();
			}
			break;
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// mDbHelper.open();
	// }

	// @Override
	// protected void onPause() {
	// super.onPause();
	// mDbHelper.close();
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
	
		String[] teachers_emails = getResources()
				.getStringArray(R.array.Emails);

		rss = "http://www.cs.teilar.gr/CS/rss.jsp?id="
				+ teachers_emails[itemPosition];
		
		httpPost = new HttpPost(rss);
		new GetDataTask().execute();
		 
		return true;
	}

}