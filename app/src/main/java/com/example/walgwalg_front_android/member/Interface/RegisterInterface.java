package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.RegisterRequest;
import com.example.walgwalg_front_android.member.DTO.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterInterface {

    //@통신 방식("통신 API명")
    @POST("/user/register")
    Call<RegisterResponse> getRegisterResponse(@Body RegisterRequest registerRequest);

}
