package com.example.mbogniruvic.speedupresto.models;

import com.google.gson.annotations.SerializedName;

public class UpdateCommandeResponse {

    @SerializedName("result")
    private Commande commande;
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
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
