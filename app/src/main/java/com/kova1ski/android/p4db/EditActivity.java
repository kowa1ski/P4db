package com.kova1ski.android.p4db;

import android.content.ContentValues;
import android.content.Intent;
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

public class EditActivity extends AppCompatActivity {

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
                Toast.makeText(this, "BORRADO para siempre", Toast.LENGTH_SHORT).show();
                return true;
            default:

        }
        // Importante --- Esta línea debe permanecer
        return super.onOptionsItemSelected(item);
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
    }
}