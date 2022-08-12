package com.example.walgwalg_front_android.member.Retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

     private String authToken;

    public AuthenticationInterceptor(String token) {
        // 원래 코드
        this.authToken = token;

        // 임시 코드
//        this.authToken = "PMAK-62c2bb573eaf6129f000481d-7a7ec096d3dd182e419dc0fb7e0473d544";
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                // Original Token Code
                .header("x-auth-token", authToken);
                // MockServer Token Code
//                .header("X-Api-Key", "PMAK-62c2bb573eaf6129f000481d-7a7ec096d3dd182e419dc0fb7e0473d544");

        Request request = builder.build();
        return chain.proceed(request);
    }
}
