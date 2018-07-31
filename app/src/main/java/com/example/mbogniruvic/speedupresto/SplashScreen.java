package com.example.mbogniruvic.speedupresto;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.models.Category;
import com.example.mbogniruvic.speedupresto.models.CategoryResponse;
import com.example.mbogniruvic.speedupresto.rest.ApiClient;
import com.example.mbogniruvic.speedupresto.rest.ApiInterface;
import com.example.mbogniruvic.speedupresto.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getAllCategories();

    }




    private void getAllCategories() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CategoryResponse> call=apiService.getAllCategories();

        call.enqueue(new Callback<CategoryResponse>() {

            @Override
            public void onResponse(Call<CategoryResponse>call, Response<CategoryResponse> response) {
                List<Category> categoryList = response.body().getResults();

                if(categoryList!=null && categoryList.size()!=0){

                    new StoreAllCategoriesTask(new DatabaseHelper(getApplicationContext()), getApplicationContext()).execute(categoryList);

                }else if(categoryList==null){
                    Toast.makeText(getApplicationContext(), "Liste null", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Liste vide", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CategoryResponse>call, Throwable t) {
                //Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }

        });

    }


    public class StoreAllCategoriesTask extends AsyncTask<List<Category>, Void, List<String>> {

        private DatabaseHelper db;
        private Context context;

        public StoreAllCategoriesTask(DatabaseHelper db, Context context) {
            this.db = db;
            this.context=context;
        }

        @Override
        protected List<String> doInBackground(List<Category>... lists) {

            List<Category> categoryList=lists[0];
            List<String> list=new ArrayList<>();
            for (Category cat : categoryList) {

                String id=db.updateCategorie(cat);
                list.add(id);

            }
            return list;
        }

        @Override
        protected void onPostExecute(List<String> listes) {

            Toast.makeText(context, "Sauv. cat. reussie", Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }
    }
}
