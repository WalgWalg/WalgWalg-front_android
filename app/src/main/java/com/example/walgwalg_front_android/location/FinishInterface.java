package com.example.walgwalg_front_android.location;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FinishInterface {

    @Multipart
    @POST("walk/finish")
    Call<FinishResponse> CreateWalk(@Part MultipartBody.Part file, @Part("requestDto") requestDto finish);
}
