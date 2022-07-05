package com.example.walgwalg_front_android.location;

import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;

public class requestDto {
    @SerializedName("walkId")
    public String walkId;

    @SerializedName("step_count")
    public int step_count;

    @SerializedName("distance")
    public int distance;

    @SerializedName("calorie")
    public int calorie;

    @SerializedName("walkTime")
    public String walkTime;

    public requestDto(String walkId,int step_count,int distance,int calorie,String walkTime){
        this.walkId=walkId;
        this.step_count=step_count;
        this.distance=distance;
        this.calorie=calorie;
        this.walkTime=walkTime;
    }

    public String getWalkId() {
        return walkId;
    }

    public void setWalkId(String walkId) {
        this.walkId = walkId;
    }

    public int getStep_count() {
        return step_count;
    }

    public void setStep_count(int step_count) {
        this.step_count = step_count;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public String getWalkTime() {
        return walkTime;
    }

    public void setWalkTime(String walkTime) {
        this.walkTime = walkTime;
    }
}
