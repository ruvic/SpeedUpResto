package com.example.mbogniruvic.speedupresto.models;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("result")
    private User user;
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private  String message;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
