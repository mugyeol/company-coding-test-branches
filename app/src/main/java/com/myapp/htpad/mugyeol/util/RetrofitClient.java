package com.myapp.htpad.mugyeol.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {

    private static Retrofit retrofit = null;


   private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();


   //build retrofit instance
    private static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl("https://mcricwiojwfb.cleancitynetworks.com/")
                    .build();
        }
        return retrofit;
    }
    public static RetrofitApi restApi = getClient().create(RetrofitApi.class);




}
