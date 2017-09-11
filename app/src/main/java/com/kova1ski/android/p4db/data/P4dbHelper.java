package com.kova1ski.android.p4db.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Usuario on 11/09/2017.
 */

public class P4dbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "P4db_database.sqlite";
    private static final int DB_VERSION = 1 ;

    public P4dbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
