package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class MyLikePojo {

    @SerializedName("boardId")
    public String boardId;

    @SerializedName("title")
    public String title;

    @SerializedName("hashTags")
    public MyLike_TAG_Pojo[] hashTags;

    @SerializedName("writeDate")
    public String writeDate;

    @SerializedName("likeCount")
    public int likeCount;

}
