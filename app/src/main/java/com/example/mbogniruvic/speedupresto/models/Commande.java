package com.example.mbogniruvic.speedupresto.models;

public class Commande {
    private String id;
    private MenuItem menu;
    private int quantite;
    private double montant;
    private String heure;
    private String date;

    //SQLite attributes
    public static final String TABLE_NAME = "Commande";
    private int idTable;

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

    public Commande(MenuItem menu, int quantite, String heure) {
        this.setMenu(menu);
        this.setQuantite(quantite);
        this.setMontant(menu.getPrice()*quantite);
        this.setHeure(heure);
    }

    public Commande(String id, MenuItem menu, int quantite, String heure) {
        this.setId(id);
        this.setMenu(menu);
        this.setQuantite(quantite);
        this.setMontant(menu.getPrice()*quantite);
        this.setHeure(heure);
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

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }
}
