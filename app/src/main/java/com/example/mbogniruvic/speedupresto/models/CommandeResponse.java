package com.example.mbogniruvic.speedupresto.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommandeResponse {

    @SerializedName("result")
    private List<Commande> commandes;
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;

    public List<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(List<Commande> commandes) {
        this.commandes = commandes;
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
