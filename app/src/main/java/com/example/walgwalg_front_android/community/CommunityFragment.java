package com.example.walgwalg_front_android.community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.member.DTO.CommunityPojo;
import com.example.walgwalg_front_android.member.DTO.CommunityRequest;
import com.example.walgwalg_front_android.member.DTO.CommunityResponse;
import com.example.walgwalg_front_android.member.DTO.CommunityTopRankPojo;
import com.example.walgwalg_front_android.member.DTO.CommunityTopRankRequest;
import com.example.walgwalg_front_android.member.DTO.CommunityTopRankResponse;
import com.example.walgwalg_front_android.member.Interface.CommunityInterface;
import com.example.walgwalg_front_android.member.Interface.CommunityTopRankInterface;
import com.example.walgwalg_front_android.member.PreferenceHelper;
import com.example.walgwalg_front_android.member.Retrofit.ServiceGenerator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityFragment extends Fragment {

    private String TAG = "CommunityFragmentTAG";

    private RecyclerView rv_top;
    private RecyclerView rv_bottom;
    private FloatingActionButton btn_add;

    private RecyclerView.LayoutManager layoutManager;
    private CommunityAdapter_Top communityAdapterTop;
    private CommunityAdapter_Bottom communityAdapterBottom;

    private CommunityRequest communityRequest;
    private CommunityResponse communityResponse;
    private CommunityTopRankResponse communityTopRankResponse;
    private CommunityTopRankRequest communityTopRankRequest;
    private CommunityInterface communityInterface;
    private CommunityTopRankInterface communityTopRankInterface;
    private PreferenceHelper preferenceHelper;

    private ArrayList<CommunityPojo> boardData;
    private ArrayList<CommunityTopRankPojo> topRankData;
    private ArrayList<CommunityTopRankPojo> testData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        init(view);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_communityFragment_to_communityAddFragment);
            }
        });

        preferenceHelper = new PreferenceHelper(getContext());

        communityInterface = ServiceGenerator.createService(CommunityInterface.class, preferenceHelper.getAccessToken());
        communityTopRankInterface = ServiceGenerator.createService(CommunityTopRankInterface.class, preferenceHelper.getAccessToken());

        communityRequest = new CommunityRequest();
        communityInterface.getCommunityAddResponse()
                .enqueue(new Callback<CommunityResponse>() {
                    @Override
                    public void onResponse(Call<CommunityResponse> call, Response<CommunityResponse> response) {
                        if (response.isSuccessful()) {
                            CommunityResponse result = response.body();
                            boardData = result.communityPojo;
                            rv_bottom.setHasFixedSize(true);
                            RecyclerDecoration spaceDecoration = new RecyclerDecoration(15);
                            rv_bottom.addItemDecoration(spaceDecoration);
                            layoutManager = new GridLayoutManager(getActivity(), 2);
                            rv_bottom.setLayoutManager(layoutManager);
                            rv_bottom.scrollToPosition(0);
                            communityAdapterBottom = new CommunityAdapter_Bottom(boardData);
                            rv_bottom.setAdapter(communityAdapterBottom);
                            rv_bottom.setItemAnimator(new DefaultItemAnimator());
//                            Log.d(TAG, result.id);
//                            Log.d(TAG, result.dateTime);
//                            Log.d(TAG, result.status);
//                            Log.d(TAG, result.message);
//                            Log.d(TAG, result.communityPojo.get(0).title);
//                            Log.d(TAG, result.communityPojo.get(0).contents);
//                            Log.d(TAG, String.valueOf(result.communityPojo.get(1).hashTags.length));
//                            Log.d(TAG, String.valueOf(result.communityPojo.get(1).stepCount));
//                            Log.d(TAG, String.valueOf(result.communityPojo.get(1).distance));
//                            Log.d(TAG, String.valueOf(result.communityPojo.get(1).calorie));
//                            Log.d(TAG, String.valueOf(result.communityPojo.get(1).course));
//                            Log.d(TAG, String.valueOf(result.communityPojo.get(1).location));
//                            Log.d(TAG, String.valueOf(result.communityPojo.get(1).nickname));
//                            Log.d(TAG, String.valueOf(result.communityPojo.get(1).likes));
                        } else {
                            try {
                                Log.d(TAG + " REST FAILED MESSAGE", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CommunityResponse> call, Throwable t) {
                        Log.d(TAG + " REST ERROR!", t.getMessage());
                    }
                });

        communityTopRankRequest = new CommunityTopRankRequest();
        communityTopRankInterface.getCommunityTopRank()
                .enqueue(new Callback<CommunityTopRankResponse>() {
                    @Override
                    public void onResponse(Call<CommunityTopRankResponse> call, Response<CommunityTopRankResponse> response) {
                        if (response.isSuccessful()) {
                            CommunityTopRankResponse result = response.body();
//                            topRankData = result.communityTopRankPojo;

                            CommunityTopRankPojo communityTopRankPojo = new CommunityTopRankPojo();
                            communityTopRankPojo.image = "https://walgwalgbucket.s3.ap-northeast-2.amazonaws.com/course/b105996a-9b06-4346-b5d2-b1d898dd81ac20220706035317screenshot.png";
                            communityTopRankPojo.parkName = "탄천";

                            topRankData.add(communityTopRankPojo);
                            rv_top.setHasFixedSize(true);
                            rv_top.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
                            rv_top.scrollToPosition(0);
                            communityAdapterTop = new CommunityAdapter_Top(topRankData);
                            rv_top.setAdapter(communityAdapterTop);
                            rv_top.setItemAnimator(new DefaultItemAnimator());
//                            Log.d(TAG+"TOP", result.id);
//                            Log.d(TAG+"TOP", result.dateTime);
//                            Log.d(TAG+"TOP", result.status);
//                            Log.d(TAG+"TOP", result.message);
//                            Log.d(TAG+"TOP", result.communityTopRankPojo.get(0).image);
//                            Log.d(TAG+"TOP", result.communityTopRankPojo.get(0).parkName);
                        } else {
                            try {
                                Log.d(TAG + " TOP REST FAILED MESSAGE", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CommunityTopRankResponse> call, Throwable t) {
                        Log.d(TAG + " REST ERROR!", t.getMessage());
                    }
                });

        return view;
    }

    private void init(View view) {
        boardData = new ArrayList<>();
        topRankData = new ArrayList<>();
        rv_top = view.findViewById(R.id.rv_top);
        rv_bottom = view.findViewById(R.id.rv_bottom);
        btn_add = view.findViewById(R.id.btn_add);
    }
}