package com.example.walgwalg_front_android.location;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LocationInterface {
    @GET("/board/region/{region}")
    Call<LocationItem> getLocationData(@Path("region") String region);
}
