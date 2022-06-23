package com.example.walgwalg_front_android.model;

public class CurrentWeather {
    private String id;         // TODO: 타입 확인
    private String main;
    private String description;
    private int icon;       // TODO: 타입 확인
    private String dt;         // TODO: 타입 확인
    private String temp;    // TODO: 타입 확인

    public CurrentWeather(String id, String main, String description, int icon, String dt, String temp){
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
        this.dt = dt;
        this.temp = temp;
    }

    public CurrentWeather() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
