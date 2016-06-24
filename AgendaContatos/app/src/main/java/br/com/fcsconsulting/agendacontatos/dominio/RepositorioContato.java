package br.com.fcsconsulting.agendacontatos.dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.*;
import android.database.sqlite.*;
import android.widget.*;

import java.util.Date;

import br.com.fcsconsulting.agendacontatos.ContatoArrayAdapter;
import br.com.fcsconsulting.agendacontatos.dominio.entidades.Contato;
import br.com.fcsconsulting.agendacontatos.R;

/**
 * Created by FCSantos on 24/01/2016.
 */

public class RepositorioContato {

    private SQLiteDatabase conn;

    public RepositorioContato(SQLiteDatabase conn) { this.conn = conn; }

    public void inserir(Contato contato)
    {
        conn.insertOrThrow(contato.TABELA, null, preencheContentValues(contato));
    }

    public void alterar(Contato contato)
    {
        conn.update(contato.TABELA, preencheContentValues(contato), " _id = ? ", new String[]{String.valueOf(contato.getId())});
    }

    public void excluir(long id)
    {
        conn.delete(Contato.TABELA, " _id = ? ", new String[]{String.valueOf(id)});
    }

    private ContentValues preencheContentValues(Contato contato)
    {
        ContentValues values = new ContentValues();

        values.put(contato.NOME, contato.getNome());
        values.put(contato.TELEFONE, contato.getTelefone());
        values.put(contato.TIPOTELEFONE, contato.getTipoEndereco());
        values.put(contato.EMAIL, contato.getEmail());
        values.put(contato.TIPOEMAIL, contato.getTipoEmail());
        values.put(contato.ENDERECO, contato.getEndereco());
        values.put(contato.TIPOENDERECO, contato.getTipoEndereco());
        values.put(contato.DATASESPECIAIS, contato.getDatasEspeciais().getTime());
        values.put(contato.TIPODATASESPECIAIS, contato.getTipoDatasEspeciais());
        values.put(contato.GRUPOS, contato.getGrupos());
        return values;
    }

    public ContatoArrayAdapter buscaContatos(Context context)
    {
        ContatoArrayAdapter adpContatos = new ContatoArrayAdapter(context, R.layout.item_contato);
        Cursor cursor = conn.query(Contato.TABELA, null, null, null, null, null, null);
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                Contato contato = new Contato();
                contato.setId(cursor.getLong(cursor.getColumnIndex(Contato.ID)));
                contato.setNome(cursor.getString(cursor.getColumnIndex(Contato.NOME)));
                contato.setTelefone(cursor.getString(cursor.getColumnIndex(Contato.TELEFONE)));
                contato.setTipoTelefone(cursor.getString(cursor.getColumnIndex(Contato.TIPOTELEFONE)));
                contato.setEmail(cursor.getString(cursor.getColumnIndex(Contato.EMAIL)));
                contato.setTipoEmail(cursor.getString(cursor.getColumnIndex(Contato.TIPOEMAIL)));
                contato.setEndereco(cursor.getString(cursor.getColumnIndex(Contato.ENDERECO)));
                contato.setTipoEndereco(cursor.getString(cursor.getColumnIndex(Contato.TIPOENDERECO)));
                contato.setDatasEspeciais(new Date(cursor.getLong(cursor.getColumnIndex(Contato.DATASESPECIAIS))));
                contato.setTipoDatasEspeciais(cursor.getString(cursor.getColumnIndex(Contato.TIPODATASESPECIAIS)));
                contato.setGrupos(cursor.getString(cursor.getColumnIndex(Contato.GRUPOS)));
                adpContatos.add(contato);
            }while (cursor.moveToNext());
        }
        return adpContatos;
    }
}
