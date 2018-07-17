package com.example.mbogniruvic.speedupresto.models;

import com.google.gson.annotations.SerializedName;

public class UpdateRestaurantResponse {

    @SerializedName("result")
    private Restaurant restaurant;
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
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
