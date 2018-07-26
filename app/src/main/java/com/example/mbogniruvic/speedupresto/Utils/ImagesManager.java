package com.example.mbogniruvic.speedupresto.Utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.MainActivity;
import com.example.mbogniruvic.speedupresto.models.UploadImageResponse;
import com.example.mbogniruvic.speedupresto.rest.ApiClient;
import com.example.mbogniruvic.speedupresto.rest.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                MainActivity.shareDB.putRootImagePath(rootPath);
            }

            stream.flush();

            stream.close();


        }catch (Exception e) // Catch the exception
        {
            e.printStackTrace();
            return "";
        }

        return imageUri;

    }

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

    public static void uploadFile(final Context context, String url){

        ApiInterface apiService = ApiClient.getImageClient().create(ApiInterface.class);

        File file=new File(url);

        RequestBody descriptionPart=RequestBody.create(MultipartBody.FORM, "description");
        RequestBody fileBody=RequestBody.create(
            MediaType.parse("multipart/form-data"), file
        );
        MultipartBody.Part body=MultipartBody.Part.createFormData("image", file.getName(), fileBody);

        Call<UploadImageResponse> call = apiService.uploadImage(
                descriptionPart,
                body
        );

        call.enqueue(new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(Call<UploadImageResponse> call,
                                   Response<UploadImageResponse> response) {
                Toast.makeText(context, response.body().getResult(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                Toast.makeText(context, "Upload echec", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
