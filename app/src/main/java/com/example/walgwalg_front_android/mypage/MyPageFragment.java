package com.example.walgwalg_front_android.mypage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.member.DTO.LoginResponse;
import com.example.walgwalg_front_android.member.DTO.TokenRequest;
import com.example.walgwalg_front_android.member.DTO.TokenResponse;
import com.example.walgwalg_front_android.member.Interface.TokenInterface;
import com.example.walgwalg_front_android.member.PreferenceHelper;
import com.example.walgwalg_front_android.member.ServiceGenerator;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyPageFragment extends Fragment {

    private TextView tv_token;
    private PreferenceHelper preferenceHelper;
    TokenInterface tokenInterface;
    TokenRequest tokenRequest;

    public MyPageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

//        tv_token = view.findViewById(R.id.tv_token);
        preferenceHelper = new PreferenceHelper(getContext());
        Log.d("MyPageFragment", "원래 access"+preferenceHelper.getAccessToken());
        Log.d("MyPageFragment", "refresh access"+preferenceHelper.getRefreshToken());

        tokenInterface = ServiceGenerator.createService(TokenInterface.class, preferenceHelper.getAccessToken());
        loadStores();

        return view;
    }


    private void loadStores() {
        tokenRequest = new TokenRequest(preferenceHelper.getRefreshToken());
        tokenInterface.getToken(tokenRequest).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.d("REST ERROR!", t.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response){
                if (response.isSuccessful()) {

                     TokenResponse result = (TokenResponse) response.body();

                    Log.d("MyPageFragment", "응답"+response.isSuccessful());
                    Log.d("MyPageFragment", "새로운 access"+result.tokenPOJO.accessToken);
                    Log.d("MyPageFragment", "refresh access"+result.tokenPOJO.refreshToken);
                    Toast.makeText(getContext(), "토큰", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("REST FAILED MESSAGE", response.message());
                }
            }

        });

    }
}