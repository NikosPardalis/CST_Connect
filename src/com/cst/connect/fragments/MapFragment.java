package com.cst.connect.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.cst.connect.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends SherlockFragment {

	Context mContext;
	GoogleMap map;
	private SupportMapFragment mMapFragment;	
	MenuItem info_legal;
	static final LatLng TEI = new LatLng(39.6275, 22.3821);
	static final LatLng CST = new LatLng(39.627604, 22.383723);
	static final LatLng LESXI = new LatLng(39.628616, 22.380676);
	static final LatLng MERIMNA = new LatLng(39.625493, 22.382733);
	static final LatLng AMFITHEATRO = new LatLng(39.627641, 22.381918);
	static final LatLng NOC = new LatLng(39.6276, 22.382116);
	static final LatLng UNIX = new LatLng(39.628391, 22.380834);
	static final LatLng LIBRARY = new LatLng(39.628158, 22.383326);
	static final LatLng CST_LABS = new LatLng(39.626937, 22.384348);
	static final LatLng MITRWO = new LatLng(39.627546, 22.381478);
	static final LatLng KULIKEIO = new LatLng(39.626596, 22.381905);
	static final LatLng LABS_T1_T2 = new LatLng(39.627139, 22.382822);
	Marker tei, cst, lesxi, merimna, amfitheatro, noc, unix, library, cst_labs,
			mitrwo, kulikeio, labs_t1_t2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// if (v != null) {
		// ViewGroup parent = (ViewGroup) v.getParent();
		// if (parent != null)
		// parent.removeView(v);
		// }
		// try {
		View v = inflater.inflate(R.layout.map_fragment, container, false);
		// } catch (InflateException e) {
		// /* map is already there, just return view as it is */
		// }

		mContext = getActivity();

		mMapFragment = (SupportMapFragment) getFragmentManager()
				.findFragmentById(R.id.mapFragment);

		if (savedInstanceState == null) {
			mMapFragment.setRetainInstance(true);
		} else {
			map = mMapFragment.getMap();
		}
		setUpMapIfNeeded();

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		if (map == null) {
			map = ((SupportMapFragment) getFragmentManager().findFragmentById(
					R.id.mapFragment)).getMap();
			if (map != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		cst = map.addMarker(new MarkerOptions().position(CST).title(
				"Τμημα Μηχανικών Πληροφορικής"));

		lesxi = map.addMarker(new MarkerOptions().position(LESXI).title(
				"Φοιτητική Λέσχη"));

		merimna = map.addMarker(new MarkerOptions().position(MERIMNA).title(
				"Σπουδαστική Μέριμνα"));

		amfitheatro = map.addMarker(new MarkerOptions().position(AMFITHEATRO)
				.title("Αμφιθέατρο ΓΤΘΕ"));

		noc = map.addMarker(new MarkerOptions().position(NOC).title(
				"Κέντρο Δικτύου"));

		unix = map.addMarker(new MarkerOptions().position(UNIX).title(
				"Εργαστήρια NT & Unix"));

		library = map.addMarker(new MarkerOptions().position(LIBRARY).title(
				"Κεντρική Βιβλιοθήκη"));

		cst_labs = map.addMarker(new MarkerOptions().position(CST_LABS).title(
				"Νέα εργαστήρια ΤΠΤ"));

		mitrwo = map.addMarker(new MarkerOptions().position(MITRWO).title(
				"Ενιαίο Σπουδαστικό Μητρώο"));

		kulikeio = map.addMarker(new MarkerOptions().position(KULIKEIO).title(
				"Κυλικείο"));

		labs_t1_t2 = map.addMarker(new MarkerOptions().position(LABS_T1_T2)
				.title("Εργαστήρια Τ1 & T2"));

		map.setMyLocationEnabled(true);
		map.getUiSettings().setRotateGesturesEnabled(false);
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(CST, 16));
		cst.showInfoWindow();
	}

	// public void onDestroyView() {
	// try {
	// mMapFragment = ((SupportMapFragment) getFragmentManager()
	// .findFragmentById(R.id.mapFragment));
	// FragmentTransaction ft = getActivity().getSupportFragmentManager()
	// .beginTransaction();
	// ft.remove(mMapFragment);
	// ft.commit();
	// } catch (Exception e) {
	// }
	// super.onDestroyView();
	// }

}
