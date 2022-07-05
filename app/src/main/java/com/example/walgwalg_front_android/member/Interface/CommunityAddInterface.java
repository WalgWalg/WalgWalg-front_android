package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.CommunityAddRequest;
import com.example.walgwalg_front_android.member.DTO.CommunityAddResponse;
import com.example.walgwalg_front_android.member.DTO.LoginRequest;
import com.example.walgwalg_front_android.member.DTO.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CommunityAddInterface {

    //@통신 방식("통신 API명")
    @POST("/board")
    Call<CommunityAddResponse> getCommunityAddResponse(@Body CommunityAddRequest communityAddRequest);

}
