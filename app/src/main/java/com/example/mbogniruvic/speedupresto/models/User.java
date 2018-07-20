package com.example.mbogniruvic.speedupresto.models;

import com.google.gson.annotations.SerializedName;


public class User {

    @SerializedName("_id")
    private String id;

    @SerializedName("email")
    private String email;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("password")
    private String password;

    @SerializedName("photo")
    private String photo;

    @SerializedName("city")
    private String city;

    @SerializedName("quartier")
    private String quartier;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public User() {
    }

    @Override
    public String toString() {
        return this.getLastName()+"  "+this.getFirstName();
    }

    public User(String id, String email, String lastName, String firstName,
                String phone, String password, String photo, String city, String quartier) {
        this.id = id;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phone = phone;
        this.password = password;
        this.photo = photo;
        this.city = city;
        this.quartier = quartier;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }
}
