package br.danieltiburciosf.rankingfutebol;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Daniel on 22/06/2016.
 */
public class Invictos extends AppCompatActivity
{
    private AdView adViewi;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invictos);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewi = findViewById(R.id.adViewi);
        adViewi.loadAd(adRequest);

        @SuppressWarnings({"unchecked"}) final
        ArrayList<Resultado>
                mLista = (ArrayList<br.danieltiburciosf.rankingfutebol.Resultado>)
                getIntent().getSerializableExtra("mLista");

        String linha;
        Global.escolha = mLista.get(1).getCampos().trim();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);
        actionBar.setTitle(mLista.get(0).getCampos());

        TextView titulInvi = findViewById(R.id.titulInvi);
        titulInvi.setText(Global.escolha);
        ArrayList<Invi> invict = new ArrayList<>();

        for (int i = 2; i < mLista.size(); i++)
        {
            Global.anos1 = "0000";
            linha = mLista.get(i).getCampos();

            Global.clube = mLista.get(i).getClube();
            for (int j = 0; j < Global.clube.length(); j++)
            {
                if (Global.clube.substring(j, j + 1).equals("/"))
                {
                    Global.estado = Global.clube.substring(j+1, Global.clube.length());
                    Global.clube = Global.clube.substring(0, j);
                    break;
                }
            }

            Invi invi = new Invi();
            if (Global.escolha.contains("Invi"))
            {
                invi.setFigu("cl" + linha.substring(19, 25).trim());
                invi.setAno(linha.substring(0, 4).trim());
                invi.setNome(mLista.get(i).getClube());
                invi.setNume(linha.substring(9, 15).trim() + "º lugar");
            }
            else if (Global.escolha.contains("Mai"))
            {
                invi.setFigu("cl" + linha.substring(9, 15).trim());
                invi.setAno(linha.substring(19, linha.length()));
                invi.setNome(mLista.get(i).getClube());
                invi.setNume(linha.substring(0, 4).trim());
            }
            else
            {
                invi.setFigu("cl" + linha.substring(9, 15).trim());
                invi.setAno(linha.substring(0, 4).trim());
                invi.setNome(linha.substring(19, linha.length()));
                invi.setNume(mLista.get(i).getClube());
            }

            invict.add(invi);
        }

        Global.anos1 = "0000";

        ListView lvInvi = findViewById(R.id.lvInvi);
        lvInvi.setAdapter(new InvAdapter(this, invict));
        lvInvi.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String clube = mLista.get(position + 2).getClube();
                //String sigla = mLista.get(0).getCampos();
                busca_clubei(clube); //, sigla);
            }
        });
    }

    public void busca_clubei(String clube) //, String sigla)
    {
        String estado = "";

        Boolean numerico;
        String pridig = clube.substring(0, 1);
        try
        {
            Integer.parseInt(pridig);
            numerico = true;
        }
        catch (NumberFormatException e)
        {
            numerico = false;
        }

        if (numerico)
        {
            for (int j = 0; j < clube.length(); j++)
            {
                if (clube.substring(j, j + 1).equals(" "))
                {
                    clube = clube.substring(j + 1, clube.length());
                    break;
                }
            }
        }

        for (int j = 0; j < clube.length(); j++)
        {
            if (clube.substring(j, j + 1).equals("/"))
            {
                estado = clube.substring(j + 1, clube.length());
                clube = clube.substring(0, j);
                break;
            }
        }

        Global.clube = clube;

        String url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=14&p=" + Global.sigla + ";" +
                clube + ";" + estado;
        new Busca_clube(this).execute(url, null, null);
    }

    public void onResume()
    {
        super.onResume();
    }

    @Override protected void onPause()
    {
        adViewi.pause();
        super.onPause();
    }

    @Override protected void onDestroy()
    {
        adViewi.destroy();
        super.onDestroy();
    }
}
