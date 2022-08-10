package com.example.walgwalg_front_android.location;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Lo_InfoInterface {
    @GET("/walk/{walkId}")
    Call<Lo_InfoItem> getInfoData(@Path("walkId") String walkId);
}
