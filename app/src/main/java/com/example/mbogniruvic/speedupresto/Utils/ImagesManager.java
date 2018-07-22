package com.example.mbogniruvic.speedupresto.Utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class ImagesManager {

    public  static  String rootPath=null;

    public static Bitmap getBitmapFromUrl(String url){

        String urlOfImage = url;
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

            return null;
        }
        return logo;
    }

    public static byte[] getBytesFromUrl(String url){

        Bitmap bitmap=getBitmapFromUrl(url);
        byte[] data=null;
        if(bitmap!=null){
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            data=stream.toByteArray();
        }
        return data;
    }

    public static String saveIntoInternalStorage(Context context, String url, String name){

        String imageUri="";

        ContextWrapper wrapper = new ContextWrapper(context);

        File file = wrapper.getDir("Images",MODE_PRIVATE);
        String extension=url.substring(url.lastIndexOf(".")+1, url.length());
        file = new File(file, name+".jpg");

        try{

            OutputStream stream = null;

            stream = new FileOutputStream(file);
            Bitmap bitmap=getBitmapFromUrl(url);

            if(bitmap==null)
                System.out.println("null : "+url);


            //if(extension.equals("jpeg") || extension.equals("jpg"))
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            /*else
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);*/

            imageUri=Uri.parse(file.getAbsolutePath()).toString();

            if(rootPath==null){
                rootPath=imageUri.substring(0, imageUri.lastIndexOf("/")+1);
            }

            stream.flush();

            stream.close();


        }catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
            return "";
        }

        return imageUri;

        // Parse the gallery image url to uri
        //Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        // Display the saved image to ImageView
        //iv_saved.setImageURI(savedImageURI);


    }

    /*public static Bitmap getBitmapFromImageUri(String imageUri, Context context){
        Uri uri=Uri.parse(imageUri);
        String path=getRealPathFromURI(context, uri);

    }*/

    private static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
