package com.example.mbogniruvic.speedupresto.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryMenuResponse {

    @SerializedName("result")
    private List<CategoryMenu> result;
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;


    public List<CategoryMenu> getResult() {
        return result;
    }

    public void setResult(List<CategoryMenu> result) {
        this.result = result;
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
