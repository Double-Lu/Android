package com.kekcom.newsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class RefreshUtils {

    //database refresher / deletes the old and puts in the new
    public static void updateNewsArticles(Context context) {
        ArrayList<NewsItem> result = null;
        URL url = NetworkUtils.makeURL();
        SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        try {
            DatabaseUtils.deleteAll(db);
            String json = NetworkUtils.getResponseFromHttpUrl(url);
            result = NetworkUtils.parseJSON(json);
            DatabaseUtils.batch(db, result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
    }
}
