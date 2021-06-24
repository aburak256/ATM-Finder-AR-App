package com.example.myapplication.models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("auth/token")
    Call<AuthTokenResponse> postToken(@Body AuthTokenRequest request);

    @GET("branch-atm")
    Call<AtmResponse> getAtm(@Header("X-token") String token, @Query("type") String atmStr, @Query("latitude") Double latitude,
                             @Query("longitude") Double longitude);

}
