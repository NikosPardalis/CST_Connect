package com.cst.connect.activities;

import java.io.File;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.utils.FileUtils;

public class FileBrowser extends FileChooserActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// if (Intent.ACTION_MAIN.equals(getIntent().getAction())) {
		// Display the file chooser dialog with default options.
		// showFileChooser();
		startExplorer();
		// }

		// if (!isIntentGetContent()) {
		// // Display the file chooser with all file types
		// showFileChooser("*/*");
		// }
	}

	final String TAG = "FileSelectorTestActivity";

	@Override
	public void onFileSelect(File file) {
		if (file != null) {
			final Context context = getApplicationContext();

			// Get the path of the Selected File.
			final String path = file.getAbsolutePath();
			Log.d(TAG, "File path: " + path);

			// Get the MIME type of the Selected File.
			String mimeType = FileUtils.getMimeType(context, file);
			Log.d(TAG, "File MIME type: " + mimeType);

			// Get the thumbnail of the Selected File, if image/video
			// final Uri uri = Uri.fromFile(file);
			// Bitmap bm = FileUtils.getThumbnail(context, uri, mimeType);

			// Here you can return any data from above to the calling Activity

			finish();
		}
	}

	@Override
	protected void onFileError(Exception e) {
		Log.e(TAG, "File select error", e);
		finish();
	}

	@Override
	protected void onFileSelectCancel() {
		Log.d(TAG, "File selections canceled");
		finish();
	}

	@Override
	protected void onFileDisconnect() {
		Log.d(TAG, "External storage disconneted");
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		}
		return true;

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}