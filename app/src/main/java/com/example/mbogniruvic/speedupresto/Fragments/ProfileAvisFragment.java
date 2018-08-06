package com.example.mbogniruvic.speedupresto.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Adapters.RecyclerViewAvisItemsAdapter;
import com.example.mbogniruvic.speedupresto.MainActivity;
import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.Tasks.StoreReviewsTask;
import com.example.mbogniruvic.speedupresto.Utils.RestaurantPreferencesDB;
import com.example.mbogniruvic.speedupresto.models.AllRestauReviewsResponse;
import com.example.mbogniruvic.speedupresto.models.Review;
import com.example.mbogniruvic.speedupresto.rest.ApiClient;
import com.example.mbogniruvic.speedupresto.rest.ApiInterface;
import com.example.mbogniruvic.speedupresto.sqlite.DatabaseHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileAvisFragment extends Fragment {

    private List<Review> reviewsList;
    private RestaurantPreferencesDB sharedDB;
    private DatabaseHelper db;
    private Context context;
    private View view;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private RelativeLayout connLayout;
    private RelativeLayout emptyLayout;
    private Button conn_refresh;
    private Button empty_refresh;

    public ProfileAvisFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_avis, container, false);

        sharedDB= MainActivity.shareDB;
        context=getContext();
        db=new DatabaseHelper(context);
        view=rootView;

        //get Adress
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerV_avis);
        progressBar=(ProgressBar) view.findViewById(R.id.progress_review);
        connLayout=(RelativeLayout) view.findViewById(R.id.avis_conn_error);
        emptyLayout=(RelativeLayout) view.findViewById(R.id.avis_empty);
        conn_refresh=(Button) view.findViewById(R.id.avis_conn_refresh);
        empty_refresh=(Button) view.findViewById(R.id.avis_empty_refresh);

        refresh();

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

        return rootView;
    }

    private void refresh(){

        String restauID=sharedDB.getString(RestaurantPreferencesDB.ID_KEY, "");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AllRestauReviewsResponse> call=apiService.getAllRestauReviews(restauID);

        call.enqueue(new Callback<AllRestauReviewsResponse>() {

            @Override
            public void onResponse(Call<AllRestauReviewsResponse> call, Response<AllRestauReviewsResponse> response) {

                if(!response.body().isError()){

                    reviewsList=response.body().getReviews();

                    if (reviewsList!=null && reviewsList.size()!=0) {

                        new StoreReviewsTask(context, db).execute(reviewsList);

                        updateInterfaceViews();

                    } else {

                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.VISIBLE);
                        connLayout.setVisibility(View.GONE);

                    }

                }else {

                    Toast.makeText(context, "Erreur de chargement des avis", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<AllRestauReviewsResponse> call, Throwable t) {


                reviewsList=db.getAllReviews();
                if (reviewsList.size()!=0) {
                    updateInterfaceViews();
                } else {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    connLayout.setVisibility(View.GONE);
                }


            }
        });
    }

    private void updateInterfaceViews(){

        RecyclerViewAvisItemsAdapter mAdapter = new RecyclerViewAvisItemsAdapter(reviewsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        connLayout.setVisibility(View.GONE);

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.closeDB();
    }
}
