package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class walkTotal {

    @SerializedName("month")
    public WalkTotalMonth walkTotalMonth;

    @SerializedName("today")
    public WalkTotalToday walkTotalToday;

    public WalkTotalMonth getWalkTotalMonth() {
        return walkTotalMonth;
    }

    public void setWalkTotalMonth(WalkTotalMonth walkTotalMonth) {
        this.walkTotalMonth = walkTotalMonth;
    }

    public WalkTotalToday getWalkTotalToday() {
        return walkTotalToday;
    }

    public void setWalkTotalToday(WalkTotalToday walkTotalToday) {
        this.walkTotalToday = walkTotalToday;
    }
}
