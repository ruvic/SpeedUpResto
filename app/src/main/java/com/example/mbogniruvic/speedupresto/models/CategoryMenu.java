package com.example.mbogniruvic.speedupresto.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryMenu {

    @SerializedName("categorie")
    private String categorie;

    @SerializedName("menus")
    private List<MenuItem> menus;


    public CategoryMenu(String categorie, List<MenuItem> menus) {
        this.categorie = categorie;
        this.menus = menus;
    }

    public CategoryMenu() {
        this.categorie = "Offres & Bons Plans";
    }

    public CategoryMenu(String categorie) {
        this.categorie = categorie;
    }

    public List<MenuItem> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuItem> menus) {
        this.menus = menus;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
}
