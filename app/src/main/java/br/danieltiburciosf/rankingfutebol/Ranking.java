package br.danieltiburciosf.rankingfutebol;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Daniel on 15/06/2016.
 */
public class Ranking extends AppCompatActivity
{
    @Nullable
    private AdView adViewr;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);

        AdRequest adRequest = new AdRequest.Builder().build();

        adViewr = findViewById(R.id.adViewr);
        adViewr.loadAd(adRequest);

        final
        ArrayList<Resultado>
                        mLista = (ArrayList<br.danieltiburciosf.rankingfutebol.Resultado>)
                                     getIntent().getSerializableExtra("mLista");

        String linha;
        Global.escolha = mLista.get(1).getCampos().trim();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);
        actionBar.setTitle(mLista.get(0).getCampos());

        TextView rodaper = findViewById(R.id.rodaper);
        TextView titulRank = findViewById(R.id.titulRank);
        titulRank.setText(Global.escolha);

        if (Global.escolha.contains("Rank"))
        {
            rodaper.setVisibility(View.VISIBLE);
        }
        else
        {
            rodaper.setVisibility(View.GONE);
        }
        ArrayList<Rank> ranks = new ArrayList<>();

        for (int i = 2; i < mLista.size(); i++)
        {
            linha = mLista.get(i).getCampos();
            Rank rank = new Rank();

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

            if (Global.escolha.contains("Tít"))
            {
                rank.setNome(Global.clube);
            }
            else
            {
                rank.setNome(mLista.get(i).getClube());
            }

            String nume;
            nume = linha.substring(9, 15).trim();
            if (Global.escolha.contains("Rank"))
            {
                rank.setFigu("cl" + linha.substring(0, 4).trim());
                if (Integer.valueOf(nume) > 1)
                {
                    nume = nume + " pontos";
                }
                else
                {
                    nume = nume + " ponto";
                }
            }
            else
            {
                if (Global.escolha.contains("Estado"))
                {
                    String pais = mLista.get(i).getClube();
                    String url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=20&p=" + pais;
                    new Busca_estado().execute(url, null, null);
                    rank.setFigu("est_" + Global.estado);
                    nume = linha.substring(0, 4).trim();
                }
                else
                {
                    rank.setFigu("cl" + linha.substring(0, 4).trim());
                }

                if (Global.escolha.contains("Maiores C"))
                {
                    /*String url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=22&p=" +
                                 Global.sigla + ';' + linha.substring(0, 4).trim() + ';' +
                                 Global.anos1 + ';' + Global.anos2;
                    Log.i("URL passada", url);
                    new Busca_titulo().execute(url, null, null);

                    nume = nume + ": " + Global.titulos; //.substring(0, Global.titulos.length() - 1);

                    /*String SQL2 = "select pon_ano " +
                                  "from PONTOS a, CLUBES b, TORNEIOS c " +
                                  "where a.clu_ordem = b.clu_ordem and" +
                                  "      a.tor_codigo = c.tor_codigo and" +
                                  "      tor_descri = '" + Global.torneio + "' and" +
                                  "      clu_nome = '" + Global.clube + "' and" +
                                  "      clu_estado = '" + Global.estado + "' and" +
                                  "      pon_classi = 1 and" +
                                  "      pon_ano between " + Global.anos1 + " and " +
                                                             Global.anos2 +
                                  " order by pon_ano desc";
                    Cursor cursor2 = BD.rawQuery(SQL2, null);
                    if (cursor2.moveToFirst())
                    {
                        int anos = 0;
                        do
                        {
                            if (anos < 9)
                            {
                                anos ++;
                                nume = nume + cursor2.getString(0).substring(2, 4) + '/';
                            }
                        } while (cursor2.moveToNext());
                        cursor2.close();
                    }*/
                    if (Integer.valueOf(nume) > 1)
                    {
                        nume = nume + " títulos";
                    }
                    else
                    {
                        nume = nume + " título";
                    }
                }
                else if (Global.escolha.contains("Estado"))
                    {
                        if (Integer.valueOf(nume) > 1)
                        {
                            nume = nume + " títulos";
                        }
                        else
                        {
                            nume = nume + " título";
                        }
                    }
                else
                    {
                        if (Integer.valueOf(nume) > 1)
                        {
                            nume = nume + " participações";
                        }
                        else
                        {
                            nume = nume + " participação";
                        }
                    }
            }
            rank.setNume(nume);

            ranks.add(rank);
        }
        ListView lvRank = findViewById(R.id.lvRank);
        lvRank.setAdapter(new RankAdapter(this, ranks));
        lvRank.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String clube = mLista.get(position + 2).getClube();
                busca_cluber(clube); //, sigla);
            }
        });
    }

    public void busca_cluber(String clube) //, String sigla)
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

    public class Busca_estado extends AsyncTask<String, Context, String>
    {
        @Override
        protected String doInBackground(String... url_atual)
        {
            HttpURLConnection conexao = null;
            try
            {
                URL url = new URL(url_atual[0]);
                BufferedReader bufferedReader = null;
                StringBuilder sb = new StringBuilder();
                try
                {
                    conexao = (HttpURLConnection) url.openConnection();
                    try
                    {
                        conexao.setRequestMethod("GET");
                        conexao.connect();
                        InputStream inputStream = conexao.getInputStream();
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    }
                    catch (Exception p)
                    {
                        p.printStackTrace();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                String json;

                while ((json = bufferedReader.readLine()) != null)
                {
                    sb.append(json);
                    sb.append("\n");
                }
                return sb.toString().trim();

            } catch (Exception e)
            {
                e.printStackTrace();
                if (conexao != null)
                {
                    conexao.disconnect();
                }

                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try
            {
                JSONArray jsonResponse = new JSONArray(result);
                JSONObject jsonChildNode = jsonResponse.getJSONObject(0);
                Global.estado = jsonChildNode.getString(jsonChildNode.names().getString(0));

                /*if (Global.escolha.contains("Maiores C"))
                {
                    String url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=22&p=" +
                            Global.sigla + ';' + Global.clube + ';' +
                            Global.anos1 + ';' + Global.anos2;
                    new Busca_titulo().execute(url, null, null);
                }*/

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public class Busca_titulo extends AsyncTask<String, Context, String>
    {
        @Override
        protected String doInBackground(String... url_atual)
        {

            Log.i("URL recebida", url_atual[0]);
            HttpURLConnection conexao = null;
            try
            {
                URL url = new URL(url_atual[0]);
                BufferedReader bufferedReader = null;
                StringBuilder sb = new StringBuilder();
                try
                {
                    conexao = (HttpURLConnection) url.openConnection();
                    try
                    {
                        conexao.setRequestMethod("GET");
                        conexao.connect();
                        InputStream inputStream = conexao.getInputStream();
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    }
                    catch (Exception p)
                    {
                        p.printStackTrace();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                String json;

                while ((json = bufferedReader.readLine()) != null)
                {
                    sb.append(json);
                    sb.append("\n");
                }
                Log.i("Json passado", sb.toString().trim());
                return sb.toString().trim();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                if (conexao != null)
                {
                    conexao.disconnect();
                }

                return null;
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                Log.i("Entrei", "onPostExecute");
                Log.i("Json recebido", result);
                JSONArray jsonResponse = new JSONArray(result);
                Global.titulos = "";
                for (int i = 0; i < jsonResponse.length(); i++)
                {
                    if (i < 9)
                    {
                        JSONObject jsonChildNode = jsonResponse.getJSONObject(i);
                        for (int j = 0; j < jsonChildNode.length(); j++)
                        {
                            Global.titulos = Global.titulos +
                                    jsonChildNode.getString(jsonChildNode.names().getString(j)) + ',';
                        }
                    }
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        //Pausando o AdView ao pausar a activity
        adViewr.pause();
        super.onPause();
    }

    @Override protected void onDestroy()
    {
        //Destruindo o AdView ao destruir a activity
        adViewr.destroy();
        super.onDestroy();
    }
}
