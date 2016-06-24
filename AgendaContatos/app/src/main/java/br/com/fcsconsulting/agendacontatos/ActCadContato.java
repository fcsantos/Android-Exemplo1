package br.com.fcsconsulting.agendacontatos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;

import java.text.DateFormat;
import java.util.*;

import br.com.fcsconsulting.agendacontatos.app.MessageBox;
import br.com.fcsconsulting.agendacontatos.app.ViewHelper;
import br.com.fcsconsulting.agendacontatos.database.DataBase;
import br.com.fcsconsulting.agendacontatos.dominio.RepositorioContato;
import br.com.fcsconsulting.agendacontatos.dominio.entidades.Contato;
import br.com.fcsconsulting.agendacontatos.util.DateUtil;

public class ActCadContato extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtTelefone;
    private EditText edtEmail;
    private EditText edtEndereco;
    private EditText edtDatasEspeciais;
    private EditText edtGrupos;

    private Spinner spnTipoTelefone;
    private Spinner spnTipoEmail;
    private Spinner spnTipoEndereco;
    private Spinner spnTipoDatasEspeciais;

    private ArrayAdapter<String> adpTipoTelefone;
    private ArrayAdapter<String> adpTipoEmail;
    private ArrayAdapter<String> adpTipoEndereco;
    private ArrayAdapter<String> adpTipoDatasEspeciais;

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private RepositorioContato repositorioContato;
    private Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_contato);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        edtNome = (EditText)findViewById(R.id.edtNome);
        edtTelefone = (EditText)findViewById(R.id.edtTelefone);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtEndereco = (EditText)findViewById(R.id.edtEndereco);
        edtDatasEspeciais = (EditText)findViewById(R.id.edtDatasEspeciais);
        edtGrupos = (EditText)findViewById(R.id.edtGrupos);

        spnTipoTelefone = (Spinner)findViewById(R.id.spnTipoTelefone);
        spnTipoEmail = (Spinner)findViewById(R.id.spnTipoEmail);
        spnTipoEndereco = (Spinner)findViewById(R.id.spnTipoEndereco);
        spnTipoDatasEspeciais = (Spinner)findViewById(R.id.spnTipoDatasEspeciais);

        CarregarTipoTelefone();
        CarregarTipoEmail();
        CarregarTipoEndereco();
        CarregarTipoDatasEspeciais();

        ExibeDataListener listener = new ExibeDataListener();
        edtDatasEspeciais.setOnClickListener(listener);
        edtDatasEspeciais.setOnFocusChangeListener(listener);
        edtDatasEspeciais.setKeyListener(null);

        Bundle bundle = getIntent().getExtras();
        if ((bundle != null) && (bundle.containsKey(ActContato.PAR_CONTATO))) {
            contato = (Contato) bundle.getSerializable(ActContato.PAR_CONTATO);
            preencheDados();
        }
        else
            contato = new Contato();

        ConexaoBD();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null)
            conn.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_cad_contato, menu);
        if (contato.getId() != 0)
            menu.getItem(1).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        switch (id)
        {
            case R.id.mni_acao1:
                salvar();
                finish();
                break;
            case R.id.mni_acao2:
                excluir();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void preencheDados()
    {
        edtNome.setText(contato.getNome());
        edtTelefone.setText(contato.getTelefone());
        spnTipoTelefone.setSelection(Integer.parseInt(contato.getTipoTelefone()));
        edtEmail.setText(contato.getEmail());
        spnTipoEmail.setSelection(Integer.parseInt(contato.getTipoEmail()));
        edtEndereco.setText(contato.getEndereco());
        spnTipoEndereco.setSelection(Integer.parseInt(contato.getTipoEndereco()));
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        String dt = format.format(contato.getDatasEspeciais());
        edtDatasEspeciais.setText(dt);
        spnTipoDatasEspeciais.setSelection(Integer.parseInt(contato.getTipoDatasEspeciais()));
        edtGrupos.setText(contato.getGrupos());
    }

    private void excluir()
    {
        try{
            repositorioContato.excluir(contato.getId());
        }
        catch (Exception ex)
        {
            MessageBox.Show(this, "Erro", "Erro ao excluir os dados: " + ex.getMessage());
        }
    }

    private void salvar()
    {
        try {
            contato.setNome(edtNome.getText().toString());
            contato.setTelefone(edtTelefone.getText().toString());
            contato.setEmail(edtEmail.getText().toString());
            contato.setEndereco(edtEndereco.getText().toString());
            contato.setGrupos(edtGrupos.getText().toString());
            contato.setTipoTelefone(String.valueOf(spnTipoTelefone.getSelectedItemPosition()));
            contato.setTipoEmail(String.valueOf(spnTipoEmail.getSelectedItemPosition()));
            contato.setTipoEndereco(String.valueOf(spnTipoEndereco.getSelectedItemPosition()));
            contato.setTipoDatasEspeciais(String.valueOf(spnTipoDatasEspeciais.getSelectedItemPosition()));

            if (contato.getId() == 0)
                repositorioContato.inserir(contato);
            else
                repositorioContato.alterar(contato);
        }
        catch (Exception ex)
        {
            MessageBox.Show(this, "Erro", "Erro ao salvar os dados: " + ex.getMessage());
        }
    }

    private void ConexaoBD() {
        try
        {
            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();
            repositorioContato = new RepositorioContato(conn);
        }
        catch (SQLException ex)
        {
            MessageBox.Show(this, "Erro", "Erro ao criar o banco: " + ex.getMessage());
        }
    }

    private ArrayAdapter<String> CarregarTipoTelefone()
    {
        adpTipoTelefone = ViewHelper.CreateArrayAdapter(this, spnTipoTelefone);
        adpTipoTelefone.add("Celular");
        adpTipoTelefone.add("Trabalho");
        adpTipoTelefone.add("Casa");
        adpTipoTelefone.add("Principal");
        adpTipoTelefone.add("Fax Trabalho");
        adpTipoTelefone.add("Fax Casa");
        adpTipoTelefone.add("Pager");
        adpTipoTelefone.add("Outros");
        return adpTipoTelefone;
    }

    private ArrayAdapter<String> CarregarTipoEmail()
    {
        adpTipoEmail = ViewHelper.CreateArrayAdapter(this, spnTipoEmail);
        adpTipoEmail.add("Casa");
        adpTipoEmail.add("Trabalho");
        adpTipoEmail.add("Outros");
        return adpTipoEmail;
    }

    private ArrayAdapter<String> CarregarTipoEndereco()
    {
        adpTipoEndereco = ViewHelper.CreateArrayAdapter(this, spnTipoEndereco);
        adpTipoEndereco.add("Casa");
        adpTipoEndereco.add("Trabalho");
        adpTipoEndereco.add("Outros");
        return adpTipoEndereco;
    }

    private ArrayAdapter<String> CarregarTipoDatasEspeciais()
    {
        adpTipoDatasEspeciais = ViewHelper.CreateArrayAdapter(this, spnTipoDatasEspeciais);
        adpTipoDatasEspeciais.add("Aniversário");
        adpTipoDatasEspeciais.add("Datas comemorativas");
        adpTipoDatasEspeciais.add("Outros");
        return adpTipoDatasEspeciais;
    }

    private void exibeData()
    {
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dlg = new DatePickerDialog(this, new SelecionaDataListener(), ano, mes, dia);
        dlg.show();
    }

    private class ExibeDataListener implements View.OnClickListener, View.OnFocusChangeListener
    {
        @Override
        public void onClick(View v) {
            exibeData();
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                exibeData();
        }
    }

    private class SelecionaDataListener implements DatePickerDialog.OnDateSetListener
    {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String dt = DateUtil.dateToString(year, monthOfYear, dayOfMonth);
            Date data = DateUtil.getDate(year, monthOfYear, dayOfMonth);
            edtDatasEspeciais.setText(dt);
            contato.setDatasEspeciais(data);
        }
    }
}
