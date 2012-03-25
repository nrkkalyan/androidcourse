package com.nrk.ltu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_IMAGE_URL = "image_url";
	public static final String KEY_CONTACT_URL = "contact_url";

	private static final String DATABASE_NAME = "MyDB1";
	private static final String DATABASE_TABLE = "contacts";
	private static final int DATABASE_VERSION = 2;

	private static final String DATABASE_CREATE = "create table contacts (_id integer primary key autoincrement, "
			+ "image_url text not null, contact_url text not null, unique(image_url,contact_url ));";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS contacts");
			onCreate(db);
		}
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	// ---insert a contact into the database---
	public long insertContact(String imageUrl, String contactUrl) {

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_IMAGE_URL, imageUrl);
		initialValues.put(KEY_CONTACT_URL, contactUrl);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	// ---deletes a particular contact---
	public boolean deleteContact(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	// ---retrieves all the contacts---
	public Cursor getAllContacts() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_IMAGE_URL, KEY_CONTACT_URL }, null, null, null, null, null);
	}

	// ---retrieves a particular contact---
	public Cursor getContacts(String imageUrl) throws SQLException {
		Cursor mCursor = db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_IMAGE_URL, KEY_CONTACT_URL }, KEY_IMAGE_URL + "='" + imageUrl + "'",
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---updates a contact---
	public boolean updateContact(long rowId, String imageUrl, String contactUrl) {
		ContentValues args = new ContentValues();
		args.put(KEY_IMAGE_URL, imageUrl);
		args.put(KEY_CONTACT_URL, contactUrl);
		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
}
