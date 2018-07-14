package com.example.mbogniruvic.speedupresto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Adapters.CategoriesAdapters;
import com.example.mbogniruvic.speedupresto.Adapters.CommandesAdapter;
import com.example.mbogniruvic.speedupresto.models.CategoryMenu;
import com.example.mbogniruvic.speedupresto.models.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    List<CategoryMenu> categoryList=new ArrayList<>();
    CategoriesAdapters mAdapter;
    RecyclerView recyclerView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        context=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recyclerV_categorie);
        mAdapter = new CategoriesAdapters(categoryList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        prepareMovieData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_menu_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                View fragmentView = getLayoutInflater().inflate(R.layout.fragment_add_categorie_menu, null);

                final BottomSheetDialog dialog = new BottomSheetDialog(context);
                dialog.setContentView(fragmentView);
                dialog.show();

                LinearLayout cat=(LinearLayout)fragmentView.findViewById(R.id.add_cat);
                LinearLayout menu=(LinearLayout)fragmentView.findViewById(R.id.add_menu);
                LinearLayout close=(LinearLayout)fragmentView.findViewById(R.id.close);

                cat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showAddCategorieDialog(context, (Activity) context);
                    }
                });

                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MenuActivity.this, AddMenuActivity.class);
                        startActivity(intent);
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.hide();
                    }
                });

            }
        });

    }

    public static void showAddCategorieDialog(final Context context, Activity activity) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.add_categorie_dialog, null);

        final EditText catEdit=(EditText)dialogView.findViewById(R.id.edit_categorie_dialog);
        Button add=(Button)dialogView.findViewById(R.id.add_cat_dialog);
        Button cancel=(Button)dialogView.findViewById(R.id.cancel_cat_dialog);

        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_cat=catEdit.getText().toString().trim();
                Toast.makeText(context, "Categorie : " + new_cat + " créée avec succès", Toast.LENGTH_LONG).show();
                b.hide();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.hide();
            }
        });

    }

    private void prepareMovieData() {

        List<String> catNoms=new ArrayList<>();
        catNoms.add("Patisserie");
        catNoms.add("Sauce");
        catNoms.add("Glaces");
        catNoms.add("Amuses Gueul");
        catNoms.add("Goûté");
        catNoms.add("Patisserie 2");
        catNoms.add("Sauce 2");
        catNoms.add("Glaces 2");
        catNoms.add("Amuses Gueul2");
        catNoms.add("Goûté 2");
        catNoms.add("Patisserie 3");
        catNoms.add("Sauce 3");
        catNoms.add("Glaces 3");
        catNoms.add("Amuses Gueul 3");
        catNoms.add("Goûté 3");

        CategoryMenu cat=null;

        for (int i=0; i<catNoms.size(); i++){
            cat=new CategoryMenu(catNoms.get(i));
            List<MenuItem> menus=new ArrayList<>();
            for (int j=0; j<10; j++){
                menus.add(new MenuItem(j+1));
            }
            cat.setMenus(menus);
            categoryList.add(cat);
        }



    }
}
