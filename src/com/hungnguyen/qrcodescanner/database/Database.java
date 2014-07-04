package com.hungnguyen.qrcodescanner.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
	static int VERSION = 1;
	static String DATABASE_NAME = "qrcodedb.db";
	static String TABLE_NAME = "qrcode";
	static String COLUMN_1 = "Id";
	static String COLUMN_2 = "Name";
	static String COLUMN_3 = "Date";
	static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "+ COLUMN_1 + " INTEGER ";
			
	public Database(Context context) {
		super(context,DATABASE_NAME,null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	public void insert(String id, String date) {
		
	}
	public void delete(String id){
		
	}

}
