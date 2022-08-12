package com.example.walgwalg_front_android.community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.member.DTO.CommunityPojo;
import com.example.walgwalg_front_android.member.DTO.CommunityRequest;
import com.example.walgwalg_front_android.member.DTO.CommunityResponse;
import com.example.walgwalg_front_android.member.DTO.PostPojo;
import com.example.walgwalg_front_android.member.DTO.PostRequest;
import com.example.walgwalg_front_android.member.DTO.PostResponse;
import com.example.walgwalg_front_android.member.DTO.RegisterResponse;
import com.example.walgwalg_front_android.member.Interface.CommunityInterface;
import com.example.walgwalg_front_android.member.Interface.CommunityTopRankInterface;
import com.example.walgwalg_front_android.member.Interface.PostInterface;
import com.example.walgwalg_front_android.member.PreferenceHelper;
import com.example.walgwalg_front_android.member.Retrofit.ServiceGenerator;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    private String TAG = "PostFragmentTAG";

    private TextView tv_name, tv_title, tv_contents, tv_step, tv_distance, tv_calorie, tv_favorite, tv_hashtag;
    private ImageView img_route;
    private MaterialButton btn_like, btn_share, btn_track;

    private PostInterface postInterface;
    private PostRequest postRequest;
    private PostResponse postResponse;
    private PreferenceHelper preferenceHelper;

    private String boardId;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        boardId = getArguments().getString("boardId");
        Log.d(TAG, boardId);

        preferenceHelper = new PreferenceHelper(getContext());
        postInterface = ServiceGenerator.createService(PostInterface.class, preferenceHelper.getAccessToken());
        postRequest = new PostRequest();
        postInterface.getPost(boardId).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()) {
                    PostResponse result = response.body();
                    tv_name.setText(result.postPojo.nickname);
                    tv_title.setText(result.postPojo.title);
                    String str_img = result.postPojo.course;
                    Log.d(TAG, str_img);
                    Glide.with(getView()).load(str_img).into(img_route);
                    tv_step.setText(String.valueOf(result.postPojo.step_count));
                    tv_distance.setText(String.valueOf(result.postPojo.distance));
                    tv_calorie.setText(String.valueOf(result.postPojo.calorie));
                    tv_contents.setText(result.postPojo.contents);
                    tv_favorite.setText(String.valueOf(result.postPojo.likes));
                    String[] favorite = result.postPojo.hashTags;
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i<favorite.length; i++){
                        sb.append("#");
                        sb.append(favorite[i]);
                    }
                    tv_hashtag.setText(sb);
                } else {
                    try {
                        Log.d(TAG + " REST FAILED MESSAGE", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d(TAG + " REST ERROR!", t.getMessage());
            }
        });


        init(view);

        return view;
    }

    private void init(View view) {
        tv_name = view.findViewById(R.id.tv_name);
        tv_title = view.findViewById(R.id.tv_title);
        img_route = view.findViewById(R.id.img_route);
        tv_contents = view.findViewById(R.id.tv_contents);
        tv_step = view.findViewById(R.id.tv_step);
        tv_distance = view.findViewById(R.id.tv_distance);
        tv_calorie = view.findViewById(R.id.tv_calorie);
        tv_favorite = view.findViewById(R.id.tv_favorite);
        tv_hashtag = view.findViewById(R.id.tv_hashtag);
        btn_like = view.findViewById(R.id.btn_like);
        btn_share = view.findViewById(R.id.btn_share);
        btn_track = view.findViewById(R.id.btn_track);
    }
}