package com.example.mbogniruvic.speedupresto.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllRestauReviewsResponse {

    @SerializedName("result")
    private List<Review> reviews;
    @SerializedName("error")
    private boolean error;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
