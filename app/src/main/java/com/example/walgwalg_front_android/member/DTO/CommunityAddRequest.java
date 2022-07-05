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
        this.walkId = "0c908994-fc3f-4caf-bdb7-2bc005d6b815";
        this.title = title;
        this.hashTags = hashTags;
        this.contents = contents;
    }

}
