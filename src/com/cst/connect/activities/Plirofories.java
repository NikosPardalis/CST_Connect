package com.cst.connect.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.cst.connect.R;
import com.viewpagerindicator.TitlePageIndicator;

public class Plirofories extends SherlockActivity {

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_layout);
				
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Πληροφορίες Τμήματος");

		ViewPagerAdapter adapter = new ViewPagerAdapter(this);
		ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
		TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);

	}

	class ViewPagerAdapter extends PagerAdapter {
		private final String[] titles = new String[] { "Τμήμα", "Γραμματεία",
				"Ενιαίο Μητρώο", "Τηλέφωνα" };
		private final Context context;
		private int[] scrollPosition = new int[titles.length];

		public ViewPagerAdapter(Context context) {
			this.context = context;
			for (int i = 0; i < titles.length; i++) {
				scrollPosition[i] = 0;
			}
		}

		@Override
		public String getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Object instantiateItem(View pager, int position) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = null;
			switch (position) {

			case 0:
				v = infalInflater.inflate(R.layout.plirofories_tmima, null);
				break;
			case 1:
				v = infalInflater
						.inflate(R.layout.plirofories_grammateia, null);
				break;
			case 2:
				v = infalInflater.inflate(R.layout.plirofories_mitrwo, null);
				break;
			case 3:
				v = infalInflater.inflate(R.layout.plirofories_tilefona, null);
				break;
			default:
				break;
			}
			((ViewPager) pager).addView(v, 0);
			return v;
		}

		@Override
		public void destroyItem(View pager, int position, Object view) {
			((ViewPager) pager).removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void finishUpdate(View view) {
		}

		@Override
		public void restoreState(Parcelable p, ClassLoader c) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View view) {
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();

		}
		return true;
	}

}
