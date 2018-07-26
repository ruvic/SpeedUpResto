package com.example.mbogniruvic.speedupresto.models;

import com.google.gson.annotations.SerializedName;

public class Commande {

    @SerializedName("_id")
    private String id;
    @SerializedName("menuID")
    private MenuItem menu;
    @SerializedName("quantite")
    private int qte;
    @SerializedName("price")
    private double montant;
    @SerializedName("createdAt")
    private String date;
    @SerializedName("state")
    private String etat;



    private String heure;

    public static  final String STATUS_EN_ATTENTE="en attente";
    public static  final String STATUS_VALIDE="validé";
    public static  final String STATUS_REFUSE="refusé";
    public static  final String STATUS_LIVRE="livré";

    //SQLite attributes
    public static final String TABLE_NAME = "Commande";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MENU_ID = "menuID";
    public static final String COLUMN_QTE = "qte";
    public static final String COLUMN_MONTANT = "montant";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + COLUMN_MENU_ID + " TEXT,"
                    + COLUMN_QTE + " INTEGER,"
                    + COLUMN_MONTANT + " REAL,"
                    + COLUMN_DATE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + " FOREIGN KEY ("+COLUMN_MENU_ID+") REFERENCES "+MenuItem.TABLE_NAME+"("+MenuItem.COLUMN_ID+")"
                    + ")";

    public Commande() {
    }

    public Commande(String id, MenuItem menu, int qte, double montant, String date, String etat) {

        this.id = id;
        this.menu = menu;
        this.qte = qte;
        this.montant = montant;
        this.date = date;
        this.etat = etat;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MenuItem getMenu() {
        return menu;
    }

    public void setMenu(MenuItem menu) {
        this.menu = menu;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double price) {
        this.montant = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }
}
