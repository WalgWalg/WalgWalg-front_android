package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class TokenResponse {

    @SerializedName("id")
    public String id;

    @SerializedName("dateTime")
    public String dateTime;

    @SerializedName("status")
    public String status;

    @SerializedName("list")
    public TokenPOJO tokenPOJO;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TokenPOJO getTokenPOJO() {
        return tokenPOJO;
    }

    public void setTokenPOJO(TokenPOJO tokenPOJO) {
        this.tokenPOJO = tokenPOJO;
    }
}
