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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Adapters.CategoriesAdapters;
import com.example.mbogniruvic.speedupresto.Adapters.CommandesAdapter;
import com.example.mbogniruvic.speedupresto.models.CategoryMenu;
import com.example.mbogniruvic.speedupresto.models.CategoryMenuResponse;
import com.example.mbogniruvic.speedupresto.models.MenuItem;
import com.example.mbogniruvic.speedupresto.rest.ApiClient;
import com.example.mbogniruvic.speedupresto.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    public static final String TAG = MenuActivity.class.getSimpleName();
    public static final String MENU_ITEM_TAG=TAG+".MENU_ITEM";
    public static final String CAT_NAME_TAG=TAG+".CAT_NAME";
    public static final String CAT_OBJECT_TAG=TAG+".CAT_OBJECT";
    public static final String CAT_CURRENT_TAG=TAG+".CURRENT";
    public static final String MENU_ITEM_VOIR_PLUS_TAG=TAG+".CAT_OBJECT_VOIR_PLUS";


    private List<CategoryMenu> categoryList;
    private CategoriesAdapters mAdapter;
    private RecyclerView recyclerView;
    private Context context;
    private TextView nbCategorieField;
    private TextView nbMenuField;

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


        //get Address
        nbCategorieField=(TextView) findViewById(R.id.nbCategorie);
        nbMenuField=(TextView)findViewById(R.id.nbMenus);

        prepareDatas();

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

    private void prepareDatas() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CategoryMenuResponse> call=apiService.getMenusByCategories("5b060369244f78388a78f157");

        call.enqueue(new Callback<CategoryMenuResponse>() {

            @Override
            public void onResponse(Call<CategoryMenuResponse>call, Response<CategoryMenuResponse> response) {
                categoryList = response.body().getResult();

                if(categoryList!=null && categoryList.size()!=0){

                    recyclerView=(RecyclerView)findViewById(R.id.recyclerV_categorie);
                    mAdapter = new CategoriesAdapters(categoryList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);

                    int nbCat=categoryList.size();
                    int nbMenu=0;

                    for (int i=0; i<nbCat; i++){
                        nbMenu+=categoryList.get(i).getMenus().size();
                    }


                    nbCategorieField.setText(format(nbCat));
                    nbMenuField.setText(format(nbMenu));



                    /*String result="";

                    for (CategoryMenu cat: categoryList) {
                        result+=cat.toString()+"\n";
                    }

                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();*/

                }else if(categoryList==null){
                    Toast.makeText(context, "Liste null", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Liste vide", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CategoryMenuResponse>call, Throwable t) {
                // Log error here since request failed
                //Log.e(TAG, t.toString());
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static String format(int number) {
        return  (number<10)?"0"+number:""+number;
    }
}
