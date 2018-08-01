package br.danieltiburciosf.rankingfutebol;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Busca_camp extends AsyncTask<String, Context, String>
{
    private Context mContext;

    public Busca_camp(Context context)
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

        String opc_ataq = Global.escolha + " de " + Global.anos1;

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

                clube = String.valueOf(num) + "º ";
                //}
                clube = clube + jsonChildNode.getString(jsonChildNode.names().getString(0)) + "/" +
                        jsonChildNode.getString(jsonChildNode.names().getString(1));
                String tiporeg;
                int j;
                if (Global.escolha.contains("Gols p"))
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
                    if (tiporeg.equals("EST_ESTADO") | tiporeg.equals("EST_PAIS"))
                    {
                        Global.ordem = campox;
                    }
                    if (tiporeg.equals("gol"))
                    {
                        tiporeg = "g" + jsonChildNode.names().getString(k).substring(7, 9);
                    }

                    linha2 = linha2 + tiporeg.toUpperCase() + "=";
                    if (tiporeg.equals("ORD"))
                    {
                        Global.ordem = campox;
                    }
                    else if (tiporeg.equals("CLA") & (Global.escolha.contains("Pont")) &
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
                    linha2 = linha2 + campox;
                }
                linha = new Resultado(clube, linha2);
                mLista.add(linha);
            }

            args.putSerializable("mLista", mLista);

            Intent i2 = new Intent(mContext, Pontuacao.class);
            i2.putExtras(args);
            mContext.startActivity(i2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

