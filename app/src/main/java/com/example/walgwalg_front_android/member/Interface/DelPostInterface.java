package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.DelPostResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface DelPostInterface {

    //@통신 방식("통신 API명")
    @DELETE("/board/{boardId}")
    Call<DelPostResponse> DEL_POST_RESPONSE_CALL(@Path("boardId") String boardId);

}
