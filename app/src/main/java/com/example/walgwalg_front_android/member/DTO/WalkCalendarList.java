package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class WalkCalendarList {

    @SerializedName("date")
    public WalkCalendarRecord walkCalendarRecord;

    public WalkCalendarRecord getWalkCalendarRecord() {
        return walkCalendarRecord;
    }

    public void setWalkCalendarRecord(WalkCalendarRecord walkCalendarRecord) {
        this.walkCalendarRecord = walkCalendarRecord;
    }
}
