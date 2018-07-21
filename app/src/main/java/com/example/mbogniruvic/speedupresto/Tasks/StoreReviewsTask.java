package com.example.mbogniruvic.speedupresto.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.models.Review;
import com.example.mbogniruvic.speedupresto.sqlite.DatabaseHelper;

import java.util.List;

public class StoreReviewsTask extends AsyncTask<List<Review>, Void, Void> {

    private Context context;
    private DatabaseHelper db;

    public StoreReviewsTask(Context context, DatabaseHelper db) {
        this.context = context;
        this.db = db;
    }

    @Override
    protected Void doInBackground(List<Review>... lists) {
        List<Review> reviewList=lists[0];

        for (Review review : reviewList) {

            db.createReview(review);

        }
        
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        /*List<Review> reviewList=db.getAllReviews();
        for (Review review :reviewList) {
            Toast.makeText(context, review.toString(),Toast.LENGTH_SHORT).show();
        }*/

    }
}
