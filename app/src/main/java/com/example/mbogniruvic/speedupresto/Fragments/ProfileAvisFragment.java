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
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.Adapters.CommandesAdapter;
import com.example.mbogniruvic.speedupresto.Adapters.RecyclerViewAvisItemsAdapter;
import com.example.mbogniruvic.speedupresto.MainActivity;
import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.Tasks.RestaurantPreferencesDB;
import com.example.mbogniruvic.speedupresto.models.AllRestauReviewsResponse;
import com.example.mbogniruvic.speedupresto.models.RestaurantResponse;
import com.example.mbogniruvic.speedupresto.models.Review;
import com.example.mbogniruvic.speedupresto.rest.ApiClient;
import com.example.mbogniruvic.speedupresto.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileAvisFragment extends Fragment {

    private List<Review> reviewsList;
    private RestaurantPreferencesDB sharedDB;
    private Context context;
    private View view;

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
        view=rootView;
        String restauID=sharedDB.getString(RestaurantPreferencesDB.ID_KEY, "");

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AllRestauReviewsResponse> call=apiService.getAllRestauReviews(restauID);

        call.enqueue(new Callback<AllRestauReviewsResponse>() {

            @Override
            public void onResponse(Call<AllRestauReviewsResponse> call, Response<AllRestauReviewsResponse> response) {
                if(!response.body().isError()){

                    reviewsList=response.body().getReviews();
                    RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.recyclerV_avis);
                    RecyclerViewAvisItemsAdapter mAdapter = new RecyclerViewAvisItemsAdapter(reviewsList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);

                }else {
                    Toast.makeText(context, "Erreur de chargement des avis", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AllRestauReviewsResponse> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
