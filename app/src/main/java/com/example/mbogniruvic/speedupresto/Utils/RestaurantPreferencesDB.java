package com.example.mbogniruvic.speedupresto.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mbogniruvic.speedupresto.models.Restaurant;

public class RestaurantPreferencesDB {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String RESTAU_PREF_NAME = "RestaurantProfile";

    // All Shared Preferences Keys
    public static final String ID_KEY = "ID";
    public static final String NOM_KEY = "NOM";
    public static final String EMAIL_KEY = "EMAIL";
    public static final String CITY_KEY = "CITY";
    public static final String PHONE_KEY = "PHONE";
    public static final String FEE_DELIVERY_KEY = "FEE_DELIVERY";
    public static final String MIN_ORDER_KEY = "MIN_ORDER";
    public static final String TIME_DELIVERY_KEY = "TIME_DELIVERY";
    public static final String IMAGE_KEY = "IMAGE";
    public static final String QUARTIER_KEY = "QUARTIER";
    public static final String BIO_KEY = "BIO";
    public static final String NOTE_KEY = "NOTE";
    public static final String STATE_KEY = "STATE";
    public static final String PASSWORD_KEY = "PASSWORD";
    public static final String LATITUDE_KEY = "LATITUDE";
    public static final String LONGITUDE_KEY = "LONGITUDE";
    public static final String ROOT_IMAGE_KEY = "ROOT_IMAGE";



    // Constructor
    public RestaurantPreferencesDB(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(RESTAU_PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void put(Restaurant restau){

        editor.putString(ID_KEY, restau.getId());
        editor.putString(NOM_KEY, restau.getNom());
        editor.putString(EMAIL_KEY, restau.getEmail());
        editor.putString(CITY_KEY, restau.getCity());
        editor.putString(PHONE_KEY, restau.getPhone());
        editor.putLong(FEE_DELIVERY_KEY, restau.getFee_delivery());
        editor.putLong(TIME_DELIVERY_KEY, restau.getTime_delivery());
        editor.putString(IMAGE_KEY, restau.getImage());
        editor.putString(QUARTIER_KEY, restau.getQuartier());
        editor.putString(BIO_KEY, restau.getBio());
        editor.putString(LATITUDE_KEY, restau.getLatitude());
        editor.putString(LONGITUDE_KEY, restau.getLongitude());
        editor.putString(PASSWORD_KEY, restau.getPassword());
        editor.putFloat(NOTE_KEY, restau.getNote());
        editor.putBoolean(STATE_KEY, restau.isState());
        editor.putInt(MIN_ORDER_KEY, restau.getMin_order());

        editor.commit();
    }

    public String getString(String key_name, String default_value){
        return pref.getString(key_name, default_value);
    }

    public long getLong(String key_name, long default_value){
        return pref.getLong(key_name, default_value);
    }

    public float getFloat(String key_name, float default_value){
        return pref.getFloat(key_name, default_value);
    }

    public boolean getBoolean(String key_name, boolean default_value){
        return  pref.getBoolean(key_name, default_value);
    }

    public int getInt(String key_name, int default_value){
        return pref.getInt(key_name, default_value);
    }

    public void clear(){
        editor.clear();
    }

    public void putRootImagePath(String path){
        editor.putString(ROOT_IMAGE_KEY, path);
        editor.commit();
    }

}
