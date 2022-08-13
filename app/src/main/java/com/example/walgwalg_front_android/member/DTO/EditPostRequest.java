package com.example.walgwalg_front_android.member.DTO;

import com.google.gson.annotations.SerializedName;

public class EditPostRequest {

    @SerializedName("boardId")
    public String boardId;

    @SerializedName("title")
    public String title;

    @SerializedName("hashTags")
    public String[] hashTags;

    @SerializedName("contents")
    public String contents;

    public EditPostRequest(String boardId, String title, String[] hashTags, String contents) {
        this.boardId = boardId;
        this.title = title;
        this.hashTags = hashTags;
        this.contents = contents;
    }

}
