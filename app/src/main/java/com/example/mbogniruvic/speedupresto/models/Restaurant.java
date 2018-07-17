package com.example.mbogniruvic.speedupresto.models;

import com.example.mbogniruvic.speedupresto.MainActivity;
import com.example.mbogniruvic.speedupresto.Tasks.RestaurantPreferencesDB;
import com.google.gson.annotations.SerializedName;

public class Restaurant {

    @SerializedName("_id")
    private String id;

    @SerializedName("nom")
    private String nom;

    @SerializedName("code")
    private String code;

    @SerializedName("image")
    private String image;

    @SerializedName("note")
    private float note;

    @SerializedName("email")
    private String email;

    @SerializedName("city")
    private String city;

    @SerializedName("quartier")
    private String quartier;

    @SerializedName("phone")
    private String phone;

    @SerializedName("nber_note")
    private long nber_note;

    @SerializedName("min_order")
    private int min_order;

    @SerializedName("fee_delivery")
    private long fee_delivery;

    @SerializedName("time_delivery")
    private long time_delivery;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("bio")
    private String bio;

    @SerializedName("state")
    private boolean state;

    public Restaurant(String id, String nom, String code, String image, float note, String email, String city, String quartier, String phone, long nber_note, int min_order, long fee_delivery,
                      long time_delivery, String latitude, String longitude, String bio, boolean state) {
        this.id = id;
        this.nom = nom;
        this.code = code;
        this.image = image;
        this.note = note;
        this.email = email;
        this.city = city;
        this.quartier = quartier;
        this.phone = phone;
        this.nber_note = nber_note;
        this.min_order = min_order;
        this.fee_delivery = fee_delivery;
        this.time_delivery = time_delivery;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bio = bio;
        this.state = state;
    }

    public Restaurant() {

    }

    public Restaurant(RestaurantPreferencesDB shDB) {
        RestaurantPreferencesDB sharedDB=shDB;

        this.id=sharedDB.getString(RestaurantPreferencesDB.ID_KEY,"");
        this.fee_delivery=sharedDB.getLong(RestaurantPreferencesDB.FEE_DELIVERY_KEY,0);
        this.time_delivery=sharedDB.getLong(RestaurantPreferencesDB.TIME_DELIVERY_KEY,0);
        this.min_order=sharedDB.getInt(RestaurantPreferencesDB.MIN_ORDER_KEY,0);
        this.latitude=sharedDB.getString(RestaurantPreferencesDB.LATITUDE_KEY,"");
        this.longitude=sharedDB.getString(RestaurantPreferencesDB.LONGITUDE_KEY,"");
        this.image=sharedDB.getString(RestaurantPreferencesDB.IMAGE_KEY,"");
        this.note=sharedDB.getFloat(RestaurantPreferencesDB.NOTE_KEY,0f);

        /*this.nom = "Restaurant le Pititi";
        this.image = "";
        this.note = 3f;
        this.nber_note = 185;
        this.min_order = 2500;
        this.fee_delivery = 500;
        this.time_delivery = 120;
        this.city = "city";
        this.quartier = "quartier";
        this.id = "identifiant";

        this.code = "CODE";

        this.email = "";
        this.phone = "69";
        this.latitude = "latitude";
        this.longitude = "longitude";
        this.bio = "bio";
        this.state = true;*/
    }

    public Restaurant(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setNber_note(long nber_note) {
        this.nber_note = nber_note;
    }

    public void setFee_delivery(long fee_delivery) {
        this.fee_delivery = fee_delivery;
    }

    public void setTime_delivery(long time_delivery) {
        this.time_delivery = time_delivery;
    }

    public int getMin_order() {
        return min_order;
    }

    public void setMin_order(int min_order) {
        this.min_order = min_order;
    }

    public long getFee_delivery() {
        return fee_delivery;
    }

    public void setFee_delivery(int fee_delivery) {
        this.fee_delivery = fee_delivery;
    }

    public long getTime_delivery() {
        return time_delivery;
    }

    public void setTime_delivery(int time_delivery) {
        this.time_delivery = time_delivery;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
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


    public void setNote(float note) {
        this.note = note;
    }


    public float getNote() {
        return note;
    }


    public long getNber_note() {
        return nber_note;
    }

    public void setNber_note(int nber_note) {
        this.nber_note = nber_note;
    }


    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", image='" + image + '\'' +
                ", note=" + note +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", quartier='" + quartier + '\'' +
                ", phone=" + phone +
                ", nber_note=" + nber_note +
                ", min_order=" + min_order +
                ", fee_delivery=" + fee_delivery +
                ", time_delivery=" + time_delivery +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", bio='" + bio + '\'' +
                ", state=" + state +
                '}';
    }
}
