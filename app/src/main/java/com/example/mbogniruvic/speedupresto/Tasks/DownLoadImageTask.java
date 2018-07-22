package com.example.mbogniruvic.speedupresto.Tasks;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.MainActivity;
import com.example.mbogniruvic.speedupresto.Utils.ConnectionStatus;
import com.example.mbogniruvic.speedupresto.Utils.ImagesManager;
import com.example.mbogniruvic.speedupresto.Utils.RestaurantPreferencesDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {

    private  ImageView view;
    private Context context;
    private String imageUri;

    public DownLoadImageTask(ImageView view) {
        this.view = view;
        this.context=view.getContext();
        this.imageUri=null;
    }

    public DownLoadImageTask(ImageView view, String id)  {
        this.view = view;
        this.context=view.getContext();
        RestaurantPreferencesDB sharedDB= MainActivity.shareDB;
        this.imageUri = sharedDB.getString(RestaurantPreferencesDB.ROOT_IMAGE_KEY,"")+id+".jpg";
    }

    @Override
    protected Bitmap doInBackground(String... urls) {

        if (imageUri==null) {
            return ImagesManager.getBitmapFromUrl(urls[0]);
        } else {
            return null;
        }

    }

    protected void onPostExecute(Bitmap result){

        if (imageUri==null){
            view.setImageBitmap(result);
        }else {
            view.setImageURI(Uri.parse(imageUri));
        }

    }


}
