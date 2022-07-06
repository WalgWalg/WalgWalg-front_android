package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class WalkTotalMonth {
    @SerializedName("stepCount")
    public int stepCount;

    @SerializedName("distance")
    public int distance;

    @SerializedName("walkTime")
    public String walkTime;

    public int getStepCount() {
        return stepCount;
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
