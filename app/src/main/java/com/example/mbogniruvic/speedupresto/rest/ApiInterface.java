package com.example.mbogniruvic.speedupresto.rest;

import com.example.mbogniruvic.speedupresto.models.AllRestauReviewsResponse;
import com.example.mbogniruvic.speedupresto.models.CategoryMenuResponse;
import com.example.mbogniruvic.speedupresto.models.CategoryResponse;
import com.example.mbogniruvic.speedupresto.models.CommandeResponse;
import com.example.mbogniruvic.speedupresto.models.CreateMenuItemResponse;
import com.example.mbogniruvic.speedupresto.models.RestaurantResponse;
import com.example.mbogniruvic.speedupresto.models.UpdateMenuitemResponse;
import com.example.mbogniruvic.speedupresto.models.UpdateRestaurantResponse;
import com.example.mbogniruvic.speedupresto.models.UploadImageResponse;
import com.example.mbogniruvic.speedupresto.models.UserResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    @POST("menuItem")
    @FormUrlEncoded
    Call<CreateMenuItemResponse> createMenuItem(
            @Field("nom") String nom,
            @Field("image") String image,
            @Field("price") double price,
            @Field("desc") String desc,
            @Field("catID") String catID,
            @Field("restauID") String restauID,
            @Field("disponible") boolean disponible
    );

    @GET("restaurants/{restauID}")
    Call<RestaurantResponse> getRestaurantProfile(@Path("restauID") String restauID);

    @GET("reviews/restau/{restauID}")
    Call<AllRestauReviewsResponse> getAllRestauReviews(@Path("restauID") String restauID);

    @GET("users/{userId}")
    Call<UserResponse> getUserWithSingleId(@Path("userId") String userId);

    @PUT("restaurants/{restaurantId}")
    @FormUrlEncoded
    Call<UpdateRestaurantResponse> updateRestaurant(
            @Path("restaurantId") String itemMenuId,
            @Field("nom") String nom,
            @Field("email") String email,
            @Field("city") String city,
            @Field("phone") String phone,
            @Field("fee_delivery") long fee_delivery,
            @Field("min_order") int min_order,
            @Field("time_delivery") long time_delivery,
            @Field("image") String image,
            @Field("quartier") String quartier,
            @Field("bio") String bio,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("note") float note
    );

    @GET("cartItems/restau/{restauID}/day/{date}")
    Call<CommandeResponse> getAllCommandes(
            @Path("restauID") String restauID,
            @Path("date") String date
    );

    @GET("cartItems/restau/{restauID}/periode/{periode}")
    Call<CommandeResponse> getAllCommandesForPeriodes(
            @Path("restauID") String restauID,
            @Path("periode") String periode
    );

    @Multipart
    @POST("upload")
    Call<UploadImageResponse> uploadImage(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part files
    );






}
