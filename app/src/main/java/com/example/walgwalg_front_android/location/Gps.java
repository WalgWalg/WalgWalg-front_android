package com.example.walgwalg_front_android.location;

import com.google.gson.annotations.SerializedName;

public class Gps {
    @SerializedName("walkId")
    public String walkId;

    @SerializedName("latitude")
    public String latitude;

    @SerializedName("longitude")
    public String longitude;

    public Gps(String walkId, String latitude, String longitude){
        this.walkId=walkId;
        this.latitude=latitude;
        this.longitude=longitude;
    }
    public String getWalkId() {
        return walkId;
    }

    public void setWalkId(String walkId) {
        this.walkId = walkId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
