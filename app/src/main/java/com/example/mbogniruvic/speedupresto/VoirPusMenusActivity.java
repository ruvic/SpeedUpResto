package com.example.mbogniruvic.speedupresto;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Adapters.CategorieItemsAdapter;
import com.example.mbogniruvic.speedupresto.Adapters.CategoriesAdapters;
import com.example.mbogniruvic.speedupresto.models.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class VoirPusMenusActivity extends AppCompatActivity {

    Toolbar toolbar;
    Context context;
    RecyclerView recyclerView;
    List<MenuItem> menusList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voir_pus_menus);
        context=this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        prepareData();

        prepareData();

        recyclerView=(RecyclerView)findViewById(R.id.recyclerV_voir_plus);
        CategorieItemsAdapter mAdapter = new CategorieItemsAdapter(menusList, true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);
        
    }

    private void prepareData() {
        menusList=new ArrayList<>();
        for (int i=1; i<20; i++){
            menusList.add(new MenuItem(i));
        }
    }
}
