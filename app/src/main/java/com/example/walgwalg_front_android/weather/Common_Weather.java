package com.example.walgwalg_front_android.weather;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common_Weather {

    public static final String APP_ID = "eb8a44abde5ec9046ae59b54bd451773";
    public static Location current_location = null;

    public static String convertUnixToDate(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = sdf.format(date);
        return formatted;
    }

    public static String convertUnixToHour(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("k a");
        String formatted = sdf.format(date);
        return formatted;
    }

    public static String convertUnixToDateV2(long dt){
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        String formatted = sdf.format(date);
        return formatted;
    }
//     37.241 127.179
//    https://api.openweathermap.org/data/2.5/forecast/daily?lat=37.241&lon=127.179&cnt=10&appid=eb8a44abde5ec9046ae59b54bd451773
}
