package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.AddLikeRequest;
import com.example.walgwalg_front_android.member.DTO.AddLikeResponse;
import com.example.walgwalg_front_android.member.DTO.DelLikeResponse;
import com.example.walgwalg_front_android.member.DTO.PostResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DelLikeInterface {

    //@통신 방식("통신 API명")
    @DELETE("/like/{boardId}")
    Call<DelLikeResponse> DEL_LIKE_RESPONSE_CALL(@Path("boardId") String boardId);

}
