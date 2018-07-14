package com.example.mbogniruvic.speedupresto.models;

public class Review {

    private int id;
    private int idRest;
    private int idMenu;
    private int idUser;
    private float note;
    private String comment;
    private String date;


    public Review(int id, int idRest, int idMenu, int idUser, float note, String comment, String date) {
        this.id = id;
        this.idRest = idRest;
        this.idMenu = idMenu;
        this.idUser = idUser;
        this.note = note;
        this.comment = comment;
        this.date = date;
    }

    public Review() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRest() {
        return idRest;
    }

    public void setIdRest(int idRest) {
        this.idRest = idRest;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
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
}
