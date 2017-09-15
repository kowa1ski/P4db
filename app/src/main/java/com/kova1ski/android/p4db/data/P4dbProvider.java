package com.kova1ski.android.p4db.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
        // ATENTOS hemos modificado este último path porque es obligatorioponerle , "/#" ,.
        // Y LA EXPLICACIÓN YA LA TENGO CRISTALINA. ES PORQUE ES SIMPLEMENTE UN PATRÓN de uri,
        // el UriMatcher compara la URI con estos dos patrones y así identifica qué tipo de uri
        // es la que tenemos enfrente. Para este segundo patrón le estamos diciendo al urimatcher que
        // una de las formas posibles de la uri es esta, que, después del path, la uri lleve añadido
        // un slash-barra seguido de un número. Y un número el Urimatcher entiende que es una #.
        // Si después del path no lleva nada, o sea que ahí se acaba la uri, es que estamos en el caso
        // anterior y en esta app se trata de una uri que quiere afectar a toda la tabla.
        uriMatcher.addURI(P4dbContract.CONTENT_AUTHORITY, P4dbContract.PATH_SEGMENT + "/#", SINGLE_ITEM_ID);
    }

    // Hay que declarar el objeto dbHelper
    private P4dbHelper dbHelper;

    @Override
    public boolean onCreate() {

        // En el onCreate del Provider está el acceso a la base de datos
        // así que vamos a nombrarlo. Afuera nombramos el objeto y aquí ya
        // lo inicializamos y le retornamos un true a la función.

        // NOTA --> que digo yo que lo de getContext es porque es un Provider
        // y tiene que escoger el contexto de donde quiera que sea llamado,
        // o sea, de otro sitio que no es este. Es por eso que no se le
        // marca el contexto sino que se llama a , getContext() , para que
        // lo obtenga de donde está trabajando realmente.
        dbHelper = new P4dbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // Declaramos el cursor. CURSOR QUE AL FINAL ES EL , RETURN ,.
        // Es el objetivo de esta función.
        Cursor cursor;

        // Para este cursor, el que yo llamo EL CURSOR PREGUNTANTE,
        // es sencillo. Accedemos a la base en modo LECTURA(pa qué más)
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        /**
         * A este método llegamos con una uri, se ve que es pasada aquí.
         * Esta uri que tenemos aquí, que nos pasan, la tenemos que leer
         * y tenemos que ver cómo termina para ver a qué se refiere. Para ello
         * LA VAMOS A FILTRAR por el URIMATCHER. Él, el urimatcher la
         * engullirá e interpretará. Una vez que ha pasado por el urimatcher
         * ya sólo tenemos que preguntar al urimatcher qué terminación le
         * ha asignado con el , int , ese de 100 ó 101.
          */
        int match = uriMatcher.match(uri);
        switch (match){
            case TODA_LA_TABLA:
                // toda la tabla, es solicitar el cursor a pelo, sin modificar
                // ninguno de los parámetros que nos pasan.
                cursor = db.query(P4dbContract.P4dbEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                // ahora el break, el cursor que sea, este o el siguiente, ya se envía
                // de retorno al final del método.
                break;
            case SINGLE_ITEM_ID:
                // caso singular, cargamos el cursor con un sólo item.
                // Para este caso hay que modificar la selección para ajustarla
                // a que pregunte el _id en el SELECTION y también hay que decirle QUÉ _id es el que
                // queremos en el selectionArgs.
                selection = P4dbContract.P4dbEntry.CN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // Venga ya está. En este caso pues el cursor igual con lo anterior modificado
                cursor = db.query(P4dbContract.P4dbEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("No se puede construir la query" +
                        "por estar tratando con la URI desconocida: " + uri);
        }

        // Tod parece que está controlado. Tod parece que ha salido bien así
        // que, antes de retornar el cursor con toda la información, hay que
        // decirle a los LISTENERS que algo ha cambiado.
        // Para hacerlo LE DECIMOS AL CURSOR que sea él el que notifique
        // los cambios que están sucediendo.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // ya no retornamos false, retornamos el cursor.
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = uriMatcher.match(uri);
        switch (match) {
            case TODA_LA_TABLA:
                // Devolvemos el String que es una dirección
                return P4dbContract.P4dbEntry.CONTENT_LIST_TYPE;
            case SINGLE_ITEM_ID:
                return P4dbContract.P4dbEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Desconocida uri "
                + uri + " con match " + match);
        }
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
