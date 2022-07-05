package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.CommunityAddRequest;
import com.example.walgwalg_front_android.member.DTO.CommunityAddResponse;
import com.example.walgwalg_front_android.member.DTO.CommunityResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CommunityInterface {

    //@통신 방식("통신 API명")
    @GET("/board")
    Call<CommunityResponse> getCommunityAddResponse();

}
