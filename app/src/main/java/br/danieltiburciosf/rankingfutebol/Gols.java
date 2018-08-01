package br.danieltiburciosf.rankingfutebol;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Daniel on 20/10/2017.
 */
public class Gols extends AppCompatActivity
{
    private AdView adViewg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gols);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewg = findViewById(R.id.adViewg);
        adViewg.loadAd(adRequest);

        @SuppressWarnings({"unchecked"}) final
        ArrayList<Resultado>
                mLista = (ArrayList<br.danieltiburciosf.rankingfutebol.Resultado>)
                getIntent().getSerializableExtra("mLista");

        String linha;
        Global.escolha = mLista.get(1).getCampos().trim();
        Global.coluna = 20;
        if (Global.escolha.contains("Gols p"))
        {
            Global.coluna = 0;
        }
        else
        {
            Global.coluna = 2;
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);
        actionBar.setTitle(mLista.get(0).getCampos());

        TextView titulGols = findViewById(R.id.titulGols);
        titulGols.setText(Global.escolha);
        ArrayList<Gol> Gol = new ArrayList<>();
        int totgol = 0;
        int totjog = 0;

        for (int i = 2; i < mLista.size(); i++)
        {
            Global.anos1 = "0000";
            linha = mLista.get(i).getCampos();

            Gol gol = new Gol();

            gol.setAnog(linha.substring(0, 4));
            gol.setGols(linha.substring(9, 15).trim());
            totgol = totgol + Integer.valueOf(linha.substring(9, 15).trim());
            gol.setJogos(linha.substring(19, 25).trim());
            totjog = totjog + Integer.valueOf(linha.substring(19, 25).trim());

            Gol.add(gol);
        }

        Gol gol = new Gol();
        gol.setAnog("Total de gols");
        gol.setGols(String.valueOf(totgol));
        gol.setJogos(String.valueOf(totjog));
        Gol.add(gol);

        Global.anos1 = "0000";

        ListView lvGol = findViewById(R.id.lvGol);
        lvGol.setAdapter(new GolAdapter(this, Gol));
    }

    public void onResume()
    {
        super.onResume();
    }

    @Override protected void onPause()
    {
        adViewg.pause();
        super.onPause();
    }

    @Override protected void onDestroy()
    {
        adViewg.destroy();
        super.onDestroy();
    }
}
