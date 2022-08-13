package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.MyInfoResponse;
import com.example.walgwalg_front_android.member.DTO.MyLikeResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyInfoInterface {

    @GET("/user/info")
    Call<MyInfoResponse> GET_MY_INFO();

}
