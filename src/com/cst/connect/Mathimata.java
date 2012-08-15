package com.cst.connect;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitleProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import android.widget.TextView;

public class Mathimata extends SherlockActivity {

	Context context = this;
	AlertDialog.Builder builder = null;
	AlertDialog alertDialog = null;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private static ProgressDialog mProgressDialog;
	private static int LIST_UPDATE_NOTIFICATION = 1;
	private static boolean FILE_EXISTS = false;
	File file, downloadsDirectory, download;
	String downloadsDirectoryPath = "/sdcard/CST Connect Downloads/";
	String filename;
	// final int COLOR_CHANGES = Color.rgb(212, 117, 33);
	// final int COLOR_CHANGES = Color.rgb(104, 153, 255);
	// final int COLOR_CHANGES = Color.rgb(51, 181, 229);
	final int COLOR_CHANGES = Color.rgb(47, 125, 153);

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_layout);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Μαθήματα");

		Toast.makeText(
				this,
				"Με γαλάζιο χρώμα είναι επισημασμένες οι αλλαγές του Προγράμματος Σπουδών 2012-2013",
				Toast.LENGTH_LONG).show();

		ViewPagerAdapter adapter = new ViewPagerAdapter(this);
		ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
		TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);
	}

	class ViewPagerAdapter extends PagerAdapter implements TitleProvider {
		private final String[] titles = new String[] { "Εξάμηνο Α",
				"Εξάμηνο Β", "Εξάμηνο Γ", "Εξάμηνο Δ", "Εξάμηνο Ε",
				"Εξάμηνο ΣΤ", "Εξάμηνο Ζ", "Εξάμηνο Η" };
		private final Context context;

		public ViewPagerAdapter(Context context) {
			this.context = context;
		}

		@Override
		public String getTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Object instantiateItem(View pager, int position) {

			ListView v = new ListView(context);
			v.setEnabled(false);
			switch (position) {

			case 0:
				SimpleAdapter examino_A_adapter = new SimpleAdapter(context,
						examino_A_group_list(), R.layout.mathimata_list_item,
						new String[] { "Τίτλος", "Ομάδα", "Τύπος", "ΔΜ",
								"Θεωρεία", "Φροντιστήριο", "Εργαστήριο" },
						new int[] { R.id.row_title, R.id.row_omada,
								R.id.row_typos, R.id.row_dm, R.id.row_theoreia,
								R.id.row_frontistirio, R.id.row_ergastirio }) {
					@SuppressWarnings("unchecked")
					@Override
					public View getView(int groupPosition, View convertView,
							ViewGroup parent) {

						if (convertView == null) {
							LayoutInflater infalInflater = (LayoutInflater) context
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							convertView = infalInflater.inflate(
									R.layout.mathimata_list_item, null);
						}

						// View v = super.getView(groupPosition,convertView,
						// parent);

						((TextView) convertView.findViewById(R.id.row_title))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τίτλος"));

						if (((String) ((Map<String, Object>) getItem(groupPosition))
								.get("Τίτλος")).contains("Φυσική")) {
							convertView.setBackgroundColor(COLOR_CHANGES);
						}

						((TextView) convertView.findViewById(R.id.row_omada))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Ομάδα"));

						((TextView) convertView.findViewById(R.id.row_typos))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τύπος"));

						((TextView) convertView.findViewById(R.id.row_dm))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("ΔΜ"));

						((TextView) convertView.findViewById(R.id.row_theoreia))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Θεωρεία"));

						((TextView) convertView
								.findViewById(R.id.row_frontistirio))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Φροντιστήριο"));

						((TextView) convertView
								.findViewById(R.id.row_ergastirio))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Εργαστήριο"));

						((ImageView) convertView
								.findViewById(R.id.mathima_icon))
								.setImageDrawable((Drawable) ((HashMap<String, Object>) getItem(groupPosition))
										.get("Εικόνα"));

						return convertView;
					}

				};
				v.setAdapter(examino_A_adapter);

				break;

			case 1:

				SimpleAdapter examino_B_adapter = new SimpleAdapter(context,
						examino_B_group_list(), R.layout.mathimata_list_item,
						new String[] { "Τίτλος", "Ομάδα", "Τύπος", "ΔΜ",
								"Θεωρεία", "Φροντιστήριο", "Εργαστήριο" },
						new int[] { R.id.row_title, R.id.row_omada,
								R.id.row_typos, R.id.row_dm, R.id.row_theoreia,
								R.id.row_frontistirio, R.id.row_ergastirio })

				{

					@SuppressWarnings("unchecked")
					@Override
					public View getView(int groupPosition, View convertView,
							ViewGroup parent) {

						if (convertView == null) {
							LayoutInflater infalInflater = (LayoutInflater) context
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							convertView = infalInflater.inflate(
									R.layout.mathimata_list_item, null);
						}

						// View v = super.getView(groupPosition,convertView,
						// parent);
						((TextView) convertView.findViewById(R.id.row_title))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τίτλος"));
						if (((String) ((Map<String, Object>) getItem(groupPosition))
								.get("Τίτλος")).contains("Ηλεκτρονικά")) {
							convertView.setBackgroundColor(COLOR_CHANGES);
						}
						((TextView) convertView.findViewById(R.id.row_omada))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Ομάδα"));

						((TextView) convertView.findViewById(R.id.row_typos))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τύπος"));

						((TextView) convertView.findViewById(R.id.row_dm))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("ΔΜ"));

						((TextView) convertView.findViewById(R.id.row_theoreia))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Θεωρεία"));

						((TextView) convertView
								.findViewById(R.id.row_frontistirio))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Φροντιστήριο"));

						((TextView) convertView
								.findViewById(R.id.row_ergastirio))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Εργαστήριο"));

						((ImageView) convertView
								.findViewById(R.id.mathima_icon))
								.setImageDrawable((Drawable) ((HashMap<String, Object>) getItem(groupPosition))
										.get("Εικόνα"));
						return convertView;
					}

				};

				v.setAdapter(examino_B_adapter);

				break;

			case 2:

				SimpleAdapter examino_C_adapter = new SimpleAdapter(context,
						examino_C_group_list(), R.layout.mathimata_list_item,
						new String[] { "Τίτλος", "Ομάδα", "Τύπος", "ΔΜ",
								"Θεωρεία", "Φροντιστήριο", "Εργαστήριο" },
						new int[] { R.id.row_title, R.id.row_omada,
								R.id.row_typos, R.id.row_dm, R.id.row_theoreia,
								R.id.row_frontistirio, R.id.row_ergastirio })

				{

					@SuppressWarnings("unchecked")
					@Override
					public View getView(int groupPosition, View convertView,
							ViewGroup parent) {

						if (convertView == null) {
							LayoutInflater infalInflater = (LayoutInflater) context
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							convertView = infalInflater.inflate(
									R.layout.mathimata_list_item, null);
						}

						// View v = super.getView(groupPosition,convertView,
						// parent);
						((TextView) convertView.findViewById(R.id.row_title))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τίτλος"));

						((TextView) convertView.findViewById(R.id.row_omada))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Ομάδα"));

						((TextView) convertView.findViewById(R.id.row_typos))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τύπος"));

						((TextView) convertView.findViewById(R.id.row_dm))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("ΔΜ"));

						((TextView) convertView.findViewById(R.id.row_theoreia))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Θεωρεία"));

						((TextView) convertView
								.findViewById(R.id.row_frontistirio))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Φροντιστήριο"));

						((TextView) convertView
								.findViewById(R.id.row_ergastirio))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Εργαστήριο"));

						((ImageView) convertView
								.findViewById(R.id.mathima_icon))
								.setImageDrawable((Drawable) ((HashMap<String, Object>) getItem(groupPosition))
										.get("Εικόνα"));
						return convertView;
					}

				};
				v.setAdapter(examino_C_adapter);

				break;

			case 3:

				SimpleAdapter examino_D_adapter = new SimpleAdapter(context,
						examino_D_group_list(), R.layout.mathimata_list_item,
						new String[] { "Τίτλος", "Ομάδα", "Τύπος", "ΔΜ",
								"Θεωρεία", "Φροντιστήριο", "Εργαστήριο" },
						new int[] { R.id.row_title, R.id.row_omada,
								R.id.row_typos, R.id.row_dm, R.id.row_theoreia,
								R.id.row_frontistirio, R.id.row_ergastirio })

				{

					@SuppressWarnings("unchecked")
					@Override
					public View getView(int groupPosition, View convertView,
							ViewGroup parent) {

						if (convertView == null) {
							LayoutInflater infalInflater = (LayoutInflater) context
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							convertView = infalInflater.inflate(
									R.layout.mathimata_list_item, null);
						}

						// View v = super.getView(groupPosition,convertView,
						// parent);
						((TextView) convertView.findViewById(R.id.row_title))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τίτλος"));

						if (((String) ((Map<String, Object>) getItem(groupPosition))
								.get("Τίτλος")).contains("Ασφάλεια")) {
							convertView.setBackgroundColor(COLOR_CHANGES);
						}
						((TextView) convertView.findViewById(R.id.row_omada))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Ομάδα"));

						((TextView) convertView.findViewById(R.id.row_typos))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τύπος"));

						((TextView) convertView.findViewById(R.id.row_dm))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("ΔΜ"));

						((TextView) convertView.findViewById(R.id.row_theoreia))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Θεωρεία"));

						((TextView) convertView
								.findViewById(R.id.row_frontistirio))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Φροντιστήριο"));

						((TextView) convertView
								.findViewById(R.id.row_ergastirio))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Εργαστήριο"));

						((ImageView) convertView
								.findViewById(R.id.mathima_icon))
								.setImageDrawable((Drawable) ((HashMap<String, Object>) getItem(groupPosition))
										.get("Εικόνα"));
						return convertView;
					}

				};

				v.setAdapter(examino_D_adapter);

				break;

			case 4:

				SimpleAdapter examino_E_adapter = new SimpleAdapter(context,
						examino_E_group_list(), R.layout.mathimata_list_item,
						new String[] { "Τίτλος", "Ομάδα", "Τύπος", "ΔΜ",
								"Θεωρεία", "Φροντιστήριο", "Εργαστήριο" },
						new int[] { R.id.row_title, R.id.row_omada,
								R.id.row_typos, R.id.row_dm, R.id.row_theoreia,
								R.id.row_frontistirio, R.id.row_ergastirio })

				{
					@SuppressWarnings("unchecked")
					@Override
					public View getView(int groupPosition, View convertView,
							ViewGroup parent) {

						if (convertView == null) {
							LayoutInflater infalInflater = (LayoutInflater) context
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							convertView = infalInflater.inflate(
									R.layout.mathimata_list_item, null);
						}

						// View v = super.getView(groupPosition,convertView,
						// parent);
						((TextView) convertView.findViewById(R.id.row_title))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τίτλος"));

						if (((String) ((Map<String, Object>) getItem(groupPosition))
								.get("Τίτλος")).contains("Βάσεις")
								|| ((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τίτλος")).contains("Υπολογισμού")) {
							convertView.setBackgroundColor(COLOR_CHANGES);
						}
						((TextView) convertView.findViewById(R.id.row_omada))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Ομάδα"));

						((TextView) convertView.findViewById(R.id.row_typos))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τύπος"));

						((TextView) convertView.findViewById(R.id.row_dm))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("ΔΜ"));

						((TextView) convertView.findViewById(R.id.row_theoreia))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Θεωρεία"));

						((TextView) convertView
								.findViewById(R.id.row_frontistirio))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Φροντιστήριο"));

						((TextView) convertView
								.findViewById(R.id.row_ergastirio))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Εργαστήριο"));

						((ImageView) convertView
								.findViewById(R.id.mathima_icon))
								.setImageDrawable((Drawable) ((HashMap<String, Object>) getItem(groupPosition))
										.get("Εικόνα"));
						return convertView;
					}

				};
				v.setAdapter(examino_E_adapter);

				break;

			case 5:

				SimpleAdapter examino_ST_adapter = new SimpleAdapter(context,
						examino_ST_group_list(), R.layout.mathimata_list_item,
						new String[] { "Τίτλος", "Ομάδα", "Τύπος", "ΔΜ",
								"Θεωρεία", "Φροντιστήριο", "Εργαστήριο" },
						new int[] { R.id.row_title, R.id.row_omada,
								R.id.row_typos, R.id.row_dm, R.id.row_theoreia,
								R.id.row_frontistirio, R.id.row_ergastirio })

				{

					@SuppressWarnings("unchecked")
					@Override
					public View getView(int groupPosition, View convertView,
							ViewGroup parent) {

						if (convertView == null) {
							LayoutInflater infalInflater = (LayoutInflater) context
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							convertView = infalInflater.inflate(
									R.layout.mathimata_list_item, null);
						}

						// View v = super.getView(groupPosition,convertView,
						// parent);
						((TextView) convertView.findViewById(R.id.row_title))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τίτλος"));

						if (((String) ((Map<String, Object>) getItem(groupPosition))
								.get("Τίτλος")).contains("Τηλεπικοινωνιακά")
								|| ((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τίτλος"))
										.contains("Ενσωματωμένα")
								|| ((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τίτλος"))
										.contains("Μεταγλωττιστές")) {
							convertView.setBackgroundColor(COLOR_CHANGES);
						}
						((TextView) convertView.findViewById(R.id.row_omada))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Ομάδα"));

						((TextView) convertView.findViewById(R.id.row_typos))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τύπος"));

						((TextView) convertView.findViewById(R.id.row_dm))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("ΔΜ"));
						((TextView) convertView.findViewById(R.id.row_dm))
								.setBackgroundColor(COLOR_CHANGES);

						((TextView) convertView.findViewById(R.id.row_theoreia))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Θεωρεία"));

						((TextView) convertView
								.findViewById(R.id.row_frontistirio))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Φροντιστήριο"));

						((TextView) convertView
								.findViewById(R.id.row_ergastirio))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Εργαστήριο"));

						((ImageView) convertView
								.findViewById(R.id.mathima_icon))
								.setImageDrawable((Drawable) ((HashMap<String, Object>) getItem(groupPosition))
										.get("Εικόνα"));
						return convertView;
					}

				};
				v.setAdapter(examino_ST_adapter);

				break;

			case 6:

				SimpleAdapter examino_Z_adapter = new SimpleAdapter(context,
						examino_Z_group_list(), R.layout.mathimata_list_item,
						new String[] { "Τίτλος", "Ομάδα", "Τύπος", "ΔΜ",
								"Θεωρεία", "Φροντιστήριο", "Εργαστήριο" },
						new int[] { R.id.row_title, R.id.row_omada,
								R.id.row_typos, R.id.row_dm, R.id.row_theoreia,
								R.id.row_frontistirio, R.id.row_ergastirio })

				{

					@SuppressWarnings("unchecked")
					@Override
					public View getView(int groupPosition, View convertView,
							ViewGroup parent) {

						if (convertView == null) {
							LayoutInflater infalInflater = (LayoutInflater) context
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							convertView = infalInflater.inflate(
									R.layout.mathimata_list_item, null);
						}

						// View v = super.getView(groupPosition,convertView,
						// parent);
						((TextView) convertView.findViewById(R.id.row_title))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τίτλος"));

						((TextView) convertView.findViewById(R.id.row_omada))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Ομάδα"));

						((TextView) convertView.findViewById(R.id.row_typos))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τύπος"));

						((TextView) convertView.findViewById(R.id.row_dm))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("ΔΜ"));

						((TextView) convertView.findViewById(R.id.row_theoreia))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Θεωρεία"));

						((TextView) convertView
								.findViewById(R.id.row_frontistirio))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Φροντιστήριο"));

						((TextView) convertView
								.findViewById(R.id.row_ergastirio))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Εργαστήριο"));

						((ImageView) convertView
								.findViewById(R.id.mathima_icon))
								.setImageDrawable((Drawable) ((HashMap<String, Object>) getItem(groupPosition))
										.get("Εικόνα"));
						return convertView;
					}

				};
				v.setAdapter(examino_Z_adapter);

				break;

			case 7:

				SimpleAdapter examino_H_adapter = new SimpleAdapter(context,
						examino_H_group_list(), R.layout.mathimata_list_item,
						new String[] { "Τίτλος", "Ομάδα", "Τύπος", "ΔΜ",
								"Θεωρεία", "Φροντιστήριο", "Εργαστήριο" },
						new int[] { R.id.row_title, R.id.row_omada,
								R.id.row_typos, R.id.row_dm, R.id.row_theoreia,
								R.id.row_frontistirio, R.id.row_ergastirio })

				{

					@SuppressWarnings("unchecked")
					@Override
					public View getView(int groupPosition, View convertView,
							ViewGroup parent) {

						if (convertView == null) {
							LayoutInflater infalInflater = (LayoutInflater) context
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							convertView = infalInflater.inflate(
									R.layout.mathimata_list_item, null);
						}

						// View v = super.getView(groupPosition,convertView,
						// parent);
						((TextView) convertView.findViewById(R.id.row_title))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τίτλος"));
						((TextView) convertView.findViewById(R.id.row_omada))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Ομάδα"));

						((TextView) convertView.findViewById(R.id.row_typos))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Τύπος"));

						((TextView) convertView.findViewById(R.id.row_dm))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("ΔΜ"));

						((TextView) convertView.findViewById(R.id.row_theoreia))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Θεωρεία"));

						((TextView) convertView
								.findViewById(R.id.row_frontistirio))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Φροντιστήριο"));

						((TextView) convertView
								.findViewById(R.id.row_ergastirio))
								.setText((String) ((Map<String, Object>) getItem(groupPosition))
										.get("Εργαστήριο"));

						((ImageView) convertView
								.findViewById(R.id.mathima_icon))
								.setImageDrawable((Drawable) ((HashMap<String, Object>) getItem(groupPosition))
										.get("Εικόνα"));
						return convertView;
					}

				};
				v.setAdapter(examino_H_adapter);

				break;

			default:
				break;
			}
			v.isTextFilterEnabled();
			((ViewPager) pager).addView(v, 0);
			return v;
		}

		private List<HashMap<String, Object>> examino_A_group_list() {
			ArrayList<HashMap<String, Object>> examino_A = new ArrayList<HashMap<String, Object>>();
			String[] examino_A_title = getResources().getStringArray(
					R.array.Examino_A_title);
			String[] examino_A_omada = getResources().getStringArray(
					R.array.Examino_A_omada);
			String[] examino_A_typos = getResources().getStringArray(
					R.array.Examino_A_typos);
			String[] examino_A_dm = getResources().getStringArray(
					R.array.Examino_A_dm);
			String[] examino_A_theoreia = getResources().getStringArray(
					R.array.Examino_A_theoreia);
			String[] examino_A_frontistirio = getResources().getStringArray(
					R.array.Examino_A_frontistirio);
			String[] examino_A_ergastirio = getResources().getStringArray(
					R.array.Examino_A_ergastirio);

			for (int i = 0; i < examino_A_title.length; i++) {
				HashMap<String, Object> m = new HashMap<String, Object>();

				m.put("Τίτλος", examino_A_title[i]);
				m.put("Ομάδα", "Ομάδα: " + examino_A_omada[i]);
				m.put("Τύπος", " // Τύπος: " + examino_A_typos[i]);
				m.put("ΔΜ", " // ΔΜ:" + examino_A_dm[i]);
				m.put("Θεωρεία", "Θεωρία: " + examino_A_theoreia[i]);
				m.put("Φροντιστήριο", " // Φροντιστήριο: "
						+ examino_A_frontistirio[i]);
				m.put("Εργαστήριο", " // Εργαστήριο: "
						+ examino_A_ergastirio[i]);
				if (examino_A_typos[i].equals("Υποχρεωτικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.red_book));
				} else if (examino_A_typos[i].equals("Επιλογής Υποχρεωτικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.blue_book));
				} else if (examino_A_typos[i].equals("Προαιρετικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.green_book));
				}
				examino_A.add(m);
			}
			return (List<HashMap<String, Object>>) examino_A;
		}

		private List<HashMap<String, Object>> examino_B_group_list() {

			ArrayList<HashMap<String, Object>> examino_B = new ArrayList<HashMap<String, Object>>();
			String[] examino_B_title = getResources().getStringArray(
					R.array.Examino_B_title);
			String[] examino_B_omada = getResources().getStringArray(
					R.array.Examino_B_omada);
			String[] examino_B_typos = getResources().getStringArray(
					R.array.Examino_B_typos);
			String[] examino_B_dm = getResources().getStringArray(
					R.array.Examino_B_dm);
			String[] examino_B_theoreia = getResources().getStringArray(
					R.array.Examino_B_theoreia);
			String[] examino_B_frontistirio = getResources().getStringArray(
					R.array.Examino_B_frontistirio);
			String[] examino_B_ergastirio = getResources().getStringArray(
					R.array.Examino_B_ergastirio);

			for (int i = 0; i < examino_B_title.length; i++) {
				HashMap<String, Object> m = new HashMap<String, Object>();

				m.put("Τίτλος", examino_B_title[i]); /*
													 * the key and it's value.
													 */
				m.put("Ομάδα", "Ομάδα: " + examino_B_omada[i]);
				m.put("Τύπος", " // Τύπος: " + examino_B_typos[i]);
				m.put("ΔΜ", " // ΔΜ:" + examino_B_dm[i]);
				m.put("Θεωρεία", "Θεωρία: " + examino_B_theoreia[i]);
				m.put("Φροντιστήριο", " // Φροντιστήριο: "
						+ examino_B_frontistirio[i]);
				m.put("Εργαστήριο", " // Εργαστήριο: "
						+ examino_B_ergastirio[i]);
				if (examino_B_typos[i].equals("Υποχρεωτικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.red_book));
				} else if (examino_B_typos[i].equals("Επιλογής Υποχρεωτικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.blue_book));
				} else if (examino_B_typos[i].equals("Προαιρετικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.green_book));
				}
				examino_B.add(m);
			}
			return (List<HashMap<String, Object>>) examino_B;
		}

		private List<HashMap<String, Object>> examino_C_group_list() {
			ArrayList<HashMap<String, Object>> examino_C = new ArrayList<HashMap<String, Object>>();
			String[] examino_C_title = getResources().getStringArray(
					R.array.Examino_C_title);
			String[] examino_C_omada = getResources().getStringArray(
					R.array.Examino_C_omada);
			String[] examino_C_typos = getResources().getStringArray(
					R.array.Examino_C_typos);
			String[] examino_C_dm = getResources().getStringArray(
					R.array.Examino_C_dm);
			String[] examino_C_theoreia = getResources().getStringArray(
					R.array.Examino_C_theoreia);
			String[] examino_C_frontistirio = getResources().getStringArray(
					R.array.Examino_C_frontistirio);
			String[] examino_C_ergastirio = getResources().getStringArray(
					R.array.Examino_C_ergastirio);

			for (int i = 0; i < examino_C_title.length; i++) {
				HashMap<String, Object> m = new HashMap<String, Object>();

				m.put("Τίτλος", examino_C_title[i]); /*
													 * the key and it's value.
													 */
				m.put("Ομάδα", "Ομάδα: " + examino_C_omada[i]);
				m.put("Τύπος", " // Τύπος: " + examino_C_typos[i]);
				m.put("ΔΜ", " // ΔΜ:" + examino_C_dm[i]);
				m.put("Θεωρεία", "Θεωρία: " + examino_C_theoreia[i]);
				m.put("Φροντιστήριο", " // Φροντιστήριο: "
						+ examino_C_frontistirio[i]);
				m.put("Εργαστήριο", " // Εργαστήριο: "
						+ examino_C_ergastirio[i]);
				if (examino_C_typos[i].equals("Υποχρεωτικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.red_book));
				} else if (examino_C_typos[i].equals("Επιλογής Υποχρεωτικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.blue_book));
				} else if (examino_C_typos[i].equals("Προαιρετικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.green_book));
				}
				examino_C.add(m);
			}
			return (List<HashMap<String, Object>>) examino_C;
		}

		private List<HashMap<String, Object>> examino_D_group_list() {
			ArrayList<HashMap<String, Object>> examino_D = new ArrayList<HashMap<String, Object>>();
			String[] examino_D_title = getResources().getStringArray(
					R.array.Examino_D_title);
			String[] examino_D_omada = getResources().getStringArray(
					R.array.Examino_D_omada);
			String[] examino_D_typos = getResources().getStringArray(
					R.array.Examino_D_typos);
			String[] examino_D_dm = getResources().getStringArray(
					R.array.Examino_D_dm);
			String[] examino_D_theoreia = getResources().getStringArray(
					R.array.Examino_D_theoreia);
			String[] examino_D_frontistirio = getResources().getStringArray(
					R.array.Examino_D_frontistirio);
			String[] examino_D_ergastirio = getResources().getStringArray(
					R.array.Examino_D_ergastirio);

			for (int i = 0; i < examino_D_title.length; i++) {
				HashMap<String, Object> m = new HashMap<String, Object>();

				m.put("Τίτλος", examino_D_title[i]); /*
													 * the key and it's value.
													 */
				m.put("Ομάδα", "Ομάδα: " + examino_D_omada[i]);
				m.put("Τύπος", " // Τύπος: " + examino_D_typos[i]);
				m.put("ΔΜ", " // ΔΜ:" + examino_D_dm[i]);
				m.put("Θεωρεία", "Θεωρία: " + examino_D_theoreia[i]);
				m.put("Φροντιστήριο", " // Φροντιστήριο: "
						+ examino_D_frontistirio[i]);
				m.put("Εργαστήριο", " // Εργαστήριο: "
						+ examino_D_ergastirio[i]);
				if (examino_D_typos[i].equals("Υποχρεωτικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.red_book));
				} else if (examino_D_typos[i].equals("Επιλογής Υποχρεωτικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.blue_book));
				} else if (examino_D_typos[i].equals("Προαιρετικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.green_book));
				}
				examino_D.add(m);
			}
			return (List<HashMap<String, Object>>) examino_D;
		}

		private List<HashMap<String, Object>> examino_E_group_list() {
			ArrayList<HashMap<String, Object>> examino_E = new ArrayList<HashMap<String, Object>>();
			String[] examino_E_title = getResources().getStringArray(
					R.array.Examino_E_title);
			String[] examino_E_omada = getResources().getStringArray(
					R.array.Examino_E_omada);
			String[] examino_E_typos = getResources().getStringArray(
					R.array.Examino_E_typos);
			String[] examino_E_dm = getResources().getStringArray(
					R.array.Examino_E_dm);
			String[] examino_E_theoreia = getResources().getStringArray(
					R.array.Examino_E_theoreia);
			String[] examino_E_frontistirio = getResources().getStringArray(
					R.array.Examino_E_frontistirio);
			String[] examino_E_ergastirio = getResources().getStringArray(
					R.array.Examino_E_ergastirio);

			for (int i = 0; i < examino_E_title.length; i++) {
				HashMap<String, Object> m = new HashMap<String, Object>();

				m.put("Τίτλος", examino_E_title[i]); /*
													 * the key and it's value.
													 */
				m.put("Ομάδα", "Ομάδα: " + examino_E_omada[i]);
				m.put("Τύπος", " // Τύπος: " + examino_E_typos[i]);
				m.put("ΔΜ", " // ΔΜ:" + examino_E_dm[i]);
				m.put("Θεωρεία", "Θεωρία: " + examino_E_theoreia[i]);
				m.put("Φροντιστήριο", " // Φροντιστήριο: "
						+ examino_E_frontistirio[i]);
				m.put("Εργαστήριο", " // Εργαστήριο: "
						+ examino_E_ergastirio[i]);
				if (examino_E_typos[i].equals("Υποχρεωτικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.red_book));
				} else if (examino_E_typos[i].equals("Επιλογής Υποχρεωτικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.blue_book));
				} else if (examino_E_typos[i].equals("Προαιρετικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.green_book));
				}
				examino_E.add(m);
			}
			return (List<HashMap<String, Object>>) examino_E;
		}

		private List<HashMap<String, Object>> examino_ST_group_list() {
			ArrayList<HashMap<String, Object>> examino_ST = new ArrayList<HashMap<String, Object>>();
			String[] examino_ST_title = getResources().getStringArray(
					R.array.Examino_ST_title);
			String[] examino_ST_omada = getResources().getStringArray(
					R.array.Examino_ST_omada);
			String[] examino_ST_typos = getResources().getStringArray(
					R.array.Examino_ST_typos);
			String[] examino_ST_dm = getResources().getStringArray(
					R.array.Examino_ST_dm);
			String[] examino_ST_theoreia = getResources().getStringArray(
					R.array.Examino_ST_theoreia);
			String[] examino_ST_frontistirio = getResources().getStringArray(
					R.array.Examino_ST_frontistirio);
			String[] examino_ST_ergastirio = getResources().getStringArray(
					R.array.Examino_ST_ergastirio);

			for (int i = 0; i < examino_ST_title.length; i++) {
				HashMap<String, Object> m = new HashMap<String, Object>();

				m.put("Τίτλος", examino_ST_title[i]); /*
													 * the key and it's value.
													 */
				m.put("Ομάδα", "Ομάδα: " + examino_ST_omada[i]);
				m.put("Τύπος", " // Τύπος: " + examino_ST_typos[i]);
				m.put("ΔΜ", " // ΔΜ:" + examino_ST_dm[i]);
				m.put("Θεωρεία", "Θεωρία: " + examino_ST_theoreia[i]);
				m.put("Φροντιστήριο", " // Φροντιστήριο: "
						+ examino_ST_frontistirio[i]);
				m.put("Εργαστήριο", " // Εργαστήριο: "
						+ examino_ST_ergastirio[i]);
				if (examino_ST_typos[i].equals("Υποχρεωτικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.red_book));
				} else if (examino_ST_typos[i].equals("Επιλογής Υποχρ.")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.blue_book));
				} else if (examino_ST_typos[i].equals("Προαιρετικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.green_book));
				}
				examino_ST.add(m);
			}
			return (List<HashMap<String, Object>>) examino_ST;
		}

		private List<HashMap<String, Object>> examino_Z_group_list() {
			ArrayList<HashMap<String, Object>> examino_Z = new ArrayList<HashMap<String, Object>>();
			String[] examino_Z_title = getResources().getStringArray(
					R.array.Examino_Z_title);
			String[] examino_Z_omada = getResources().getStringArray(
					R.array.Examino_Z_omada);
			String[] examino_Z_typos = getResources().getStringArray(
					R.array.Examino_Z_typos);
			String[] examino_Z_dm = getResources().getStringArray(
					R.array.Examino_Z_dm);
			String[] examino_Z_theoreia = getResources().getStringArray(
					R.array.Examino_Z_theoreia);
			String[] examino_Z_frontistirio = getResources().getStringArray(
					R.array.Examino_Z_frontistirio);
			String[] examino_Z_ergastirio = getResources().getStringArray(
					R.array.Examino_Z_ergastirio);

			for (int i = 0; i < examino_Z_title.length; i++) {
				HashMap<String, Object> m = new HashMap<String, Object>();

				m.put("Τίτλος", examino_Z_title[i]); /*
													 * the key and it's value.
													 */
				m.put("Ομάδα", "Ομάδα: " + examino_Z_omada[i]);
				m.put("Τύπος", " // Τύπος: " + examino_Z_typos[i]);
				m.put("ΔΜ", " // ΔΜ:" + examino_Z_dm[i]);
				m.put("Θεωρεία", "Θεωρία: " + examino_Z_theoreia[i]);
				m.put("Φροντιστήριο", " // Φροντιστήριο: "
						+ examino_Z_frontistirio[i]);
				m.put("Εργαστήριο", " // Εργαστήριο: "
						+ examino_Z_ergastirio[i]);
				if (examino_Z_typos[i].equals("Υποχρεωτικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.red_book));
				} else if (examino_Z_typos[i].equals("Επιλογής Υποχρ.")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.blue_book));
				} else if (examino_Z_typos[i].equals("Προαιρετικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.green_book));
				}
				examino_Z.add(m);
			}
			return (List<HashMap<String, Object>>) examino_Z;
		}

		private List<HashMap<String, Object>> examino_H_group_list() {
			ArrayList<HashMap<String, Object>> examino_H = new ArrayList<HashMap<String, Object>>();
			String[] examino_H_title = getResources().getStringArray(
					R.array.Examino_H_title);
			String[] examino_H_omada = getResources().getStringArray(
					R.array.Examino_H_omada);
			String[] examino_H_typos = getResources().getStringArray(
					R.array.Examino_H_typos);
			String[] examino_H_dm = getResources().getStringArray(
					R.array.Examino_H_dm);
			String[] examino_H_theoreia = getResources().getStringArray(
					R.array.Examino_H_theoreia);
			String[] examino_H_frontistirio = getResources().getStringArray(
					R.array.Examino_H_frontistirio);
			String[] examino_H_ergastirio = getResources().getStringArray(
					R.array.Examino_H_ergastirio);

			for (int i = 0; i < examino_H_title.length; i++) {
				HashMap<String, Object> m = new HashMap<String, Object>();

				m.put("Τίτλος", examino_H_title[i]); /*
													 * the key and it's value.
													 */
				m.put("Ομάδα", "Ομάδα: " + examino_H_omada[i]);
				m.put("Τύπος", " // Τύπος: " + examino_H_typos[i]);
				m.put("ΔΜ", " // ΔΜ:" + examino_H_dm[i]);
				m.put("Θεωρεία", "Θεωρία: " + examino_H_theoreia[i]);
				m.put("Φροντιστήριο", " // Φροντιστήριο: "
						+ examino_H_frontistirio[i]);
				m.put("Εργαστήριο", " // Εργαστήριο: "
						+ examino_H_ergastirio[i]);
				if (examino_H_typos[i].equals("Υποχρεωτικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.red_book));
				} else if (examino_H_typos[i].equals("Επιλογής Υποχρεωτικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.blue_book));
				} else if (examino_H_typos[i].equals("Προαιρετικό")) {
					m.put("Εικόνα",
							getResources().getDrawable(R.drawable.green_book));
				}
				examino_H.add(m);
			}
			return (List<HashMap<String, Object>>) examino_H;
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

	class DownloadFileAsync extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected String doInBackground(String... aurl) {
			int count;

			try {

				URL url = new URL(aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();

				downloadsDirectory = new File(downloadsDirectoryPath);

				if (!downloadsDirectory.exists()) {
					downloadsDirectory.mkdir();
				}

				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(
						downloadsDirectoryPath + filename);

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
			}
			return null;
		}

		protected void onProgressUpdate(String... progress) {
			// Log.d("ANDRO_ASYNC", progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String unused) {
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			createNotification();
			LIST_UPDATE_NOTIFICATION++;
		}
	}

	public void createNotification() {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		int icon = R.drawable.notification_icon;
		CharSequence tickerText = "Λήψη αρχείου ολοκληρώθηκε";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		CharSequence contentTitle = filename;
		CharSequence contentText = "Κάντε κλικ για άνοιγμα του αρχείου.";

		Intent notificationIntent = new Intent();
		notificationIntent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(downloadsDirectoryPath + filename);
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		String ext = file.getName().substring(file.getName().indexOf(".") + 1);
		String type = mime.getMimeTypeFromExtension(ext);
		notificationIntent.setDataAndType(Uri.fromFile(file), type);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(this, contentTitle, contentText,
				contentIntent);

		mNotificationManager.notify(LIST_UPDATE_NOTIFICATION, notification);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage("Λήψη αρχείου...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getSupportMenuInflater().inflate(R.menu.menu_programma_spoudon, menu);

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.alusides:

			Intent alusides = new Intent("com.cst.connect.Alusides");
			startActivity(alusides);

			break;

		case R.id.download_programma:

			LayoutInflater inflater_programma = (LayoutInflater) context
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout_programma = inflater_programma.inflate(
					R.layout.custom_dialog,
					(ViewGroup) findViewById(android.R.id.list), false);

			builder = new AlertDialog.Builder(context);
			builder.setView(layout_programma);

			builder.setTitle("Πρόγραμμα Σπουδών");
			filename = "OS_2010.pdf";
			download = new File(downloadsDirectoryPath + filename);
			if (download.exists()) {
				FILE_EXISTS = true;
			} else {
				FILE_EXISTS = false;
			}

			if (!FILE_EXISTS) {

				builder.setMessage("Πρόκειται να γίνει λήψη του αρχείου "
						+ filename + " (4 MB). Συνέχεια;");
				builder.setPositiveButton("Λήψη Αρχείου",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (isOnline()) {
									startDownload();

								} else {

									Toast.makeText(Mathimata.this,
											R.string.DataFailureWarning,
											Toast.LENGTH_LONG).show();
								}

							}

							private void startDownload() {
								new DownloadFileAsync()
										.execute("http://www.cs.teilar.gr/CS/Data/OS_2010.pdf");
							}
						});
				FILE_EXISTS = true;

			} else if (FILE_EXISTS) {
				builder.setMessage("Έχετε ήδη κατεβάσει το πρόγραμμα σπουδών.Κάντε κλικ στο κουμπί για άνοιγμα του αρχείου.");
				builder.setPositiveButton("’νοιγμα Αρχείου",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								openFile();

							}

						});
			}

			builder.setNeutralButton("Κλείσιμο",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							alertDialog.dismiss();
						}
					});

			alertDialog = builder.create();
			alertDialog.show();
			break;

		}
		return true;
	}

	public void openFile() {
		Intent notificationIntent = new Intent();
		notificationIntent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(downloadsDirectoryPath + filename);
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		String ext = file.getName().substring(file.getName().indexOf(".") + 1);
		String type = mime.getMimeTypeFromExtension(ext);
		notificationIntent.setDataAndType(Uri.fromFile(file), type);
		startActivity(notificationIntent);

	}

	private Boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnected())
			return true;

		return false;
	}

}
