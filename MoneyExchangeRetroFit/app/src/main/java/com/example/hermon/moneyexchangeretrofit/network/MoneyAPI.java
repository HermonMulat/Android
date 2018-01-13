package com.example.hermon.moneyexchangeretrofit.network;

import com.example.hermon.moneyexchangeretrofit.data.MoneyResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoneyAPI {
    @GET("latest")
    Call<MoneyResult> getRatesForUsd(@Query("base") String base); // can add extra params here
}
