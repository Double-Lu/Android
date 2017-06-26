package com.kekcom.newsapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView newsTextView;
    private ProgressBar progress;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsTextView = (TextView)findViewById(R.id.news_data);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        search = (EditText) findViewById(R.id.searchQuery);

        new FetchNewsTask().execute();
    }

    public class FetchNewsTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            URL weatherRequestUrl = NetworkUtils.buildUrl();

            try {
                String NewsResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                return NewsResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String newsData) {
            if (newsData != null) {
                newsTextView.append((newsData) + "\n\n\n");
            }
            progress.setVisibility(View.INVISIBLE);
        }
    }
}
