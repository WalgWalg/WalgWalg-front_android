package com.example.walgwalg_front_android.member.Interface;

import com.example.walgwalg_front_android.member.DTO.LocationCommunityResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LocationCommunityInterface {

    //@통신 방식("통신 API명")
    @GET("/board/region/{region}")
    Call<LocationCommunityResponse> getRegionPost(@Path("region") String region);

}
