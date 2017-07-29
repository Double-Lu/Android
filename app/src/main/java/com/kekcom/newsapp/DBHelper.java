package com.kekcom.newsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    //database name in the phone
    private static final String DATABASE_NAME = "newsArticles.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //create the table on start
    @Override
    public void onCreate(SQLiteDatabase database) {

        String query = "CREATE TABLE " + Contract.TABLE_ARTICLES.TABLE_NAME + " ("+
                Contract.TABLE_ARTICLES._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.TABLE_ARTICLES.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                Contract.TABLE_ARTICLES.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                Contract.TABLE_ARTICLES.COLUMN_NAME_URLTOIMAGE + " TEXT, " +
                Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHDATE + " DATE " +
                "); ";
        Log.d("table", query);
        database.execSQL(query);
    }

    //this happens whenever database version increments
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
