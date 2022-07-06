package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class CommunityAddRequest {

    @SerializedName("walkId")
    public String walkId;

    @SerializedName("title")
    public String title;

    @SerializedName("hashTags")
    public String[] hashTags;

    @SerializedName("contents")
    public String contents;

    public CommunityAddRequest(String walkId, String title, String[] hashTags, String contents) {
        this.walkId = walkId;
        this.title = title;
        this.hashTags = hashTags;
        this.contents = contents;
    }

}
