package com.example.mbogniruvic.speedupresto.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mbogniruvic.speedupresto.R;
import com.example.mbogniruvic.speedupresto.models.Commande;
import com.example.mbogniruvic.speedupresto.models.Review;

import java.util.List;

public class RecyclerViewAvisItemsAdapter extends RecyclerView.Adapter<RecyclerViewAvisItemsAdapter.MyViewHolder> {

    List<Review> reviewsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nomReview, descriptionView;

        public MyViewHolder(View view) {
            super(view);

            nomReview=(TextView)view.findViewById(R.id.nom_review);
            descriptionView=(TextView)view.findViewById(R.id.description_rev);
        }
    }

    public RecyclerViewAvisItemsAdapter(List<Review> reviewsList) {
        this.reviewsList = reviewsList;
    }

    @Override
    public RecyclerViewAvisItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_item_list_review, parent, false);

        return new RecyclerViewAvisItemsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAvisItemsAdapter.MyViewHolder holder, int position) {
        Review review= reviewsList.get(position);
        holder.nomReview.setText("Paul Xavier");
        holder.descriptionView.setText(review.getComment());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }
}
