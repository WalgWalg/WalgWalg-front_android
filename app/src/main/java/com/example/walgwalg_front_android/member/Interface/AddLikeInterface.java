package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.AddLikeRequest;
import com.example.walgwalg_front_android.member.DTO.AddLikeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddLikeInterface {

    //@통신 방식("통신 API명")
    @POST("/like")
    Call<AddLikeResponse> PostAddLike(@Body AddLikeRequest likeRequest);

}
