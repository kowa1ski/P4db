package com.kova1ski.android.p4db;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.kova1ski.android.p4db.data.P4dbContract;

/**
 * Created by Usuario on 16/09/2017.
 */

public class P4dbCursorAdaper extends CursorAdapter {

    public P4dbCursorAdaper(Context context, Cursor c) {
        // Empezamos completando el constructor. El tema de las flags es
        // un tema que no nos hace falta o, al menos, no sé para qué sirve.
        // lo que sí sé es que no nos hacen falta ni ahora ni luego así que
        // las ponemos a 0 para que pase ese parámetro a la superclase y del
        // método eliminamos ese parámetro porque no nos hace falta porque
        // ya se lo estamos pasando en bruto.
        super(context, c, 0);
    }

    /**
     *
     * @param context   El contexto
     * @param cursor    El cursor desde el cual se obtienen los datos.
     *                  El cursor es movido a la posición correcta.
     * @param parent    El parent al cual la nueva VISTA es INCORPORADA.
     * @return          INFLAMOS LA LISTA QUE QUEREMOS(two_line_list_item p.ej)
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Usamos un inflador de layout LayoutInflater, de decimos dónde
        // queremos inflarlo(from context) y luego le decimos que INFLE la
        // vista que deseamos, la de los dos items en este caso.
        return LayoutInflater.from(context).inflate(
                android.R.layout.two_line_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // En ese método tenemos que tomar el control de la vista inflada
        // en el método anterior. Aquí es donde ocurre la asignación de
        // valores en la vista que ha de mostrarlos.

        // uno de los parámetros que se nos pasa es el CURSOR.
        // Este cursor, en el estado en que se nos pasa lo tenemos que
        // manipular para saber qué celda es la que está activa.

        // Declaramos los textViews
        TextView textViewNombre = (TextView) view.findViewById(android.R.id.text1);
        TextView textViewPeso = (TextView) view.findViewById(android.R.id.text2);

        // Localizamos las columnas en las que estamos interesados.
        int nombreColumIndex = cursor.getColumnIndex(P4dbContract.P4dbEntry.CN_NOMBRE);
        int pesoColumIndex = cursor.getColumnIndex(P4dbContract.P4dbEntry.CN_PESO);

        // Leemos los datos de la fila actual en esas columnas
        String stringNombre = cursor.getString(nombreColumIndex);
        String stringPeso = cursor.getString(pesoColumIndex);

        // Asignamos los valores a las views. FÁCIL.
        textViewNombre.setText(stringNombre);
        textViewPeso.setText(stringPeso);

        // Y YA ESTÁAAAAAAAA
    }
}