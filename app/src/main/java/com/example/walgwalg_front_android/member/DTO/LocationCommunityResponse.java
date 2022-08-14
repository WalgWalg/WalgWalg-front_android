package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LocationCommunityResponse {

    @SerializedName("id")
    public String id;

    @SerializedName("dateTime")
    public String dateTime;

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("list")
    public ArrayList<CommunityPojo> communityPojo;

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

    public ArrayList<CommunityPojo> getCommunityPojo() {
        return communityPojo;
    }

    public void setCommunityPojo(ArrayList<CommunityPojo> communityPojo) {
        this.communityPojo = communityPojo;
    }

}
