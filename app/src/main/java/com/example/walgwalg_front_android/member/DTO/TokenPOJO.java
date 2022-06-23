package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class TokenPOJO {

    @SerializedName("accessToken")
    public String accessToken;

    @SerializedName("refreshToken")
    public String refreshToken;
}
