package com.kova1ski.android.p4db;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });
    }

    // NOTA IMPORTANTE ---- Sobre el inflate del MENU. este método es únicamente
    // para poder inflar el MENU. Para obtener las respuestas a los items seleccionados
    // está el siguiente que es onOptionsItemSelected, con una variable item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Este es el método que tenemos que usar para darle funcionalidad a los
    // diferentes botones que metamos en el menú.
    // En concreto, este método se ha generado automáticamente al crear esta app.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement  --- (Esto lo ponía y no sé qué significa)
        // Vamos a cambiar este if por un switch. En esta app no hace falta pero quiero que
        // sea siempre así para estar siempre preparado para que en el caso de que quiera
        // añadir más botones, tener el código preparado. Aunque si quiero añadir más botones
        // bastaría con anidar unos , if , dentro de otros sin problema. Prefiero el switch.
        switch (id){
            case R.id.action_main_molon:
                Toast.makeText(this, "Has pulsado el botón molón", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
