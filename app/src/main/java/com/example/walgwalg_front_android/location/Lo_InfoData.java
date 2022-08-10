package com.example.walgwalg_front_android.location;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Lo_InfoData {
    @SerializedName("walkId")
    private String walkId;

    @SerializedName("walkTime")
    private String walkTime;

    @SerializedName("stepCount")
    private int stepCount;

    @SerializedName("distance")
    private int distance;

    @SerializedName("calorie")
    private int calorie;

    @SerializedName("location")
    private String location;

    @SerializedName("gpsList")
    public List<Lo_InfoGps> list;

    public String tolist() {
        return "list=" + list +
                "}";
    }

    public Lo_InfoData(String walkId, String walkTime, int stepCount, int distance, int calorie, String location, List<Lo_InfoGps> list) {
        this.walkId = walkId;
        this.walkTime = walkTime;
        this.stepCount = stepCount;
        this.distance = distance;
        this.calorie = calorie;
        this.location = location;
        this.list = list;
    }

    public String getWalkId() {
        return walkId;
    }

    public void setWalkId(String walkId) {
        this.walkId = walkId;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getWalkTime() {
        return walkTime;
    }

    public void setWalkTime(String walkTime) {
        this.walkTime = walkTime;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Lo_InfoGps> getList() {
        return list;
    }

    public void setList(List<Lo_InfoGps> list) {
        this.list = list;
    }
}
