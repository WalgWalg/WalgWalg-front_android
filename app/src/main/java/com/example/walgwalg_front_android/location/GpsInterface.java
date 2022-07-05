package com.example.walgwalg_front_android.location;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GpsInterface {
    @POST("walk/gps")
    Call<GpsResponse> CreateGps(@Body Gps gps);
}
