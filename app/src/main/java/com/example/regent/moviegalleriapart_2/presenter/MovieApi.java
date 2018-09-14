package com.example.regent.moviegalleriapart_2.presenter;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieApi {

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit(Context context){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.themoviedb.org/3/")
                    .build();
        }

        return retrofit;
    }

}
