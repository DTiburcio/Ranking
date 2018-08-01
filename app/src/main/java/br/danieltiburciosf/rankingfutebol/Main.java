package br.danieltiburciosf.rankingfutebol;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Daniel on 19/10/2015.
 */
public class Main extends AppCompatActivity //implements TrendzListener
{
    //private FirebaseAnalytics mFirebaseAnalytics;
    private static final String AD_UNIT_ID = "ca-app-pub-7390880956140424/5525994795";
    ListView lista;
    private AdView adView;
    private InterstitialAd interstitial;
    String[] siglas = new String[200];

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true; //super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.avaliar:
            {
                Uri url = Uri.parse("https://play.google.com/store/apps/details?id=br.danieltiburciosf.rankingfutebol");
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

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (!isOnline())
        {
            android.widget.Toast.makeText(getApplicationContext(), "Conecte-se à Internet e acesse novamente",
                    android.widget.Toast.LENGTH_LONG).show();
            finish();
        }
        try
        {
            this.deleteDatabase("ranking.db");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Calendar hoje = Calendar.getInstance();
        hoje.add(Calendar.DAY_OF_MONTH, 1);

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(AD_UNIT_ID);
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);

        lista = findViewById(R.id.lista);
        adView = findViewById(R.id.adView);
        adView.loadAd(adRequest);

        MobileAds.initialize(this, AD_UNIT_ID);

        String url = "http://www.danieltiburcio.com.br/ranking/get_mysql.php?c=15";
        new Busca(this).execute(url, null, null);
    }

    public class Busca extends AsyncTask<String, Context, String>
    {
        private Context mContext;
        public Busca(Context context)
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

            try
            {
                JSONArray jsonResponse = new JSONArray(result);
                for (int i = 0; i < jsonResponse.length(); i++)
                {
                    JSONObject jsonChildNode = jsonResponse.getJSONObject(i);
                    labels.add(jsonChildNode.getString(jsonChildNode.names().getString(0)));
                    siglas[i] = jsonChildNode.getString(jsonChildNode.names().getString(1));
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext,
                    android.R.layout.simple_list_item_1, android.R.id.text1, labels);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            lista.setAdapter(dataAdapter);

            lista.setOnItemClickListener(new ListView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id)
                {
                    String itemValue = (String) lista.getItemAtPosition(position);
                    Global.sigla = siglas[position];
                    Global.torneio = itemValue;
                    Bundle args = new Bundle();
                    args.putString("Torneio", itemValue);
                    Intent i2 = new Intent(Main.this, Principal.class);
                    i2.putExtras(args);
                    startActivity(i2);
                }

            });
        }
    }

    public boolean isOnline()
    {
        android.net.ConnectivityManager manager = (android.net.ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return manager.getActiveNetworkInfo() != null &&
               manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void onBackPressed()
    {
        if (interstitial.isLoaded())
        {
            interstitial.show();
        }
        super.onBackPressed();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        adView.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        adView.destroy();
        super.onDestroy();
    }
}
