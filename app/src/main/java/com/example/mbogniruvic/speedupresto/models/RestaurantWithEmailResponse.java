package com.example.mbogniruvic.speedupresto.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantWithEmailResponse {

    @SerializedName("result")
    private List<Restaurant> restaurants;
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
