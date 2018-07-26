package com.example.mbogniruvic.speedupresto.models;

import com.google.gson.annotations.SerializedName;

public class UploadImageResponse {

    @SerializedName("result")
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
