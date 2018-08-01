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
 * Created by Daniel on 08/06/2016.
 */

public class Pontuacao extends AppCompatActivity
{
    private AdView adViewp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pontuacao);

        AdRequest adRequest = new AdRequest.Builder().build();
        adViewp = findViewById(R.id.adViewp);
        adViewp.loadAd(adRequest);

        @SuppressWarnings({"unchecked"}) final
        ArrayList<Resultado>
                mLista = (ArrayList<br.danieltiburciosf.rankingfutebol.Resultado>)
                                                      getIntent().getSerializableExtra("mLista");

        String linha;
        Global.coluna = 20;
        if (Global.escolha.contains("Ataque"))
        {
            Global.coluna = 5;
        }
        else if (Global.escolha.contains("Saldo"))
        {
            Global.coluna = 7;
        }
        else if (Global.escolha.contains("Pontu"))
        {
            Global.coluna = 0;
        }
        else if (Global.escolha.contains("Mais V"))
        {
            Global.coluna = 2;
        }
        else if (Global.escolha.contains("Campeões"))
        {
            Global.coluna = 99;
        }
        else if (Global.escolha.contains("Aprov"))
        {
            Global.coluna = 8;
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);
        actionBar.setTitle(mLista.get(0).getCampos());

        ArrayList<Ponto> pontos = new ArrayList<>();

        TextView titul = findViewById(R.id.titul);
        titul.setText(Global.escolha);
        for (int i = 2; i < mLista.size(); i++)
        {
            linha = mLista.get(i).getCampos();
            Ponto ponto = new Ponto();

            Global.clube = mLista.get(i).getClube();
            ponto.setClub(Global.clube);
            Global.clube = Global.clube.substring(0, Global.clube.length() - 4);

            for (int j = 0; j < Global.clube.length(); j++)
            {
                if (Global.clube.substring(j, j + 1).equals(" "))
                {
                    Global.clube = Global.clube.substring(j + 1, Global.clube.length());
                    break;
                }
            }

            String tipo;
            Global.anopp = "";
            for (int k = 5; k < linha.length(); k = k + 10)
            {
                tipo = linha.substring(k, k + 3);
                if (tipo.equals("OBS"))
                {
                    break;
                }
                if (tipo.equals("ANO"))
                {
                    Global.anopp = linha.substring(k + 4, k + 10).trim();
                    break;
                }
            }

            ponto.setAnop(Global.anopp);

            if (Global.escolha.contains("Estado"))
            {
                ponto.setFigu("est_" + linha.substring(0, 4).trim());
            }
            else
            {
                ponto.setFigu("cl" + linha.substring(0, 4).trim());
            }

            for (int k = 5; k < linha.length(); k = k + 10)
            {
                tipo = linha.substring(k, k + 3);

                switch (tipo)
                    {
                        case "PON":
                        {
                            ponto.setPont(linha.substring(k + 4, k + 10).trim());
                            break;
                        }
                        case "JOG":
                        {
                            ponto.setJogo(linha.substring(k + 4, k + 10).trim());
                            break;
                        }
                        case "VIT":
                        {
                            ponto.setVito(linha.substring(k + 4, k + 10).trim());
                            break;
                        }
                        case "EMP":
                        {
                            ponto.setEmpa(linha.substring(k + 4, k + 10).trim());
                            break;
                        }
                        case "DER":
                        {
                            ponto.setDerr(linha.substring(k + 4, k + 10).trim());
                            break;
                        }
                        case "GPR":
                        {
                            ponto.setGolp(linha.substring(k + 4, k + 10).trim());
                            break;
                        }
                        case "GCO":
                        {
                            ponto.setGolc(linha.substring(k + 4, k + 10).trim());
                            break;
                        }
                        case "SAL":
                        {
                            ponto.setSald(linha.substring(k + 4, k + 10).trim());
                            break;
                        }
                        case "APR":
                        {
                            ponto.setApro(linha.substring(k + 4, k + 10).trim() + "%");
                            break;
                        }
                        case "OBS":
                        {
                            ponto.setObse(linha.substring(k + 4, k + 44).trim());
                            k = k + 34;
                            break;
                        }
                        case "ART":
                        {
                            ponto.setArti(linha.substring(k + 4, linha.length()).trim());
                            k = linha.length();
                            break;
                        }
                    }

            }

            pontos.add(ponto);
        }

        ListView lv = findViewById(R.id.lv);
        lv.setAdapter(new PontoAdapter(this, pontos));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (!Global.escolha.contains("Estado"))
                {
                    String clube = mLista.get(position + 2).getClube();
                    //String estado = mLista.get(0).getCampos();
                    busca_clube(clube); //, estado);
                }
            }
        });
    }

    public void busca_clube(String clube) //, String sigla)
    {
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
        String estado = "";
        for (int j = 0; j < clube.length(); j++)
        {
            if (clube.substring(j, j + 1).equals("/"))
            {
                estado = clube.substring(j+1, clube.length());
                clube = clube.substring(0, j);
                break;
            }
        }
        Global.clube = clube;
        Global.estado = estado;

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
        adViewp.pause();
        super.onPause();
    }

    @Override protected void onDestroy()
    {
        adViewp.destroy();
        super.onDestroy();
    }
}
