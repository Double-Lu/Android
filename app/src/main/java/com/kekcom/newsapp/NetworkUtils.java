package com.kekcom.newsapp;

/**
 * Created by lujac on 6/25/2017.
 */
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public class NetworkUtils {
    public static final String TAG = "NetworkUtils";
    private static final String BASE_URL = "https://newsapi.org/v1/articles";

    final static String SOURCE_PARAM = "source";
    private static final String source = "the-next-web";
    final static String SORTBY_PARAM = "sortBy";
    private static final String sortby = "latest";
    final static String APIKEY_PARAM = "apiKey";
    private static final String apikey = "668c652f589c429ca494d3c5887b66e3";

    public static URL buildUrl() {
        Log.d("urltest", "BUILDING...");
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SOURCE_PARAM, source)
                .appendQueryParameter(SORTBY_PARAM, sortby)
                .appendQueryParameter(APIKEY_PARAM, apikey)
                .build();

        URL url = null;
        try {
            System.out.println("BUILT" + builtUri.toString());
            Log.d("urltest", "BUILT" + builtUri.toString());
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

