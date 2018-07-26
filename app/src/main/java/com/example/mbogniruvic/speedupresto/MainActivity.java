package com.example.mbogniruvic.speedupresto;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Fragments.LivreFragment;
import com.example.mbogniruvic.speedupresto.Fragments.NonLivreFragment;
import com.example.mbogniruvic.speedupresto.Fragments.RefuseFragment;
import com.example.mbogniruvic.speedupresto.Tasks.StoreAllCategoriesTask;
import com.example.mbogniruvic.speedupresto.Utils.ConnectionStatus;
import com.example.mbogniruvic.speedupresto.Utils.ImagesManager;
import com.example.mbogniruvic.speedupresto.Utils.RestaurantPreferencesDB;
import com.example.mbogniruvic.speedupresto.models.Category;
import com.example.mbogniruvic.speedupresto.models.CategoryResponse;
import com.example.mbogniruvic.speedupresto.models.Restaurant;
import com.example.mbogniruvic.speedupresto.models.RestaurantResponse;
import com.example.mbogniruvic.speedupresto.rest.ApiClient;
import com.example.mbogniruvic.speedupresto.rest.ApiInterface;
import com.example.mbogniruvic.speedupresto.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String restauID="5b060369244f78388a78f157";
    public static RestaurantPreferencesDB shareDB;
    private Restaurant restaurant;
    private DatabaseHelper db;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //Store restaurant informations in sharePreference
        //get Restaurant profile from Databases

        getRestaurantProfile();

        shareDB=new RestaurantPreferencesDB(context);
        db=new DatabaseHelper(getApplicationContext());

        //db.dropDatabase();
        //Toast.makeText(context, "Suppression reussie", Toast.LENGTH_SHORT).show();

        getAllCategories();




    }

    private void getRestaurantProfile() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RestaurantResponse> call=apiService.getRestaurantProfile(restauID);

        call.enqueue(new Callback<RestaurantResponse>() {

            @Override
            public void onResponse(Call<RestaurantResponse> call, Response<RestaurantResponse> response) {
                if(!response.body().isError()){
                    restaurant=response.body().getRestaurant();

                    shareDB.put(restaurant);

                    new SaveProfileImageTask().execute();

                    Toast.makeText(context, "Infos chargé avec succès", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Echec du chargement des informations", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestaurantResponse> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    class  SaveProfileImageTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            if(ConnectionStatus.getInstance(context).isOnline()){
                ImagesManager.saveIntoInternalStorage(getApplicationContext(), restaurant.getImage(), restaurant.getId());

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            /*String url=shareDB.getString(RestaurantPreferencesDB.ROOT_IMAGE_KEY, "");
            url+=shareDB.getString(RestaurantPreferencesDB.ID_KEY,"")+".jpg";

            Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
            ImagesManager.uploadFile(context, url);*/

        }
    }

    private void getAllCategories() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CategoryResponse> call=apiService.getAllCategories();

        call.enqueue(new Callback<CategoryResponse>() {

            @Override
            public void onResponse(Call<CategoryResponse>call, Response<CategoryResponse> response) {
                List<Category> categoryList = response.body().getResults();

                if(categoryList!=null && categoryList.size()!=0){

                    new StoreAllCategoriesTask(db, context).execute(categoryList);

                }else if(categoryList==null){
                    Toast.makeText(context, "Liste null", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Liste vide", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CategoryResponse>call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_refresh) {


            return true;

        }

        if (id == R.id.change_periode_range_menu) {

            Dialog dialog=new Dialog(context);
            dialog.setContentView(R.layout.change_commande_view_periode);
            dialog.show();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent=null;
        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_menu) {
            intent=new Intent(MainActivity.this, MenuActivity.class);
        } else if (id == R.id.nav_profile) {
            intent=new Intent(MainActivity.this, ProfileActivity.class);
            //intent=new Intent(MainActivity.this, RecyclerViewActivity.class);
        } else if (id == R.id.nav_about) {

        }


        if(intent!=null){
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NonLivreFragment(), "NON LIVRE");
        adapter.addFragment(new LivreFragment(), "LIVRE");
        adapter.addFragment(new RefuseFragment(), "REFUSE");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.closeDB();
    }
}
