package com.lnu.mycountries.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class CountryDao {
	
	// Database fields
	private SQLiteDatabase		database;
	private AddCountryDbHelper	dbHelper;
	private String[]			allColumns	= { AddCountryDbHelper.COLUMN_ID, //
			AddCountryDbHelper.COLUMN_YEAR_OF_VISIT, //
			AddCountryDbHelper.COLUMN_COUNTRY_NAME };
	
	public CountryDao(Context context) {
		dbHelper = new AddCountryDbHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Country create(String task) {
		ContentValues values = new ContentValues();
		values.put(AddCountryDbHelper.COLUMN_COUNTRY_NAME, task);
		long insertId = database.insert(AddCountryDbHelper.COUNTRIES_TABLE_NAME, null, values);
		Cursor cursor = database.query(AddCountryDbHelper.COUNTRIES_TABLE_NAME, allColumns,
				AddCountryDbHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Country newTask = cursorToTask(cursor);
		cursor.close();
		return newTask;
	}
	
	public void delete(Country task) {
		long id = task.getId();
		System.out.println("Task deleted with id: " + id);
		database.delete(AddCountryDbHelper.COUNTRIES_TABLE_NAME, AddCountryDbHelper.COLUMN_ID + " = " + id, null);
	}
	
	public Country find(long taskId) {
		String restrict = AddCountryDbHelper.COLUMN_ID + "=" + taskId;
		Cursor cursor = database.query(true, AddCountryDbHelper.COUNTRIES_TABLE_NAME, allColumns, restrict, null, null,
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Country task = cursorToTask(cursor);
			return task;
		}
		// Make sure to close the cursor
		cursor.close();
		return null;
	}
	
	public boolean update(long taskId, String task) {
		ContentValues args = new ContentValues();
		args.put(AddCountryDbHelper.COLUMN_COUNTRY_NAME, task);
		
		String restrict = AddCountryDbHelper.COLUMN_ID + "=" + taskId;
		return database.update(AddCountryDbHelper.COUNTRIES_TABLE_NAME, args, restrict, null) > 0;
	}
	
	public List<Country> getAllTasks() {
		List<Country> tasks = new ArrayList<Country>();
		
		Cursor cursor = database.query(AddCountryDbHelper.COUNTRIES_TABLE_NAME, allColumns, null, null, null, null,
				null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Country task = cursorToTask(cursor);
			tasks.add(task);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return tasks;
	}
	
	private Country cursorToCountry(Cursor cursor) {
		Country task = new Country();
		task.setId(cursor.getLong(0));
		task.setTask(cursor.getString(1));
		return task;
	}
}