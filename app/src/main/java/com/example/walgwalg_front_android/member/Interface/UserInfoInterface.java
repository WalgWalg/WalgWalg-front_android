package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.UserInfoResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserInfoInterface {

    //@통신 방식("통신 API명")
    @GET("/user/info")
    Call<UserInfoResponse> getUserInfoResponse();
}
