package com.cst.connect.helper;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.os.AsyncTask;

public class AsyncTextProvider extends AsyncTask<String, Void, String> {

	public AsyncResponse callback;

	public AsyncTextProvider(AsyncResponse callback) {
		this.callback = callback;
	}

	@Override
	protected String doInBackground(String... urls) {

		String result = "";
		String url = urls[0];
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (doc != null) {
			Element message = doc.select("div[dir]").first();
			result = message.text();
		}

		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		callback.processResult(result);
	}
}
