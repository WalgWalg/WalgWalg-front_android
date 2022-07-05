package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.TokenRequest;
import com.example.walgwalg_front_android.member.DTO.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TokenInterface {

    @POST("/user/update/accessToken")
    Call<TokenResponse> getToken(@Body TokenRequest tokenRequest);

//    @FormUrlEncoded
//    @PATCH("/auth/token")
//    Call<Map<String, String>> refreshToken(@Field("refresh_token") String refreshToken);
}
