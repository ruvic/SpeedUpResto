package com.example.mbogniruvic.speedupresto.Tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.models.Category;
import com.example.mbogniruvic.speedupresto.models.CategoryMenu;
import com.example.mbogniruvic.speedupresto.models.MenuItem;
import com.example.mbogniruvic.speedupresto.sqlite.DatabaseHelper;

import java.util.List;

public class StoreMenusTask extends AsyncTask<List<CategoryMenu>,Void,Boolean>  {

    private DatabaseHelper db;
    private Context context;

    public StoreMenusTask(DatabaseHelper db, Context context) {
        this.db = db;
        this.context=context;
    }


    @Override
    protected Boolean doInBackground(List<CategoryMenu>... lists) {
        List<CategoryMenu> categoryMenus=lists[0];

        for (CategoryMenu catMenu : categoryMenus) {

            for (MenuItem menu : catMenu.getMenus()) {

                db.updateMenuItem(menu);

            }

        }


        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        /*List<MenuItem> menuItems=db.getAllMenuItems();
        for (MenuItem menu : menuItems) {
            Toast.makeText(context,menu.toString(), Toast.LENGTH_SHORT).show();
        }*/

    }
}
