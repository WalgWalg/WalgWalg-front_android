package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class MyWalkPojo {

    @SerializedName("id")
    public String id;

    @SerializedName("walkDate")
    public String walkDate;

    @SerializedName("stepCount")
    public int stepCount;

    @SerializedName("distance")
    public int distance;

    @SerializedName("calorie")
    public int calorie;

    @SerializedName("walkTime")
    public String walkTime;

    @SerializedName("course")
    public String course;

    @SerializedName("location")
    public String location;

}
