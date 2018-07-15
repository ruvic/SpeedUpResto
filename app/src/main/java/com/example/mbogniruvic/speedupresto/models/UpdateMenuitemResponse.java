package com.example.mbogniruvic.speedupresto.models;

import com.google.gson.annotations.SerializedName;

public class UpdateMenuitemResponse {

    @SerializedName("result")
    private MenuItem menu;
    @SerializedName("error")
    private String error;
    @SerializedName("message")
    private String message;

    public MenuItem getMenu() {
        return menu;
    }

    public void setMenu(MenuItem menu) {
        this.menu = menu;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
