package com.myapp.htpad.lotto.util.retrofit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClientApi {

    private static Retrofit retrofit = null;



   private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();


    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl("https://www.dhlottery.co.kr/")
                    .build();
        }
        return retrofit;
    }




}
