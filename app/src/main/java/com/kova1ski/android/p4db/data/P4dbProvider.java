package com.kova1ski.android.p4db.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Usuario on 12/09/2017.
 */

public class P4dbProvider extends ContentProvider {

    // Declaramos el código URIMATCHER para el contenido del uri en ambos casos.
    private static final int TODA_LA_TABLA = 100 ;
    private static final int SINGLE_ITEM_ID = 101 ;

    // Creamos el URIMATCHER
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH) ;

    // Y dejamos hechas las dos direcciones de acceso
    static {
        uriMatcher.addURI(P4dbContract.CONTENT_AUTHORITY, P4dbContract.PATH_SEGMENT, TODA_LA_TABLA);
        uriMatcher.addURI(P4dbContract.CONTENT_AUTHORITY, P4dbContract.PATH_SEGMENT, SINGLE_ITEM_ID);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
