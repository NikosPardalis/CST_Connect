package com.cst.connect;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class CSMapView extends SherlockMapActivity implements LocationListener {

	List<Overlay> mapOverlays;
	Drawable drawable;
	MapItems itemizedOverlay;
	MapView mapView;
	MapController mapController;
	private LocationManager locationManager;
	private GeoPoint currentPoint;
	private Location currentLocation = null;
	Context context = this;
	boolean gps_enabled = false;
	boolean network_enabled = false;
	private String provider;
	GPSStatuslistener gpsStatuslistener;


	
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mapview);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Χάρτης Τμήματος");

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);
		
		getLastLocation();
		
		CSOverlay csoverlay = new CSOverlay();

		List<Overlay> overlayList = mapView.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.map_pin_mini);

		overlayList.add(csoverlay);
		mapController = mapView.getController();
		mapController.setCenter(new GeoPoint((int) (39.6275f * 1e6),
				(int) (22.3821f * 1e6)));
		mapController.setZoom(18);

		GeoPoint CST = new GeoPoint((int) (39.627604f * 1e6),
				(int) (22.383723f * 1e6));
		OverlayItem overlayItem_CST = new OverlayItem(CST,
				"Τμημα Πληροφορικής & Τηλεπικοινωνιών", "");

		GeoPoint lesxi = new GeoPoint((int) (39.628616f * 1e6),
				(int) (22.380676f * 1e6));
		OverlayItem overlayItem_lesxi = new OverlayItem(lesxi,
				"Φοιτητική Λέσχη", "");

		GeoPoint merimna = new GeoPoint((int) (39.625493f * 1e6),
				(int) (22.382733f * 1e6));
		OverlayItem overlayItem_merimna = new OverlayItem(merimna,
				"Σπουδαστική Μέριμνα", "");

		GeoPoint gthe = new GeoPoint((int) (39.627641f * 1e6),
				(int) (22.381918f * 1e6));
		OverlayItem overlayItem_gthe = new OverlayItem(gthe, "Αμφιθέατρο ΓΤΘΕ",
				"");

		GeoPoint noc = new GeoPoint((int) (39.6276f * 1e6),
				(int) (22.382116f * 1e6));
		OverlayItem overlayItem_noc = new OverlayItem(noc, "Κέντρο Δικτύου", "");

		GeoPoint nt_unix_labs = new GeoPoint((int) (39.628391f * 1e6),
				(int) (22.380834f * 1e6));
		OverlayItem overlayItem_nt_unix_labs = new OverlayItem(nt_unix_labs,
				"Εργαστήρια NT & Unix", "");

		GeoPoint vivliothiki = new GeoPoint((int) (39.628158 * 1e6),
				(int) (22.383326f * 1e6));
		OverlayItem overlayItem_vivliothiki = new OverlayItem(vivliothiki,
				"Κεντρική Βιβλιοθήκη", "");

		GeoPoint CST_new = new GeoPoint((int) (39.626937f * 1e6),
				(int) (22.384348f * 1e6));
		OverlayItem overlayItem_CST_new = new OverlayItem(CST_new,
				"Νέα κτήρια ΤΠΤ", "");

		GeoPoint mitrwo = new GeoPoint((int) (39.627546f * 1e6),
				(int) (22.381478f * 1e6));
		OverlayItem overlayItem_mitrwo = new OverlayItem(mitrwo,
				"Ενιαίο Σπουδαστικό Μητρώο", "");

		GeoPoint Kulikeio = new GeoPoint((int) (39.626596f * 1e6),
				(int) (22.381905f * 1e6));
		OverlayItem overlayItem_Kulikeio = new OverlayItem(Kulikeio,
				"Νέο Κυλικείο", "");

		GeoPoint t_labs = new GeoPoint((int) (39.627139f * 1e6),
				(int) (22.382822f * 1e6));
		OverlayItem overlayItem_t_labs = new OverlayItem(t_labs,
				"Εργαστήρια Τ1 & T2", "");

		mapOverlays = mapView.getOverlays();
		itemizedOverlay = new MapItems(drawable, mapView);

		itemizedOverlay.addOverlay(overlayItem_CST);
		itemizedOverlay.addOverlay(overlayItem_lesxi);
		itemizedOverlay.addOverlay(overlayItem_merimna);
		itemizedOverlay.addOverlay(overlayItem_gthe);
		itemizedOverlay.addOverlay(overlayItem_noc);
		itemizedOverlay.addOverlay(overlayItem_nt_unix_labs);
		itemizedOverlay.addOverlay(overlayItem_vivliothiki);
		itemizedOverlay.addOverlay(overlayItem_CST_new);
		itemizedOverlay.addOverlay(overlayItem_mitrwo);
		itemizedOverlay.addOverlay(overlayItem_Kulikeio);
		itemizedOverlay.addOverlay(overlayItem_t_labs);

		mapOverlays.add(itemizedOverlay);
		
		
				
	}

	class CSOverlay extends Overlay {

		public boolean onTouchEvent(MotionEvent e, MapView mapView) {

			return super.onTouchEvent(e, mapView);
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
//			Toast.makeText(getApplicationContext(),
//					"Δεν έχει ενεργοποιηθεί καμία υπηρεσία εντοπισμού.",
//					Toast.LENGTH_SHORT).show();

		} else if (network_enabled && !gps_enabled) {
			criteria.setAccuracy(Criteria.ACCURACY_COARSE);
			bestProvider = locationManager.getBestProvider(criteria, true);

		} else if ((gps_enabled && !network_enabled)
				|| (network_enabled && gps_enabled)) {
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			bestProvider = locationManager.getBestProvider(criteria, true);

		}
		
		if(gps_enabled){
		    locationManager.addGpsStatusListener(gpsStatuslistener);
		}else{
		    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, this);
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

	class MapItems extends BalloonItemizedOverlay<OverlayItem> {

		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		Context mContext;

		public MapItems(Drawable defaultMarker, MapView mapView) {
			super(boundCenter(defaultMarker), mapView);
			mContext = mapView.getContext();
		}

		public void addOverlay(OverlayItem overlay) {

			mOverlays.add(overlay);
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {

			return mOverlays.get(i);
		}

		@Override
		public int size() {

			return mOverlays.size();
		}

		@Override
		protected boolean onBalloonTap(final int index, OverlayItem item) {

			return true;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
//		int SDK_INT = android.os.Build.VERSION.SDK_INT;
//		if (SDK_INT >= 8) {
			getSupportMenuInflater().inflate(R.menu.menu_maps, menu);
//		} else if (SDK_INT == 7) {
//			getSupportMenuInflater().inflate(R.menu.menu_maps_eclair, menu);
//		}

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();

			break;

		case R.id.skarifima:

			Intent intent = new Intent("com.cst.connect.MobileWebView");
			intent.putExtra("Link", "skarifima");
			startActivity(intent);

			break;

		case R.id.ploigisi:

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
	            //TODO: your code that get location updates, e.g. set active location listener 
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
//		Log.d("onProviderDisabled", provider);
//		if (locationManager != null && getBestProvider() != null) {
//			locationManager.requestLocationUpdates(getBestProvider(), 1000, 1,
//					this);
//		}
//		getLastLocation();
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

}
