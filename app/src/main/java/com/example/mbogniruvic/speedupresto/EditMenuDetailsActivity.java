package com.example.mbogniruvic.speedupresto;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

public class EditMenuDetailsActivity extends AppCompatActivity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu_details);

        context=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.details_menu_edit_categorie);
        spinner.setItems("-- Ajouter une catégorie --", "Jelly Bean", "KitKat", "Lollipop", "Marshmallow");
        spinner.setSelectedIndex(1);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Toast.makeText(context, "Clicked "+item, Toast.LENGTH_LONG).show();
                if(position==0){
                    MenuActivity.showAddCategorieDialog(context, (Activity) context);
                }
            }
        });
    }
}
