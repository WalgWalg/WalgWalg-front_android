package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class userInfoList {

    @SerializedName("nickname")
    public String nickname;

    @SerializedName("profile")
    public String profile;

    @SerializedName("address")
    public String address;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
