package com.example.mbogniruvic.speedupresto;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Fragments.DatePickerFragment;
import com.example.mbogniruvic.speedupresto.Fragments.LivreFragment;
import com.example.mbogniruvic.speedupresto.Fragments.NonLivreFragment;
import com.example.mbogniruvic.speedupresto.Fragments.RefuseFragment;
import com.example.mbogniruvic.speedupresto.Tasks.DownLoadImageTask;
import com.example.mbogniruvic.speedupresto.Utils.RestaurantPreferencesDB;
import com.example.mbogniruvic.speedupresto.models.Restaurant;
import com.example.mbogniruvic.speedupresto.sqlite.DatabaseHelper;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static RestaurantPreferencesDB shareDB;
    private Restaurant restaurant;
    private DatabaseHelper db;
    Context context;
    public static  NonLivreFragment nonLivreFragment;
    public static  LivreFragment livreFragment;
    public static  RefuseFragment refuseFragment;

    public static LinearLayout currentDayHint;
    public static LinearLayout dayHint;
    public static LinearLayout periodeHint;
    public static TextView dayText;
    public static TextView periodeStartText;
    public static TextView periodeEndText;

    public boolean hasChooseType=false;
    private boolean isDayFilter;
    private String startDay;
    private String endDay;

    private ImageView imageHeaderNav;
    private TextView titleHeaderNav;

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
        View headerLayout=navigationView.inflateHeaderView(R.layout.nav_header_main);
        imageHeaderNav= (ImageView)headerLayout.findViewById(R.id.imageView);
        titleHeaderNav=(TextView)headerLayout.findViewById(R.id.textView);

        navigationView.setNavigationItemSelectedListener(this);

        //getAdresses
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        currentDayHint = (LinearLayout) findViewById(R.id.cmd_day_current_hint);
        dayHint = (LinearLayout) findViewById(R.id.cmd_day_hint);
        periodeHint = (LinearLayout) findViewById(R.id.cmd_periode_hint);
        dayText = (TextView) findViewById(R.id.day);
        periodeStartText = (TextView) findViewById(R.id.periode_start);
        periodeEndText = (TextView) findViewById(R.id.periode_end);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        shareDB=new RestaurantPreferencesDB(context);
        db=new DatabaseHelper(getApplicationContext());

        //db.dropDatabase();
        //Toast.makeText(context, "Suppression reussie", Toast.LENGTH_SHORT).show();
        titleHeaderNav.setText(
                shareDB.getString(RestaurantPreferencesDB.NOM_KEY, "")
        );

        new DownLoadImageTask(
                imageHeaderNav,
                shareDB.getString(RestaurantPreferencesDB.ID_KEY, "")
        ).execute(
                shareDB.getString(RestaurantPreferencesDB.IMAGE_KEY, "")
        );

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

            setupViewPager(viewPager);

            //new NotificationTask(getApplicationContext()).execute();

            return true;

        }

        if (id == R.id.change_periode_range_menu) {

            //get Adresses
            final Dialog dialog=new Dialog(context);
            dialog.setContentView(R.layout.change_commande_view_periode);

            final MaterialSpinner spinner=(MaterialSpinner)dialog.findViewById(R.id.type_filtre_spinner);
            final RelativeLayout dayFilterLayout=(RelativeLayout)dialog.findViewById(R.id.day_filter_layout);
            final LinearLayout periodeFilterLayout=(LinearLayout) dialog.findViewById(R.id.periode_filter_layout);
            final EditText dayText=(EditText)dialog.findViewById(R.id.day_edit);
            final EditText startPeriode=(EditText)dialog.findViewById(R.id.day_start);
            final EditText endPeriode=(EditText)dialog.findViewById(R.id.day_end);
            ImageButton loadDate=(ImageButton)dialog.findViewById(R.id.loadDayBtn);
            ImageButton loadStartDate=(ImageButton)dialog.findViewById(R.id.loadStartDayBtn);
            ImageButton loadEndDate=(ImageButton)dialog.findViewById(R.id.loadEndDayBtn);
            Button cancelBtn=(Button)dialog.findViewById(R.id.cancel_filtre_btn);
            Button validerBtn=(Button)dialog.findViewById(R.id.valider_filtre_btn);

            //init field
            List<String> items=new ArrayList<>();
            items.add("Jour");
            items.add("Période");
            spinner.setItems(items);

            //event listener
            spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    hasChooseType=true;
                    if(position==0){
                        dayFilterLayout.setVisibility(View.VISIBLE);
                        periodeFilterLayout.setVisibility(View.GONE);
                        isDayFilter=true;
                    }else {
                        dayFilterLayout.setVisibility(View.GONE);
                        periodeFilterLayout.setVisibility(View.VISIBLE);
                        isDayFilter=false;
                    }
                }
            });

            loadDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePicker(dayText);
                }
            });

            loadStartDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePicker(startPeriode);
                }
            });

            loadEndDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePicker(endPeriode);
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            validerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (hasChooseType) {
                        if(spinner.getSelectedIndex()==0){
                            if(dayText.getText().toString().isEmpty()){
                                showMessage("Veuillez choisir une date");
                            }else{

                                startDay=dayText.getText().toString();
                                setHintVisibleLayout(dayHint);
                                dialog.dismiss();
                                setupViewPager(viewPager);

                            }
                        }else{

                            if(startPeriode.getText().toString().isEmpty()
                                    || endPeriode.getText().toString().isEmpty()){
                                showMessage("La date de début et de fin doivent être remplies");
                            }else {

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                                try {
                                    Date dateStart= sdf.parse(startPeriode.getText().toString());
                                    Date dateEnd= sdf.parse(endPeriode.getText().toString());

                                    if(dateStart.compareTo(dateEnd)>0){
                                        showMessage("La date de fin doit être inférieure à la date début");
                                    }else{

                                        startDay=startPeriode.getText().toString();
                                        endDay=endPeriode.getText().toString();
                                        dialog.dismiss();
                                        setHintVisibleLayout(periodeHint);
                                        setupViewPager(viewPager);

                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    showMessage("Erreur"+e.getMessage());
                                }
                            }
                        }
                    } else {
                        showMessage("Veuillez choisir une type de filtre");
                    }

                }

                private void showMessage(String msg){
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }

                private void setHintVisibleLayout(LinearLayout layout){

                    if(layout.equals(currentDayHint)){
                        currentDayHint.setVisibility(View.VISIBLE);
                        dayHint.setVisibility(View.GONE);
                        periodeHint.setVisibility(View.GONE);
                    }else if(layout.equals(dayHint)){
                        MainActivity.dayText.setText(getStartDay().replace("-", ":"));
                        currentDayHint.setVisibility(View.GONE);
                        dayHint.setVisibility(View.VISIBLE);
                        periodeHint.setVisibility(View.GONE);
                    }else{
                        periodeStartText.setText(getStartDay().replace("-", ":"));
                        periodeEndText.setText(getEndDay().replace("-", ":"));
                        currentDayHint.setVisibility(View.GONE);
                        dayHint.setVisibility(View.GONE);
                        periodeHint.setVisibility(View.VISIBLE);
                    }
                }

            });


            dialog.show();


            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void showDatePicker(EditText editText){
        DialogFragment newFragment = new DatePickerFragment(editText);
        newFragment.show(getSupportFragmentManager(), "date picker");
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

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        nonLivreFragment=new NonLivreFragment();
        nonLivreFragment.setMainActivity(this);

        livreFragment=new LivreFragment();
        refuseFragment=new RefuseFragment();

        adapter.addFragment(nonLivreFragment, "NON LIVRE");
        adapter.addFragment(livreFragment, "LIVRE");
        adapter.addFragment(refuseFragment, "REFUSE");
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

    public ViewPager getViewPager() {
        return viewPager;
    }

    public boolean isDayFilter() {
        return isDayFilter;
    }

    public void setDayFilter(boolean dayFilter) {
        isDayFilter = dayFilter;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }
}
