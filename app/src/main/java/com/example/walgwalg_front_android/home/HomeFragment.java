package com.example.walgwalg_front_android.home;

import static android.graphics.BlendMode.COLOR;

import android.content.Intent;
import android.os.Bundle;

import androidx.compose.foundation.shape.RoundedCornerShape;
import androidx.compose.ui.graphics.Color;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.location.RecordActivity;
import com.example.walgwalg_front_android.member.DTO.RegisterResponse;
import com.example.walgwalg_front_android.member.DTO.UserInfoResponse;
import com.example.walgwalg_front_android.member.DTO.WalkTotalResponse;
import com.example.walgwalg_front_android.member.Interface.CommunityAddInterface;
import com.example.walgwalg_front_android.member.Interface.RegisterInterface;
import com.example.walgwalg_front_android.member.Interface.UserInfoInterface;
import com.example.walgwalg_front_android.member.Interface.WalkTotalInterface;
import com.example.walgwalg_front_android.member.Retrofit.RetrofitClient;
import com.example.walgwalg_front_android.member.Retrofit.ServiceGenerator;
import com.google.android.material.button.MaterialButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private String TAG = "HomeFragment";

    UserInfoResponse userInfoResponse;
    WalkTotalResponse walkTotalResponse;
    private UserInfoInterface userInfoInterface;
    private WalkTotalInterface walkTotalInterface;
//    private RetrofitClient retrofitClient;
    private HomeRetrofitClient homeRetrofitClient;
    private static String usertoken;

    private MaterialCalendarView materialCalendarView;
    private MaterialButton btn_weather;
    private Button btn_start;
    private ImageView img_user;
    private TextView tv_nickname, txt_cntstep, txt_cntkm, txt_cntkcal;
    private DatePicker calendar;
    public static FragmentManager fragmentManager;
    private boolean isNavigating = false;
    private String name, profile;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);

//        CalendarView.setTop(R.color.mainColor);

//        materialCalendarView.setColor;

//        materialCalendarView.state().edit()
//                .setFirstDayOfWeek(Calendar.SUNDAY)
//                .setMinimumDate(CalendarDay.from(2021, 0,1))
//                .setMaximumDate(CalendarDay.from(2030,12,31))
//                .setCalendarDisplayMode(CalendarMode.MONTHS)
//                .commit();
        //retrofit 생성
        homeRetrofitClient = HomeRetrofitClient.getInstance();
        userInfoInterface = HomeRetrofitClient.getUserInfoInterface();
        walkTotalInterface = HomeRetrofitClient.getWalkTotalInterface();

//        userInfoInterface = ServiceGenerator.createService(UserInfoInterface.class, "PMAK-62c2bb573eaf6129f000481d-7a7ec096d3dd182e419dc0fb7e0473d544");
//        walkTotalInterface = ServiceGenerator.createService(WalkTotalInterface.class, "PMAK-62c2bb573eaf6129f000481d-7a7ec096d3dd182e419dc0fb7e0473d544");

        userInfoInterface.getUserInfoResponse()
                        .enqueue(new Callback<UserInfoResponse>() {
                            @Override
                            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                                if(response.isSuccessful()){
                                    userInfoResponse = response.body();
                                    name = userInfoResponse.list.getNickname();
                                    profile = userInfoResponse.list.getProfile();
                                    Log.d(TAG, "user : " + name +" / " + profile);
                                    tv_nickname.setText(name);
                                    if (profile != null){
                                        Picasso.get().load(profile)
                                                .transform(new RoundedTransformation(360, 0))
                                                .into(img_user);
                                    }
//                                    Glide.with(img_user).load(profile);
                                }
                                else{
                                    try {
                                        Log.d("UserInfo REST FAILED MESSAGE", response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("UserInfo REST FAILED MESSAGE", response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                                Log.d("UserInfo REST ERROR!", t.getMessage());
                            }
                        });

        walkTotalInterface.getWalkTotalResponse()
                        .enqueue(new Callback<WalkTotalResponse>() {
                            @Override
                            public void onResponse(Call<WalkTotalResponse> call, Response<WalkTotalResponse> response) {
                                if(response.isSuccessful()){
                                    walkTotalResponse = response.body();
                                    String cntstep = String.valueOf(walkTotalResponse.walkTotalList.walkTotal.walkTotalMonth.getStepCount());
                                    String cntkm = String.valueOf(walkTotalResponse.walkTotalList.walkTotal.walkTotalMonth.getDistance());
                                    String cntkcal = String.valueOf(walkTotalResponse.walkTotalList.walkTotal.walkTotalMonth.getStepCount()/300);

                                    txt_cntstep.setText(cntstep);
                                    txt_cntkm.setText(cntkm);
                                    txt_cntkcal.setText(cntkcal);

                                    Log.d(TAG, "TotalMonth : " + cntstep +" / " + cntkm + " / " + cntkcal);
                                }
                                else {
                                    try {
                                        Log.d("WalkTotalMonth REST FAILED MESSAGE", response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("WalkTotalMonth REST FAILED MESSAGE", response.message());

                                }
                            }

                            @Override
                            public void onFailure(Call<WalkTotalResponse> call, Throwable t) {
                                Log.d("WalkTotalMonth REST ERROR!", t.getMessage());
                            }
                        });




        btn_weather.setOnClickListener(task -> {
//            fragmentManager = getActivity().getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.homeFragment, new WeatherFragment()).addToBackStack(null).commit();
            isNavigating = true;
            Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_weatherFragment);
        });
        isNavigating = false;
        btn_start.setOnClickListener(task ->{
            isNavigating = true;
//            Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_recordFragment);
            startActivity(new Intent(getActivity(), RecordActivity.class));
        });

        return view;
    }

    public void gettoken(String token){
        usertoken=token;
        Log.d("LocationFragment","로그인에서 받아온 토큰: "+usertoken);
    }

    public void init(View view) {
//        materialCalendarView = view.findViewById(R.id.calendar);
        btn_weather = view.findViewById(R.id.btn_weather);
        btn_start = view.findViewById(R.id.btn_start);
        img_user = view.findViewById(R.id.img_user);
        tv_nickname = view.findViewById(R.id.tv_nickname);
        txt_cntkcal = view.findViewById(R.id.txt_cntkcal);
        txt_cntkm = view.findViewById(R.id.txt_cntkm);
        txt_cntstep = view.findViewById(R.id.txt_cntstep);
        calendar = view.findViewById(R.id.calendar);
        homeRetrofitClient = new HomeRetrofitClient(usertoken);
    }
}