package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class WalkTotalList {

    @SerializedName("nickName")
    public String nickName;

    @SerializedName("walkTotal")
    public walkTotal walkTotal;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public com.example.walgwalg_front_android.member.DTO.walkTotal getWalkTotal() {
        return walkTotal;
    }

    public void setWalkTotal(com.example.walgwalg_front_android.member.DTO.walkTotal walkTotal) {
        this.walkTotal = walkTotal;
    }
}
