package com.kekcom.newsapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DatabaseUtils {

    public static Cursor getAll(SQLiteDatabase db) {
        Cursor cursor = db.query(
                Contract.TABLE_ARTICLES.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHDATE + " DESC"
        );
        return cursor;
    }

    //insert table field values in a batch (Arraylist of newsItems)
    public static void batch(SQLiteDatabase database, ArrayList<NewsItem> newsArticles) {

        database.beginTransaction();
        try {
            for (NewsItem article : newsArticles) {
                ContentValues cv = new ContentValues();
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_TITLE, article.getTitle());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_DESCRIPTION, article.getDescription());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_URLTOIMAGE, article.getUrlToImage());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHDATE, article.getPublishedAt());
                database.insert(Contract.TABLE_ARTICLES.TABLE_NAME, null, cv);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    //deletes all field values in the table
    public static void deleteAll(SQLiteDatabase db) {
        db.delete(Contract.TABLE_ARTICLES.TABLE_NAME, null, null);
    }
}
