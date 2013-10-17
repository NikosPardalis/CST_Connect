package com.cst.connect.activities;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cst.connect.R;
import com.cst.connect.helper.AsyncResponse;
import com.cst.connect.helper.ImageFileSize;
import com.cst.connect.helper.RssHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageDisplayer extends SherlockActivity implements
		OnNavigationListener, AsyncResponse {
	WebView webView;
	TextView failView;
	Context mContext = this;
	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	ProgressBar progress;
	String url, title;
	String path, imgSrcHtml;
	RssHelper helper = new RssHelper(mContext);;
	File image, new_image = null;
	String local_image_size, remote_image_size = "";
	private Menu optionsMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.web_image);

		Intent sender = getIntent();
		title = sender.getExtras().getString("title");

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		webViewSetup();
		imageLoaderSetup();

		if (title.contains("programma")) {

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

			getSupportActionBar().setNavigationMode(
					ActionBar.NAVIGATION_MODE_LIST);
			getSupportActionBar().setListNavigationCallbacks(list, this);

		}

		if (title.contains("alusides")) {

			Context context = getSupportActionBar().getThemedContext();
			ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(
					context, R.array.alusides_list,
					R.layout.sherlock_spinner_item);
			list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

			getSupportActionBar().setNavigationMode(
					ActionBar.NAVIGATION_MODE_LIST);
			getSupportActionBar().setListNavigationCallbacks(list, this);
			actionBar.setTitle("Προαπαιτούμενα");
		}

		if (title.contains("exetastiki")) {
			actionBar.setTitle("Πρόγραμμα Εξεταστικής");
			url = "https://sites.google.com/site/cstconnectbackend/exetastiki.png";
			imageLoader(url);
		} else if (title.contains("skarifima")) {
			actionBar.setTitle("Σκαρίφημα");
			url = "https://sites.google.com/site/cstconnectbackend/skarifima.png";
			imageLoader(url);
		}

	}

	public void webViewSetup() {

		webView = (WebView) findViewById(R.id.web);
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

	public void imageLoaderSetup() {

		options = new DisplayImageOptions.Builder().cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.resetViewBeforeLoading(true)
				.bitmapConfig(Bitmap.Config.ARGB_8888)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	public void imageLoaderMethods(String url) {

		ImageLoadingListener listener = new AnimateFirstDisplayListener();
		imageLoader.loadImage(url, options, listener);

	}

	public void imageLoader(String url) {

		if (helper.isOnline()) {
			new ImageFileSize(this).execute(url);
		} else {
			imageLoaderMethods(url);
		}

	}

	@Override
	public void processResult(String output) {
		remote_image_size = output;
		Log.d("remote_image_size", remote_image_size);
		image = DiscCacheUtil.findInCache(url, imageLoader.getDiscCache());
		if (image == null) {
			imageLoaderMethods(url);
			return;
		}
		local_image_size = Long.toString(image.length());
		Log.d("local_image_size", local_image_size);

		if (!remote_image_size.equals(local_image_size)) {
			webView.clearCache(true);
			DiscCacheUtil.removeFromCache(url, imageLoader.getDiscCache());
			Toast.makeText(this, "Η εικόνα έχει ανανεωθεί", Toast.LENGTH_LONG)
					.show();
		}
		imageLoaderMethods(url);
	}

	public void displayImage(String url) {
		File imageFile = imageLoader.getDiscCache().get(url);
		path = "file://" + imageFile.getAbsolutePath();
		imgSrcHtml = "<html><head></head><body><img src=\"" + path
				+ "\"></body></html>";

		webView.loadDataWithBaseURL("", imgSrcHtml, "text/html", "utf-8", "");

	}

	private class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			setRefreshActionButtonState(true);
			failView = (TextView) findViewById(R.id.failView);
			failView.setVisibility(4);
			super.onLoadingStarted(imageUri, view);
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			setRefreshActionButtonState(false);
			displayImage(url);
			super.onLoadingComplete(imageUri, view, loadedImage);
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {

			setRefreshActionButtonState(false);
			failView = (TextView) findViewById(R.id.failView);
			failView.setVisibility(0);
			super.onLoadingFailed(imageUri, view, failReason);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		this.optionsMenu = menu;
		getSupportMenuInflater().inflate(R.menu.menu_refresh, menu);
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
				imageLoaderMethods(url);
			} else {
				Toast.makeText(mContext,
						"Δεν υπάρχει διαθέσιμη σύνδεση στο δίκτυο.",
						Toast.LENGTH_SHORT).show();
			}
			break;

		}
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		switch (itemPosition) {
		case 0:
			if (title.contains("programma")) {
				url = "https://sites.google.com/site/cstconnectbackend/programma_1.png";
			} else if (title.contains("alusides")) {
				url = "https://sites.google.com/site/cstconnectbackend/proapaitoumena_pliroforikis.png";
			}
			imageLoader(url);
			break;
		case 1:

			if (title.contains("programma")) {
				url = "https://sites.google.com/site/cstconnectbackend/programma_3.png";
			} else if (title.contains("alusides")) {
				url = "https://sites.google.com/site/cstconnectbackend/proapaitoumena_diktion.png";
			}
			imageLoader(url);
			break;
		case 2:
			if (title.contains("programma")) {
				url = "https://sites.google.com/site/cstconnectbackend/programma_5.png";
			} else if (title.contains("alusides")) {
				url = "https://sites.google.com/site/cstconnectbackend/proapaitoumena_logismikou.png";
			}
			imageLoader(url);
			break;
		case 3:

			url = "https://sites.google.com/site/cstconnectbackend/programma_7.png";
			imageLoader(url);
			break;
		}

		return true;
	}

	public int getScale() {
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		int width = display.getWidth();
		Double val = new Double(width) / new Double(799);
		val = val * 100d;
		return val.intValue();
	}

	@Override
	protected void onDestroy() {
		image = null;
		super.onDestroy();
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

}
