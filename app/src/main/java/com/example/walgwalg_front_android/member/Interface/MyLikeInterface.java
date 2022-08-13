package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.MyLikeRequest;
import com.example.walgwalg_front_android.member.DTO.MyLikeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface MyLikeInterface {

    //@통신 방식("통신 API명")
    @GET("/like")
    Call<MyLikeResponse> GetMyLike();

}
