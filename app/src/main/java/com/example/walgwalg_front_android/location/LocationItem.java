package com.example.walgwalg_front_android.location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationItem {
    @SerializedName("id")
    public String id;

    @SerializedName("dateTime")
    public String dateTime;

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("list")
    public List<LocationData> list;

    public String tolist(){
        return "list="+list+
                "}";
    }

    public LocationItem(String id,String dateTime,String status,String message,List<LocationData> list){
        this.id=id;
        this.dateTime=dateTime;
        this.status=status;
        this.message=message;
        this.list=list;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void setList(List<LocationData> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


    public List<LocationData> getList() {
        return list;
    }

}

