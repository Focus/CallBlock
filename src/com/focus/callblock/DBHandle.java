package com.focus.callblock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DBHandle  {
	
	private static final String DATABASE_NAME = "callblock.db";
	private static final String TABLE_NAME = "filters";
	private static final int DATABASE_VERSION = 1;
	
	private Context context;
	private SQLiteDatabase db;
	
	private SQLiteStatement statement;
	private static final String INSERT = "insert into " + TABLE_NAME + " (numbers, days, startMin, stopMin) values (?,?,?,?)";

	public DBHandle(Context cont){
		context = cont;
		OpenHelper helper = new OpenHelper(context);
		db = helper.getWritableDatabase();
		statement = db.compileStatement(INSERT);
	}
	
	public long insert(String numbers, String days, int start, int stop){
		statement.bindString(1, numbers);
		statement.bindString(2, days);
		statement.bindDouble(3, start);
		statement.bindDouble(4, stop);
		return statement.executeInsert();
	}
	
	public void deleteAll(){
		db.delete(TABLE_NAME, null, null);
	}
	
	public ArrayList<String> getActiveFilters(){
		List<String> ret = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		int min = cal.get(Calendar.MINUTE) + cal.get(Calendar.HOUR_OF_DAY)*60;
		int dow = cal.get(Calendar.DAY_OF_WEEK);
		
		Cursor c = db.query(TABLE_NAME, new String[]{"numbers", "days", "startMin", "stopMin"},
				"(CASE WHEN startMin < stopMin THEN " +
				min + " BETWEEN startMin AND stopMin " +
						"WHEN startMin>=stopMin THEN ("+
						min + " BETWEEN startMin AND 24*60) OR ("+
						min + " BETWEEN 0 AND stopMin) END)",
				null, null, null, null);
		if(c.moveToFirst()){
			do{
				String days = c.getString(c.getColumnIndex("days"));
				if(c.getInt(c.getColumnIndex("startMin")) <= c.getInt(c.getColumnIndex("stopMin"))){
					if(days.indexOf(""+dow) != -1)
						ret.add(c.getString(c.getColumnIndex("numbers")));
				}
				else{
					if( (days.indexOf(""+dow) != -1) && (min >= c.getInt(c.getColumnIndex("startMin"))) )
						ret.add(c.getString(c.getColumnIndex("numbers")));
					else if((days.indexOf(""+(dow-1)) != -1) && (min <= c.getInt(c.getColumnIndex("stopMin"))))
						ret.add(c.getString(c.getColumnIndex("numbers")));
				}
			}while(c.moveToNext());
		}
		if(!c.isClosed() && c != null)
			c.close();
		return (ArrayList<String>) ret;
	}
	
	public ArrayList<Filter> getAllFilters(){
		List<Filter> ret = new ArrayList<Filter>();
		
		Cursor c = db.query(TABLE_NAME, new String[]{"numbers", "days", "startMin", "stopMin"},
				null, null, null, null, null);
		if(c.moveToFirst()){
			do{
				Filter fil = new Filter(c.getString(c.getColumnIndex("numbers")), c.getString(c.getColumnIndex("days")),
						c.getInt(c.getColumnIndex("startMin")), c.getInt(c.getColumnIndex("stopMin")));
				ret.add(fil);
			}while(c.moveToNext());
		}
		if(!c.isClosed() && c != null)
			c.close();
		return (ArrayList<Filter>) ret;
	}
	
	public void delete(String numbers, String days, int start, int stop){
		db.delete(TABLE_NAME, "numbers='" + numbers + "' AND days='" + days +"' AND startMin=" + start +" AND stopMin=" + stop, null);
	}
	
	private static class OpenHelper extends SQLiteOpenHelper{

		public OpenHelper(Context cont) {
			super(cont, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) { //numbers, days, startHour, startMin, stopHour, stopMin
			db.execSQL("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY, numbers TEXT, days TEXT, startMin INTEGER, stopMin INTEGER)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
		
	}

}
