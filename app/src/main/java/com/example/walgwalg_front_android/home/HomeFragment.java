package com.example.walgwalg_front_android.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.location.RecordActivity;
import com.example.walgwalg_front_android.member.DTO.UserInfoResponse;
import com.example.walgwalg_front_android.member.DTO.WalkCalendarResponse;
import com.example.walgwalg_front_android.member.DTO.WalkTotalResponse;
import com.example.walgwalg_front_android.member.Interface.UserInfoInterface;
import com.example.walgwalg_front_android.member.Interface.WalkCalendarInterface;
import com.example.walgwalg_front_android.member.Interface.WalkTotalInterface;
import com.google.android.material.button.MaterialButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {

    private String TAG = "HomeFragment";

    UserInfoResponse userInfoResponse;
    WalkTotalResponse walkTotalResponse;
    WalkCalendarResponse walkCalendarResponse;
    private UserInfoInterface userInfoInterface;
    private WalkTotalInterface walkTotalInterface;
    private WalkCalendarInterface walkCalendarInterface;
//    private RetrofitClient retrofitClient;
    private HomeRetrofitClient homeRetrofitClient;
    private static String usertoken;

    // Calendar
    private MaterialCalendarView materialCalendarView;
    private RecyclerView recyclerviewRecord;
    private List<String> recordlists  = new ArrayList<>();
    private List<LocalDateTime> recordlistsDate = new ArrayList<LocalDateTime>();
    private List<CalendarDay> recordCalendarDay = new ArrayList<>();
    private ArrayList<RecordData> arrayRecords = new ArrayList<>();
    private CalendarAdater calendarAdater = new CalendarAdater(arrayRecords, getContext());
    private LinearLayoutManager linearLayoutManager;
//    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss");
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy'-'MM'-'dd'T'HH':'mm':'ss");
    private LocalDateTime dateTime;
    private EventDecorator eventDecorator;
    private Calendar calendar = Calendar.getInstance();
    private CalendarDay calendarDay;

    private MaterialButton btn_weather;
    private Button btn_start;
    private ImageView img_user;
    private TextView tv_nickname, txt_cntstep, txt_cntkm, txt_cntkcal, txt_month, txt_day;
//    private DatePicker calendar;
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
        walkCalendarInterface = HomeRetrofitClient.getWalkCalendarInterface();

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
                                        Log.d("WalkTotalMonth REST FAILED MESSAGE1", response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("WalkTotalMonth REST FAILED MESSAGE2", response.message());

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

        // 캘린더
        materialCalendarView.setSelectedDate(CalendarDay.today());

        // 날짜 클릭 시 이벤트
        materialCalendarView.setOnDateChangedListener(this);
        // 달력이 변화할 때의 이벤트
        materialCalendarView.setOnMonthChangedListener(this);

        walkCalendarInterface.getWalkCalendarResponse()
                .enqueue(new Callback<WalkCalendarResponse>() {
            @Override
            public void onResponse(Call<WalkCalendarResponse> call, Response<WalkCalendarResponse> response) {
                if (response.isSuccessful()){
                    walkCalendarResponse = response.body();

                    int size = walkCalendarResponse.walkCalendarList.size();

                    for(int i = 0; i<size; i++){
                        String save = walkCalendarResponse.walkCalendarList.get(i).getWalkDate();
                        int idx = save.indexOf(".");

                        recordlists.add(save.substring(0,idx));

//                            recordlistsDate.add(formatter.parse(recordlists.get(i)));
                        recordlistsDate.add(LocalDateTime.parse(recordlists.get(i), formatter));


                    }

                    Log.d(TAG, "lists : " + recordlists);
                    Log.d(TAG, "형변환 후 lists : " + recordlistsDate);

                    if (recordlistsDate.size()>0){
                        ApiSimulator apiSimulator = new ApiSimulator(recordlistsDate);
                        apiSimulator.execute();
                    }

                }
                else {
                    try {
                        Log.d("WalkCalendar REST FAILED MESSAGE", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("WalkCalendar REST FAILED MESSAGE", response.message());

                }
            }

            @Override
            public void onFailure(Call<WalkCalendarResponse> call, Throwable t) {
                Log.d("WalkCalendar REST ERROR!", t.getMessage());
            }
        });


        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerviewRecord.setLayoutManager(linearLayoutManager);
        recyclerviewRecord.setAdapter(calendarAdater);

        //데이터 추가방법
//        NailshopData nailshopData = new NailshopData(R.drawable.edge, R.drawable.ic_baseline_bookmark_white, "만두네일", null,"5.0", "(112)","11:00~21:00", "(휴무:일)", "경기도 용인시 수지구 현암로 123번길 33" ,null, null, null);
//        arrayShops.add(nailshopData);
//        nailshopAdapter.notifyDataSetChanged();

        RecordData recordData = new RecordData("12:30", "1","2","3");
        arrayRecords.add(recordData);
        RecordData recordData2 = new RecordData("12:30", "3","2","1");
        arrayRecords.add(recordData2);
        calendarAdater.notifyDataSetChanged();

        return view;
    }

    public void gettoken(String token){
        usertoken=token;
        Log.d("LocationFragment","로그인에서 받아온 토큰: "+usertoken);
    }

    public void init(View view) {
        materialCalendarView = view.findViewById(R.id.materialcalendar1);
        txt_month = view.findViewById(R.id.txt_month);
        txt_day = view.findViewById(R.id.txt_day);
        recyclerviewRecord = view.findViewById(R.id.recyclerviewRecord);
        btn_weather = view.findViewById(R.id.btn_weather);
        btn_start = view.findViewById(R.id.btn_start);
        img_user = view.findViewById(R.id.img_user);
        tv_nickname = view.findViewById(R.id.tv_nickname);
        txt_cntkcal = view.findViewById(R.id.txt_cntkcal);
        txt_cntkm = view.findViewById(R.id.txt_cntkm);
        txt_cntstep = view.findViewById(R.id.txt_cntstep);
//        calendar = view.findViewById(R.id.calendar);
        homeRetrofitClient = new HomeRetrofitClient(usertoken);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        String month = String.valueOf(date.getMonth());
        String day = String.valueOf(date.getDay());
        txt_month.setText(month);
        txt_day.setText(day);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }

    class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        List<LocalDateTime> recordlistsDate;

        public ApiSimulator(List<LocalDateTime> recordlistsDate) {
            this.recordlistsDate = recordlistsDate;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids){
            try{
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            for(int i=0; i<recordlistsDate.size(); i++){



                int year = recordlistsDate.get(i).getYear();
                int month = recordlistsDate.get(i).getMonthValue();
                int dayy = recordlistsDate.get(i).getDayOfMonth();

                calendar.set(year, month, dayy);

                CalendarDay day = CalendarDay.from(year, month, dayy);

                dates.add(day);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays){
            super.onPostExecute(calendarDays);

            if (getActivity().isFinishing()){
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays));
        }
    }
}