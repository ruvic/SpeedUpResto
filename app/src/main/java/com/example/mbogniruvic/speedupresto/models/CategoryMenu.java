package com.example.mbogniruvic.speedupresto.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryMenu implements Parcelable {

    @SerializedName("categorie")
    private String categorie;

    @SerializedName("menus")
    private List<MenuItem> menus;

    public CategoryMenu(String categorie, List<MenuItem> menus) {
        this.categorie = categorie;
        this.menus = menus;
    }

    protected CategoryMenu(Parcel in) {
        categorie = in.readString();
        menus = in.createTypedArrayList(MenuItem.CREATOR);
    }

    public static final Creator<CategoryMenu> CREATOR = new Creator<CategoryMenu>() {
        @Override
        public CategoryMenu createFromParcel(Parcel in) {
            return new CategoryMenu(in);
        }

        @Override
        public CategoryMenu[] newArray(int size) {
            return new CategoryMenu[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(categorie);
        parcel.writeTypedList(menus);
    }

    @Override
    public String toString(){
        String items="";
        for (MenuItem item : menus) {
            items+=item.toString()+" --- ";
        }
        return this.categorie+" : "+items;
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
