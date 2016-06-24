package br.com.fcsconsulting.agendacontatos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import android.database.sqlite.*;
import android.database.*;

import br.com.fcsconsulting.agendacontatos.app.FiltraDados;
import br.com.fcsconsulting.agendacontatos.app.MessageBox;
import br.com.fcsconsulting.agendacontatos.database.DataBase;
import br.com.fcsconsulting.agendacontatos.dominio.RepositorioContato;
import br.com.fcsconsulting.agendacontatos.dominio.entidades.Contato;


public class ActContato extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private ImageButton btnAdicionar;
    private EditText edtPesquisa;
    private ListView lstContatos;
    private ArrayAdapter<Contato> adpContatos;

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private RepositorioContato repositorioContato;
    private FiltraDados filtradados;

    public static final String PAR_CONTATO = "CONTATO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_contato);
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

        btnAdicionar = (ImageButton)findViewById(R.id.btnAdicionar);
        edtPesquisa = (EditText)findViewById(R.id.edtPesquisa);
        lstContatos = (ListView)findViewById(R.id.lstContatos);

        btnAdicionar.setOnClickListener(this);
        lstContatos.setOnItemClickListener(this);

        ConexaoBD();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null)
            conn.close();
    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent(this, ActCadContato.class);
        startActivityForResult(it, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adpContatos = repositorioContato.buscaContatos(this);
        filtradados.setArrayAdapter(adpContatos);
        lstContatos.setAdapter(adpContatos);
    }

    private void ConexaoBD() {
        try
        {
            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();
            repositorioContato = new RepositorioContato(conn);
            adpContatos = repositorioContato.buscaContatos(this);

            lstContatos.setAdapter(adpContatos);

            filtradados = new FiltraDados(adpContatos);
            edtPesquisa.addTextChangedListener(filtradados);
        }
        catch (SQLException ex)
        {
            MessageBox.Show(this, "Erro", "Erro ao criar o banco: " + ex.getMessage());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Contato contato = adpContatos.getItem(position);
        Intent it = new Intent(this, ActCadContato.class);
        it.putExtra(PAR_CONTATO, contato);
        startActivityForResult(it, 0);
    }
}
