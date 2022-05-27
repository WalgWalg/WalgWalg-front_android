package com.example.walgwalg_front_android.location;

public class LocationItem {
    String tv_distance;
    int iv_map;

    public LocationItem(int iv_map, String tv_distance) {
        this.iv_map = iv_map;
        this.tv_distance = tv_distance;
    }

    public String getTv_distance() {
        return tv_distance;
    }

    public void setTv_distance(String tv_distance) {
        this.tv_distance = tv_distance;
    }

    public int getIv_map() {
        return iv_map;
    }

    public void setIv_map(int iv_map) {
        this.iv_map = iv_map;
    }

}

