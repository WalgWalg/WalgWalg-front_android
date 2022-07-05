package com.example.walgwalg_front_android.member.Retrofit;

import com.example.walgwalg_front_android.member.Interface.CommunityAddInterface;
import com.example.walgwalg_front_android.member.Interface.LoginInterface;
import com.example.walgwalg_front_android.member.Interface.RegisterInterface;
import com.example.walgwalg_front_android.member.Interface.TokenInterface;
import com.example.walgwalg_front_android.member.Interface.UserInfoInterface;
import com.example.walgwalg_front_android.member.Interface.WalkTotalInterface;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private static LoginInterface loginInterface;
    private static RegisterInterface registerInterface;
    private static TokenInterface tokenInterface;
    private static CommunityAddInterface communityAddInterface;
    private static UserInfoInterface userInfoInterface;
    private static WalkTotalInterface walkTotalInterface;
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

        // 로그인
        loginInterface = retrofit.create(LoginInterface.class);

        // 회원가입
        registerInterface = retrofit.create(RegisterInterface.class);

        // 토큰
        tokenInterface = retrofit.create(TokenInterface.class);

        // 게시판 등록
        communityAddInterface = retrofit.create(CommunityAddInterface.class);

        // 회원정보 조회
        userInfoInterface = retrofit.create(UserInfoInterface.class);

        // 산책 통계 조회
        walkTotalInterface = retrofit.create(WalkTotalInterface.class);
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

    public static RegisterInterface getRegistRetrofitInterface() {
        return registerInterface;
    }

    public static TokenInterface getTokenRetrofitInterface() {
        return tokenInterface;
    }

    public static CommunityAddInterface getCommunityAddInterface() {return communityAddInterface;}

    public static UserInfoInterface getUserInfoInterface(){
        return userInfoInterface;
    }

    public static WalkTotalInterface getWalkTotalInterface() {
        return walkTotalInterface;
    }

}
