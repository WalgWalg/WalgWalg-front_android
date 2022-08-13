package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class AddLikeRequest {

    @SerializedName("boardId")
    public String boardId;

    public AddLikeRequest(String boardId) {
        this.boardId = boardId;
    }

}
