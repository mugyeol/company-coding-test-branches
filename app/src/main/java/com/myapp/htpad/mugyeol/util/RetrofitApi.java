package com.myapp.htpad.mugyeol.util;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myapp.htpad.mugyeol.CarModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApi {
    @FormUrlEncoded
    @POST("mobile/v1/auth/")
    Call<JsonObject> login(
            @Field("userId") String userid,
            @Field("password") String password,
            @Field("deviceType") String deviceType
    );
    @GET("mobile/v1/users/self/vehicles/")
    Call<CarModel> getCarlist(
            @Header("authorization") String token
    );

    @FormUrlEncoded
    @PUT("mobile/v1/users/self/vehicles/{vehicleIdx}/favorite")
    Call<JsonObject> putFavorite(
            @Header("authorization") String token,
            @Path("vehicleIdx")  int idx,
            @Field("status") boolean favorite
    );





}
