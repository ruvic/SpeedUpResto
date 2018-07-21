package com.example.mbogniruvic.speedupresto.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.models.Category;
import com.example.mbogniruvic.speedupresto.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

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

    }
}
