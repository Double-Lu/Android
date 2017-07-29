package com.kekcom.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>, NewsAdapter.ItemClickListener {

    private static final String TAG = "MainActivity";
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    //local database
    private SQLiteDatabase database;
    //database iterator
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //get phone's preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //from there get if app ever exists, execute first launch if not
        if (prefs.getBoolean("firstLaunch", true)) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.restartLoader(1, null, this).forceLoad();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isfirst", false);
            editor.commit();
        }

        ScheduleUtils.scheduleRefresh(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //local database
        database = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(database);
        adapter = new NewsAdapter(cursor, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNum = item.getItemId();

        if (itemNum == R.id.search) {
            //for search menu
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.restartLoader(1, null, this).forceLoad();
        }

        return true;
    }

    //database refresher
    @Override
    public Loader<Void> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Void>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public Void loadInBackground() {
                RefreshUtils.updateNewsArticles(MainActivity.this);
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        progressBar.setVisibility(View.GONE);

        //iterate the new database
        database = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(database);
        adapter = new NewsAdapter(cursor, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    //implement link
    @Override
    public void onItemClick(Cursor cursor, int clickedItemIndex) {
        cursor.moveToPosition(clickedItemIndex);
        String url = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_URLTOIMAGE));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

}
