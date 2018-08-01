package br.danieltiburciosf.rankingfutebol;

//import android.widget.ProgressBar;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Daniel on 03/04/2017.
 */

public class LoadImageTask extends AsyncTask<String, Void, Drawable>
{
    //ProgressBar progress;
    private WeakReference<ImageView> imageViewReference;

    public LoadImageTask(ImageView imageView)
    {
        imageViewReference = new WeakReference<>(imageView);
    }

    @Override
    protected Drawable doInBackground(String... url)
    {

        Drawable drawable = null;

        try
        {
            drawable = LoadImageFromWebOperations(url[0]);
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return drawable;
    }

    private Drawable LoadImageFromWebOperations(String url) throws IOException
    {
        InputStream is = (InputStream) new URL(url).getContent();
        return Drawable.createFromStream(is, "src name");
    }

    protected void onPostExecute(Drawable drawable)
    {
        imageViewReference.get().setImageDrawable(drawable);
    }
}
