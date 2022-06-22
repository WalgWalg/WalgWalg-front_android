package com.example.walgwalg_front_android.member;

import com.example.walgwalg_front_android.member.Interface.LoginInterface;
import com.example.walgwalg_front_android.member.Interface.RegisterInterface;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private static LoginInterface loginInterface;
    private static RegisterInterface registerInterface;
    //사용하고 있는 서버 BASE 주소
    private static String baseUrl = "http://ec2-15-165-129-147.ap-northeast-2.compute.amazonaws.com:8080";



    private RetrofitClient() {
    	//로그를 보기 위한 Interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        //retrofit 설정
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client) //로그 기능 추가
                .build();

        loginInterface = retrofit.create(LoginInterface.class);
        registerInterface = retrofit.create(RegisterInterface.class);
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public static LoginInterface getLoginRetrofitInterface() {
        return loginInterface;
    }

    public static RegisterInterface getRegistRetrofitInterface(){
        return registerInterface;
    }

}
