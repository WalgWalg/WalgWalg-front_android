package com.example.walgwalg_front_android.community;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.ViewModel.MyInfoViewModel;
import com.example.walgwalg_front_android.ViewModel.MyLikeViewModel;
import com.example.walgwalg_front_android.ViewModel.MyLocationViewModel;
import com.example.walgwalg_front_android.member.DTO.CommunityPojo;
import com.example.walgwalg_front_android.member.DTO.CommunityRequest;
import com.example.walgwalg_front_android.member.DTO.CommunityResponse;
import com.example.walgwalg_front_android.member.DTO.CommunityTopRankPojo;
import com.example.walgwalg_front_android.member.DTO.CommunityTopRankRequest;
import com.example.walgwalg_front_android.member.DTO.CommunityTopRankResponse;
import com.example.walgwalg_front_android.member.DTO.LocationCommunityRequest;
import com.example.walgwalg_front_android.member.DTO.LocationCommunityResponse;
import com.example.walgwalg_front_android.member.DTO.MyLikePojo;
import com.example.walgwalg_front_android.member.Interface.CommunityInterface;
import com.example.walgwalg_front_android.member.Interface.CommunityTopRankInterface;
import com.example.walgwalg_front_android.member.Interface.LocationCommunityInterface;
import com.example.walgwalg_front_android.member.PreferenceHelper;
import com.example.walgwalg_front_android.member.Retrofit.ServiceGenerator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityFragment extends Fragment {

    private String TAG = "CommunityFragmentTAG";

    private RecyclerView rv_top;
    private RecyclerView rv_bottom;
    private FloatingActionButton btn_add;
    private ChipGroup location_chipGroup;
    private TextInputEditText edt_search;
    private MaterialButton btn_search;

    private MyLocationViewModel myLocationViewModel;
    private MyLikeViewModel myLikeViewModel;

    private RecyclerView.LayoutManager layoutManager;
    private CommunityAdapter_Top communityAdapterTop;
    private CommunityAdapter_Bottom communityAdapterBottom;

    private CommunityInterface communityInterface;
    private CommunityRequest communityRequest;
    private CommunityResponse communityResponse;
    private CommunityTopRankInterface communityTopRankInterface;
    private CommunityTopRankResponse communityTopRankResponse;
    private CommunityTopRankRequest communityTopRankRequest;
    private LocationCommunityInterface locationCommunityInterface;
    private LocationCommunityRequest locationCommunityRequest;
    private LocationCommunityResponse locationCommunityResponse;
    private PreferenceHelper preferenceHelper;

    private ArrayList<CommunityPojo> boardData, filteredList;
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

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = edt_search.getText().toString();

                if(!search.isEmpty()){
                    specificCommunity(edt_search.getText().toString());
                }else{
                    EntireCommunity();
                }

            }
        });

        myLocationViewModel = new ViewModelProvider(requireActivity()).get(MyLocationViewModel.class);
        myLikeViewModel = new ViewModelProvider(requireActivity()).get(MyLikeViewModel.class);

        preferenceHelper = new PreferenceHelper(getContext());

        communityInterface = ServiceGenerator.createService(CommunityInterface.class, preferenceHelper.getAccessToken());
        communityTopRankInterface = ServiceGenerator.createService(CommunityTopRankInterface.class, preferenceHelper.getAccessToken());
        locationCommunityInterface = ServiceGenerator.createService(LocationCommunityInterface.class, preferenceHelper.getAccessToken());

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

        // 상단 랭크 조회
        topRank();

        // 게시판 전체 조회
        EntireCommunity();

        chip_Si_Do.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
//                    Log.d(TAG, "시도 체크 : "+ String.valueOf(chip_Si_Do.isChecked()));
                    specificCommunity(chip_Si_Do.getText().toString());
                } else if (b == false) {
                    Log.d(TAG, "시도 체크 : " + String.valueOf(chip_Si_Do.isChecked()));
                    EntireCommunity();
                }
            }
        });

        chip_Si_Gun_Gu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    Log.d(TAG, "시도 체크 : " + String.valueOf(chip_Si_Gun_Gu.isChecked()));
                    specificCommunity(chip_Si_Gun_Gu.getText().toString());
                } else if (b == false) {
                    Log.d(TAG, "시도 체크 : " + String.valueOf(chip_Si_Gun_Gu.isChecked()));
                    EntireCommunity();
                }
            }
        });

        chip_Eup_Myeon_Dong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    Log.d(TAG, "시도 체크 : " + String.valueOf(chip_Eup_Myeon_Dong.isChecked()));
                    specificCommunity(chip_Eup_Myeon_Dong.getText().toString());
                } else if (b == false) {
                    Log.d(TAG, "시도 체크 : " + String.valueOf(chip_Eup_Myeon_Dong.isChecked()));
                    EntireCommunity();
                }
            }
        });

//        if (chip_Si_Do.isChecked()) {
//            Log.d(TAG, "시도 체크 : "+ String.valueOf(chip_Si_Do.isChecked()));
//            specificCommunity(chip_Si_Do.getText().toString());
//        } else if (chip_Si_Gun_Gu.isChecked()) {
//            Log.d(TAG, "시도 체크 : "+ String.valueOf(chip_Si_Gun_Gu.isChecked()));
//            specificCommunity(chip_Si_Gun_Gu.getText().toString());
//        } else if (chip_Eup_Myeon_Dong.isChecked()) {
//            Log.d(TAG, "시도 체크 : "+ String.valueOf(chip_Eup_Myeon_Dong.isChecked()));
//            specificCommunity(chip_Eup_Myeon_Dong.getText().toString());
//        }

        return view;
    }

    public void specificCommunity(String location) {
        locationCommunityRequest = new LocationCommunityRequest();
        locationCommunityInterface.getRegionPost(location)
                .enqueue(new Callback<LocationCommunityResponse>() {
                    @Override
                    public void onResponse(Call<LocationCommunityResponse> call, Response<LocationCommunityResponse> response) {
                        if (response.isSuccessful()) {
                            locationCommunityResponse = response.body();
                            if (locationCommunityResponse.status.equals("200")) {
                                boardData = locationCommunityResponse.communityPojo;
                                DateComparator dateComparator = new DateComparator();
                                Collections.sort(boardData, dateComparator);
                                bottom_rv_update(boardData, myLikeViewModel.getMyLikeData().getValue());
                            } else {
                                Log.d(TAG + " Error ", response.message());
                            }

                        } else {
                            try {
                                Log.d(TAG + " REST FAILED MESSAGE", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LocationCommunityResponse> call, Throwable t) {
                        Log.d(TAG + " REST ERROR!", t.getMessage());
                    }
                });
    }

    public void EntireCommunity() {
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
                            DateComparator dateComparator = new DateComparator();
                            Collections.sort(boardData, dateComparator);
//                            Log.d(TAG, boardData.get(2).date);
                            bottom_rv_update(boardData, myLikeViewModel.getMyLikeData().getValue());

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
    }

    public void topRank() {
        communityTopRankRequest = new CommunityTopRankRequest();
        communityTopRankInterface.getCommunityTopRank()
                .enqueue(new Callback<CommunityTopRankResponse>() {
                    @Override
                    public void onResponse(Call<CommunityTopRankResponse> call, Response<CommunityTopRankResponse> response) {
                        if (response.isSuccessful()) {
                            CommunityTopRankResponse result = response.body();
                            topRankData = result.communityTopRankPojo;

//                            CommunityTopRankPojo communityTopRankPojo = new CommunityTopRankPojo();
//                            communityTopRankPojo.image = "https://walgwalgbucket.s3.ap-northeast-2.amazonaws.com/course/b105996a-9b06-4346-b5d2-b1d898dd81ac20220706035317screenshot.png";
//                            communityTopRankPojo.parkName = "탄천";
//                            topRankData.add(communityTopRankPojo);


                            if (topRankData == null || topRankData.isEmpty()) {
                                CommunityTopRankPojo communityTopRankPojo = new CommunityTopRankPojo();
                                communityTopRankPojo.image = "";
                                communityTopRankPojo.parkName = "로드에 실패하였습니다.";
                                topRankData.add(communityTopRankPojo);
                            }

                            for (CommunityTopRankPojo data : topRankData) {
                                if (data.parkName.isEmpty()) {
                                    data.parkName = "로드에 실패하였습니다.";
                                }
                                if (data.image.isEmpty()) {
                                    data.image = "";
                                }
                            }

                            top_rv_update(topRankData);

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
    }

    public void top_rv_update(ArrayList<CommunityTopRankPojo> topRankData) {
        rv_top.setHasFixedSize(true);
        rv_top.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        rv_top.scrollToPosition(0);
        communityAdapterTop = new CommunityAdapter_Top(topRankData);
        rv_top.setAdapter(communityAdapterTop);
        rv_top.setItemAnimator(new DefaultItemAnimator());
    }

    public void bottom_rv_update(ArrayList<CommunityPojo> boardData, ArrayList<MyLikePojo> myLikeData) {
        rv_bottom.setHasFixedSize(true);
//        RecyclerDecoration spaceDecoration = new RecyclerDecoration(15);
//        rv_bottom.addItemDecoration(spaceDecoration);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        rv_bottom.setLayoutManager(layoutManager);
        rv_bottom.scrollToPosition(0);
        communityAdapterBottom = new CommunityAdapter_Bottom(boardData, myLikeData);
        rv_bottom.setAdapter(communityAdapterBottom);
        rv_bottom.setItemAnimator(new DefaultItemAnimator());
    }

    private void init(View view) {
        boardData = new ArrayList<>();
        topRankData = new ArrayList<>();
        filteredList = new ArrayList<>();
        edt_search = view.findViewById(R.id.edt_search);
        rv_top = view.findViewById(R.id.rv_top);
        rv_bottom = view.findViewById(R.id.rv_bottom);
        location_chipGroup = view.findViewById(R.id.chipGroup);
        btn_add = view.findViewById(R.id.btn_add);
        btn_search = view.findViewById(R.id.btn_search);
    }

    static class DateComparator implements Comparator<CommunityPojo> {
        @Override
        public int compare(CommunityPojo value1, CommunityPojo value2) {
            String TAG = "CompareDate";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date day1 = null;
            Date day2 = null;

            try {
                day1 = format.parse(value1.date);
                day2 = format.parse(value2.date);
                Log.d(TAG, "Day1 : " + day1);
                Log.d(TAG, "Day2 : " + day2);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            int result = day1.compareTo(day2);
            return result;
        }
    }

//    public void searchFilter(String searchText) {
//        filteredList.clear();
//
//        for (int i = 0; i < boardData.size(); i++) {
//            if (boardData.get(i).toLowerCase().contains(searchText.toLowerCase())) {
//                filteredList.add(boardData.get(i));
//            }
//        }
//
//        foodAdapter.filterList(filteredList);
//    }

}