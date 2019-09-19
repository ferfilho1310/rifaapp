package com.example.apprifa.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInit {

    private final Retrofit retrofit;


    public RetrofitInit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://ws.matheuscastiglioni.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public PostmonService getcep(){

        return retrofit.create(PostmonService.class);
    }
}
