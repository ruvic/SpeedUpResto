package com.example.mbogniruvic.speedupresto.rest;

import com.example.mbogniruvic.speedupresto.models.Category;
import com.example.mbogniruvic.speedupresto.models.CategoryMenuResponse;
import com.example.mbogniruvic.speedupresto.models.CategoryResponse;
import com.example.mbogniruvic.speedupresto.models.MenuItem;
import com.example.mbogniruvic.speedupresto.models.UpdateMenuitemResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    /*@GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);*/

    @GET("menuItem/restau/{restauID}")
    Call<CategoryMenuResponse> getMenusByCategories(@Path("restauID") String restauID);

    @GET("categoryMenu")
    Call<CategoryResponse> getAllCategories();

    @PUT("menuItem/{itemMenuId}")
    @FormUrlEncoded
    Call<UpdateMenuitemResponse> updateMenuItem(
                @Path("itemMenuId") String itemMenuId,
                @Field("nom") String nom,
                @Field("image") String image,
                @Field("price") double price,
                @Field("desc") String desc,
                @Field("catID") String catID,
                @Field("restauID") String restauID,
                @Field("disponible") boolean disponible
            );





}
