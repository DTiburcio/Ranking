package br.danieltiburciosf.rankingfutebol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Daniel on 15/06/2016.
 */
public class RankAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<Rank> lista;

    public RankAdapter(Context context, ArrayList<Rank> lista)
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
        Rank ranks = lista.get(position);

        View layout;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.ranks, null);
        }
        else
        {
            layout = convertView;
        }

        TextView figurank = layout.findViewById(R.id.figurank);
        figurank.setText(ranks.getFigu());
        Global.figu = ranks.getFigu();
        TextView nomer = layout.findViewById(R.id.nomer);
        nomer.setText(ranks.getNome());

        TextView numer = layout.findViewById(R.id.numer);
        numer.setText(ranks.getNume());

        if (Global.escolha.equals(("Campeões")))
        {
            nomer.getLayoutParams().height = 40;
            numer.getLayoutParams().height = 40;
        }

        ImageView escudor = layout.findViewById(R.id.escudor);
        try
        {
            int result = R.drawable.class.getField(Global.figu).getInt(null);
            escudor.setImageResource(result);
        }
        catch (Exception e)
        {
            String URL = "http://www.danieltiburcio.com.br/ranking/" + Global.figu + ".jpg";

            try
            {
                LoadImageTask task = new LoadImageTask(escudor);
                task.execute(URL);
            }
            catch (Exception p)
            {
                escudor.setImageResource(0);
            }
        }

        return layout;
    }

}

