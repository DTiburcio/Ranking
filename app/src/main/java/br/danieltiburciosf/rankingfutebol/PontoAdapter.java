package br.danieltiburciosf.rankingfutebol;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Daniel on 08/06/2016.
 */
public class PontoAdapter extends BaseAdapter
{

    private Context context;
    private ArrayList<Ponto> lista;

    public PontoAdapter(Context context, ArrayList<Ponto> lista)
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
        Ponto pontos = lista.get(position);

        View layout;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.pontos, null);
        }
        else
        {
            layout = convertView;
        }

        TextView figurinha = (layout.findViewById(R.id.figurinha));
        figurinha.setText(pontos.getFigu());
        Global.figu = pontos.getFigu();
        TextView clubes = (layout.findViewById(R.id.clubes));
        clubes.setText(pontos.getClub());
        TextView ponto = (layout.findViewById(R.id.ponto));
        ponto.setText(pontos.getPont());
        if (Global.coluna == 0)
        {
            ponto.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.Black));
            ponto.setBackgroundColor(ContextCompat.getColor(context.getApplicationContext(), R.color.MediumSeaGreen));
        }
        if (Global.coluna == 99)
        {
            // Campeões = mover ano
            TextView anop = (layout.findViewById(R.id.anop));
            anop.setText(pontos.getAnop());
            LinearLayout linhano = layout.findViewById(R.id.linhano);
            linhano.setVisibility(View.VISIBLE);
        }
        TextView jogo = (layout.findViewById(R.id.jogo));
        jogo.setText(pontos.getJogo());
        TextView vitoria = (layout.findViewById(R.id.vitoria));
        vitoria.setText(pontos.getVito());
        if (Global.coluna == 2)
        {
            vitoria.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.Black));
            vitoria.setBackgroundColor(ContextCompat.getColor(context.getApplicationContext(), R.color.MediumSeaGreen));
        }
        TextView empate = (layout.findViewById(R.id.empate));
        empate.setText(pontos.getEmpa());
        TextView derrota = (layout.findViewById(R.id.derrota));
        derrota.setText(pontos.getDerr());
        TextView golpro = (layout.findViewById(R.id.golpro));
        golpro.setText(pontos.getGolp());
        if (Global.coluna == 5)
        {
            golpro.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.Black));
            golpro.setBackgroundColor(ContextCompat.getColor(context.getApplicationContext(), R.color.MediumSeaGreen));
        }
        TextView golcon = (layout.findViewById(R.id.golcon));
        golcon.setText(pontos.getGolc());
        TextView saldo = (layout.findViewById(R.id.saldo));
        saldo.setText(pontos.getSald());
        if (Global.coluna == 7)
        {
            saldo.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.Black));
            saldo.setBackgroundColor(ContextCompat.getColor(context.getApplicationContext(), R.color.MediumSeaGreen));
        }
        String xsaldo = saldo.getText().toString();
        if (xsaldo.equals(""))
        {
            saldo.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.Blue));
        }
        else if (Double.valueOf(saldo.getText().toString()) < 0)
        {
            saldo.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.Red));
        }
        else
        {
            saldo.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.Blue));
        }

        TextView aprov = (layout.findViewById(R.id.aprov));
        aprov.setText(pontos.getApro());
        if (Global.coluna == 8)
        {
            aprov.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.Black));
            aprov.setBackgroundColor(ContextCompat.getColor(context.getApplicationContext(), R.color.MediumSeaGreen));
        }

        TextView observ = (layout.findViewById(R.id.observ));
        observ.setText(pontos.getObse());
        if (observ.getText().toString().trim().equals(""))
        {
            observ.setVisibility(View.GONE);
        }
        else
        {
            observ.setVisibility(View.VISIBLE);
        }

        TextView artilhep = (layout.findViewById(R.id.artilhep));
        artilhep.setText(pontos.getArti());
        if (artilhep.getText().toString().trim().equals(""))
        {
            artilhep.setVisibility(View.GONE);
        }
        else
        {
            String art = "Artilheiro(s): " + artilhep.getText().toString();
            artilhep.setText(art);
            artilhep.setVisibility(View.VISIBLE);
        }

        ImageView escudo = layout.findViewById(R.id.escudo);
        try
        {
            int result = R.drawable.class.getField(Global.figu).getInt(null);
            escudo.setImageResource(result);
        }
        catch (Exception e)
        {
            String URL = "http://www.danieltiburcio.com.br/ranking/" + Global.figu + ".jpg";
            try
            {
                LoadImageTask task = new LoadImageTask(escudo);
                task.execute(URL);
            }
            catch (Exception p)
            {
                escudo.setImageResource(0);
            }
        }

        return layout;
    }

}
