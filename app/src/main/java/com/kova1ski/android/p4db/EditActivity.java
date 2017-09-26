package com.kova1ski.android.p4db;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.kova1ski.android.p4db.data.P4dbContract;
import com.kova1ski.android.p4db.data.P4dbHelper;

// Esta línea ha cambiado pero no se ha mecanizado a mano,
// viene DISPARADA WEYYY desde el THIS del initLoader.
// Recordemos, eso sí, SUSTITUIR el Object por Cursor.
public class EditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Declaramos los editText
    // IMPORTANTE AQUÍ SÓLO SE DECLARAN, LUEGO SE
    // MONTAN PERO EN EL onCreate.
    EditText editTextNombre;
    EditText editTextPeso;

    // Inicializamos las variables que recogerán el contenido de los editText
    private String nombre;
    private int peso;

    // Declaramos aquí el currentItemUri con el mismo nombre que en la otra
    // actividad porque LO VAMOS A RESCATAR. yeeeaaaaa!!!!
    private Uri currentItemUri;

    // Vamos con vista a crear un loader que nos carque los editText
    // Aquí hay que inicializar su inicio a 0.
    private static final int EXISTING_ITEM_LOADER = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Montamos los editText
        editTextNombre = (EditText) findViewById(R.id.editTextEditNombre);
        editTextPeso = (EditText) findViewById(R.id.editTextEditPeso);

        // Si venimos a esta pantalla en modo edición tenemos que saberlo, y
        // para eso leemos en el intent que nos ha traído hasta aquí.
        Intent intent = getIntent(); // hemos rescatado el intent
        currentItemUri = intent.getData(); // LO TENEMOS!!

        // Vamos a probarlo
        if (currentItemUri == null){
            // Evidentemente, si es null, es que NO viene en modo edición
            // sino que ha venido con el botón de agregar.
            // Vamos a decirle que haga algo, va.
            setTitle("NUEVO REGISTRO");
        } else {
            // Este es el caso en que NO ES null, sino que viene cargadito :-)
            // Vamos a decirle que cambie el título.
            setTitle("EDICIÓN DE ITEM");

            // getLOADERmanager.
            // Vamos a darle caña a este , else ,.
            // Desde aquí vamos, directamente a inicializar el loader y
            // así, aprovechamos su THIS para disparar la secuencia del
            // implement y los métodos.
            getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
            // Para que nos vamos a mentir, este THIS es una maravilla
            // Eso sí debemos recordar cambiar los Object por defecto por Cursor.

        }


    }

    // Vamos a inflar el menú, FUERA DEL onCreate.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    // Y la selección de los items. He escrito
    // simplemente onOption y ya se genera tod esto
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_menu_item_molon:

                Toast.makeText(this, "Has pulsado el botón molón", Toast.LENGTH_SHORT).show();

                // IMPORTANTE --- aquí no va un break, va un
                // return true y así no retorna a la superclase.
                return true;
            case R.id.edit_menu_item_Edit_or_Agregar_OK:

                // Fácil. Desde este botón vamos simplemente a enviarlo a
                // un método que nos lo haga tod y luego a finalizar
                // la app para volver al listado principal del Main.

                // Vamos, hacia el método que vamos a especializar
                edit_or_Agregar(); /*alt+intro para crearlo*/

                // y terminamos toda actividad. Chupado.
                finish();

                 return true;
            case R.id.edit_menu_item_deleteBasura:
                // Llamamos desde aquí a una función que hará el trabajo de
                // la eliminación del registro. Y no hace falta que
                // le pasemos nada.
                eliminarRegistro();
                return true; // Esto lo dejamos
            default:

        }
        // Importante --- Esta línea debe permanecer
        return super.onOptionsItemSelected(item);
    }

    private void eliminarRegistro() {

        // Estamos en eliminar registro, que venimos llamados desde el
        // botón de eliminar.
        // Antes de nada una pequeña comprobación para comprobar que
        // la uri viene cargada y luego fácil, llamamos al provider
        // para que elimine el currentItemuri. Allí, en el provider
        // haremos el trabajo más elaborado. Aquí sencillo.
        if (currentItemUri != null){

            // Fácil, mandamos al provider la orden de ejecución
            // La orden de ejecución va al provider el cual devuelve
            // un número, el número de filas borradas por lo que lo metemos
            // en una variable para poder medirlo después
            int rowsDeleted = getContentResolver().delete(currentItemUri, null, null);

            // Y ahora lo medimos e informamos al usuario.
            if (rowsDeleted == 0){
                Toast.makeText(this, "eliminación NO EFECTUADA", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "REGISTRO ELIMINADO", Toast.LENGTH_SHORT).show();
            }
            // IMPORTANTE BASTANTE. HAY QUE FINALIZAR LA ACTIVIDAD UNA VEZ EFECTUADA
            // PARA VOLVER A LA PANTALLA PRINCIPAL
            finish();
        }
    }

    /**
     * En este método se editan o agregan registros a la base.
     */
    private void edit_or_Agregar() {

        // No nos vamos a complicar y lo vamos a ir completando paso a paso.
        // Primero vamos a hacer que agregue y, cuando funcione, ya le
        // añadimos el código para la edición de los items.

        // VAMOS A AGREGAR.
        // Recogemos la información de los editText en las
        // variables al efecto.
        nombre = editTextNombre.getText().toString();
        peso = Integer.parseInt(editTextPeso.getText().toString());

        // Cargamos esto en un ContentValues para enviárselo al
        // ContentProvider a través del Resolver.
        ContentValues contentValues = new ContentValues();
        contentValues.put(P4dbContract.P4dbEntry.CN_NOMBRE, nombre);
        contentValues.put(P4dbContract.P4dbEntry.CN_PESO, peso);



        // Es hora de diferenciar si lo que queremos hacer con este botón
        // es editar o agregar a un nuevo registro los datos que estamos leyendo
        // de los editText. Es muy sencillo joder. El currentItemUri nos dirá
        // qué es lo que estamos intentando conseguir. Así que sin más, medimos
        // esa uri y ya. Evidentemente, metemos tod el código de agregación en
        // el interior del if para poder marcar caminos diferentes.
        // ¿Sería interesante hacerlo con un switch case en la próxima
        // práctica??.
        if (currentItemUri == null) {
            // LO TENEMOS TOD PARA LA AGREGACIÓN.
            // Al Resolver hay que pasarle una URI y el contentValues. La
            // URI será la uri de la tabla.
            // También recogeremos lo que nos devuelve el Provider que
            // ya sabemos que es una uri conteniendo la row creada.
            Uri uriQueDevuelveElProvider =
                    getContentResolver().insert(P4dbContract.P4dbEntry.CONTENT_URI, contentValues);

            // Bueno pues ya hemos hecho comit para averiguar que la base se crea
            // y así lo hemos conseguido comprobándolo directamente
            // en el teléfono. Aquí sólo queda, en este primer paso, un
            // pequeño detalle. Simplemente vamos a comprobar con este código
            // que el registro se ha añadido correctamente y se lo vamos a
            // decir al usuario.

            // Comprobamos la URI devuelta y, dependiendo de la misma, sabremos
            // si tod ha estado correcto o no y, en consecuencia vamos a
            // generar unos Toast.
            if (uriQueDevuelveElProvider == null) {
                Toast.makeText(this, "ERROR uri " + uriQueDevuelveElProvider
                        + " que ha devuelto el provider es " +
                        "incorrecta", Toast.LENGTH_SHORT).show();
            } else {
                // Si no es null la uri, es que está bien.
                Toast.makeText(this, "REGISTRO " +
                        "AÑADIDO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Bueno, pues ya estamos editando. Lo bueno es que ya lo tenemos tod
            // así que sin más venga. Editar es UPDATEAR.
            // SUPERSENCILLO. Le pasamos lo que tenemos y lo demás en null porque
            // la uri ya está apuntando directamente al currentItemUri, o sea,
            // al item que actualmente estamos tratando de UPDATEAR y con eso
            // ya tenemos suficiente porque SERÁ EN EL PROVIDER DONDE desgranaremos
            // esa información y haremos el trabajo de chinos. Aquí es fácil,
            // simplemente le pasamos lo que tenemos y allí en el provider ya
            // arreglamos tod.

            // En este caso, el provider nos devuelve el número de filas afectadas
            // con este método. Sólo tendríamos que ir al update del provider en virgen
            // simplemente y ver que el valor que devuelve es 0. La conclusión es de
            // sentido común.
            int rowsAfected = getContentResolver().update(currentItemUri, contentValues, null, null);

            // Ahora medimos si tod ha salido bien.
            if (rowsAfected == 0){
                // Si no hay filas updateadas, algo ha salido mal.
                Toast.makeText(this, "EDICIÓN DE ITEM FALLIDA", Toast.LENGTH_SHORT).show();
            } else {
                // Si sí que ha habido filas updateadas, es que tod ha salido bien.
                // Habrá sido una claro, en nuestro caso.
                Toast.makeText(this, "EDICIÓN REALIZADA CON ÉXITO", Toast.LENGTH_SHORT).show();

                // y DE AQUÍ NOS VAMOS AL PROVIDER A RELLENAR AQUEL MÉTODO AL QUE
                // ESTAMOS RECLAMANDO DESDE AQUÍ Y QUE HASTA ESTE PASO NO HEMOS COMPLETADO.
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Aquí nada nuevo. Vamos a cargar un cursorLoader que
        // vamos a retornar. O mejor dicho vamos a RETORNAR UN CursorLoader.
        // Lo de siempre, projection y adelante.
        String[] projection = {
                P4dbContract.P4dbEntry._ID,
                P4dbContract.P4dbEntry.CN_NOMBRE,
                P4dbContract.P4dbEntry.CN_PESO };

        return new CursorLoader(this,
                currentItemUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        // Una vez que ya ha finalizado la primera parte del loader en segundo
        // plano, tenemos que coger ese CursorLoader cargadito.
        // Antes de nada nos tenemos que asegurar que está cargadito. Si
        // tiene menos de una fila es que no hay nada que aprovechar y también
        // que algo no ha ido bien.
        if (cursor == null || cursor.getCount() < 1 ){
            // En este caso no hay nada, está vacío o no tiene nada.
            // Se retorna y ya.
            return;
        }

        // Fuera del anterior IF, es que tod ha ido estupenamente.
        // Vamos a leer ese CursorLoader, vamos a ver qué hay escrito en
        // las celdas y lo vamos a copiar en los editText.
        // Para eso vamos a tener un método. Vamos a saber numéricamente
        // las columnas que nos interesan, luego, como ya las tenemos localizadas
        // esas celdas sólo extraemos su información y luego la copiamos en los
        // editText.
        // Hacemos un IF con un MOVETOFIRST y toda la secuencia se viene sola.
        if (cursor.moveToFirst()){

            // identificamos las columnas con INT porque es la forma
            // en que vamos a localizar las celdas.
            int nombreColumIndex = cursor.getColumnIndex(P4dbContract.P4dbEntry.CN_NOMBRE);
            int pesoColumIndex = cursor.getColumnIndex(P4dbContract.P4dbEntry.CN_PESO);

            String nombre = cursor.getString(nombreColumIndex);
            int peso = cursor.getInt(pesoColumIndex);

            editTextNombre.setText(nombre);
            editTextPeso.setText(Integer.toString(peso));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // Y el reset es poner los editText en blanco Y YA.
        editTextNombre.setText("");
        editTextPeso.setText("");
    }
}










