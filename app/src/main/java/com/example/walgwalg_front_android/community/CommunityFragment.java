package com.example.walgwalg_front_android.community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.walgwalg_front_android.ViewModel.MyInfoViewModel;
import com.example.walgwalg_front_android.ViewModel.MyLocationViewModel;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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
    private ChipGroup location_chipGroup;

    private MyLocationViewModel myLocationViewModel;

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

    private String Si_Do;
    private String Si_Gun_Gu;
    private String Eup_Myeon_Dong;

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

        myLocationViewModel = new ViewModelProvider(requireActivity()).get(MyLocationViewModel.class);

        Si_Do = myLocationViewModel.getMyLocationData().getValue().Si_Do;
        Si_Gun_Gu = myLocationViewModel.getMyLocationData().getValue().Si_Gun_Gu;
        Eup_Myeon_Dong = myLocationViewModel.getMyLocationData().getValue().Eup_Myeon_Dong;

        Chip chip_Si_Do = new Chip(getContext());
        Chip chip_Si_Gun_Gu = new Chip(getContext());
        Chip chip_Eup_Myeon_Dong = new Chip(getContext());

        chip_Si_Do.setText(Si_Do);
        chip_Si_Gun_Gu.setText(Si_Gun_Gu);
        chip_Eup_Myeon_Dong.setText(Eup_Myeon_Dong);

        chip_Si_Do.setCheckable(true);
        chip_Si_Gun_Gu.setCheckable(true);
        chip_Eup_Myeon_Dong.setCheckable(true);

//        chip_Si_Do.setTextSize(18);
//        chip_Si_Gun_Gu.setTextSize(18);
//        chip_Eup_Myeon_Dong.setTextSize(18);

        chip_Si_Do.setChipBackgroundColorResource(R.color.mainColor);
        chip_Si_Gun_Gu.setChipBackgroundColorResource(R.color.mainColor);
        chip_Eup_Myeon_Dong.setChipBackgroundColorResource(R.color.mainColor);

        location_chipGroup.addView(chip_Si_Do);
        location_chipGroup.addView(chip_Si_Gun_Gu);
        location_chipGroup.addView(chip_Eup_Myeon_Dong);

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
                        Log.d(TAG, response.message());
                        if (response.isSuccessful()) {
                            CommunityResponse result = response.body();
                            Log.d(TAG, result.status);
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
        location_chipGroup = view.findViewById(R.id.chipGroup);
        btn_add = view.findViewById(R.id.btn_add);
    }
}