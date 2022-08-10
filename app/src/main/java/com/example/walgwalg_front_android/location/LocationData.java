package com.example.walgwalg_front_android.location;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

public class LocationData {
    @SerializedName("walkId")
    private String walkId;

    @SerializedName("course")
    private String course;

    @SerializedName("distance")
    private int distance;


    public LocationData(String walkId,int distance,String course){
        this.walkId=walkId;
        this.course=course;
        this.distance=distance;

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

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourse() {
        return course;
    }
}
