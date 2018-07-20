package com.example.mbogniruvic.speedupresto.models;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("_id")
    private String id;
    @SerializedName("titre")
    private String titre;
    @SerializedName("code")
    private String code;
    @SerializedName("__v")
    private String __v;

    private String timestamps;

    //DEBUT : SQLite attributes
    public static final String TABLE_NAME = "CategoryMenu";
    private int idTable;

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITRE = "titre";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + COLUMN_TITRE + " TEXT,"
                    + COLUMN_CODE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    //FIN : SQLite attributes

    public Category(String id, String titre, String code) {
        this.id = id;
        this.titre = titre;
        this.code = code;
    }

    public Category() {
    }

    public Category(String id, String titre, String code, String __v) {
        this.id = id;
        this.titre = titre;
        this.code = code;
        this.__v = __v;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id : "+this.getId()+
                "code : "+this.getCode()+
                "titre : "+this.getTitre();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }
}
