package com.kova1ski.android.p4db;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.kova1ski.android.p4db.data.P4dbContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // El cursor adapter ya está completado. Así que aquí, lo
    // primero es declararlo.
    P4dbCursorAdaper p4dbCursorAdaper;

    // Declaramos también el listView directamente en el onCreate aunque
    // podríamos hacerlo aquí pero nos ahorramos este ámbito así.

    // Nos acordamos también del loader que va a gestionarnos el tema de
    // la carga en segundo plano y vamos a inicializar su parámetro a 0.
    private static final int ITEMS_LOADER = 0 ;

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

        // Ahora dentro del onCreate, vamos a inicializar los objetos que acabamos de declarar que
        // son el adaptador y el listview.
        // EL CONTEXTO ES ESTE MISMO Y el cursor se lo PASAMOS EN NULL.
        p4dbCursorAdaper = new P4dbCursorAdaper(this, null);
        // asociamos la lista con el xml.
        ListView listViewItemsEnXml = (ListView) findViewById(R.id.listViewItemsMain);

        // Y montamos el adaptador en el listView
        listViewItemsEnXml.setAdapter(p4dbCursorAdaper);

        // Ahora, ya por último el loader. Lo inicializamos conscientes de que, por un lado, ya
        // sólo nos falta implementar esto y por otro que ESTE THIS dispara toda una secuencia
        // que , implements , y genera los métodos automáticamente.
        getLoaderManager().initLoader(ITEMS_LOADER, null, this);


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

    /**
     * Lo primero es el onCreate del loader. Le tenemos que decir aquello
     * que queremos que haga cuando se le llame.
     *
     * @param id        Este parámetro no lo usamos
     * @param args      Este otro parámetro tampoco lo vamos a usar
     * @return          retornamos un CursorLoader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

       // Definimos un projection que indica las columnas que
        // vamos a usar
         String[] projection = {
                 P4dbContract.P4dbEntry._ID,
                 P4dbContract.P4dbEntry.CN_NOMBRE,
                 P4dbContract.P4dbEntry.CN_PESO
         };

         // Este loader ejecutará el método QUERY del ContentProvider en
        // un hilo en background.
        // Y eso mismo es lo que retornamos
        return new CursorLoader(this,               // Contexto
                P4dbContract.P4dbEntry.CONTENT_URI, // Uri de la tabla
                projection,                         // Columnas
                null,                               // cláusula
                null,                               // no selection arguments
                null);                              // default short order
    } // Ok, hecho.

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        // Recordamos que estamos trabajando en segundo plano, en
        // otro hilo.
        // Cuando finaliza de hacer lo que le hemos dicho en
        // el onCreate, o sea que el cursor lo tenemos cargadito :-)
        // pues entonces lo montamos en el adapter. Mira para
        // hacerlo más claro voy a cambiar el nombre de la variable Cursor
        // y la voy a cambiar de data->cursor, así lo entenderemos mejor.
        p4dbCursorAdaper.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // En este método reseteamos el cursor. Si nos fijamos
        // es igual que el mismo método que el finished pero a este
        // no le pasan el cursor. Adivinas cómo se resetea si no
        // tenemos ese parámetro cursor?? PUES CLARO!! Lo pasamos
        // pero en null ;-).
        p4dbCursorAdaper.swapCursor(null);
    }
}









