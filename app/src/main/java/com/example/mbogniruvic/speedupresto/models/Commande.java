package com.example.mbogniruvic.speedupresto.models;

public class Commande {
    private int id;
    private MenuItem menu;
    private int quantite;
    private double montant;
    private String heure;

    public Commande(MenuItem menu, int quantite, String heure) {
        this.setMenu(menu);
        this.setQuantite(quantite);
        this.setMontant(menu.getPrice()*quantite);
        this.setHeure(heure);
    }

    public Commande(int id, MenuItem menu, int quantite, String heure) {
        this.setId(id);
        this.setMenu(menu);
        this.setQuantite(quantite);
        this.setMontant(menu.getPrice()*quantite);
        this.setHeure(heure);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
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
