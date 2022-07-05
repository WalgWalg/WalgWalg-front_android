package com.example.walgwalg_front_android.location;

import com.google.gson.annotations.SerializedName;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.Part;

public class Finish {

    @SerializedName("file")
    public MultipartBody.Part file;

    @SerializedName("requestDto")
    public requestDto requestDto;

    public Finish(MultipartBody.Part file, requestDto requestDto){
        this.file=file;
        this.requestDto=requestDto;
    }

    public MultipartBody.Part getFile() {
        return file;
    }

    public void setFile(MultipartBody.Part file) {
        this.file = file;
    }

    public requestDto getRequestDto() {
        return requestDto;
    }

    public void setRequestDto(requestDto requestDto) {
        this.requestDto = requestDto;
    }
}
