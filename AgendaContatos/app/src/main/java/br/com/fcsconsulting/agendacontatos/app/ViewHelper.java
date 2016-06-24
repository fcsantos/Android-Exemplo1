package br.com.fcsconsulting.agendacontatos.app;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by FCSantos on 25/01/2016.
 */
public class ViewHelper {

    public static ArrayAdapter<String> CreateArrayAdapter(Context ctx, Spinner spinner)
    {
        ArrayAdapter arrayadapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item);
        arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayadapter);
        return arrayadapter;
    }
}
