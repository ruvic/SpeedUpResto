package com.example.mbogniruvic.speedupresto.Tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {

    ImageView view;

    public DownLoadImageTask(ImageView view) {
        this.view = view;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urlOfImage = urls[0];
        Bitmap logo = null;
        try{
            InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
            logo = BitmapFactory.decodeStream(is);
        }catch(Exception e){ // Catch the download exception
            e.printStackTrace();
        }
        return logo;
    }

    protected void onPostExecute(Bitmap result){
        view.setImageBitmap(result);
    }
}
