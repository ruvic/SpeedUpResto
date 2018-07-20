package com.example.mbogniruvic.speedupresto.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mbogniruvic.speedupresto.models.Category;
import com.example.mbogniruvic.speedupresto.models.Commande;
import com.example.mbogniruvic.speedupresto.models.MenuItem;
import com.example.mbogniruvic.speedupresto.models.Review;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "speedUpRestau_db";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Category.CREATE_TABLE);
        db.execSQL(MenuItem.CREATE_TABLE);
        db.execSQL(Commande.CREATE_TABLE);
        db.execSQL(Review.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + Review.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Commande.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MenuItem.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Category.TABLE_NAME);

        onCreate(db);
    }

    /**
     * Méthode pour la gestion revues
     * */
    public void createReview(Review review){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Review.COLUMN_ID, review.getId());
        values.put(Review.COLUMN_FIRST_NAME, review.getIdUser().getFirstName());
        values.put(Review.COLUMN_LAST_NAME, review.getIdUser().getLastName());
        values.put(Review.COLUMN_NOTE, review.getNote());
        values.put(Review.COLUMN_COMMENT, review.getComment());
        values.put(Review.COLUMN_DATE, review.getDate());

        // insert row
        db.insert(Review.TABLE_NAME, null, values);

    }

    public Review getRecentReview(){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + Review.TABLE_NAME + " ORDER BY "
                + "date("+ Review.COLUMN_TIMESTAMP +") DESC limit 1";

        Cursor c = db.rawQuery(selectQuery, null);

        if(c!=null){
            c.moveToFirst();
            Review review=new Review();
            review.setId(c.getString(c.getColumnIndex(Review.COLUMN_ID)));
            review.setNote(c.getFloat(c.getColumnIndex(Review.COLUMN_NOTE)));
            review.setDate(c.getString(c.getColumnIndex(Review.COLUMN_DATE)));
            review.setComment(c.getString(c.getColumnIndex(Review.COLUMN_COMMENT)));
            review.getIdUser().setFirstName(c.getString(c.getColumnIndex(Review.COLUMN_FIRST_NAME)));
            review.getIdUser().setLastName(c.getString(c.getColumnIndex(Review.COLUMN_LAST_NAME)));
            return  review;
        }else{
            return  null;
        }
    }

    public List<Review> getAllReviews(){
        List<Review> list = new ArrayList<Review>();
        String selectQuery = "SELECT  * FROM " + Review.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Review review=new Review();
                review.setId(c.getString(c.getColumnIndex(Review.COLUMN_ID)));
                review.setNote(c.getFloat(c.getColumnIndex(Review.COLUMN_NOTE)));
                review.setDate(c.getString(c.getColumnIndex(Review.COLUMN_DATE)));
                review.setComment(c.getString(c.getColumnIndex(Review.COLUMN_COMMENT)));
                review.getIdUser().setFirstName(c.getString(c.getColumnIndex(Review.COLUMN_FIRST_NAME)));
                review.getIdUser().setLastName(c.getString(c.getColumnIndex(Review.COLUMN_LAST_NAME)));

                list.add(review);
            } while (c.moveToNext());
        }

        return  list;
    }

    /**
     * Méthode de gestion des catégories
     */

    public void createCategory(Category cat){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Category.COLUMN_ID, cat.getId());
        values.put(Category.COLUMN_TITRE, cat.getTitre());
        values.put(Category.COLUMN_CODE, cat.getCode());

        // insert row
        db.insert(Category.TABLE_NAME, null, values);

    }


    public Category getCategory(String catID){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + Category.TABLE_NAME + " WHERE "
                +Category.COLUMN_ID +"="+"'"+catID+"'";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c!=null) {
            c.moveToFirst();
            Category cat=new Category();
            cat.setId(c.getString(c.getColumnIndex(Category.COLUMN_ID)));
            cat.setCode(c.getString(c.getColumnIndex(Category.COLUMN_CODE)));
            cat.setTitre(c.getString(c.getColumnIndex(Category.COLUMN_TITRE)));
            return  cat;

        } else {
            return null;
        }
    }

    public List<Category> getAllCategories(){
        List<Category> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + Category.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Category cat=new Category();
                cat.setId(c.getString(c.getColumnIndex(Category.COLUMN_ID)));
                cat.setCode(c.getString(c.getColumnIndex(Category.COLUMN_CODE)));
                cat.setTitre(c.getString(c.getColumnIndex(Category.COLUMN_TITRE)));

                list.add(cat);
            } while (c.moveToNext());
        }

        return  list;
    }

    public boolean isCategoryExist(Category cat){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + Category.TABLE_NAME +" " +
                " WHERE "+Category.COLUMN_ID+"="+cat.getId();

        Cursor c = db.rawQuery(selectQuery, null);

        return c!=null;
    }

    public long updateCategorie(Category cat){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Category.COLUMN_CODE, cat.getCode());
        values.put(Category.COLUMN_TITRE, cat.getTitre());

        // updating row
        return db.update(Category.TABLE_NAME, values, Category.COLUMN_ID + " = ?",
                new String[] { String.valueOf(cat.getId()) });

    }

    /**
     * Méthode pour la gestion des menuItems
     */

    public void createMenuItem(MenuItem menu){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MenuItem.COLUMN_ID, menu.getId());
        values.put(MenuItem.COLUMN_CAT_ID, menu.getIdCat());
        values.put(MenuItem.COLUMN_NOM, menu.getNom());
        values.put(MenuItem.COLUMN_IMAGE, menu.getImage());
        values.put(MenuItem.COLUMN_PRICE, menu.getPrice());
        values.put(MenuItem.COLUMN_DESC, menu.getDesc());
        values.put(MenuItem.COLUMN_DISP, (menu.isDispo())?1:0);

        // insert row
        db.insert(MenuItem.TABLE_NAME, null, values);
    }

    public MenuItem getRecentMenuItem(){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + MenuItem.TABLE_NAME + " ORDER BY "
                + "date("+ Review.COLUMN_TIMESTAMP +") DESC limit 1";

        Cursor c = db.rawQuery(selectQuery, null);

        if(c!=null){
            c.moveToFirst();
            MenuItem menu=new MenuItem();
            menu.setId(c.getString(c.getColumnIndex(MenuItem.COLUMN_ID)));
            menu.setIdCat(c.getString(c.getColumnIndex(MenuItem.COLUMN_CAT_ID)));
            menu.setNom(c.getString(c.getColumnIndex(MenuItem.COLUMN_NOM)));
            menu.setImage(c.getString(c.getColumnIndex(MenuItem.COLUMN_IMAGE)));
            menu.setPrice(c.getDouble(c.getColumnIndex(MenuItem.COLUMN_PRICE)));
            menu.setDesc(c.getString(c.getColumnIndex(MenuItem.COLUMN_DESC)));
            menu.setDispo(c.getInt(c.getColumnIndex(MenuItem.COLUMN_DISP))==1);
            return  menu;
        }else{
            return  null;
        }
    }

    public List<MenuItem> getAllMenuItems(){

        List<MenuItem> list = new ArrayList<MenuItem>();

        String selectQuery = "SELECT  * FROM " + MenuItem.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                MenuItem menu=new MenuItem();
                menu.setId(c.getString(c.getColumnIndex(MenuItem.COLUMN_ID)));
                menu.setIdCat(c.getString(c.getColumnIndex(MenuItem.COLUMN_CAT_ID)));
                menu.setNom(c.getString(c.getColumnIndex(MenuItem.COLUMN_NOM)));
                menu.setImage(c.getString(c.getColumnIndex(MenuItem.COLUMN_IMAGE)));
                menu.setPrice(c.getDouble(c.getColumnIndex(MenuItem.COLUMN_PRICE)));
                menu.setDesc(c.getString(c.getColumnIndex(MenuItem.COLUMN_DESC)));
                menu.setDispo(c.getInt(c.getColumnIndex(MenuItem.COLUMN_DISP))==1);

                list.add(menu);
            } while (c.moveToNext());
        }

        return  list;
    }

    public long updateMenuItem(MenuItem menu){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MenuItem.COLUMN_ID, menu.getId());
        values.put(MenuItem.COLUMN_CAT_ID, menu.getIdCat());
        values.put(MenuItem.COLUMN_NOM, menu.getNom());
        values.put(MenuItem.COLUMN_IMAGE, menu.getImage());
        values.put(MenuItem.COLUMN_PRICE, menu.getPrice());
        values.put(MenuItem.COLUMN_DESC, menu.getDesc());
        values.put(MenuItem.COLUMN_DISP, (menu.isDispo())?1:0);

        // updating row
        return db.update(MenuItem.TABLE_NAME, values, MenuItem.COLUMN_ID + " = ?",
                new String[] { String.valueOf(menu.getId()) });

    }

    /**
     * Methode de gestion des commandes
     */

    public void createCommande(Commande cmd){

    }

    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}
