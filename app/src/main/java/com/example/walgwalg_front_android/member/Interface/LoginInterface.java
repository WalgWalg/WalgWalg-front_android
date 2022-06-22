package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.LoginRequest;
import com.example.walgwalg_front_android.member.DTO.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginInterface {

    //@통신 방식("통신 API명")
    @POST("/user/login")
    Call<LoginResponse> getLoginResponse(@Body LoginRequest loginRequest);

}
