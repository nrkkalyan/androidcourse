package com.lnu.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class BaseEntityDAO<Entity extends BaseEntity> extends SQLiteOpenHelper {
	
	protected static final String	COLUMN_ID	= "_id";
	// Database fields
	private SQLiteDatabase			database;
	
	public BaseEntityDAO(Context context, String databaseName, int dbVersion) {
		super(context, databaseName, null, dbVersion);
	}
	
	public BaseEntityDAO(Context context, int dbVersion) {
		super(context, "defaultDabase.db", null, dbVersion);
	}
	
	private void open() throws SQLException {
		database = getWritableDatabase();
	}
	
	@Override
	public void close() {
		try {
			database.close();
			super.close();
		} catch (Exception ignore) {
			// Ignore
		}
	}
	
	private SQLiteDatabase getDatabase() {
		if (database == null || !database.isOpen()) {
			open();
		}
		return database;
	}
	
	protected abstract String[] getAllColumns();
	
	protected abstract String getTableName();
	
	protected abstract Entity getManagedEntity();
	
	protected abstract void unMarshal(Entity entity, Cursor cursor);
	
	protected abstract void marshal(ContentValues args, Entity entity);
	
	public Entity create(Entity entity) {
		ContentValues values = new ContentValues();
		marshal(values, entity);
		
		try {
			long insertId = getDatabase().insert(getTableName(), null, values);
			Cursor cursor = getDatabase().query(getTableName(), getColumns(), COLUMN_ID + " = " + insertId, null, null,
					null, null);
			try {
				cursor.moveToFirst();
				return cursorToEntity(cursor);
			} finally {
				// Make sure to close the cursor
				try {
					cursor.close();
				} catch (Exception ignore) {
					// Ignore
				}
			}
		} finally {
			// Make sure to close the database
			close();
		}
	}
	
	private String[] getColumns() {
		List<String> columns = new ArrayList<String>();
		columns.add(COLUMN_ID);
		for (String column : getAllColumns()) {
			columns.add(column);
		}
		return columns.toArray(new String[columns.size()]);
	}
	
	public void delete(Entity entity) {
		
		long id = entity.getId();
		try {
			getDatabase().delete(getTableName(), "_id=" + id, null);
		} finally {
			// Make sure to close the database
			close();
		}
	}
	
	public Entity find(long id) {
		String restrict = COLUMN_ID + "=" + id;
		try {
			Cursor cursor = getDatabase().query(true, getTableName(), getColumns(), restrict, null, null, null, null,
					null);
			try {
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToFirst();
					Entity task = cursorToEntity(cursor);
					return task;
				}
			} finally {
				// Make sure to close the cursor
				try {
					cursor.close();
				} catch (Exception ignore) {
					// Ignore
				}
			}
		} finally {
			// Make sure to close the database
			close();
		}
		return null;
	}
	
	public boolean update(Entity entity) {
		if (entity.getId() == null) {
			throw new IllegalStateException("Entity id must not be null.");
		}
		ContentValues args = new ContentValues();
		marshal(args, entity);
		String restrict = COLUMN_ID + "= ?" + entity.getId();
		try {
			return getDatabase().update(getTableName(), args, restrict, null) > 0;
		} finally {
			// Make sure to close the database
			close();
		}
	}
	
	public List<Entity> findAll() {
		List<Entity> tasks = new ArrayList<Entity>();
		try {
			Cursor cursor = getDatabase().query(getTableName(), getColumns(), null, null, null, null, null);
			try {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					Entity task = cursorToEntity(cursor);
					tasks.add(task);
					cursor.moveToNext();
				}
			} finally {
				// Make sure to close the cursor
				try {
					cursor.close();
				} catch (Exception ignore) {
					// Ignore
				}
			}
		} finally {
			// Make sure to close the database
			close();
		}
		return tasks;
	}
	
	protected Entity cursorToEntity(Cursor cursor) {
		Entity entity = getManagedEntity();
		entity.setId(cursor.getLong(0));
		unMarshal(entity, cursor);
		return entity;
	}
	
}