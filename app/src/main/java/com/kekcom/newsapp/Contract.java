package com.kekcom.newsapp;

import android.provider.BaseColumns;


public class Contract {

    public static class TABLE_ARTICLES implements BaseColumns {

        //Database table name
        public static final String TABLE_NAME = "newsArticles";
        //fields of SQL table
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_URLTOIMAGE = "url";
        public static final String COLUMN_NAME_PUBLISHDATE = "publishdate";
    }
}
