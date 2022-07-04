package com.example.walgwalg_front_android.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.model.CurrentWeather;
import com.example.walgwalg_front_android.model.WeatherForecstResult;
import com.example.walgwalg_front_android.retrofit.IOpenWeatherMap;
import com.example.walgwalg_front_android.retrofit.RetrofitClient;
import com.example.walgwalg_front_android.weather.Common_Weather;

import org.w3c.dom.Text;

import java.util.ArrayList;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class TodayWeatherFragment extends Fragment {

    private String TAG = "TodayWeatherFragment";
    private RecyclerView recyclerView1;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayoutManager linearLayoutManager;
    private TextView txt_time, txt_temp;
    private ImageView img_weather;
    Bundle bundle = new Bundle();

    private String cur_latitude = "00";
    private String cur_longitude = "00";

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    static TodayWeatherFragment instance;

    public static TodayWeatherFragment getInstance() {
        if (instance == null) {
            instance = new TodayWeatherFragment();
        }
        return instance;
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TodayWeatherFragment() {
        // Required empty public constructor.
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);

    }


    // TODO: Rename and change types and number of parameters
    public static TodayWeatherFragment newInstance(String param1, String param2) {
        TodayWeatherFragment fragment = new TodayWeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "5 / " + cur_latitude + "/" + cur_longitude);
//        WeatherFragment weatherFragment = new WeatherFragment();
//        FragmentManager fragmentManager = weatherFragment.getParentFragmentManager();
//        fragmentManager.setFragmentResultListener("requestKey", this, new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
//                String result = bundle.getString("bundleKey");
//                Log.d(TAG, result);
//                // Do something with the result..
//            }
//        });
//        if (getArguments() != null) {
////            mParam1 = getArguments().getString(ARG_PARAM1);
////            mParam2 = getArguments().getString(ARG_PARAM2);
//            cur_longitude = getArguments().getString("cur_longitude");
//            cur_latitude = getArguments().getString("cur_latitude");
//            Log.d(TAG, "2 / " + cur_latitude + "/" + cur_longitude);
//        }
//        Bundle mArgs = getArguments();
//        if(mArgs != null){
////            cur_longitude = mArgs.getString("cur_longitude");
//            cur_latitude = mArgs.getString("test");
//            Log.d(TAG, "2 / " + cur_latitude + "/" + cur_longitude);
//        }

//        getChildFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//                cur_longitude = result.getString("cur_longitude");
//                cur_latitude = result.getString("cur_latitude");
//                Log.d(TAG, "2 / " + cur_latitude + "/" + cur_longitude);
//            }
//        });

//        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//                cur_longitude = result.getString("cur_longitude");
//                cur_latitude = result.getString("cur_latitude");
//                Log.d(TAG, "2 / " + cur_latitude + "/" + cur_longitude);
//            }
//        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_weather, container, false);

        WeatherViewModel weatherViewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
        weatherViewModel.getCur_latitude().observe(getViewLifecycleOwner(), item -> {
            Log.d(TAG, "위도인지 고도인지 모르겠다"+weatherViewModel.getCur_latitude().getValue());
        });

        weatherViewModel.getCur_longitude().observe(getViewLifecycleOwner(), item -> {
            Log.d(TAG, "위도인지 고도인지 모르겠다"+weatherViewModel.getCur_longitude().getValue());
        });

        init(view);

//        Bundle bundle = getArguments();
//
//        if(getArguments() != null){ //야기
//            cur_longitude = bundle.getString("cur_longitude");
//            cur_latitude = bundle.getString("cur_latitude");
//            Log.d(TAG, "2 / " + cur_latitude + "/" + cur_longitude);
//        }

        onCreate(bundle);

        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView1.setLayoutManager(linearLayoutManager);
        getForecastWeatherInformation();

        return view;
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    private void getForecastWeatherInformation() {

        Log.d(TAG, "4 " + cur_latitude + "/" + cur_longitude);

        compositeDisposable.add(mService.getForecastWeatherByLatLng(
                                String.valueOf(cur_latitude),
                                String.valueOf(cur_longitude),
//                                "37.432124",
//                                "127.129064",
                                Common_Weather.APP_ID,
                                "metric",
                                "kr")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<WeatherForecstResult>() {
                            @Override
                            public void accept(WeatherForecstResult weatherForecstResult) throws Exception {
                                Log.d(TAG, "TodayWeatherFragment Result: " + weatherForecstResult.hourly.get(0).weather.get(0).getIcon());
                                displayForecastWeather(weatherForecstResult);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d(TAG, "Exception::: " + throwable.getMessage());

                            }
                        })
        );
    }

    private void displayForecastWeather(WeatherForecstResult weatherForecstResult) {

        WeatherAdapter adapter = new WeatherAdapter(getContext(), weatherForecstResult);
        recyclerView1.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        Log.d(TAG, "Check adapter");
    }

    public void init(View view) {
        recyclerView1 = view.findViewById(R.id.recyclerview1);
        txt_temp = view.findViewById(R.id.txt_temp);
        txt_time = view.findViewById(R.id.txt_time);
        img_weather = view.findViewById(R.id.img_weather);
    }
}