package com.example.walgwalg_front_android.home;

import android.util.Log;

import com.example.walgwalg_front_android.member.Interface.UserInfoInterface;
import com.example.walgwalg_front_android.member.Interface.WalkTotalInterface;
import com.example.walgwalg_front_android.member.Retrofit.RetrofitClient;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeRetrofitClient {

    private String TAG = "HomeRetrofitClient";
    private static HomeRetrofitClient instance = null;
    private static UserInfoInterface userInfoInterface;
    private static WalkTotalInterface walkTotalInterface;
    private static String authToken;

    public HomeRetrofitClient(String token) {
        this.authToken = token;
        Log.d(TAG,"토큰: "+authToken);
    }

    //사용하고 있는 서버 BASE 주소
    private static String baseUrl = "http://ec2-15-165-129-147.ap-northeast-2.compute.amazonaws.com:8080";

    public HomeRetrofitClient() {
//        //로그를 보기 위한 Interceptor
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor)
//                .build();

        //로그를 보기 위한 Interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest=chain.request().newBuilder().header("x-auth-token", authToken).build();
                        Log.d(TAG,"적용토큰: "+authToken);
                        return chain.proceed(newRequest);
                    }
                }).build();


        //retrofit 설정
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client) //로그 기능 추가
                .build();

        // 회원정보 조회
        userInfoInterface = retrofit.create(UserInfoInterface.class);

        // 산책 통계 조회
        walkTotalInterface = retrofit.create(WalkTotalInterface.class);
    }

    public static HomeRetrofitClient getInstance() {
        if (instance == null) {
            instance = new HomeRetrofitClient();
        }
        return instance;
    }

    public static UserInfoInterface getUserInfoInterface(){
        return userInfoInterface;
    }

    public static WalkTotalInterface getWalkTotalInterface() {
        return walkTotalInterface;
    }
}
