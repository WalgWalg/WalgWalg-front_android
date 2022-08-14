package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalkCalendarList {

    @SerializedName("walkDate")
    public String walkDate;

    @SerializedName("stepCount")
    public int stepCount;

    @SerializedName("distance")
    public int distance;

    @SerializedName("walkTime")
    public String walkTime;

    public int getStepCount() {
        return stepCount;
    }

    public String getWalkDate() {
        return walkDate;
    }

    public void setWalkDate(String walkDate) {
        this.walkDate = walkDate;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
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
}
