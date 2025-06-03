package com.example.weather_api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface API {

    @Headers({
            "X-API-KEY: 88c6b664-a80f-47b7-9aa8-8fcad45dbf83",
            "Content-Type: application/json"
    })
    @GET("api/v2.2/films/{id}")
    Call<MovieResponse> getMovieById(@Path("id") int id);
}
