package br.danieltiburciosf.rankingfutebol;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.view.MenuInflater;
import android.net.Uri;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import org.json.JSONArray;
import org.json.JSONObject;

public class Principal extends AppCompatActivity
{
    Spinner ano1;
    Spinner ano2;
    Spinner clube;
    RadioGroup opcoes1;
    RadioButton opc_pont;
    RadioButton opc_ataq;
    RadioButton opc_sald;
    RadioButton opc_vito;
    RadioButton opc_apro;
    RadioButton opc_esta;
    RadioButton opc_rank;
    RadioButton opc_camp;
    RadioButton opc_camest;
    RadioButton opc_mcam;
    RadioButton opc_part;
    RadioButton opc_invi;
    RadioButton opc_arti;
    RadioButton opc_mart;
    RadioButton opc_gola;
    RadioButton opc_golma;
    RadioButton opc_golme;
    RadioButton opc_club;
    LinearLayout ll;
    TextView labclube;

    AutoCompleteTextView pesquisa;
    int tela = 0;
    boolean isFromClearCheck;
    private AdView adView;
    private InterstitialAd interstitial;
    private static final String AD_UNIT_ID = "ca-app-pub-7390880956140424/5525994795";

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.pesquisar:
            {
                Pesquisas(this);
                return true;
            }
            case R.id.avaliar:
            {
                Uri url = Uri.parse("https://play.google.com/store/apps/details?id=br.danieltiburciosf.rankingfutebol&feature=search_result#?t=W251bGwsMSwxLDEsImJyLmlkZWlhaW5mb3JtYXRpY2EuaWRlcGFncHJvZiJd");
                Intent it = new Intent(Intent.ACTION_VIEW, url);
                startActivity(it);
                return true;
            }
            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opcoes);// opcxx);

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(AD_UNIT_ID);
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);
        adView = findViewById(R.id.adView);
        adView.loadAd(adRequest);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);

        actionBar.setTitle(Global.torneio);

        ano1 = findViewById(R.id.ano1);
        ano2 = findViewById(R.id.ano2);
        opcoes1 = findViewById(R.id.opcoes1);
        clube = findViewById(R.id.clube);
        labclube = findViewById(R.id.labclube);
        pesquisa = findViewById(R.id.pesquisa);
        ll = findViewById(R.id.ll);
        opc_pont = findViewById(R.id.opc_pont);
        opc_ataq = findViewById(R.id.opc_ataq);
        opc_sald = findViewById(R.id.opc_sald);
        opc_vito = findViewById(R.id.opc_vito);
        opc_apro = findViewById(R.id.opc_apro);
        opc_esta = findViewById(R.id.opc_esta);
        opc_rank = findViewById(R.id.opc_rank);
        opc_camp = findViewById(R.id.opc_camp);
        opc_camest = findViewById(R.id.opc_camest);
        opc_mcam = findViewById(R.id.opc_mcam);
        opc_part = findViewById(R.id.opc_part);
        opc_invi = findViewById(R.id.opc_invi);
        opc_arti = findViewById(R.id.opc_arti);
        opc_mart = findViewById(R.id.opc_mart);
        opc_gola = findViewById(R.id.opc_gola);
        opc_golma = findViewById(R.id.opc_golma);
        opc_golme = findViewById(R.id.opc_golme);
        opc_club = findViewById(R.id.opc_club);
        opcoes1.clearCheck();
        opc_pont.setChecked(true);

        opcoes1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (isFromClearCheck)
                {
                    isFromClearCheck = false;
                }

                labclube.setVisibility(View.INVISIBLE);
                clube.setVisibility(View.INVISIBLE);
                pesquisa.setVisibility(View.INVISIBLE);
                if ((checkedId == R.id.opc_camp) | (checkedId == R.id.opc_invi) |
                    (checkedId == R.id.opc_arti) | (checkedId == R.id.opc_mart) |
                    (checkedId == R.id.opc_gola) | (checkedId == R.id.opc_golma) |
                    (checkedId == R.id.opc_golme) |(checkedId == R.id.opc_club))
                {
                    ll.setVisibility(View.GONE);
                    if (checkedId == R.id.opc_club)
                    {
                        labclube.setVisibility(View.VISIBLE);
                        clube.setVisibility(View.VISIBLE);
                        pesquisa.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    ll.setVisibility(View.VISIBLE);
                }
        }});

        String url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=17&p=" + Global.sigla;
        new Busca_ano(this).execute(url, null, null);
    }

    public void Pesquisas(Context context)
    {
        Global.escolha = "";
        Global.anos1 = ano1.getSelectedItem().toString();
        Global.anos2 = ano2.getSelectedItem().toString();
        String url;
        tela = 0;
        if (opc_pont.isChecked()) // Pontuação
          {
            tela = 1;
            Global.escolha = "Pontuação" + " de " + Global.anos1;
            if (Global.anos1.equals(Global.anos2))
            {
                url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=1&p=" + Global.sigla + ";" +
                        Global.anos1 + ";" + Global.anos2;
            }
            else
            {
                Global.escolha = Global.escolha + " a " + Global.anos2;
                url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=0&p=" + Global.sigla + ";" +
                                                                Global.anos1 + ";" + Global.anos2 +"&o=0";
            }
          }

         else if (opc_ataq.isChecked()) // Melhor ataque
           {
             tela = 1;
             Global.escolha = "Melhor Ataque";
               url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=0&p=" + Global.sigla + ";" +
                       Global.anos1 + ";" + Global.anos2 +"&o=1";
           }
         else if (opc_sald.isChecked()) // Melhor saldo de gols
          {
            tela = 1;
            Global.escolha = "Melhor Saldo de Gols";
              url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=0&p=" + Global.sigla + ";" +
                      Global.anos1 + ";" + Global.anos2 +"&o=2";
          }
        else if (opc_vito.isChecked()) // Mais Vitórias
        {
            tela = 1;
            Global.escolha = "Mais Vitórias";
            url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=0&p=" + Global.sigla + ";" +
                    Global.anos1 + ";" + Global.anos2 +"&o=3";
        }
        else if (opc_apro.isChecked()) // Aproveitamento
        {
            tela = 1;
            Global.escolha = "Aproveitamento";
            url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=0&p=" + Global.sigla + ";" +
                    Global.anos1 + ";" + Global.anos2 +"&o=4";
        }
        else if (opc_esta.isChecked()) // Estado/País
        {
            tela = 1;
            Global.escolha = "Estado/País";
            url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=2&p=" + Global.sigla + ";" +
                    Global.anos1 + ";" + Global.anos2;
        }
        else if (opc_rank.isChecked()) // Ranking 1º/10º
           {
               tela = 2;
               Global.escolha = "Ranking 1º ao 10º";
               url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=3&p=" + Global.sigla + ";" +
                       Global.anos1 + ";" + Global.anos2;
           }

        else if (opc_camp.isChecked()) // Campeões
           {
               tela = 1;
               Global.escolha = "Campeões";
               url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=4&p=" + Global.sigla;
           }


        else if (opc_mcam.isChecked()) // Maiores Campeões
        {
            /*
            select clu_nome, clu_estado, a.clu_ordem, count(*) as pon_titulo, D.PON_ANO
                  from PONTOS a, CLUBES b
INNER JOIN PONTOS AS D ON A.CLU_ORDEM = D.CLU_ORDEM AND D.PON_ANO = A.PON_ANO
       AND D.PON_CLASSI = 1 AND D.pon_ano between 1930 and 2018

                  where a.tor_codigo = 'COPAMU' and
                        a.clu_ordem = b.clu_ordem and
                        A.pon_classi = 1 and
                        A.pon_ano between 1930 and 2018

group by clu_nome, clu_estado, a.clu_ordem, D.PON_ANO
order by pon_titulo desc, clu_nome
             */
            tela = 2;
            Global.escolha = "Maiores Campeões";
            url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=5&p=" + Global.sigla + ";" +
                    Global.anos1 + ";" + Global.anos2;
        }

        else if (opc_camest.isChecked()) // Títulos por Estado/País
        {
            tela = 2;
            Global.escolha = "Títulos por Estado/País";
            url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=6&p=" + Global.sigla + ";" +
                    Global.anos1 + ";" + Global.anos2;
        }

        else if (opc_part.isChecked()) // Mais Participações
        {
            tela = 2;
            Global.escolha = "Mais Participações";
            url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=7&p=" + Global.sigla + ";" +
                    Global.anos1 + ";" + Global.anos2;
        }

        else if (opc_invi.isChecked()) // Invictos
        {
            tela = 3;
            Global.escolha = "Invictos";
            url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=8&p=" + Global.sigla;
        }

        else if (opc_arti.isChecked()) // Artilheiros
        {
            tela = 3;
            Global.escolha = "Artilheiros";
            url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=9&p=" + Global.sigla;
        }

        else if (opc_mart.isChecked()) // Maiores Artilheiros
        {
            tela = 3;
            Global.escolha = "Maiores Artilheiros";
            url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=10&p=" + Global.sigla;
        }

        else if ((opc_gola.isChecked()) | (opc_golma.isChecked()) | (opc_golme.isChecked())) // Gols
        {
            tela = 4;
            if (opc_gola.isChecked())
             {
                 Global.escolha = "Gols por Ano";
                 url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=11&p=" + Global.sigla;
             }
            else if (opc_golma.isChecked())
             {
                 Global.escolha = "Mais Gols por Ano";
                 url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=12&p=" + Global.sigla;
             }
            else
             {
                 Global.escolha = "Média de Gols por Ano";
                 url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=13&p=" + Global.sigla;
             }
        }

        else if (opc_club.isChecked()) // Clube
           {
               String nomclu;
               String estado = "";
               nomclu = clube.getSelectedItem().toString();
               Global.clube = nomclu;
               for (int k = nomclu.length() - 1; k > 1; k--)
               {
                   if (nomclu.substring(k, k+1).equals("/"))
                   {
                       estado = nomclu.substring(k + 1, nomclu.length());
                       nomclu = nomclu.substring(0, k);
                       break;
                   }
               }
               Global.escolha = "Clube";
               Global.estado = estado;
               url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=14&p=" + Global.sigla + ";" +
                       nomclu + ";" + estado;
           }
        else
        {
            url = "";
        }

        if (!url.equals(""))
           {
               new Busca_resul(context).execute(url, null, null);
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
        adView.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        //Destruindo o AdView ao destruir a activity
        adView.destroy();
        super.onDestroy();
    }

    public class Busca_ano extends AsyncTask<String, Context, String>
    {
        private Context mContext;

        public Busca_ano(Context context)
        {
            super();
            mContext = context;
        }
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
            List<String> labels = new ArrayList<>();
            int num = 0;

            try
            {
                JSONArray jsonResponse = new JSONArray(result);
                for (int i = 0; i < jsonResponse.length(); i++)
                {
                    JSONObject jsonChildNode = jsonResponse.getJSONObject(i);
                    for (int j = 0; j < jsonChildNode.length(); j++)
                    {
                        num = num + 1;
                        labels.add(jsonChildNode.getString(jsonChildNode.names().getString(j)));
                    }
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext,
                    android.R.layout.simple_spinner_item, labels);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            ano1.setAdapter(dataAdapter);
            ano2.setAdapter(dataAdapter);
            ano1.setSelection(0, true);
            ano2.setSelection(num - 1, true);

            String url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=18&p=" + Global.sigla;
            new Busca_est(mContext).execute(url, null, null);
        }
    }

    public class Busca_est extends AsyncTask<String, Context, String>
    {
        private Context mContext;

        public Busca_est(Context context)
        {
            super();
            mContext = context;
        }
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
            int num = 0;
            try
            {
                JSONArray jsonResponse = new JSONArray(result);
                for (int i = 0; i < jsonResponse.length(); i++)
                {
                    num = num + 1;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            if (num > 1)
            {
                opc_esta.setVisibility(View.VISIBLE);
                opc_camest.setVisibility(View.VISIBLE);
            }
            else
            {
                opc_esta.setVisibility(View.GONE);
                opc_camest.setVisibility(View.GONE);
            }
            String url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=19&p=" + Global.sigla;
            new Busca_clutor(mContext).execute(url, null, null);
        }
    }

    public class Busca_clutor extends AsyncTask<String, Context, String>
    {
        private Context mContext;

        public Busca_clutor(Context context)
        {
            super();
            mContext = context;
        }
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
            List<String> labels = new ArrayList<>();
            String clubant = "?";
            try
            {
                JSONArray jsonResponse = new JSONArray(result);
                for (int i = 0; i < jsonResponse.length(); i++)
                {
                    JSONObject jsonChildNode = jsonResponse.getJSONObject(i);
                    String clube = jsonChildNode.getString(jsonChildNode.names().getString(0)) + "/" +
                            jsonChildNode.getString(jsonChildNode.names().getString(1));
                    if (!clube.equals(clubant))
                    {
                        labels.add(jsonChildNode.getString(jsonChildNode.names().getString(0)) + "/" +
                                jsonChildNode.getString(jsonChildNode.names().getString(1)));
                        clubant = clube;
                    }

                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext,
                    android.R.layout.simple_spinner_item, labels);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            clube.setAdapter(dataAdapter);
            AutoCompleteTextView autoCompleteTextView = findViewById(R.id.pesquisa);
            autoCompleteTextView.setAdapter(dataAdapter);
            clube.setSelection(0, true);
        }
    }

    public class Busca_resul extends AsyncTask<String, Context, String>
    {
        private Context mContext;

        public Busca_resul(Context context)
        {
            super();
            mContext = context;
        }
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
            Bundle args = new Bundle();

            ArrayList<Resultado> mLista = new ArrayList<>();

            String opc_ataq = Global.escolha;
            if (!Global.escolha.equals("Clube") &
                !Global.escolha.equals("Campeões") &
                !Global.escolha.equals("Invictos") &
                !Global.escolha.equals("Artilheiros") &
                !Global.escolha.contains("Gols por") &
                !Global.escolha.equals("Maiores Artilheiros") &
                !Global.escolha.equals("Mais Participações"))
            {
                opc_ataq = opc_ataq + " de " + ano1.getSelectedItem().toString();
                if (!ano1.getSelectedItem().toString().equals(ano2.getSelectedItem().toString()))
                {
                    opc_ataq = opc_ataq + " a " + ano2.getSelectedItem().toString();
                }
            }

            Resultado linha = new Resultado(Global.sigla, Global.torneio);
            mLista.add(linha);

            linha = new Resultado(Global.escolha, opc_ataq);
            String clube;
            String campox;
            String linha2;
            int num = 0;
            mLista.add(linha);

            try
            {
                JSONArray jsonResponse = new JSONArray(result);

                if (jsonResponse.length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Opção sem dados a apresentar",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                for (int i = 0; i < jsonResponse.length(); i++)
                {
                    JSONObject jsonChildNode = jsonResponse.getJSONObject(i);

                    num = num + 1;
                    if (Global.escolha.contains("Gols p"))
                    {
                        linha2 = jsonChildNode.getString(jsonChildNode.names().getString(0));
                    }
                    else
                    {
                        linha2 = jsonChildNode.getString(jsonChildNode.names().getString(2));
                    }

                    if (linha2.substring(0, 1).equals("0"))
                    {
                        linha2 = jsonChildNode.getString(jsonChildNode.names().getString(1));
                    }
                    while (linha2.length() < 5)
                    {
                        linha2 = linha2 + " ";
                    }
                    if (Global.escolha.equals("Campeões") |
                            Global.escolha.equals("Maiores Campeões") |
                            Global.escolha.equals("Artilheiros") |
                            Global.escolha.equals("Invictos") |
                            Global.escolha.equals("Maiores Artilheiros") |
                            Global.escolha.equals("Clube") |
                            Global.escolha.contains("Gols p") |
                            Global.escolha.equals("Mais Participações"))
                    {
                        clube = "";
                    }
                    else
                    {
                        clube = String.valueOf(num) + "º ";
                    }
                    clube = clube + jsonChildNode.getString(jsonChildNode.names().getString(0)) + "/" +
                                    jsonChildNode.getString(jsonChildNode.names().getString(1));
                    String tiporeg;
                    int j;
                    if ((Global.escolha.contains("Gols p")) | (Global.escolha.contains("Tít")))
                    {
                        j = 1;
                    }
                    else
                    {
                        j = 3;
                    }
                    for (int k = j; k < jsonChildNode.length(); k++)
                    {
                        tiporeg = jsonChildNode.names().getString(k).substring(4, 7);
                        campox = jsonChildNode.getString(jsonChildNode.names().getString(k)).trim();
                        if (tiporeg.equals("gol"))
                        {
                            tiporeg = "g" + jsonChildNode.names().getString(k).substring(7, 9);
                        }

                        linha2 = linha2 + tiporeg.toUpperCase() + "=";

                        if (tiporeg.equals("cla") & (Global.escolha.contains("Pont")) &
                                (Global.anos1.equals(Global.anos2)))
                        {
                            clube = campox + "º " +
                                    jsonChildNode.getString(jsonChildNode.names().getString(0)) + "/" +
                                    jsonChildNode.getString(jsonChildNode.names().getString(1));
                        }

                        if (campox.length() < 6)
                        {
                            do
                            {
                                campox = " " + campox;
                            } while (campox.length() < 6);
                        }
                        if (tiporeg.equals("obs"))
                        {
                            if (campox.trim().equals("null"))
                            {
                                campox = " ";
                            }
                            do
                            {
                                campox = campox + " ";
                            } while (campox.length() < 40);
                        }
                        if (tiporeg.equals("art"))
                        {
                            if (campox.trim().equals("null"))
                            {
                                campox = " ";
                            }
                        }

                        linha2 = linha2 + campox;
                    }
                    linha = new Resultado(clube, linha2);
                    mLista.add(linha);
                }

                args.putSerializable("mLista", mLista);

                Intent i2;
                if (tela == 1)
                {
                    i2 = new Intent(mContext, Pontuacao.class);
                }
                else if (tela == 2)
                {
                    i2 = new Intent(mContext, Ranking.class);
                }
                else if (tela == 3)
                {
                    i2 = new Intent(mContext, Invictos.class);
                }
                else if (tela == 4)
                {
                    i2 = new Intent(mContext, Gols.class);
                }
                else
                {
                    i2 = new Intent(mContext, Clube.class);
                }
                i2.putExtras(args);
                startActivity(i2);
                }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }

}
