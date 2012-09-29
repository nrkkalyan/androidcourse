package com.lnu.mycountries.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lnu.db.BaseEntityDAO;

public class CountryDAO extends BaseEntityDAO<Country> {
	
	private static final String			COUNTRIES_TABLE_NAME	= "countries";
	
	private static final String			COLUMN_YEAR_OF_VISIT	= "year";
	private static final String			COLUMN_COUNTRY_NAME		= "name";
	
	private static final String			DATABASE_NAME			= "mycountries.db";
	private static final int			DATABASE_VERSION		= 1;
	
	private static final String			DATABASE_CREATE			= "create table " + COUNTRIES_TABLE_NAME + " ("
																		+ COLUMN_ID
																		+ " integer primary key autoincrement, "
																		+ COLUMN_YEAR_OF_VISIT + " integer not null, "
																		+ COLUMN_COUNTRY_NAME + " text not null);";
	
	private static final List<Country>	defaultCountriesList	= new ArrayList<Country>();
	{
		defaultCountriesList.add(new Country(2008, "France"));
		defaultCountriesList.add(new Country(2006, "UK"));
		defaultCountriesList.add(new Country(2009, "USA"));
	}
	
	public CountryDAO(Context context) {
		super(context, DATABASE_NAME, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		
		for (Country country : defaultCountriesList) {
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_COUNTRY_NAME, country.getName());
			cv.put(COLUMN_YEAR_OF_VISIT, country.getYear());
			db.insert(COUNTRIES_TABLE_NAME, null, cv);
		}
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
	
}