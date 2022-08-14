package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.CommunityResponse;
import com.example.walgwalg_front_android.member.DTO.MyBoardResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyBoardInterface {

    //@통신 방식("통신 API명")
    @GET("/board/myBoard")
    Call<MyBoardResponse> getMyBoard();

}
