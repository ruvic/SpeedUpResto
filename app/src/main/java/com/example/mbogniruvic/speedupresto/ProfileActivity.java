package com.example.mbogniruvic.speedupresto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Fragments.ProfileAvisFragment;
import com.example.mbogniruvic.speedupresto.Fragments.ProfileInfosFragment;
import com.example.mbogniruvic.speedupresto.Tasks.DownLoadImageTask;
import com.example.mbogniruvic.speedupresto.Tasks.RestaurantPreferencesDB;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {


    ViewPager viewPager;
    TabLayout tabLayout;
    Context context;
    private ImageView profileImageView;
    private TextView restoNomView;
    private TextView localView;
    private TextView noteView;
    private RestaurantPreferencesDB shareDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        context=this;
        viewPager = (ViewPager) findViewById(R.id.profile_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.prof_tab);
        tabLayout.setupWithViewPager(viewPager);

        //get Adresses
        profileImageView=(ImageView)findViewById(R.id.prof_image);
        restoNomView=(TextView)findViewById(R.id.prof_resto_name);
        localView=(TextView)findViewById(R.id.prof_local);
        noteView=(TextView)findViewById(R.id.prof_note);

        //init field
        shareDB=MainActivity.shareDB;
        new DownLoadImageTask(profileImageView).execute(shareDB.getString(RestaurantPreferencesDB.IMAGE_KEY, null));
        restoNomView.setText(shareDB.getString(RestaurantPreferencesDB.NOM_KEY, "<Nom du restaurant>"));

        String local=shareDB.getString(RestaurantPreferencesDB.CITY_KEY, "<city>")+" , "
                +shareDB.getString(RestaurantPreferencesDB.QUARTIER_KEY, "<quartier>");
        localView.setText(local);
        noteView.setText(shareDB.getFloat(RestaurantPreferencesDB.NOTE_KEY, 0.0f)+" / 10");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.edit_details_menu :

                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);

                break;
            default: break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileInfosFragment(), "INFOS");
        adapter.addFragment(new ProfileAvisFragment(), "AVIS");
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
    public void onBackPressed() {
        //super.onBackPressed();
        if(getIntent().getIntExtra(EditProfileActivity.FROM_EDIT_PROFILE_TAG,0)==1){
            Intent intent=new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        }else {
            super.onBackPressed();
        }

    }
}
