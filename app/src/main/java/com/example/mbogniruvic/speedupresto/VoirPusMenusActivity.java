package com.example.mbogniruvic.speedupresto;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mbogniruvic.speedupresto.Adapters.CategorieItemsAdapter;
import com.example.mbogniruvic.speedupresto.Tasks.DownLoadImageTask;
import com.example.mbogniruvic.speedupresto.Utils.ConnectionStatus;
import com.example.mbogniruvic.speedupresto.Utils.RestaurantPreferencesDB;
import com.example.mbogniruvic.speedupresto.models.CategoryMenu;
import com.example.mbogniruvic.speedupresto.models.MenuItem;

import java.util.List;

public class VoirPusMenusActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView logoRestoView;
    TextView nomRestoView;
    TextView nomCatView;
    TextView nbMenusView;
    List<MenuItem> menusList;
    Context context;
    CategoryMenu currentCat;
    RestaurantPreferencesDB shareDB;


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

        currentCat=getIntent().getParcelableExtra(MenuActivity.CAT_OBJECT_TAG);
        menusList=currentCat.getMenus();
        shareDB=MainActivity.shareDB;


        //get Adresses
        recyclerView=(RecyclerView)findViewById(R.id.recyclerV_voir_plus);
        logoRestoView=(ImageView)findViewById(R.id.voir_plus_logo_restau);
        nomRestoView=(TextView)findViewById(R.id.voir_plus_nom_restau);
        nomCatView=(TextView)findViewById(R.id.voir_plus_nom_categorie);
        nbMenusView=(TextView)findViewById(R.id.voir_plus_nbMenus);

        //init field
        initFields();
        
    }

    private void initFields() {

        CategorieItemsAdapter mAdapter = new CategorieItemsAdapter(menusList, currentCat ,true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if (ConnectionStatus.getInstance(context).isOnline()) {
            new DownLoadImageTask(logoRestoView).execute(shareDB.getString(RestaurantPreferencesDB.IMAGE_KEY, ""));
        } else {
            new DownLoadImageTask(logoRestoView, shareDB.getString(RestaurantPreferencesDB.ID_KEY,"")).execute(shareDB.getString(RestaurantPreferencesDB.IMAGE_KEY, ""));
        }
        nomRestoView.setText(shareDB.getString(RestaurantPreferencesDB.NOM_KEY, "<nom du restaurant>"));
        nomCatView.setText(currentCat.getCategorie());
        nbMenusView.setText(MenuActivity.format(currentCat.getMenus().size()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh :

                initFields();

                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

}
