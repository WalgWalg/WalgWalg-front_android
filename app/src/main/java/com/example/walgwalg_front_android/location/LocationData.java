package com.example.walgwalg_front_android.location;

import com.google.gson.annotations.SerializedName;

public class LocationData {
    @SerializedName("boardId")
    private String boardId;

    @SerializedName("title")
    private String title;

    @SerializedName("contents")
    private String contents;

    @SerializedName("hashTags")
    private String[] hashTags;

    @SerializedName("step_count")
    private int step_count;

    @SerializedName("distance")
    private int distance;

    @SerializedName("calorie")
    private int calorie;

    @SerializedName("course")
    private String course;

    @SerializedName("location")
    private String location;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("likes")
    private int likes;

    public LocationData(String boardId,String title,String contents,String[] hashTags,int step_count
            ,int distance,int calorie,String course,String location,String nickname,int likes){
        this.boardId=boardId;
        this.title=title;
        this.contents=contents;
        this.hashTags=hashTags;
        this.step_count=step_count;
        this.distance=distance;
        this.calorie=calorie;
        this.course=course;
        this.location=location;
        this.nickname=nickname;
        this.likes=likes;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String[] getHashTags() {
        return hashTags;
    }

    public void setHashTags(String[] hashTags) {
        this.hashTags = hashTags;
    }

    public int getStep_count() {
        return step_count;
    }

    public void setStep_count(int step_count) {
        this.step_count = step_count;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
