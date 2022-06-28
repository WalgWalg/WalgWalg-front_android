package com.example.walgwalg_front_android.member.DTO;

import com.example.walgwalg_front_android.member.PreferenceHelper;
import com.google.gson.annotations.SerializedName;

import retrofit2.http.Header;

public class TokenRequest {

    @SerializedName("refreshToken")
    public String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public TokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
