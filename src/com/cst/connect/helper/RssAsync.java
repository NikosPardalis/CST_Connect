package com.cst.connect.helper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.methods.HttpPost;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.cst.connect.activities.Rss;

public class RssAsync extends
		AsyncTask<HttpPost, Void, ArrayList<HashMap<String, String>>> {

	Context mContext;
	Rss rss = new Rss();
	RssHelper helper = new RssHelper(mContext);
	RssParser parser = new RssParser(mContext);
	String sdcard = Environment.getExternalStorageDirectory().getPath();
	private ArrayList<HashMap<String, String>> localist = new ArrayList<HashMap<String, String>>();
	public AsyncTaskListener callback;
	private volatile boolean running = true;

	public RssAsync(AsyncTaskListener callback) {
		this.callback = callback;
	}

	Boolean ZERO_FLAG = false;

	@Override
	protected void onCancelled() {
		running = false;

	}

	@Override
	protected ArrayList<HashMap<String, String>> doInBackground(
			HttpPost... httpPost) {

		HttpPost mHttpPost = httpPost[0];
		String xml = parser.getXML(mHttpPost);
		Document doc = parser.XMLfromString(xml);

		if (doc != null) {
			NodeList children = doc.getElementsByTagName("item");
			ZERO_FLAG = false;
			if (children.getLength() == 0) {
				// ZERO_FLAG = true;
				// publishProgress();
				return localist;
			}
			if (!isCancelled()) {

				for (int i = 0; i < children.getLength(); i++) {

					HashMap<String, String> map = new HashMap<String, String>();

					Element e = (Element) children.item(i);
					map.put("title", parser.getValue(e, "title"));
					map.put("pubDate", parser.getValue(e, "pubDate"));
					map.put("link", parser.getValue(e, "link"));
					map.put("description", parser.getValue(e, "description"));
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
					if (isCancelled() || running == false) {
						Log.d("Parsing Canceled", "True");
						localist.clear();
						return localist;
						// break;
					}
				}
			}
			return localist;

		} else {
			ZERO_FLAG = true;

			// publishProgress();
			return localist;
		}

	}

	// @Override
	// protected void onCancelled() {
	// Log.d("onCancelled", "true");
	// // localist.clear();
	// }

	@Override
	protected void onProgressUpdate(Void... values) {

		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
		super.onPostExecute(result);

		// dialog.dismiss();
		// pullToRefreshView.onRefreshComplete();

		callback.onTaskComplete(result);
	}
}
