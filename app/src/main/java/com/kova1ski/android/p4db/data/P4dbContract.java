package com.kova1ski.android.p4db.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Usuario on 11/09/2017.
 */

public class P4dbContract {
    public P4dbContract() {
    }  // Le nombramos un constructor vacío para evitar malentendidos.

    // Declaramos el CONTENT_AUTHORITY
    public static final String CONTENT_AUTHORITY = "com.kova1ski.android.p4db";

    // Ahora la base para la uri del contentProvider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY) ;

    // El segmento posible
    public static final String PATH_SEGMENT = "p4dbtabla";

    // Creamos una clase interna ENTRY para la tabla y sus columnas.
    public static final class P4dbEntry implements BaseColumns {

        // Nombre de la tabla
        public static final String TABLE_NAME = "p4dbtabla";

        // Nombre de las columnas
        public static final String CN_ID = BaseColumns._ID;
        public static final String CN_NOMBRE = "nombre";
        public static final String CN_PESO = "peso";

        // Ahora las direcciones que me hacen falta para el
        // Provider. La de la lista entera y la de un sólo item.

        // Lista completa.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_SEGMENT ;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_SEGMENT ;
    }














}
