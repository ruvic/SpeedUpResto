package com.example.mbogniruvic.speedupresto.models;

import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("_id")
    private String id;
    @SerializedName("restauID")
    private String idRest;
    @SerializedName("menuID")
    private String idMenu;
    @SerializedName("userID")
    private User idUser;
    @SerializedName("note")
    private float note;
    @SerializedName("comment")
    private String comment;
    @SerializedName("updatedAt")
    private String date;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdRest() {
        return idRest;
    }

    public void setIdRest(String idRest) {
        this.idRest = idRest;
    }

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Review(String id, String idRest, String idMenu, User idUser, float note, String comment, String date) {

        this.id = id;
        this.idRest = idRest;
        this.idMenu = idMenu;
        this.idUser = idUser;
        this.note = note;
        this.comment = comment;
        this.date = date;
    }
}
