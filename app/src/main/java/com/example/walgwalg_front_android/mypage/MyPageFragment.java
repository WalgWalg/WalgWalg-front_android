package com.example.walgwalg_front_android.mypage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.ViewModel.MyBoardViewModel;
import com.example.walgwalg_front_android.ViewModel.MyInfoViewModel;
import com.example.walgwalg_front_android.ViewModel.MyLikeViewModel;
import com.example.walgwalg_front_android.community.CommunityFragment;
import com.example.walgwalg_front_android.member.DTO.CommunityPojo;
import com.example.walgwalg_front_android.member.DTO.MyBoardRequest;
import com.example.walgwalg_front_android.member.DTO.MyBoardResponse;
import com.example.walgwalg_front_android.member.DTO.MyInfoPojo;
import com.example.walgwalg_front_android.member.DTO.MyLikePojo;
import com.example.walgwalg_front_android.member.DTO.MyWalkPojo;
import com.example.walgwalg_front_android.member.DTO.MyWalkRequest;
import com.example.walgwalg_front_android.member.DTO.MyWalkResponse;
import com.example.walgwalg_front_android.member.DTO.TokenRequest;
import com.example.walgwalg_front_android.member.DTO.TokenResponse;
import com.example.walgwalg_front_android.member.Interface.MyBoardInterface;
import com.example.walgwalg_front_android.member.Interface.MyWalkInterface;
import com.example.walgwalg_front_android.member.Interface.TokenInterface;
import com.example.walgwalg_front_android.member.PreferenceHelper;
import com.example.walgwalg_front_android.member.Retrofit.ServiceGenerator;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyPageFragment extends Fragment {

    private String TAG = "MyPageFragmentTAG";

    private TextView tv_nickname_profile, tv_km_count, tv_walk_count, tv_post_count, tv_title_1, tv_title_2, tv_hashtag_1, tv_hashtag_2, tv_date_1, tv_date_2;
    private MaterialButton btn_favorite_1, btn_favorite_2;
    private ImageView iv_map_1, iv_map_2;

    private MyWalkRequest myWalkRequest;
    private MyWalkResponse myWalkResponse;
    private MyWalkInterface myWalkInterface;
    private MyBoardInterface myBoardInterface;
    private MyBoardRequest myBoardRequest;
    private MyBoardResponse myBoardResponse;

    private PreferenceHelper preferenceHelper;

    private MyInfoViewModel myInfoViewModel;
    private MyLikeViewModel myLikeViewModel;
    private MyBoardViewModel myBoardViewModel;

//    TokenInterface tokenInterface;
//    TokenRequest tokenRequest;

    private ArrayList<MyWalkPojo> myWalkData;
    private ArrayList<MyLikePojo> myLikeBoardData;
    private String nickname;
    private int km_count, walk_count, post_count;

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
//        Log.d("MyPageFragment", "원래 access"+preferenceHelper.getAccessToken());
//        Log.d("MyPageFragment", "refresh access"+preferenceHelper.getRefreshToken());
//        tokenInterface = ServiceGenerator.createService(TokenInterface.class, preferenceHelper.getAccessToken());
//        loadStores();

        init(view);

        myInfoViewModel = new ViewModelProvider(requireActivity()).get(MyInfoViewModel.class);
        myLikeViewModel = new ViewModelProvider(requireActivity()).get(MyLikeViewModel.class);
        myBoardViewModel = new ViewModelProvider(requireActivity()).get(MyBoardViewModel.class);
        preferenceHelper = new PreferenceHelper(getContext());

        myWalkResponse();
        myBoardResponse();

        myInfoViewModel.getMyInfoData().observe(getActivity(), new Observer<MyInfoPojo>() {
            @Override
            public void onChanged(MyInfoPojo myInfoPojo) {
                nickname = myInfoPojo.nickname;
                tv_nickname_profile.setText(nickname);
            }
        });

        myBoardViewModel.getMyBoardData().observe(getActivity(), new Observer<ArrayList<CommunityPojo>>() {
            @Override
            public void onChanged(ArrayList<CommunityPojo> communityPojos) {
                post_count = communityPojos.size();
                tv_post_count.setText(String.valueOf(post_count));
            }
        });

        myLikeViewModel.getMyLikeData().observe(getActivity(), new Observer<ArrayList<MyLikePojo>>() {
            @Override
            public void onChanged(ArrayList<MyLikePojo> myLikePojos) {
//                myLikeBoardData = myLikePojos;
                DateComparator dateComparator = new DateComparator();
                Collections.sort(myLikePojos, dateComparator);
                Collections.reverse(myLikePojos);

                if(myLikePojos.size()>0){
                    if (myLikePojos.get(0) != null) {
                    Glide.with(getContext())
                            .load("")
                            .error(R.drawable.ic_error_24)
                            .fallback(R.drawable.ic_error_24)
                            .into(iv_map_1);
                    tv_title_1.setText(myLikePojos.get(0).title);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < myLikePojos.get(0).hashTags.length; i++) {
                        sb.append("#");
                        sb.append(myLikePojos.get(0).hashTags[i].tag);
                    }
                    tv_hashtag_1.setText(sb);
                    tv_date_1.setText(myLikePojos.get(0).writeDate);
                }

                if (myLikePojos.size()>1) {
                    Glide.with(getContext())
                            .load("")
                            .error(R.drawable.ic_error_24)
                            .fallback(R.drawable.ic_error_24)
                            .into(iv_map_2);
                    tv_title_2.setText(myLikePojos.get(1).title);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < myLikePojos.get(1).hashTags.length; i++) {
                        sb.append("#");
                        sb.append(myLikePojos.get(1).hashTags[i]);
                    }
                    tv_hashtag_2.setText(sb);
                    tv_date_2.setText(myLikePojos.get(1).writeDate);
                }
                }

            }
        });


        return view;
    }

    private void myBoardResponse() {
        myBoardInterface = ServiceGenerator.createService(MyBoardInterface.class, preferenceHelper.getAccessToken());
        myBoardRequest = new MyBoardRequest();
        myBoardInterface.getMyBoard().enqueue(new Callback<MyBoardResponse>() {
            @Override
            public void onResponse(Call<MyBoardResponse> call, Response<MyBoardResponse> response) {
                if (response.isSuccessful()) {
                    MyBoardResponse result = response.body();
                    if (result.status.equals("200")) {
                        myBoardViewModel.saveMyBoard(result.communityPojo);
                    }
                } else {
                    try {
                        Log.d(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyBoardResponse> call, Throwable t) {
                Log.d("MyBoardResponse REST ERROR!", t.getMessage());
            }
        });
    }

    private void myWalkResponse() {
        myWalkInterface = ServiceGenerator.createService(MyWalkInterface.class, preferenceHelper.getAccessToken());
        myWalkRequest = new MyWalkRequest();
        myWalkInterface.getMyWalkResponse().enqueue(new Callback<MyWalkResponse>() {
            @Override
            public void onResponse(Call<MyWalkResponse> call, Response<MyWalkResponse> response) {
                if (response.isSuccessful()) {
                    MyWalkResponse myWalkResponse = response.body();
                    myWalkData = myWalkResponse.myWalkPojo;
                    Log.d(TAG, String.valueOf(myWalkData.size()));
                    Log.d(TAG, String.valueOf(myWalkData.size()));
                    Log.d(TAG, String.valueOf(myWalkData.get(0).location));
                    for (MyWalkPojo data : myWalkData) {
                        km_count += data.distance;
                    }
                    walk_count = myWalkData.size();
                    tv_km_count.setText(String.valueOf(km_count));
                    tv_walk_count.setText(String.valueOf(walk_count));

                } else {
                    try {
                        Log.d(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyWalkResponse> call, Throwable t) {
                Log.d("MyPageFragment REST ERROR!", t.getMessage());
            }
        });
    }

    private void init(View view) {
        tv_nickname_profile = view.findViewById(R.id.tv_nickname_profile);
        tv_km_count = view.findViewById(R.id.tv_km_count);
        tv_walk_count = view.findViewById(R.id.tv_walk_count);
        tv_post_count = view.findViewById(R.id.tv_post_count);
        tv_title_1 = view.findViewById(R.id.tv_title_1);
        tv_title_2 = view.findViewById(R.id.tv_title_2);
        tv_hashtag_1 = view.findViewById(R.id.tv_hashtag_1);
        tv_hashtag_2 = view.findViewById(R.id.tv_hashtag_2);
        tv_date_1 = view.findViewById(R.id.tv_date_1);
        tv_date_2 = view.findViewById(R.id.tv_date_2);
        btn_favorite_1 = view.findViewById(R.id.btn_favorite_1);
        btn_favorite_2 = view.findViewById(R.id.btn_favorite_2);
        iv_map_1 = view.findViewById(R.id.iv_map_1);
        iv_map_2 = view.findViewById(R.id.iv_map_2);
    }

    static class DateComparator implements Comparator<MyLikePojo> {
        @Override
        public int compare(MyLikePojo value1, MyLikePojo value2) {
            String TAG = "CompareDate";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date day1 = null;
            Date day2 = null;

            try {
                day1 = format.parse(value1.writeDate);
                day2 = format.parse(value2.writeDate);
                Log.d(TAG, "Day1 : " + day1);
                Log.d(TAG, "Day2 : " + day2);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            int result = day1.compareTo(day2);
            return result;
        }
    }


//    private void loadStores() {
//        tokenRequest = new TokenRequest(preferenceHelper.getRefreshToken());
//        tokenInterface.getToken(tokenRequest).enqueue(new Callback<TokenResponse>() {
//            @Override
//            public void onFailure(Call<TokenResponse> call, Throwable t) {
//                Log.d("REST ERROR!", t.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response){
//                if (response.isSuccessful()) {
//
//                     TokenResponse result = (TokenResponse) response.body();
//
//                    Log.d("MyPageFragment", "응답"+response.isSuccessful());
//                    Log.d("MyPageFragment", "새로운 access"+result.tokenPOJO.accessToken);
//                    Log.d("MyPageFragment", "refresh access"+result.tokenPOJO.refreshToken);
//                    Toast.makeText(getContext(), "토큰", Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.d("REST FAILED MESSAGE", response.message());
//                }
//            }
//
//        });
//
//    }
}