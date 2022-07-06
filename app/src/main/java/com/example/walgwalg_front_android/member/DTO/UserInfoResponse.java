package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserInfoResponse {
    @SerializedName("id")
    public String id;

    @SerializedName("datetime")
    public String datetime;

    @SerializedName("status")
    public int status;

    @SerializedName("message")
    public String message;

    @SerializedName("list")
    public userInfoList list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public userInfoList getList() {
        return list;
    }

    public void setList(userInfoList list) {
        this.list = list;
    }
}
