package com.example.walgwalg_front_android.location;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Post {
    @SerializedName("walkDate")
    public String walkDate;

    @SerializedName("location")
    public String location;

    @SerializedName("address")
    public String address;

    public Post(String walkDate, String location, String address){
        this.walkDate=walkDate;
        this.location=location;
        this.address=address;
    }

    public String getWalkDate() {
        return walkDate;
    }

    public String getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public void setWalkDate(String walkDate) {
        this.walkDate = walkDate;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
