package br.danieltiburciosf.rankingfutebol;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Daniel on 22/06/2016.
 */
public class InvAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<Invi> lista;

    public InvAdapter(Context context, ArrayList<Invi> lista)
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
        Invi invs = lista.get(position);

        View layout;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.invs, null);
        }
        else
        {
            layout = convertView;
        }

        TextView figurinv = layout.findViewById(R.id.figurinv);
        figurinv.setText(invs.getFigu());
        Global.figu = invs.getFigu();
        TextView nomei = layout.findViewById(R.id.nomei);
        nomei.setText(invs.getNome());

        Global.anos2 = invs.getAno();
        TextView anoi = layout.findViewById(R.id.anoi);
        anoi.setText(Global.anos2);

        LinearLayout linhaano = layout.findViewById(R.id.linhaano);

        if ((Global.anos1.equals("0000")))// | (Global.anos1.equals(Global.anos2)))
        {
            linhaano.setVisibility(View.VISIBLE);
        }
        else if (!Global.anos1.equals(Global.anos2))
        {
            linhaano.setVisibility(View.VISIBLE);
        }
        else
        {
            linhaano.setVisibility(View.GONE);
        }
        Global.anos1 = Global.anos2;

        TextView numei = layout.findViewById(R.id.numei);
        numei.setText(invs.getNume());

        if (Global.escolha.contains("Maiores Art"))
        {
            anoi.setTextColor(Color.parseColor("#0000FF"));
        }
        else if (Global.escolha.equals(("Artilheiros")))
        {
            nomei.getLayoutParams().height = 40;
            numei.getLayoutParams().height = 40;
        }

        ImageView escudoi = layout.findViewById(R.id.escudoi);
        try
        {
            int result = R.drawable.class.getField(Global.figu).getInt(null);
            escudoi.setImageResource(result);
        }
        catch (Exception e)
        {
            String URL = "http://www.danieltiburcio.com.br/ranking/" + Global.figu + ".jpg";
            try
            {
                LoadImageTask task = new LoadImageTask(escudoi);
                task.execute(URL);
            }
            catch (Exception p)
            {
                escudoi.setImageResource(0);
            }
        }
        return layout;
    }

}


