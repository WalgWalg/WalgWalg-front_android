package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyWalkResponse {

    @SerializedName("id")
    public String id;

    @SerializedName("dateTime")
    public String dateTime;

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("list")
    public ArrayList<MyWalkPojo> myWalkPojo;

}
