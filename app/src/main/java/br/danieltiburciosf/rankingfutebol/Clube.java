package br.danieltiburciosf.rankingfutebol;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.CountDownTimer;
/**
 * Created by Daniel on 23/06/2016.
 */
public class Clube extends AppCompatActivity
{
    private AdView adViewc;
    String linha;
    String classi;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clube);


        AdRequest adRequest = new AdRequest.Builder().build();
        adViewc = findViewById(R.id.adViewc);
        adViewc.loadAd(adRequest);

        @SuppressWarnings({"unchecked"}) final
        ArrayList<Resultado>
                mLista = (ArrayList<br.danieltiburciosf.rankingfutebol.Resultado>)
                getIntent().getSerializableExtra("mLista");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);
        actionBar.setTitle(mLista.get(0).getCampos());

        TextView clunome = findViewById(R.id.clunome);
        clunome.setText(Global.clube);

        ImageView escudoc = findViewById(R.id.escudoc);
        try
        {
            int result = R.drawable.class.getField("cl" + mLista.get(2).getCampos().substring(0, 4).trim()).getInt(null);
            escudoc.setImageResource(result);
        }
        catch (Exception e)
        {
            try
            {
                String URL = "http://www.danieltiburcio.com.br/ranking/cl" +
                             mLista.get(2).getCampos().substring(0, 4).trim() + ".jpg";

                LoadImageTask task = new LoadImageTask(escudoc);
                task.execute(URL);
            }
            catch (Exception p)
            {
                escudoc.setImageResource(0);
            }
        }

        linha = "";
        int linhas = 0;
        int[] totais = {0, 0, 0, 0, 0, 0, 0, 0};

        ArrayList<Club> clubes = new ArrayList<>();

        int totpon = 0;

        for (int i = 2; i < mLista.size(); i++)
        {
            linha = mLista.get(i).getCampos();
            classi = linha.substring(19, 25).trim() + "º";

            /*String url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=21&p=" + Global.torneio + ';' +
                         linha.substring(11, 15).trim();
            new Busca_numero().execute(url, null, null);

            classi = classi + " de " + Global.anopp;

            /*SQL = "select count(*) as numero " +
                  "from PONTOS a, TORNEIOS b " +
                  "where b.tor_descri = '" + Global.torneio + "'" +
                  "      and a.tor_codigo = b.tor_codigo " +
                  "      and a.pon_ano = " + linha.substring(11, 15).trim();


            try
            {
                cursor = BD.rawQuery(SQL, null);
                if (cursor.moveToFirst())
                {
                    classi = classi + " de " + cursor.getString(0);
                }
                cursor.close();
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }*/

            Club clube = new Club();
            clube.setClac(classi);
            clube.setAnoc(linha.substring(11, 15));
            int ano = Integer.valueOf(linha.substring(11, 15).trim());

            String tipo;

            for (int k = 25; k < linha.length(); k = k + 10)
            {
                tipo = linha.substring(k, k + 3);

                switch (tipo)
                {
                    case "PON":
                    {
                        clube.setPontc(linha.substring(k + 4, k + 10).trim());
                        totais[0] = totais[0] + Integer.valueOf(linha.substring(k + 4, k + 10).trim());
                        break;
                    }
                    case "JOG":
                    {
                        clube.setJogoc(linha.substring(k + 4, k + 10).trim());
                        totais[1] = totais[1] + Integer.valueOf(linha.substring(k + 4, k + 10).trim());
                        if (ano < 1995)
                        {
                            totpon = totpon +  Integer.valueOf(linha.substring(k + 4, k + 10).trim()) * 2;
                        }
                        else
                        {
                            totpon = totpon +  Integer.valueOf(linha.substring(k + 4, k + 10).trim()) * 3;
                        }
                        break;
                    }
                    case "VIT":
                    {
                        clube.setVitoc(linha.substring(k + 4, k + 10).trim());
                        totais[2] = totais[2] + Integer.valueOf(linha.substring(k + 4, k + 10).trim());
                        break;
                    }
                    case "EMP":
                    {
                        clube.setEmpac(linha.substring(k + 4, k + 10).trim());
                        totais[3] = totais[3] + Integer.valueOf(linha.substring(k + 4, k + 10).trim());
                        break;
                    }
                    case "DER":
                    {
                        clube.setDerrc(linha.substring(k + 4, k + 10).trim());
                        totais[4] = totais[4] + Integer.valueOf(linha.substring(k + 4, k + 10).trim());
                        break;
                    }
                    case "GPR":
                    {
                        clube.setGolpc(linha.substring(k + 4, k + 10).trim());
                        totais[5] = totais[5] + Integer.valueOf(linha.substring(k + 4, k + 10).trim());
                        break;
                    }
                    case "GCO":
                    {
                        clube.setGolcc(linha.substring(k + 4, k + 10).trim());
                        totais[6] = totais[6] + Integer.valueOf(linha.substring(k + 4, k + 10).trim());
                        break;
                    }
                    case "SAL":
                    {
                        clube.setSaldc(linha.substring(k + 4, k + 10).trim());
                        totais[7] = totais[7] + Integer.valueOf(linha.substring(k + 4, k + 10).trim());
                        break;
                    }
                    case "APR":
                    {
                        clube.setAproc(linha.substring(k + 4, k + 10).trim() + "%");
                        break;
                    }
                    case "OBS":
                    {
                        clube.setObse(linha.substring(k + 4, k + 44).trim());
                        k = k + 34;
                        break;
                    }
                    case "ART":
                    {
                        clube.setArti(linha.substring(k + 4, linha.length()).trim());
                        k = linha.length();
                        break;
                    }
                }

            }

            clubes.add(clube);
            linhas++;
        }

        if (linhas > 1)
        {
            Club clube = new Club();
            clube.setAnoc(linhas + " campanhas");
            clube.setClac("----");
            clube.setPontc(String.valueOf(totais[0]));
            clube.setJogoc(String.valueOf(totais[1]));
            clube.setVitoc(String.valueOf(totais[2]));
            clube.setEmpac(String.valueOf(totais[3]));
            clube.setDerrc(String.valueOf(totais[4]));
            clube.setGolpc(String.valueOf(totais[5]));
            clube.setGolcc(String.valueOf(totais[6]));
            clube.setSaldc(String.valueOf(totais[7]));

            int aprovei = totais[0] * 100;
            aprovei = aprovei / totpon;
            clube.setAproc(String.valueOf(aprovei) + "%");
            clubes.add(clube);
        }

        ListView lvc = findViewById(R.id.lvc);
        lvc.setAdapter(new ClubeAdapter(this, clubes));
        lvc.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Global.anos1 = mLista.get(position + 2).getCampos().substring(11, 15).trim();
                Global.anos2 = Global.anos1;
                String url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=1&p=" + Global.sigla + ";" +
                             Global.anos1 + ";" + Global.anos2;
                Global.escolha = "Pontuação de " + Global.anos1;
                new Busca_camp(getApplicationContext()).execute(url, null, null);
            }
        });
    }

    public class Busca_numero extends AsyncTask<String, Context, String>
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
                Log.i("Json recebido", result);
                JSONArray jsonResponse = new JSONArray(result);
                JSONObject jsonChildNode = jsonResponse.getJSONObject(0);
                Global.anopp = jsonChildNode.getString(jsonChildNode.names().getString(0));
                Log.i("Número de clubes", Global.anopp);
                /*new CountDownTimer(5000, 1000)
                {
                    public void onTick(long millisUntilFinished)
                    {
                        Log.i("seconds", "remaining: " + millisUntilFinished / 1000);
                    }

                    public void onFinish()
                    {
                        Log.i("Feito","done!");
                    }
                }.start();*/
            }
            catch (Exception el)
            {
                el.printStackTrace();
            }
        }
    }

    public void onResume()
    {
        super.onResume();
    }

    @Override protected void onPause()
    {
        adViewc.pause();
        super.onPause();
    }

    @Override protected void onDestroy()
    {
        adViewc.destroy();
        super.onDestroy();
    }
}
