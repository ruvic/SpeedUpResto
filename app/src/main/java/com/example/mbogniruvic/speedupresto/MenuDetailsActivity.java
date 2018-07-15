package com.example.mbogniruvic.speedupresto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Tasks.DownLoadImageTask;
import com.example.mbogniruvic.speedupresto.models.CategoryMenu;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class MenuDetailsActivity extends AppCompatActivity {

    public static final String TAG = MenuDetailsActivity.class.getSimpleName();
    public static final String MENU_ITEM_TAG=TAG+".MENU_ITEM";
    public static final String CAT_CURRENT_TAG=TAG+".CAT_CURRENT";
    public static final String MENU_ITEM_VOIR_PLUS_TAG=TAG+".CAT_OBJECT_VOIR_PLUS";

    Context context;
    private com.example.mbogniruvic.speedupresto.models.MenuItem menu;
    private boolean isVoirPlus;
    private CategoryMenu currentCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_details);

        context=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        menu = getIntent().getParcelableExtra(MenuActivity.MENU_ITEM_TAG);
        currentCat=getIntent().getParcelableExtra(MenuActivity.CAT_CURRENT_TAG);
        isVoirPlus=getIntent().getBooleanExtra(MenuActivity.MENU_ITEM_VOIR_PLUS_TAG, false);

        //get field address
        ImageView menu_image=(ImageView) findViewById(R.id.details_menu_image);
        EditText categorieField=(EditText)findViewById(R.id.details_categorie);
        EditText nomMenuField=(EditText) findViewById(R.id.details_nom_menu);
        EditText prixField=(EditText) findViewById(R.id.details_prix);
        TextView dispoField=(TextView) findViewById(R.id.details_dispo);
        EditText descField=(EditText) findViewById(R.id.details_desc);

        //update field
        new DownLoadImageTask(menu_image).execute(menu.getImage());
        categorieField.setText(currentCat.getCategorie());
        nomMenuField.setText(menu.getNom());
        prixField.setText(menu.getPrice()+"");

        if(menu.isDispo()){
            dispoField.setText(getString(R.string.menu_dispo));
            dispoField.setTextColor(getResources().getColor(R.color.green));
        }else{
            dispoField.setText(getString(R.string.menu_non_dispo));
            dispoField.setTextColor(getResources().getColor(R.color.red));
        }

        descField.setText(menu.getDesc());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.edit_details_menu:
                Intent intent=new Intent(this, EditMenuDetailsActivity.class);
                intent.putExtra(MenuDetailsActivity.MENU_ITEM_TAG, menu);
                intent.putExtra(MenuDetailsActivity.CAT_CURRENT_TAG, currentCat);
                intent.putExtra(MenuDetailsActivity.MENU_ITEM_VOIR_PLUS_TAG, isVoirPlus);
                startActivity(intent);
                break;
            default: break;
        }

        return super.onOptionsItemSelected(item);
    }
}
