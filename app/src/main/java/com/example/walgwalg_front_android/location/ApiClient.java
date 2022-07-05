package com.example.walgwalg_front_android.location;

import android.util.Log;

import com.example.walgwalg_front_android.member.PreferenceHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static String BASE_URL = "http://ec2-15-165-129-147.ap-northeast-2.compute.amazonaws.com:8080/";
    private static Retrofit retrofit;
    private static PreferenceHelper preferenceHelper;
    private static String authToken;

    public ApiClient(String token) {
        this.authToken = token;
        Log.d("post","토큰: "+authToken);
    }

//    public Response intercept(Interceptor.Chain chain) throws IOException {
//        Request original = chain.request();
//
//        Request.Builder builder = original.newBuilder()
//                .header("x-auth-token", authToken);
//
//        Request request = builder.build();
//        return chain.proceed(request);
//    }

    public static Retrofit getClient(){

        //로그를 보기 위한 Interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest=chain.request().newBuilder().header("x-auth-token", authToken).build();
                        Log.d("post","적용토큰: "+authToken);
                        return chain.proceed(newRequest);
                    }
                }).build();

//        if(retrofit == null){
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            Log.d("post","적용토큰: "+authToken);
//        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;

    }
}
