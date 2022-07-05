package com.example.walgwalg_front_android.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekWeatherFragment extends Fragment {

    private String TAG = "WeekWeatherFragment";
    private ArrayList<CurrentWeather> arrayWeather = new ArrayList<>();
//    private WeatherAdapter weatherAdapter = new WeatherAdapter(arrayWeather, getContext());
    private RecyclerView recyclerView2;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayoutManager linearLayoutManager;
    private TextView txt_time, txt_temp;
    private ImageView img_weather;

    Bundle bundle = new Bundle();

    private String cur_latitude = "00";
    private String cur_longitude = "00";

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    static WeekWeatherFragment instance;

    public static WeekWeatherFragment getInstance() {
        if (instance == null) {
            instance = new WeekWeatherFragment();
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

    public WeekWeatherFragment() {
        // Required empty public constructor
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeekWeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeekWeatherFragment newInstance(String param1, String param2) {
        WeekWeatherFragment fragment = new WeekWeatherFragment();
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
        View view = inflater.inflate(R.layout.fragment_week_weather, container, false);

        init(view);

        WeatherViewModel weatherViewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
        weatherViewModel.getCur_latitude().observe(getViewLifecycleOwner(), item -> {
            Log.d(TAG, "위도인지 고도인지 모르겠다2"+weatherViewModel.getCur_latitude().getValue());
        });

        weatherViewModel.getCur_longitude().observe(getViewLifecycleOwner(), item -> {
            Log.d(TAG, "위도인지 고도인지 모르겠다2"+weatherViewModel.getCur_longitude().getValue());
        });

        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView2.setLayoutManager(linearLayoutManager);

//        recyclerView2.setAdapter(weatherAdapter);

//        CurrentWeather currentWeather = new CurrentWeather(1, null, null, R.drawable.icon_cloud, 24, 36.0);
//        arrayWeather.add(currentWeather);
//        weatherAdapter.notifyDataSetChanged();

        getForecastWeatherInformation();
        //        weatherAdapter.notifyDataSetChanged();

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
                                Log.d(TAG, "WeekWeatherFragment Result: " + weatherForecstResult.daily.get(0).weather.get(0).getIcon());
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

        WeekWeatherAdater adapter = new WeekWeatherAdater(getContext(), weatherForecstResult);
        recyclerView2.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        Log.d(TAG, "Check adapter2");
    }

    public void init(View view) {
        recyclerView2 = view.findViewById(R.id.recyclerview2);
        txt_temp = view.findViewById(R.id.txt_temp);
        txt_time = view.findViewById(R.id.txt_time);
        img_weather = view.findViewById(R.id.img_weather);

    }
}