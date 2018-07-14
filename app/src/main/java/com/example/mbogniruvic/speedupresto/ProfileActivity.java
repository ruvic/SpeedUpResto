package com.example.mbogniruvic.speedupresto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mbogniruvic.speedupresto.Adapters.RecyclerViewBiographieAdapter;
import com.example.mbogniruvic.speedupresto.Adapters.RecyclerViewCommentsAdapter;
import com.example.mbogniruvic.speedupresto.Adapters.RecyclerViewContactsAdapter;
import com.example.mbogniruvic.speedupresto.Adapters.RecyclerViewPlusAdapter;
import com.example.mbogniruvic.speedupresto.Fragments.LivreFragment;
import com.example.mbogniruvic.speedupresto.Fragments.NonLivreFragment;
import com.example.mbogniruvic.speedupresto.Fragments.ProfileAvisFragment;
import com.example.mbogniruvic.speedupresto.Fragments.ProfileInfosFragment;
import com.example.mbogniruvic.speedupresto.Fragments.RefuseFragment;
import com.example.mbogniruvic.speedupresto.itemModels.BiographieItemModel;
import com.example.mbogniruvic.speedupresto.itemModels.CommentItemsModel;
import com.example.mbogniruvic.speedupresto.itemModels.ContactItemModel;
import com.example.mbogniruvic.speedupresto.itemModels.PlusItemModels;
import com.example.mbogniruvic.speedupresto.models.Restaurant;
import com.example.mbogniruvic.speedupresto.models.Review;
import com.github.aakira.expandablelayout.Utils;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    List<BiographieItemModel> biographs;
    List<ContactItemModel> contacts;
    List<PlusItemModels> plusItems;
    List<CommentItemsModel> commentItems;
    ViewPager viewPager;
    TabLayout tabLayout;

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

        viewPager = (ViewPager) findViewById(R.id.profile_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.prof_tab);
        tabLayout.setupWithViewPager(viewPager);

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

    private void prepareComment() {

        commentItems=new ArrayList<>();
        List<Review> comments=new ArrayList<>();

        Review comment=new Review();
        comment.setNote(7.5f);
        comment.setDate("Tuesday, 25, 2018");
        comment.setComment(getString(R.string.mini_text));
        comments.add(comment);

        comment=new Review();
        comment.setNote(8.0f);
        comment.setDate("Tuesday, 25, 2018");
        comment.setComment(getString(R.string.mini_text));
        comments.add(comment);

        comment=new Review();
        comment.setNote(2.5f);
        comment.setDate("Friday, 27, 2018");
        comment.setComment(getString(R.string.mini_text));
        comments.add(comment);

        CommentItemsModel item=new CommentItemsModel(
                comments,
                R.color.material_light_blue_500,
                R.color.white,
                Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR)
        );

        commentItems.add(item);

    }

    private void preparePlus() {

        plusItems=new ArrayList<>();

        plusItems.add(new PlusItemModels(
                new Restaurant(),
                R.color.material_light_blue_500,
                R.color.white,
                Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR)
        ));

    }

    private void prepareContact() {

        contacts=new ArrayList<>();

        contacts.add(new ContactItemModel(
                "695715681 / 671030522",
                "toto_tata@gmail.com",
                R.color.material_light_blue_500,
                R.color.white,
                Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR)));

    }

    private void prepareBiographie() {
        biographs=new ArrayList<>();

        biographs.add(new BiographieItemModel(
                getString(R.string.mini_text),
                R.color.material_light_blue_500,
                R.color.white,
                Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR)));

    }
}
