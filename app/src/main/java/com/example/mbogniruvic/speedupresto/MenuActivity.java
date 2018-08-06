package com.example.mbogniruvic.speedupresto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Adapters.CategoriesAdapters;
import com.example.mbogniruvic.speedupresto.Tasks.DownLoadImageTask;
import com.example.mbogniruvic.speedupresto.Tasks.StoreMenusTask;
import com.example.mbogniruvic.speedupresto.Utils.ConnectionStatus;
import com.example.mbogniruvic.speedupresto.Utils.RestaurantPreferencesDB;
import com.example.mbogniruvic.speedupresto.models.Category;
import com.example.mbogniruvic.speedupresto.models.CategoryMenu;
import com.example.mbogniruvic.speedupresto.models.CategoryMenuResponse;
import com.example.mbogniruvic.speedupresto.models.CategoryResponse;
import com.example.mbogniruvic.speedupresto.models.MenuItem;
import com.example.mbogniruvic.speedupresto.rest.ApiClient;
import com.example.mbogniruvic.speedupresto.rest.ApiInterface;
import com.example.mbogniruvic.speedupresto.sqlite.DatabaseHelper;

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
    private DatabaseHelper db;
    private RestaurantPreferencesDB shareDB;
    private ImageView logoRestauview;
    private TextView nomRestauView;
    private TextView nbCategorieField;
    private TextView nbMenuField;
    private RelativeLayout progressBarLayout;
    private RelativeLayout menuErrorConnectionLayout;
    private RelativeLayout menuEmptyLayout;
    private Button conn_refresh;
    private Button empty_refresh;



    @SuppressLint("WrongViewCast")
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
        recyclerView=(RecyclerView)findViewById(R.id.recyclerV_categorie);
        logoRestauview=(ImageView)findViewById(R.id.logo_restau);
        nomRestauView=(TextView)findViewById(R.id.nom_restau);
        nbCategorieField=(TextView) findViewById(R.id.nbCategorie);
        nbMenuField=(TextView)findViewById(R.id.nbMenus);
        progressBarLayout=(RelativeLayout) findViewById(R.id.menu_progress_bar);
        menuErrorConnectionLayout=(RelativeLayout)findViewById(R.id.menu_conn_error);
        menuEmptyLayout=(RelativeLayout)findViewById(R.id.menu_empty);
        conn_refresh=(Button)findViewById(R.id.menu_conn_refresh);
        empty_refresh=(Button)findViewById(R.id.menu_empty_refresh);

        conn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        empty_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        shareDB=MainActivity.shareDB;
        db=new DatabaseHelper(getApplicationContext());

        nomRestauView.setText(shareDB.getString(RestaurantPreferencesDB.NOM_KEY, "<nom du restaurant>"));

        if (ConnectionStatus.getInstance(context).isOnline()) {
            new DownLoadImageTask(logoRestauview).execute(shareDB.getString(RestaurantPreferencesDB.IMAGE_KEY, ""));
        } else {
            new DownLoadImageTask(logoRestauview, shareDB.getString(RestaurantPreferencesDB.ID_KEY,""))
                    .execute(shareDB.getString(RestaurantPreferencesDB.IMAGE_KEY, ""));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_menu_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MenuActivity.this, AddMenuActivity.class);
                startActivity(intent);

            }
        });

        if(db.getAllCategories().size()==0){
            getAllCategories();
        }else{
            prepareDatas();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_action_refresh :
                refresh();
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh(){

        if (ConnectionStatus.getInstance(context).isOnline()) {
            new DownLoadImageTask(logoRestauview).execute(shareDB.getString(RestaurantPreferencesDB.IMAGE_KEY, ""));
        } else {
            new DownLoadImageTask(logoRestauview, shareDB.getString(RestaurantPreferencesDB.ID_KEY, "")).execute(shareDB.getString(RestaurantPreferencesDB.IMAGE_KEY, ""));
        }
        progressBarLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        menuErrorConnectionLayout.setVisibility(View.GONE);
        menuEmptyLayout.setVisibility(View.GONE);
        prepareDatas();

    }



    private void prepareDatas() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CategoryMenuResponse> call=apiService.getMenusByCategories(
                shareDB.getString(RestaurantPreferencesDB.ID_KEY,"")
        );

        call.enqueue(new Callback<CategoryMenuResponse>() {

            @Override
            public void onResponse(Call<CategoryMenuResponse>call, Response<CategoryMenuResponse> response) {
                if (!response.body().isError()) {
                    categoryList = response.body().getResult();

                    if(categoryList!=null && categoryList.size()!=0){

                        new StoreMenusTask(db, getApplicationContext()).execute(categoryList);

                        updateInterfaceViews();

                    }else{
                        progressBarLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        menuErrorConnectionLayout.setVisibility(View.GONE);
                        menuEmptyLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CategoryMenuResponse>call, Throwable t) {

                categoryList=new ArrayList<>();

                List<MenuItem> menuItems=db.getAllMenuItems();

                if (menuItems.size()!=0) {

                    for (int i=0; i<menuItems.size(); i++){

                        MenuItem menu=menuItems.get(i);

                        Category currentCat=db.getCategory(menu.getIdCat());

                        CategoryMenu catMenu=new CategoryMenu();
                        catMenu.setCategorie(currentCat.getTitre());

                        do {
                            catMenu.getMenus().add(menu);
                            i++;
                            if (i<menuItems.size()) {
                                menu=menuItems.get(i);
                            }
                        }while (i<menuItems.size() && menu.getIdCat().equals(currentCat.getId()));

                        categoryList.add(catMenu);

                        i--;

                    }

                    updateInterfaceViews();

                } else {

                    progressBarLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    menuErrorConnectionLayout.setVisibility(View.VISIBLE);
                    menuEmptyLayout.setVisibility(View.GONE);

                }

            }
        });

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

                }

            }

            @Override
            public void onFailure(Call<CategoryResponse>call, Throwable t) {
                progressBarLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                menuEmptyLayout.setVisibility(View.GONE);
                menuErrorConnectionLayout.setVisibility(View.VISIBLE);
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

            //Toast.makeText(context, "Sauv. cat. reussie", Toast.LENGTH_SHORT).show();
            prepareDatas();

        }
    }

    private  void updateInterfaceViews(){

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

        progressBarLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        menuErrorConnectionLayout.setVisibility(View.GONE);
        menuEmptyLayout.setVisibility(View.GONE);

    }



    public static String format(int number) {
        return  (number<10)?"0"+number:""+number;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //db.closeDB();
    }
}
