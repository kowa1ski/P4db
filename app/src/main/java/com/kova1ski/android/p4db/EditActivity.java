package com.kova1ski.android.p4db;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
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
                Toast.makeText(this, "Agregado o editado", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.edit_menu_item_deleteBasura:
                Toast.makeText(this, "BORRADO para siempre", Toast.LENGTH_SHORT).show();
                return true;
            default:

        }
        // Importante --- Esta línea debe permanecer
        return super.onOptionsItemSelected(item);
    }
}





