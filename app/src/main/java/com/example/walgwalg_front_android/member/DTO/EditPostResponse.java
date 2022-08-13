package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class EditPostResponse {

    @SerializedName("id")
    public String id;

    @SerializedName("dateTime")
    public String dateTime;

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("list")
    public String list;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

}
