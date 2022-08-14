package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class CommunityPojo{

    @SerializedName("boardId")
    public String boardId;

    @SerializedName("title")
    public String title;

    @SerializedName("image")
    public String image;

    @SerializedName("date")
    public String date;

     @SerializedName("hashTags")
    public String[] hashTags;

    @SerializedName("likes")
    public int likes;
}
