package br.com.fcsconsulting.agendacontatos.app;

import android.text.*;
import android.widget.ArrayAdapter;

import br.com.fcsconsulting.agendacontatos.dominio.entidades.Contato;

/**
 * Created by FCSantos on 28/01/2016.
 */
public class FiltraDados implements TextWatcher{

    private ArrayAdapter<Contato> arrayAdapter;

    public FiltraDados(ArrayAdapter<Contato> arrayAdapter)
    {
        this.arrayAdapter = arrayAdapter;
    }

    public void setArrayAdapter(ArrayAdapter<Contato> arrayAdapter)
    {
        this.arrayAdapter = arrayAdapter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        arrayAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
