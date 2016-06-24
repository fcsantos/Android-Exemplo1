package br.com.fcsconsulting.agendacontatos;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.fcsconsulting.agendacontatos.dominio.entidades.Contato;

/**
 * Created by FCSantos on 28/01/2016.
 */
public class ContatoArrayAdapter extends ArrayAdapter<Contato> {

    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public ContatoArrayAdapter(Context context, int resource)
    {
        super(context, resource);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = null;
        ViewHolder vh = null;
        if (convertView == null)
        {
            vh = new ViewHolder();
            view = inflater.inflate(resource, parent, false);

            vh.txtCor = (TextView)view.findViewById(R.id.txtCor);
            vh.txtNome = (TextView)view.findViewById(R.id.txtNome);
            vh.txtTelefone = (TextView)view.findViewById(R.id.txtTelefone);

            view.setTag(vh);

            convertView = view;
        }
        else
        {
            vh = (ViewHolder)convertView.getTag();
            view = convertView;
        }

        Contato contato = getItem(position);

        if (contato.getNome().startsWith("T"))
        {
            vh.txtCor.setBackgroundColor(context.getResources().getColor(R.color.amarelo));
        }
        else if (contato.getNome().startsWith("F")) {
            vh.txtCor.setBackgroundColor(context.getResources().getColor(R.color.vermelho));
        }
        else {
            vh.txtCor.setBackgroundColor(context.getResources().getColor(R.color.verde));
        }
        vh.txtNome.setText(contato.getNome());
        vh.txtTelefone.setText(contato.getTelefone());

        return view;
    }

    static class ViewHolder
    {
        TextView txtCor;
        TextView txtNome;
        TextView txtTelefone;
    }
}
