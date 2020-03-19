package com.example.bookstore.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAdapter {

    public PaytmProcess retroFit() {


    Retrofit client = new Retrofit.Builder()
            .baseUrl("https://bookstoreandroidproject.000webhostapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    return  client.create(PaytmProcess.class);
}
}
