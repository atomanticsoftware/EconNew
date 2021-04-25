package com.bigtech.econ.api;

import com.bigtech.econ.api.model.ApiForecastResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ForecastApiInterface {
    @GET("/forecast.json")
    Call<ApiForecastResponse> forecast();
}
