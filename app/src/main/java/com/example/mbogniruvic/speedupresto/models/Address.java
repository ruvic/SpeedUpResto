package com.example.mbogniruvic.speedupresto.models;

public class Address {

    private int id;
    private String nom;
    private String ville;
    private String quartier;
    private String phone;
    private String details;
    private String Latitude;
    private String longitude;

    public Address(int id, String nom, String ville, String quartier, String phone,
                   String details, String latitude, String longitude) {
        this.id = id;
        this.nom = nom;
        this.ville = ville;
        this.quartier = quartier;
        this.phone = phone;
        this.details = details;
        Latitude = latitude;
        this.longitude = longitude;
    }

    public Address() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", ville='" + ville + '\'' +
                ", quartier='" + quartier + '\'' +
                ", phone='" + phone + '\'' +
                ", details='" + details + '\'' +
                ", Latitude='" + Latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
