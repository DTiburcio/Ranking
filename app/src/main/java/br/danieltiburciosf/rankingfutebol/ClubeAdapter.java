package br.danieltiburciosf.rankingfutebol;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Daniel on 24/06/2016.
 */
public class ClubeAdapter extends BaseAdapter
{

    private Context context;
    private ArrayList<Club> lista;

    public ClubeAdapter(Context context, ArrayList<Club> lista)
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
        Club clubes = lista.get(position);

        View layout;
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.clubes, null);
        }
        else
        {
            layout = convertView;
        }

        TextView classc = layout.findViewById(R.id.classc);
        classc.setText(clubes.getClac());

        TextView anoc =  layout.findViewById(R.id.anoc);
        anoc.setText(clubes.getAnoc());

        TextView pontoc =  layout.findViewById(R.id.pontoc);
        pontoc.setText(clubes.getPontoc());

        TextView jogo =  layout.findViewById(R.id.jogoc);
        jogo.setText(clubes.getJogoc());
        TextView vitoria =  layout.findViewById(R.id.vitoriac);
        vitoria.setText(clubes.getVitoc());

        TextView empate =  layout.findViewById(R.id.empatec);
        empate.setText(clubes.getEmpac());
        TextView derrota =  layout.findViewById(R.id.derrotac);
        derrota.setText(clubes.getDerrc());
        TextView golpro =  layout.findViewById(R.id.golproc);
        golpro.setText(clubes.getGolpc());

        TextView golcon =  layout.findViewById(R.id.golconc);
        golcon.setText(clubes.getGolcc());
        TextView saldo =  layout.findViewById(R.id.saldoc);
        saldo.setText(clubes.getSaldc());

        if (Double.valueOf(saldo.getText().toString()) < 0)
        {
            saldo.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.Red));
        }
        else
        {
            saldo.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.Blue));
        }

        TextView aprov =  layout.findViewById(R.id.aprovc);
        aprov.setText(clubes.getAproc());

        TextView observc =  layout.findViewById(R.id.observc);
        observc.setText(clubes.getObse());
        if (observc.getText().toString().trim().equals(""))
        {
            observc.setVisibility(View.GONE);
        }
        else
        {
            String obs = "(" + observc.getText().toString() + ")";
            observc.setText(obs);
            observc.setVisibility(View.VISIBLE);
        }

        TextView artilhec =  layout.findViewById(R.id.artilhec);
        artilhec.setText(clubes.getArti());
        if (artilhec.getText().toString().trim().equals(""))
        {
            artilhec.setVisibility(View.GONE);
        }
        else
        {
            String art = "Artilheiro(s): " + artilhec.getText().toString();
            artilhec.setText(art);
            artilhec.setVisibility(View.VISIBLE);
        }
        return layout;
    }

}

