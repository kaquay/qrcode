package com.hungnguyen.qrcodescanner.database;

import java.util.ArrayList;

import com.hungnguyen.qrcodescanner.model.HistoryEntryItemObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
	static int VERSION = 1;
	static String DATABASE_NAME = "qrcodedb.db";
	static String TABLE_NAME = "qrcode";
	static String COLUMN_1 = "Id";
	static String COLUMN_2 = "Name";
	static String COLUMN_3 = "Date";
	static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( " + COLUMN_1
			+ " INTEGER PRIMARY KEY ";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void insert(String name, String date) {
//		String query = "INSERT INTO " + TABLE_NAME + "(" + COLUMN_2 + ","
//				+ COLUMN_3 + ") VALUES(" + name + ", DATE(NOW())";
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_2, name);
		values.put(COLUMN_3, date);
		db.insert(TABLE_NAME, null, values);
		db.close();
	}

	public void delete(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_1 + "="
				+ id;
		db.execSQL(query);
		db.close();
	}

	public ArrayList<String> getAllDate() {
		ArrayList<String> listValues = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT DISTINCT DATE(" + COLUMN_3 + ") FROM "
				+ TABLE_NAME;
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

	public ArrayList<HistoryEntryItemObject> getValueByDate(String day,
			String month, String year) {
		ArrayList<HistoryEntryItemObject> listValues = new ArrayList<HistoryEntryItemObject>();
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT " + COLUMN_1 + ", " + COLUMN_2 + " FROM "
				+ TABLE_NAME + " WHERE DAY(" + COLUMN_3 + ")=" + day
				+ " AND MONTH(" + COLUMN_3 + ")=" + month + " AND YEAR("
				+ COLUMN_3 + ")=" + year;
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			do {
				String id = cursor.getString(0);
				String name = cursor.getString(1);
				listValues.add(new HistoryEntryItemObject(id, name));
			} while (cursor.moveToNext());
		}
		db.close();
		return listValues;
	}
}
