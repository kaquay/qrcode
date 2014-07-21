package com.hungnguyen.qrcodescanner.database;

import java.util.ArrayList;

import com.hungnguyen.qrcodescanner.model.HistoryItemObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
	static int VERSION = 1;
	static String DATABASE_NAME = "qrcodedb.db";
	static String TABLE_NAME = "qrcodes";
	static String COLUMN_1 = "id";
	static String COLUMN_2 = "name";
	static String COLUMN_3 = "date";
	static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( " + COLUMN_1
			+ " INTEGER PRIMARY KEY, " + COLUMN_2 + " TEXT, " + COLUMN_3
			+ " DATETIME )";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * insert to sqlite
	 * 
	 * @param name
	 *            : value
	 * @param date
	 *            : format DD/MM/YYYY
	 */
	public void insert(String name, String date) {
		// String query = "INSERT INTO " + TABLE_NAME + "(" + COLUMN_2 + ","
		// + COLUMN_3 + ") VALUES(" + name + ", DATE(NOW())";
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_2, name);
		values.put(COLUMN_3, date);
		db.insert(TABLE_NAME, null, values);
		db.close();
	}

	/**
	 * Delete Row
	 * 
	 * @param id
	 */
	public void delete(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_1 + "="
				+ id;
		db.execSQL(query);
		db.close();
	}

	/**
	 * Get all date DISTINTC
	 * 
	 * @return
	 */
	public ArrayList<String> getAllDate() {
		ArrayList<String> listValues = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT DISTINCT " + COLUMN_3 + " FROM " + TABLE_NAME;
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			do {
				String st = cursor.getString(0);
				listValues.add(st);
			} while (cursor.moveToNext());
		}
		db.close();
		return listValues;
	}

	/**
	 * Select all row where have date like 'date'
	 * 
	 * @param date
	 *            format DD/MM/YYYY
	 * @return
	 */
	public ArrayList<HistoryItemObject> getValueByDate(String date) {
		ArrayList<HistoryItemObject> listValues = new ArrayList<HistoryItemObject>();
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT " + COLUMN_1 + ", " + COLUMN_2 + " FROM "
				+ TABLE_NAME + " WHERE " + COLUMN_3 + "='" + date + "'";
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			do {
				String id = cursor.getString(0);
				String name = cursor.getString(1);
				listValues.add(new HistoryItemObject(id, name));
			} while (cursor.moveToNext());
		}
		db.close();
		return listValues;
	}

	public boolean isHaveValues() {
		boolean result = true;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
		if (cursor.moveToFirst())
			result = false;

		return result;
	}

	public void DeleteAllItem() {
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "DELETE FROM " + TABLE_NAME;
		db.execSQL(query);
		db.close();
	}
}
