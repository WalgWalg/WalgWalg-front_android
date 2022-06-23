package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class LoginResponse {

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

    public String getStatus() {
        return status;
    }

    public TokenPOJO getTokenPOJO() {
        return tokenPOJO;
    }
}
