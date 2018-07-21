package com.example.mbogniruvic.speedupresto.models;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbogniruvic.speedupresto.R;
import com.google.gson.annotations.SerializedName;

public class MenuItem implements Parcelable {

    @SerializedName("_id")
    private String id;

    @SerializedName("catID")
    private String IdCat;

    @SerializedName("image")
    private String image;

    @SerializedName("nom")
    private String nom;

    @SerializedName("price")
    private double price;

    @SerializedName("desc")
    private String desc;

    @SerializedName("disponible")
    private boolean isDispo;

    private String nomCat;
    private String timestamps;

    //SQLite attributes
    public static final String TABLE_NAME = "MenuItem";
    private int idTable;

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CAT_ID = "catID";
    public static final String COLUMN_NOM = "nom";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_DISP = "disponible";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + COLUMN_CAT_ID + " TEXT,"
                    + COLUMN_NOM + " TEXT,"
                    + COLUMN_IMAGE + " TEXT,"
                    + COLUMN_PRICE + " REAL,"
                    + COLUMN_DESC + " TEXT,"
                    + COLUMN_DISP + " NUMERIC,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + " FOREIGN KEY ("+COLUMN_CAT_ID+") REFERENCES "+Category.TABLE_NAME+"("+Category.COLUMN_ID+")"
                    + ")";


    public MenuItem(String id, String idCat, String image, String nom,
                    double price, String desc, boolean isDispo) {
        this.id = id;
        this.IdCat = idCat;
        this.image = image;
        this.nom = nom;
        this.price = price;
        this.desc = desc;
        this.isDispo = isDispo;
    }


    public MenuItem(int id) {

        this.nom = "beafsteak Plantain + Pomme";
        this.price = 1500;
        this.isDispo = true;

        this.id = "id " + id;
        this.IdCat = "idCat";
        this.image = "http://speedup.com";
        this.desc = "description";

    }

    protected MenuItem(Parcel in) {
        id = in.readString();
        IdCat = in.readString();
        image = in.readString();
        nom = in.readString();
        price = in.readDouble();
        desc = in.readString();
        isDispo = in.readInt()==1;
    }

    public MenuItem() {
    }

    @Override
    public String toString() {
        String result="MenuItem { Id_menu : "+this.getId()+"\n"
                +"nom : "+this.getNom()+"\n"
                +"image : "+this.getImage()+"\n"
                +"catID : "+this.getIdCat()+"\n"
                +"price : "+this.getPrice()+"\n"
                +"desc : "+this.getDesc()+"\n"
                +"dispo : "+this.isDispo+" }";
        return result;
    }

    public static final Creator<MenuItem> CREATOR = new Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel in) {
            return new MenuItem(in);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(getIdCat());
        parcel.writeString(image);
        parcel.writeString(nom);
        parcel.writeDouble(price);
        parcel.writeString(desc);
        parcel.writeInt(isDispo?1:0);
    }


    public void setIdCat(String idCat) {
        IdCat = idCat;
    }

    public boolean isDispo() {
        return isDispo;
    }

    public void setDispo(boolean dispo) {
        isDispo = dispo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCat() {
        return IdCat;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public boolean isEqual(MenuItem obj) {

        if (this.id.equalsIgnoreCase(obj.getId())){

            return true;
        }

        return false;
    }

    public String getNomCat() {
        return nomCat;
    }

    public void setNomCat(String nomCat) {
        this.nomCat = nomCat;
    }
}
