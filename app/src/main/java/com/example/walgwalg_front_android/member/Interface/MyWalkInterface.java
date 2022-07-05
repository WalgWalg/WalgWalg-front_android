package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.LoginRequest;
import com.example.walgwalg_front_android.member.DTO.LoginResponse;
import com.example.walgwalg_front_android.member.DTO.MyWalkResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyWalkInterface {

    //@통신 방식("통신 API명")
    @GET("/walk/list")
    Call<MyWalkResponse> getMyWalkResponse();
}
