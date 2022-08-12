package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalkCalendarList {

    @SerializedName("date")
    public List<WalkCalendarRecord> walkCalendarRecord;

    public List<WalkCalendarRecord> getWalkCalendarRecord() {
        return walkCalendarRecord;
    }

    public void setWalkCalendarRecord(List<WalkCalendarRecord> walkCalendarRecord) {
        this.walkCalendarRecord = walkCalendarRecord;
    }
}
