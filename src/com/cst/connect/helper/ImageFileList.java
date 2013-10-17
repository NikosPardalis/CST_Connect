package com.cst.connect.helper;

import java.io.IOException;
import java.net.URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

import com.cst.connect.R;

public class ImageFileList extends AsyncTask<Void, Void, String[]> {

	Context mContext;
	String mUrl = mContext.getResources().getString(R.string.backend_site);
	String fileName = "programma_mathimaton_";
	String ext = ".png";
	
	public interface ListResponse {
		void returnedList(String[] list);
	}

	@Override
	protected String[] doInBackground(Void... params) {
		
		long size = 0;
		for (int i = 0; i <= 7; i++) {
			String imageUrl = mUrl + fileName+i+ext;
			Log.d("imageUrl", imageUrl);
			try {
				URL imageFileUrl = new URL(imageUrl);
				size = imageFileUrl.openConnection().getContentLength();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			StringBuilder sb = new StringBuilder();
			SharedPreferences pref = mContext.getSharedPreferences("LessonsList", 0); // 0 - for private mode

//			if (size!=0){
//				
//				sb.append(playlists[i]).append(",");			
//				
//			}
//			Editor editor = pref.edit();
//			editor.putString(LessonList, sb.toString());
		}
		return null;
	}

	@Override
	protected void onPostExecute(String[] result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}
