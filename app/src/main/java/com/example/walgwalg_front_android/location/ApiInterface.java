package com.example.walgwalg_front_android.location;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("/park/{region}")
    Call<TestItem> getData(@Path("region") String region);
}
