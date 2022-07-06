package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.UserInfoResponse;
import com.example.walgwalg_front_android.member.DTO.WalkTotalResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WalkTotalInterface {

    //@통신 방식("통신 API명")
    @GET("/walk/total")
    Call<WalkTotalResponse> getWalkTotalResponse();
}
