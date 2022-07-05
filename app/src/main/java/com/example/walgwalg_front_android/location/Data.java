package com.example.walgwalg_front_android.location;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("parkName")
    private String parkName;

    @SerializedName("roadAddress")
    private String roadAddress;

    @SerializedName("numberAddress")
    private String numberAddress;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    public Data(String parkName,String roadAddress,String numberAddress,String latitude,String longitude){
        this.parkName=parkName;
        this.roadAddress=roadAddress;
        this.numberAddress=numberAddress;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public void setNumberAddress(String numberAddress) {
        this.numberAddress = numberAddress;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getParkName() {
        return parkName;
    }

    public String getNumberAddress() {
        return numberAddress;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
