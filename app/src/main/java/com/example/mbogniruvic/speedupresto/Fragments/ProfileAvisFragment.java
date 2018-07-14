package com.example.mbogniruvic.speedupresto.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mbogniruvic.speedupresto.Adapters.CommandesAdapter;
import com.example.mbogniruvic.speedupresto.Adapters.RecyclerViewAvisItemsAdapter;
import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.models.Review;

import java.util.ArrayList;
import java.util.List;

public class ProfileAvisFragment extends Fragment {

    List<Review> reviewsList;
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


        prepareReviews();

        RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerV_avis);
        RecyclerViewAvisItemsAdapter mAdapter = new RecyclerViewAvisItemsAdapter(reviewsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    private void prepareReviews() {

        reviewsList=new ArrayList<>();
        for (int i=0 ; i<20; i++){
            Review review=new Review();
            review.setComment(getString(R.string.mini_text));
            review.setDate("08/07/2018 , 12:10");
            review.setNote(3.5f);
            reviewsList.add(review);
        }

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
