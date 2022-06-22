package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("userid")
    public String userid;

    @SerializedName("password")
    public String password;

    @SerializedName("nickname")
    public String nickname;

    @SerializedName("address")
    public String address;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public RegisterRequest(String userid, String password, String nickname, String address) {
        this.userid = userid;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
    }
}
