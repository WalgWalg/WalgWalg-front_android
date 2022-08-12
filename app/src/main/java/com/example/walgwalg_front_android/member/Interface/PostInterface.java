package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.PostResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PostInterface {

    //@통신 방식("통신 API명")
    @GET("/board/{boardId}")
    Call<PostResponse> getPost(@Path ("boardId") String boardId);

}
