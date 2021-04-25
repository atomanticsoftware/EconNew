package com.widget.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * SQLiteOpenHelper class to create and update database table two table exists
 * on DB_NAME=DataList database 1:DataS_TABLE_NAME=DataList To store Datas
 * 2:WIDGET_INFO_TABLE_NAME=widgetInfo to store widget information
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DataS_TABLE_NAME = "DataList",
			WIDGET_ID = "widgetId", Data_HEADING = "DataHeading",
			Data_DESCRIPTION = "DataDescription",
			LIST_MEMBERS = "memberCount", Data_DATE = "DataDate",
			Data_IMAGE = "DataImageUrl", DB_NAME = "DataList",
			LAST_UPDATE_DATE = "lastUpdateDate", USER_ID = "userId",
			WIDGET_INFO_TABLE_NAME = "widgetInfo", TYPE = "type",
			LIST_ID = "listId", TYPE_MELIST = "0", TYPE_TIMELINE = "1",
			Data_ID = "DataId", Data_IMAGE_FILENAME = "fileName",LIST_NAME="listName";

	public static final int DB_VERSION = 2;

	public static final String CREATE_DataS_TABLE = "create table if not exists "
			+ DataS_TABLE_NAME
			+ " (_id integer primary key autoincrement, "
			+ WIDGET_ID
			+ " text, "
			+ Data_HEADING
			+ " text, "
			+ Data_DESCRIPTION
			+ " text, "
			+ LIST_MEMBERS
			+ " text, "
			+ Data_DATE
			+ " text, "
			+ Data_IMAGE
			+ " text, "
			+ Data_IMAGE_FILENAME
			+ " text, "
			+ Data_ID
			+ " text, "
			+ USER_ID
			+ " text);";

	public static final String CREATE_WIDGET_INFO_TABLE = "create table if not exists "
			+ WIDGET_INFO_TABLE_NAME
			+ " (_id integer primary key autoincrement, "
			+ WIDGET_ID
			+ " text, "
			+ LAST_UPDATE_DATE
			+ " text, "
			+ TYPE
			+ " text, "
			+ LIST_NAME
			+ " text, "
			+ LIST_ID + " text);";

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("create Data table", CREATE_DataS_TABLE);
		db.execSQL(CREATE_WIDGET_INFO_TABLE);
		db.execSQL(CREATE_DataS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("drop table if exists " + WIDGET_INFO_TABLE_NAME);
		db.execSQL("drop table if exists " + DataS_TABLE_NAME);
		onCreate(db);

	}

}
