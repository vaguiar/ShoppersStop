package com.example.shoppersstop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CreateDatabase extends SQLiteOpenHelper {

  public static final String TABLE_Items = "items";
  public static final String COLUMN_Name = "Name";
  public static final String COLUMN_Catagory = "Catagory";
  public static final String COLUMN_X = "X";
  public static final String COLUMN_Y = "Y";
  
  public static final String TABLE_Map = "map";
  public static final String COLUMN_ID = "ID";
  public static final String COLUMN_P1_X = "P1_X";
  public static final String COLUMN_P1_Y = "P1_Y";
  public static final String COLUMN_P2_X = "P2_X";
  public static final String COLUMN_P2_Y = "P2_Y";
  
  private static final String DATABASE_NAME = "shoppersStop.db";
  private static final int DATABASE_VERSION = 3;

  // Database creation sql statement
  private static final String DATABASE_CREATE_ItemMap = "create table "
      + TABLE_Items + "(" 
      + COLUMN_Name + " text primary key,  " 
      + COLUMN_Catagory + " text ,  " 
      + COLUMN_X    + " integer not null , " 
      + COLUMN_Y    + " integer not null);";

  private static final String DATABASE_CREATE_Map = "create table "
	      + TABLE_Map + "(" + COLUMN_ID
	      + " integer primary key autoincrement,"
	      + COLUMN_Catagory + " text, "
	      + COLUMN_P1_X + " integer not null, "
	      + COLUMN_P1_Y + " integer not null, "
	      + COLUMN_P2_X + " integer not null, "
	      + COLUMN_P2_Y + " integer not null);";
  
  public CreateDatabase(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE_ItemMap);
    database.execSQL(DATABASE_CREATE_Map);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(CreateDatabase.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_Items);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_Map);
    onCreate(db);
  }

} 