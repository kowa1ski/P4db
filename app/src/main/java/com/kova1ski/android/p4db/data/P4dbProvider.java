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
import android.util.Log;

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

        // La única manera de insertar un registro nuevo en la tabla es
        // llamando actuar en toda la tabla. Es por ello que la Uri que
        // se no s pasa tiene que ser de toda la tabla y el ContentValues
        // contendrá los valores del nuevo registro. Entonces está claro que
        // la Uri que se nos pasa tiene un patrón que es el de toda la tabla
        // y es por eso que necesitamos averiguarlo con el uriMatcher que
        // nos arrojará un valor de 100 midiendo el , match,.

        // Luego entonces, medimos la Uri que se nos está pasando.
        final int match = uriMatcher.match(uri);
        switch (match){
            case TODA_LA_TABLA:
                return insertNewItem(uri, values);
            default:
                throw new IllegalArgumentException("La inserción no es" +
                        "soportada para la uri " + uri);
        }
    }

    private Uri insertNewItem(Uri uri, ContentValues values) {

        // Un requisito es que el nombre no sea null. Lo chequeamos.
        String nombreQueComprobamos = values.getAsString(P4dbContract.P4dbEntry.CN_NOMBRE);
        if (nombreQueComprobamos == null){
            throw new IllegalArgumentException("ERROR, el nombre es null");
        }
        // El otro campo no hace falta chequearlo porque puede ser null.

        // Vamos allá.
        // Accedemos a la base
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // ejecutamos la inserción Y AL TIEMPO ATRAPAMOS EL RETORNO que
        // es el número de filas afectadas, vamos, uno en este caso.
        long id = db.insert(P4dbContract.P4dbEntry.TABLE_NAME, null, values);

        // Ahora comprobamos que la inserción ha sido correcta
        if (id == -1){
            Log.e("TAG_PROVIDER", "ERROR EN LA INSERCIÓN, VALOR -1 al " +
                    "intentar insertar la uri: " + uri);
        }

        // Si ya hemos pasado los filtros y tod ha ido bien, ya tenemos insertado
        // el nuevo registro.
        // ESTE MÉTODO RETORNA UNA URI. LA URI DE LA NUEVA ROW.

        // Antes del retorno notificamos el cambio. Le damos la uri que
        // ha cambiado
        getContext().getContentResolver().notifyChange(uri, null);

        // Evidentemente retornamos la uri, la nueva uri que construimos
        // a partir de la de antes, que es de la tabla(recordamos? claro que sí),
        // y le añadimos la variable que hemos creado y que contiene la
        // fila row que hemos creado nueva.
        return ContentUris.withAppendedId(uri, id);




    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Retornamos el número de filas que borramos.
        // Estamos recibiendo la URI
        // Debemos modificar la claúsula y los argumentos, en nuestro caso.

        // Empezamos.
        // Accedemos a la base
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // Declaramos la variable donde vamos a meter el valor de retorno.
        int rowsDeleted;

        // Comtemplamos ambos casos de Uri con el match y lo
        // metemos en un , int , que será 100 ó 101.
        final int match = uriMatcher.match(uri);
        switch (match){
            case TODA_LA_TABLA:
                // En este caso no hay nada que modificar más. Borramos y
                // ya está, borramos tod, claro está.
                rowsDeleted = database.delete(P4dbContract.P4dbEntry.TABLE_NAME,
                        null, null);
                break;
            case SINGLE_ITEM_ID:
                // El caso que nos interesa. Lo de siempre.
                selection = P4dbContract.P4dbEntry.CN_ID + "=?";
                // Este es más jodidete porque hay que usar un parseId y
                // tod eso pero es tod lo mismo.
                selectionArgs = new String[]{
                        String.valueOf(ContentUris.parseId(uri))
                };
                // Chupado.
                // Y con tod hecho ya, vamos al borrado.
                rowsDeleted = database.delete(
                        P4dbContract.P4dbEntry.TABLE_NAME,
                        selection,
                        selectionArgs );
                break;
            default:
                throw new IllegalArgumentException("No es posible eliminar" +
                        "por error en tratamiento de uri " + uri);
        }

        // IMPORTANTE, YA FUERA DEL SWITCH, HAY QUE DECIR A TODOS LOS LISTENER
        // QUE ALGO HA CAMBIADO.
        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        // Más o menos como el insert. Comenzamos por pasos sencillos.
        // Venga, vamos. Tenemos que nos pasan una uri. Vamos a comprobar qué
        // tipo de uri es metiéndole el urimatcher para que identifique el patrón
        // que tenemos enfrente.
        final int match = uriMatcher.match(uri);
        switch (match){
            case TODA_LA_TABLA:
                // Esto no lo usamos en esta app. Serviría si quisiéramos
                // resetear todos los pesos de las mascotas(en la app de
                // mascotas :-) ).
                // Y como no sirve de nada y hay que retornar un número
                // pues, por no hacerlo de manera directa desde aquí, que bien
                // se podría, lo pasamos al método que vamos a crear al efecto.
                return updateItem(uri, values, selection, selectionArgs);
            // Y ahora el que nos interesa de verdad.
            case SINGLE_ITEM_ID:
                // Para este caso debemos extrar el _id de la URI para identificar
                //el item en concreto que queremos updatear. Luego, en la selección
                // haremos , "_id=?" , y también los selection arguments serán un
                // String Array conteniendo el ID actual.
                // En el contenedor de valores ContentValues ya nos vendrán
                // los datos a cambiar. Eso último se lo pasamos tal cual, claro.
                //
                // Venga, le decimos que queremos updatear los registros en base
                // a su _id.
                // Esto se hace en el selection, se le dice eso mismo: _id=?
                selection = P4dbContract.P4dbEntry.CN_ID + "=?";
                // Y el selectionArgs le pasa ese argumento. Y qué _id es?. Pues la
                // extraemos de la uri que nos pasan que la contiene.
                selectionArgs = new String[] {
                        String.valueOf(ContentUris.parseId(uri))
                };

                // Y con estos parámetros modificados ya lo tenemos tod.
                // Al igual que en el otro caso nos vamos a apoyar en el método
                // que nos sirve de auxiliar de este. Evidentemente le estamos
                // pasando la misma instrucción que en el caso anterior pero
                // con los parámetros interesantes ya modificados.

                // De esta forma el siguiente método updateItem que hemos creado
                // recibe los mismo tod el rato, en un caso o en el otro, desde
                // este método pero estos parámetros que nos interesan ya están
                // modificados y NO COMO EN EL CASO ANTERIOR que están pasando
                // tal cual nos llegan.
                return updateItem(uri, values, selection, selectionArgs);
            default:
                // como no, metemos el default para capturar una
                // eventual excepción.
                throw new IllegalArgumentException("UPDATE no es soportada para la uri " + uri);
        }
    }

    // FÍJATE QUÉ PASADA.
    // ACABO DE CREAR EL MÉTODO CON LA BOMBILLA Y ÉL SOLITO YA SABE QUE TIENE
    // QUE DEVOLVER UN INT.
    // QUÉ GUAY, QUÉ FELICIDAD.
    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // VENGAAAA !!!! qué esto ya está chupado.
        // Vamos a coger los parámetros que nos están pasando y ya está, vamos
        // a UPDATEAR.

        // Accedemos a la base en modo escritura.
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // Y YA ESTÁAAAAA JODER.
        // Le damos el UPDATEADO ya.
        int filasUpdateadas = database.update(P4dbContract.P4dbEntry.TABLE_NAME,
                values, selection, selectionArgs);

        // Ha sido muy muy sencillo. Qué nos queda? poco.
        // Hay que
        // DECIR A TODOS LOS LISTERNERS QUE ALGO HA CAMBIADO !!
        // Aprovechamos y verificamos que hemos updateado, al menos,
        // una fila.
        if (filasUpdateadas != 0 ){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Y por último retornamos el valor , int , como es lógico.
        // Sabemos que en el método de destino también se va a comprobar
        // que al menos una fila ha sido updateada y nos lo dirá
        // con un Toast en el Edit, así que sin miedo.
        return filasUpdateadas;
    }
}