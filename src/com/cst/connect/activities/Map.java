package com.cst.connect.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cst.connect.R;
import com.cst.connect.fragments.MapFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.maps.GeoPoint;

public class Map extends SherlockFragmentActivity implements LocationListener {

	private Fragment mFragment;
	private LocationManager locationManager;
	private GeoPoint currentPoint;
	private Location currentLocation = null;
	Context mContext = this;
	boolean gps_enabled = false;
	boolean network_enabled = false;
	private String provider;
	GPSStatuslistener gpsStatuslistener;
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_placeholder);

		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);

		mActionBar.setTitle("Χάρτης Τμήματος");

		checkPlayServices();

		if (savedInstanceState == null) {
			FragmentTransaction mFt = getSupportFragmentManager()
					.beginTransaction();
			mFragment = Fragment.instantiate(mContext,
					MapFragment.class.getName());
			mFt.add(R.id.fragment, mFragment);
			mFt.commit();
		}

	}

	protected boolean isRouteDisplayed() {
		return false;
	}

	public void getLastLocation() {
		provider = getBestProvider();
		if (provider != null) {
			currentLocation = locationManager.getLastKnownLocation(provider);

			if (currentLocation == null) {
				Toast.makeText(
						this,
						"Η τοποθεσία δεν έχει εντοπιστεί ακόμη. Ενεργοποιήστε τις υπηρεσίες εντοπισμού θέσης για ταχύτερο εντοπισμό.",
						Toast.LENGTH_SHORT).show();
			} else {

				setCurrentLocation(currentLocation);
				Toast.makeText(
						this,
						"Η τοποθεσία εντοπίστηκε. Μπορείτε να πλοηγηθήτε στις εγκαταστάσεις του ΤΕΙ πατώντας το κουμπί πλοήγησης.",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(
					this,
					"Η τοποθεσία δεν έχει εντοπιστεί ακόμη. Ενεργοποιήστε τις υπηρεσίες εντοπισμού θέσης για ταχύτερο εντοπισμό.",
					Toast.LENGTH_SHORT).show();
		}
	}

	public String getBestProvider() {
		String bestProvider = null;
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		gps_enabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		network_enabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		Criteria criteria = new Criteria();
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);

		if (!network_enabled && !gps_enabled) {
			// Toast.makeText(getApplicationContext(),
			// "Δεν έχει ενεργοποιηθεί καμία υπηρεσία εντοπισμού.",
			// Toast.LENGTH_SHORT).show();

		} else if (network_enabled && !gps_enabled) {
			criteria.setAccuracy(Criteria.ACCURACY_COARSE);
			bestProvider = locationManager.getBestProvider(criteria, true);

		} else if ((gps_enabled && !network_enabled)
				|| (network_enabled && gps_enabled)) {
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			bestProvider = locationManager.getBestProvider(criteria, true);

		}

		if (gps_enabled) {
			locationManager.addGpsStatusListener(gpsStatuslistener);
		} else {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, this);
		}
		return bestProvider;
	}

	public void setCurrentLocation(Location location) {
		int currLatitude = (int) (location.getLatitude() * 1E6);
		int currLongitude = (int) (location.getLongitude() * 1E6);
		currentPoint = new GeoPoint(currLatitude, currLongitude);

		currentLocation = new Location("");
		currentLocation.setLatitude(currentPoint.getLatitudeE6() / 1e6);
		currentLocation.setLongitude(currentPoint.getLongitudeE6() / 1e6);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// int SDK_INT = android.os.Build.VERSION.SDK_INT;
		// if (SDK_INT >= 8) {
		MenuItem info_legal = menu.add("Σχετικά με Google Maps");
		getSupportMenuInflater().inflate(R.menu.menu_maps, menu);
		// } else if (SDK_INT == 7) {
		// getSupportMenuInflater().inflate(R.menu.menu_maps_eclair, menu);
		// }

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			break;

		case 0:

			Intent legal = new Intent(mContext, LegalNotices.class);
			startActivity(legal);
			break;

		case R.id.skarifima:

			Intent intent = new Intent(getBaseContext(), ImageDisplayer.class);
			intent.putExtra("title", "skarifima");
			startActivity(intent);

			break;

		case R.id.ploigisi:

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage(
					"Θέλετε να λάβετε οδηγίες πλοήγησης για τις εγκαταστάσεις του ΤΕΙ Λάρισας;")

					.setPositiveButton("Πλοήγηση",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									if (currentPoint != null) {
										navigate(currentPoint);
									} else if (currentPoint == null) {
										Toast.makeText(
												getApplicationContext(),
												"Η τοποθεσία δεν έχει εντοπιστεί ακόμη. Ενεργοποιήστε τις υπηρεσίες εντοπισμού θέσης για ταχύτερο εντοπισμό.",
												Toast.LENGTH_SHORT).show();
									}
									if (getBestProvider() == null) {
										Intent gps = new Intent(
												android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
										startActivity(gps);
									}

								}
							})
					.setNegativeButton("’κυρο",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}
		return true;
	}

	public void navigate(GeoPoint currentPoint) {
		String current;
		Intent intent;

		String CST = "39.627604,22.383723";
		current = String.valueOf(currentPoint);
		current = current.substring(0, 2) + "." + current.substring(2, 11)
				+ "." + current.substring(11, 17);
		Log.d("Curent position", current);
		intent = new Intent(android.content.Intent.ACTION_VIEW,
				Uri.parse("http://maps.google.com/maps?saddr=" + current
						+ "&daddr=" + CST));
		startActivity(intent);
	}

	private class GPSStatuslistener implements GpsStatus.Listener {

		@Override
		public void onGpsStatusChanged(int event) {
			switch (event) {
			case GpsStatus.GPS_EVENT_STARTED:
				Log.d(null, "ongpsstatus changed started");
				// TODO: your code that get location updates, e.g. set active
				// location listener
				getLastLocation();
				break;
			case GpsStatus.GPS_EVENT_STOPPED:
				Log.d(null, "ongpsstatus changed stopped");
			}
		}
	}

	@Override
	public void onLocationChanged(Location newLocation) {
		setCurrentLocation(newLocation);

	}

	@Override
	public void onProviderDisabled(String provider) {
		// Log.d("onProviderDisabled", provider);
		// if (locationManager != null && getBestProvider() != null) {
		// locationManager.requestLocationUpdates(getBestProvider(), 1000, 1,
		// this);
		// }
		// getLastLocation();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d("onProviderEnabled", provider);

		if (locationManager != null && getBestProvider() != null) {
			locationManager.requestLocationUpdates(getBestProvider(), 1000, 1,
					this);
		}

		getLastLocation();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		locationManager.removeUpdates(this);
		locationManager.addGpsStatusListener(gpsStatuslistener);

	}


	@Override
	protected void onResume() {
		super.onResume();

		checkPlayServices();
		if (locationManager != null && getBestProvider() != null) {
			locationManager.requestLocationUpdates(getBestProvider(), 400, 1,
					this);
		}
		getLastLocation();
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	public boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(mContext);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode,
						(Activity) mContext, PLAY_SERVICES_RESOLUTION_REQUEST)
						.show();
			} else {
				Log.i("", "This device is not supported.");
				// finish();
			}
			return false;
		}
		return true;
	}
}
