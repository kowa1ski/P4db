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

        // Con los campos nombrados ya en el Contract vamos a
        // terminar este método.

        // Debemos crear el String para crear la base de datos.

        // Creamos la sentencia SQL
        String SQL_CREATE_TABLE = "CREATE TABLE " + P4dbContract.P4dbEntry.TABLE_NAME
                + " ("  // Y ahora las columnas
                + P4dbContract.P4dbEntry.CN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + P4dbContract.P4dbEntry.CN_NOMBRE + " TEXT NOT NULL, "
                + P4dbContract.P4dbEntry.CN_PESO + " INTEGER);" ;

        // Y ejecutamos la sentencia invocando al , db , de este método.
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
