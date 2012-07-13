package com.cst.connect;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class MobileWebView extends SherlockActivity {

	WebView webView;
	boolean loadingFinished = true;
	boolean redirect = false;	
	Context context;
	
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.webview);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		Intent sender = getIntent();
		String url = sender.getExtras().getString("Link");

		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		
				        
		if (url.contains("skarifima")) {
			actionBar.setTitle("Σκαρίφημα");
			String data = "<body>" + "<img src=\"skarifima.jpg\"/></body>";
			webView.loadDataWithBaseURL("file:///android_asset/", data,
					"text/html", "utf-8", null);
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

		} else if (url.contains("exetastiki")) {
			actionBar.setTitle("Πρόγραμμα Εξεταστικής");
			String data = "<body>" + "<img src=\"exetastiki.png\"/></body>";
			webView.loadDataWithBaseURL("file:///android_asset/", data,
					"text/html", "utf-8", null);
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

		} else {
			webView.loadUrl(url);
		}

		setSupportProgressBarIndeterminateVisibility(true);
		if (url.contains("cs.teilar")) {
			actionBar.setTitle("CS Mobile");
		} else if (url.contains("myweb")) {
			actionBar.setTitle("Web Mail");
		} else if (url.contains("Results")) {
			actionBar.setTitle("Βαθμολογίες");
			webView.getSettings().setUseWideViewPort(true);
			if (getResources().getConfiguration().orientation == 1)
				Toast.makeText(
						getApplicationContext(),
						"Για καλύτερη προβολή περιστρέψτε την συσκευή σας οριζόντια",
						Toast.LENGTH_SHORT).show();
		} else if (url.contains("diloseis")) {			
			actionBar.setTitle("Δηλώσεις Εξαμήνων");
			webView.getSettings().setUseWideViewPort(true);
			if (getResources().getConfiguration().orientation == 1)
				Toast.makeText(
						getApplicationContext(),
						"Για καλύτερη προβολή περιστρέψτε την συσκευή σας οριζόντια",
						Toast.LENGTH_SHORT).show();
		}

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				setSupportProgressBarIndeterminateVisibility(false);

			}

		});

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
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

}
