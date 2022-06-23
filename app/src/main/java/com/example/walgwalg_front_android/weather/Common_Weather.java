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
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formatted = sdf.format(date);
        return formatted;
    }
}
