package com.cst.connect.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.cst.connect.R;
import com.cst.connect.helper.RssHelper;

public class Links extends SherlockActivity {
	ListView list;
	Context context = this;
	RssHelper helper = new RssHelper(context);

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

				
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Links");

		SimpleAdapter links_adapter = new SimpleAdapter(context, links_list(),
				R.layout.links_list_item, new String[] { "Όνομα", "URL" },
				new int[] { R.id.link_name, R.id.link_url })

		{

			@SuppressWarnings("unchecked")
			@Override
			public View getView(int groupPosition, View convertView,
					ViewGroup parent) {

				if (convertView == null) {
					LayoutInflater infalInflater = (LayoutInflater) context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = infalInflater.inflate(
							R.layout.links_list_item, null);
				}

				((TextView) convertView.findViewById(R.id.link_name))
						.setText((String) ((Map<String, Object>) getItem(groupPosition))
								.get("Όνομα"));

				((TextView) convertView.findViewById(R.id.link_url))
						.setText((String) ((Map<String, Object>) getItem(groupPosition))
								.get("URL"));

				((ImageView) convertView.findViewById(R.id.link_icon))
						.setImageDrawable((Drawable) ((HashMap<String, Object>) getItem(groupPosition))
								.get("Εικόνα"));
				return convertView;
			}

		};

		ListView lv = (ListView) findViewById(android.R.id.list);
		lv.setAdapter(links_adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("rawtypes")
			@Override
			public void onItemClick(AdapterView parentView, View childView,
					int position, long id) {
				if (helper.isOnline()) {
					String[] urls = getResources().getStringArray(
							R.array.links_urls);
					String url = urls[position];
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					startActivity(intent);
				} else {
					Toast.makeText(Links.this, R.string.DataFailureWarning,
							Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	private List<HashMap<String, Object>> links_list() {
		ArrayList<HashMap<String, Object>> links = new ArrayList<HashMap<String, Object>>();

		String[] links_names = getResources().getStringArray(
				R.array.links_names);
		String[] links_urls = getResources().getStringArray(R.array.links_urls);
		String[] links_images = getResources().getStringArray(
				R.array.links_images);

		for (int i = 0; i < links_names.length; ++i) {
			HashMap<String, Object> m = new HashMap<String, Object>();

			m.put("Όνομα", links_names[i]);
			m.put("URL", links_urls[i]);
			m.put("Εικόνα",
					getResources().getDrawable(
							getResources().getIdentifier(links_images[i], null,
									null)));

			links.add(m);
		}
		return (List<HashMap<String, Object>>) links;
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
