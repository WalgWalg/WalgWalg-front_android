package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WalkCalendarResponse {

    @SerializedName("id")
    public String id;

    @SerializedName("dateTime")
    public String dateTime;

    @SerializedName("status")
    public int status;

    @SerializedName("message")
    public String message;

    @SerializedName("list")
    public List<WalkCalendarList> walkCalendarList;

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

    public List<WalkCalendarList> getWalkCalendarList() {
        return walkCalendarList;
    }

    public void setWalkCalendarList(List<WalkCalendarList> walkCalendarList) {
        this.walkCalendarList = walkCalendarList;
    }
}
