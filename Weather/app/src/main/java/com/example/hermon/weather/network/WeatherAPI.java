package com.example.hermon.weather.network;

import com.example.hermon.weather.data.WeatherResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("/data/2.5/weather")
    Call<WeatherResult> getWeather(@Query("q") String city, @Query("units") String unit,
                                   @Query("appid") String apiKey);
}
