package br.danieltiburciosf.rankingfutebol;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Daniel on 20/10/2017.
 */
public class GolAdapter extends BaseAdapter
{

    private Context context;
    private ArrayList<Gol> lista;

    public GolAdapter(Context context, ArrayList<Gol> lista)
    {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount()
    {
        return lista.size();
    }

    @Override
    public Object getItem(int position)
    {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Gol Gols = lista.get(position);

        View layout;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.gol, null);
        }
        else
        {
            layout = convertView;
        }

        TextView anog = layout.findViewById(R.id.anog);
        anog.setText(Gols.getAnog());

        TextView golsg = layout.findViewById(R.id.golsg);
        golsg.setText(Gols.getGols());

        TextView jogosg = layout.findViewById(R.id.jogosg);
        jogosg.setText(Gols.getJogos());

        Double mediagol = (Double.valueOf(Gols.getGols()) / Double.valueOf(Gols.getJogos()));
        TextView mediag = layout.findViewById(R.id.mediag);

        DecimalFormat df = new DecimalFormat("#,##0.000");
        mediag.setText(df.format(mediagol));

        if (Global.escolha.equals("Gols por Ano"))//if (Global.coluna == 0)
        {
            golsg.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.Black));
            golsg.setBackgroundColor(ContextCompat.getColor(context.getApplicationContext(), R.color.MediumSeaGreen));
        }
        else if (Global.escolha.equals("Mais Gols por Ano"))
        {
            jogosg.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.Black));
            jogosg.setBackgroundColor(ContextCompat.getColor(context.getApplicationContext(), R.color.MediumSeaGreen));
        }
        else
        {
            mediag.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.Black));
            mediag.setBackgroundColor(ContextCompat.getColor(context.getApplicationContext(), R.color.MediumSeaGreen));
        }

        return layout;
    }
}
