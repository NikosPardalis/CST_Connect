package com.cst.connect.helper;

import java.io.IOException;
import java.net.URL;

import android.os.AsyncTask;

public class ImageFileSize extends AsyncTask<String, Void, String> {

	public AsyncResponse callback;

	public ImageFileSize(AsyncResponse callback) {
		this.callback = callback;
	}

	@Override
	protected String doInBackground(String... urls) {
		long size = 0;
		String response = "";
		String url = urls[0];
		try {
			URL imageFileUrl = new URL(url);
			size = imageFileUrl.openConnection().getContentLength();
		} catch (IOException e) {
			e.printStackTrace();
		}
		response = Long.toString(size);
		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		callback.processResult(result);
	}
}
