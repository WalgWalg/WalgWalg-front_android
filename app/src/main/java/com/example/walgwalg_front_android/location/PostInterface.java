package com.example.walgwalg_front_android.location;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostInterface {

    @POST("walk/start")
    Call<PostResponse> CreatePost(@Body Post post);

}
