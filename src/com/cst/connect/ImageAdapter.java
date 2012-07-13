package com.cst.connect;

import android.content.Context;
import android.view.*;
import android.widget.*;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private Integer[] mThumbIds = { R.drawable.rss_icon, R.drawable.map_icon,
			R.drawable.book_icon, R.drawable.info_icon, R.drawable.link_icon,
			R.drawable.emails_icon };
	private String[] mThumbIds_Strings = { "Ανακοινώσεις", "Χάρτης",
			"Μαθήματα", "Πληροφορίες", "Χρήσιμα Links", "Mobile Sites" };

	public ImageAdapter(Context c) {
		this.mContext = c;
	}

	public int getCount() {
		return mThumbIds.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View gridView;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			gridView = new View(mContext);
			gridView = inflater.inflate(R.layout.gridview_item, null);
		} else {
			gridView = convertView;
		}

		TextView textView = (TextView) gridView
				.findViewById(R.id.grid_item_text);
		textView.setText(mThumbIds_Strings[position]);

		ImageView imageView = (ImageView) gridView
				.findViewById(R.id.grid_item_image);
		imageView.setImageResource(mThumbIds[position]);

		// imageView = new ImageView(mContext);
		// imageView.setLayoutParams(new GridView.LayoutParams(100, 100));

		return gridView;
	}

}
