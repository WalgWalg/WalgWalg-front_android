package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("userid")
    public String userid;

    @SerializedName("password")
    public String password;

    public String getUserid() {
        return userid;
    }

    public String getPassword() {
        return password;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginRequest(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }

}
