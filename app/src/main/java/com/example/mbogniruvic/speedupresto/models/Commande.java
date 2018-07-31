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
    @SerializedName("updatedAt")
    private String dateMaj;
    @SerializedName("state")
    private String etat;
    @SerializedName("cartID")
    private String cartID;

    private String heure;
    private String jour;




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
    public static final String COLUMN_ETAT = "etat";
    public static final String COLUMN_CREATED_AT = "createdAt";
    public static final String COLUMN_UPDATED_AT = "updateAT";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + COLUMN_MENU_ID + " TEXT,"
                    + COLUMN_QTE + " INTEGER,"
                    + COLUMN_ETAT + " TEXT,"
                    + COLUMN_MONTANT + " REAL,"
                    + COLUMN_CREATED_AT + " TEXT,"
                    + COLUMN_UPDATED_AT + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + " FOREIGN KEY ("+COLUMN_MENU_ID+") REFERENCES "+MenuItem.TABLE_NAME+"("+MenuItem.COLUMN_ID+")"
                    + ")";

    public Commande() {
    }

    public Commande(String id, MenuItem menu, int qte, double montant, String date, String etat, String cartID) {

        this.id = id;
        this.menu = menu;
        this.qte = qte;
        this.montant = montant;
        this.date = date;
        this.etat = etat;
        this.cartID = cartID;
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

    public void setMontant(double montant) {
        this.montant = montant;
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

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public String getHeure() {
        String heure =date.split("T")[1];
        return heure.split("\\.")[0];
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getJour() {
        return date.split("T")[0];
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getDateMaj() {
        String jour=dateMaj.split("T")[0];
        String heure=(dateMaj.split("T")[1]).split("\\.")[0];
        return jour+" à "+heure;
    }

    public void setDateMaj(String dateMaj) {
        this.dateMaj = dateMaj;
    }
}
