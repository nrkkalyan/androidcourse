package com.lnu.mycountries.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lnu.db.BaseEntityDataSource;

public class CountryDataSource extends BaseEntityDataSource<Country> {
	
	private static final String	COUNTRIES_TABLE_NAME	= "countries";
	
	private static final String	COLUMN_YEAR_OF_VISIT	= "year";
	private static final String	COLUMN_COUNTRY_NAME		= "name";
	
	private static final String	DATABASE_NAME			= "countries.db";
	private static final int	DATABASE_VERSION		= 1;
	
	private static final String	DATABASE_CREATE			= "create table " + COUNTRIES_TABLE_NAME + " (" + COLUMN_ID
																+ " integer primary key autoincrement, "
																+ COLUMN_YEAR_OF_VISIT + " integer not null, "
																+ COLUMN_COUNTRY_NAME + " text not null);";
	
	public CountryDataSource(Context context) {
		super(context, DATABASE_NAME, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + COUNTRIES_TABLE_NAME);
		onCreate(db);
	}
	
	@Override
	protected String[] getAllColumns() {
		return new String[] { COLUMN_YEAR_OF_VISIT, COLUMN_COUNTRY_NAME };
	}
	
	@Override
	protected String getTableName() {
		return COUNTRIES_TABLE_NAME;
	}
	
	@Override
	protected Country getManagedEntity() {
		return new Country();
	}
	
	@Override
	protected void unMarshal(Country entity, Cursor cursor) {
		entity.setYear(cursor.getInt(1));
		entity.setName(cursor.getString(2));
	}
	
	@Override
	protected void marshal(ContentValues values, Country entity) {
		values.put(COLUMN_YEAR_OF_VISIT, entity.getYear());
		values.put(COLUMN_COUNTRY_NAME, entity.getName());
	}
	
	// // Database fields
	// private SQLiteDatabase database;
	// private AddCountryDbHelper dbHelper;
	// private String[] allColumns = { COLUMN_ID, //
	// COLUMN_YEAR_OF_VISIT, //
	// COLUMN_COUNTRY_NAME };
	//
	// public CountryDataSource(Context context) {
	// dbHelper = new AddCountryDbHelper(context);
	// }
	//
	// public void open() throws SQLException {
	// database = dbHelper.getWritableDatabase();
	// }
	//
	// public void close() {
	// dbHelper.close();
	// }
	//
	// public Country create(String task) {
	// ContentValues values = new ContentValues();
	// values.put(COLUMN_COUNTRY_NAME, task);
	// long insertId = database.insert(COUNTRIES_TABLE_NAME,
	// null, values);
	// Cursor cursor = database.query(COUNTRIES_TABLE_NAME,
	// allColumns,
	// COLUMN_ID + " = " + insertId, null, null, null, null);
	// cursor.moveToFirst();
	// Country newTask = cursorToTask(cursor);
	// cursor.close();
	// return newTask;
	// }
	//
	// @Override
	// public void delete(Country task) {
	// long id = task.getId();
	// System.out.println("Task deleted with id: " + id);
	// database.delete(COUNTRIES_TABLE_NAME,
	// COLUMN_ID + " = " + id, null);
	// }
	//
	// @Override
	// public Country find(long taskId) {
	// String restrict = COLUMN_ID + "=" + taskId;
	// Cursor cursor = database.query(true,
	// COUNTRIES_TABLE_NAME, allColumns, restrict, null,
	// null,
	// null, null, null);
	// if (cursor != null && cursor.getCount() > 0) {
	// cursor.moveToFirst();
	// Country task = cursorToTask(cursor);
	// return task;
	// }
	// // Make sure to close the cursor
	// cursor.close();
	// return null;
	// }
	//
	// public boolean update(long taskId, String task) {
	// ContentValues args = new ContentValues();
	// args.put(COLUMN_COUNTRY_NAME, task);
	//
	// String restrict = COLUMN_ID + "=" + taskId;
	// return database.update(COUNTRIES_TABLE_NAME, args,
	// restrict, null) > 0;
	// }
	//
	// public List<Country> getAllTasks() {
	// List<Country> tasks = new ArrayList<Country>();
	//
	// Cursor cursor = database.query(COUNTRIES_TABLE_NAME,
	// allColumns, null, null, null, null,
	// null);
	//
	// cursor.moveToFirst();
	// while (!cursor.isAfterLast()) {
	// Country task = cursorToTask(cursor);
	// tasks.add(task);
	// cursor.moveToNext();
	// }
	// // Make sure to close the cursor
	// cursor.close();
	// return tasks;
	// }
	//
	// private Country cursorToCountry(Cursor cursor) {
	// Country task = new Country();
	// task.setId(cursor.getLong(0));
	// task.setTask(cursor.getString(1));
	// return task;
	// }
}