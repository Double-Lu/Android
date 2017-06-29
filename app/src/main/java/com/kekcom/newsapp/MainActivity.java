package com.kekcom.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //private TextView newsTextView;
    private ProgressBar progress;
    private EditText search;
    private RecyclerView rv;
    private NewsAdapter na;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //newsTextView = (TextView)findViewById(R.id.news_data);

        progress = (ProgressBar) findViewById(R.id.progressBar);
        search = (EditText) findViewById(R.id.searchQuery);

        rv = (RecyclerView)findViewById(R.id.recyclerview_news);

        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.setVisibility(View.VISIBLE);

        new FetchNewsTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();

        if (itemNumber == R.id.search) {
            String s = search.getText().toString();
            FetchNewsTask task = new FetchNewsTask();
            task.execute();
        }

        return true;
    }

    public class FetchNewsTask extends AsyncTask<String, Void, ArrayList<NewsItem>> implements NewsAdapter.ItemClickListener{
        ArrayList<NewsItem> data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<NewsItem> doInBackground(String... params) {
            ArrayList<NewsItem> result = null;

            URL newsRequestUrl = NetworkUtils.buildUrl();
            Log.d("urltest", "url: " + newsRequestUrl.toString());

            try {
                String NewsResponse = NetworkUtils
                        .getResponseFromHttpUrl(newsRequestUrl);
                result = NetworkUtils.parseJSON(NewsResponse);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsItem> newsData) {

            this.data = newsData;
            super.onPostExecute(data);
            progress.setVisibility(View.GONE);
            if(data != null){
                NewsAdapter adapter = new NewsAdapter(this);
                adapter.setNewsData(data);
                rv.setAdapter(adapter);
            }
        }

        @Override
        public void onItemClick(int clickedItemIndex) {
            String url = data.get(clickedItemIndex).getUrl();
            Log.d("urltest", String.format("Url %s", url));
            openWebPage(url);

        }

        public void openWebPage(String url) {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
}
