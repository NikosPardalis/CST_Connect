package com.cst.connect.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FeedsDbAdapter {

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private final Context mCtx;

	private static final String DEBUG_TAG = "RSSDatabase";
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "rss_data";
	
	public static String TABLE = "anakoinoseis";
	public static final String ID = "_id";
	public static final String TITLE = "_title";
	public static final String PUBDATE = "_pubdate";
	public static final String DESCRIPTION = "_description";
	public static final String LINK = "_link";


//	private static final String CREATE_TABLE = ;

//	private static final String DB_SCHEMA = CREATE_TABLE;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
										
			db.execSQL("CREATE TABLE " + TABLE + " ("
					+ ID + " integer PRIMARY KEY AUTOINCREMENT, " + TITLE
					+ " text NOT NULL, " + PUBDATE + " text NOT NULL, " + DESCRIPTION
					+ " text NOT NULL, " + LINK + " text NOT NULL);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(DEBUG_TAG,
					"Upgrading database. Existing contents will be lost. ["
							+ oldVersion + "]->[" + newVersion + "]");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE);
			onCreate(db);
		}
	}

	public FeedsDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public FeedsDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {		
		mDbHelper.close();
	}

	public long createAnakoinosi(String title, String pubdate,
			String description, String link) {
		
		ContentValues values = new ContentValues();
		values.put(TITLE, title);
		values.put(PUBDATE, pubdate);
		values.put(DESCRIPTION, description);
		values.put(LINK, link);
		
//	 // Check if row already existed in database
//    if (!isAnakoinosiExists(mDb, site.getRSSLink())) {
//        // site not existed, create a new row
//    	mDb.insert(TABLE, null, values);
//        mDb.close();
//    } else {
//        // site already existed update the row
//        updateSite(site);
//        mDb.close();
//    }	

		return mDb.insert(TABLE, null, values);
	}

	public Cursor fetchRss() {

		return mDb.query(TABLE, new String[] { ID, TITLE, PUBDATE, DESCRIPTION,	LINK }, null, null, null, null, null);
	}
	
	public Cursor fetchRssNews() {

		return mDb.query(TABLE, new String[] { ID, TITLE, PUBDATE, DESCRIPTION,	LINK }, null, null, null, null, null);
	}
	
	public Cursor fetchRssTeachersAll() {

		return mDb.query(TABLE, new String[] { ID, TITLE, PUBDATE, DESCRIPTION,	LINK }, null, null, null, null, null);
	}
	
	public Cursor fetchRssTeacher() {

		return mDb.query(TABLE, new String[] { ID, TITLE, PUBDATE, DESCRIPTION,	LINK }, null, null, null, null, null);
	}
	
	public Cursor fetchRssCalendar() {

		return mDb.query(TABLE, new String[] { ID, TITLE, PUBDATE, DESCRIPTION,	LINK }, null, null, null, null, null);
	}

}
