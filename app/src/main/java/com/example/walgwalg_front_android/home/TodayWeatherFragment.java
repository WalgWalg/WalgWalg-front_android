package com.example.walgwalg_front_android.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayWeatherFragment extends Fragment {

    private String TAG = "TodayWeatherFragment";
    private ArrayList<CurrentWeather> arrayWeather = new ArrayList<>();
//    private WeatherAdapter weatherAdapter = new WeatherAdapter(arrayWeather, getContext());
    private RecyclerView recyclerView1;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayoutManager linearLayoutManager;
    private TextView txt_time, txt_temp;
    private ImageView img_weather;

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;


    static TodayWeatherFragment instance;

    public static TodayWeatherFragment getInstance(){
        if(instance == null){
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayWeatherFragment.
     */
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_weather, container, false);

        init(view);

        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        getForecastWeatherInformation();

//        for(int i=1; i<=10; i++){
//            switch (i%5){
//                case 0:
//                    arrayWeather.add(new CurrentWeather(null, null, null, R.drawable.icon_cloud,"12", "21"));
//                    break;
//                case 1:
//                    arrayWeather.add(new CurrentWeather(null, null, null, R.drawable.icon_rain,"13", "22"));
//                    break;
//                case 2:
//                    arrayWeather.add(new CurrentWeather(null, null, null, R.drawable.icon_snow,"14", "23"));
//                    break;
//                case 3:
//                    arrayWeather.add(new CurrentWeather(null, null, null, R.drawable.icon_sun,"15", "24"));
//                    break;
//                case 4:
//                    arrayWeather.add(new CurrentWeather(null, null, null, R.drawable.icon_sun,"16", "25"));
//                    break;
//            }
//        }

        recyclerView1.setLayoutManager(linearLayoutManager);
//        recyclerView1.setAdapter(weatherAdapter);

//        CurrentWeather currentWeather = new CurrentWeather(1, null, null, R.drawable.icon_cloud, 24, 36.0);
//        arrayWeather.add(currentWeather);
//        weatherAdapter.notifyDataSetChanged();

//        for(int i=1; i<=10; i++){
//            switch (i%5){
//                case 0:
//                    arrayWeather.add(new CurrentWeather(null, null, null, R.drawable.icon_cloud,"12", "21"));
//                    break;
//                case 1:
//                    arrayWeather.add(new CurrentWeather(null, null, null, R.drawable.icon_rain,"13", "22"));
//                    break;
//                case 2:
//                    arrayWeather.add(new CurrentWeather(null, null, null, R.drawable.icon_snow,"14", "23"));
//                    break;
//                case 3:
//                    arrayWeather.add(new CurrentWeather(null, null, null, R.drawable.icon_sun,"15", "24"));
//                    break;
//                case 4:
//                    arrayWeather.add(new CurrentWeather(null, null, null, R.drawable.icon_sun,"16", "25"));
//                    break;
//            }
//        }

//        weatherAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onStop(){
        compositeDisposable.clear();
        super.onStop();
    }

    private void getForecastWeatherInformation() {

        compositeDisposable.add(mService.getForecastWeatherByLatLng(
                String.valueOf(Common_Weather.current_location.getLatitude()),
                String.valueOf(Common_Weather.current_location.getLongitude()),
                Common_Weather.APP_ID,
                "metric",
                "kr")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecstResult>() {
                    @Override
                    public void accept(WeatherForecstResult weatherForecstResult) throws Exception {
                        displayForecastWeather(weatherForecstResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "" + throwable.getMessage());

                    }
                })
        );
    }

    private void displayForecastWeather(WeatherForecstResult weatherForecstResult) {
        WeatherAdapter adapter = new WeatherAdapter(getContext(), weatherForecstResult);
        recyclerView1.setAdapter(adapter);
    }

    public void init(View view) {
        recyclerView1 = view.findViewById(R.id.recyclerview1);
        txt_temp = view.findViewById(R.id.txt_temp);
        txt_time = view.findViewById(R.id.txt_time);
        img_weather = view.findViewById(R.id.img_weather);
    }
}