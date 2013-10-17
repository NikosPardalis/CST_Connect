package com.cst.connect.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.cst.connect.R;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class LegalNotices extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.legal);

		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("Σχετικά με Google Maps");

		TextView legal = (TextView) findViewById(R.id.legal);

		legal.setText(GooglePlayServicesUtil
				.getOpenSourceSoftwareLicenseInfo(this));
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}

}