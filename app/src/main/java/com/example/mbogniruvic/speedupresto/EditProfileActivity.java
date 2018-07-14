package com.example.mbogniruvic.speedupresto;

import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import static android.widget.Toast.LENGTH_SHORT;

public class EditProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final MaterialSpinner edit_quartier = (MaterialSpinner) findViewById(R.id.edit_quartier);
        edit_quartier.setItems("Nkolmesseng", "Eleveur", "Melen", "Essos", "Mvog Bi");
        edit_quartier.setSelectedIndex(1);
        edit_quartier.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Toast.makeText(view.getContext(), item, Toast.LENGTH_SHORT).show();
            }
        });

        MaterialSpinner edit_ville= (MaterialSpinner) findViewById(R.id.edit_ville);
        edit_ville.setItems("Yaoundé", "Douala", "Kribi", "Edea", "Buea");
        edit_ville.setSelectedIndex(1);
        edit_ville.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Toast.makeText(view.getContext(), item, Toast.LENGTH_SHORT).show();
            }
        });


    }
}
