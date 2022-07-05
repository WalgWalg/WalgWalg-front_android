package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.CommunityResponse;
import com.example.walgwalg_front_android.member.DTO.CommunityTopRankPojo;
import com.example.walgwalg_front_android.member.DTO.CommunityTopRankResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CommunityTopRankInterface {

    //@통신 방식("통신 API명")
    @GET("/board/top")
    Call<CommunityTopRankResponse> getCommunityTopRank();

}
