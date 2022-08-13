package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.EditPostRequest;
import com.example.walgwalg_front_android.member.DTO.EditPostResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EditPostInterface {

    //@통신 방식("통신 API명")
    @POST("/board/update")
    Call<EditPostResponse> EDIT_POST_RESPONSE_CALL(@Body EditPostRequest editPostRequest);


}
