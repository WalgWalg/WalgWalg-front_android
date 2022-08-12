package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class PostPojo {

    @SerializedName("boardId")
    public String boardId;

    @SerializedName("date")
    public String date;

    @SerializedName("title")
    public String title;

    @SerializedName("contents")
    public String contents;

     @SerializedName("hashTags")
    public String[] hashTags;

    @SerializedName("step_count")
    public int step_count;

    @SerializedName("distance")
    public int distance;

    @SerializedName("calorie")
    public int calorie;

    @SerializedName("course")
    public String course;

    @SerializedName("location")
    public String location;

    @SerializedName("nickname")
    public String nickname;

    @SerializedName("likes")
    public int likes;

}
