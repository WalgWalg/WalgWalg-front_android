package com.example.walgwalg_front_android.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.model.CurrentWeather;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekWeatherFragment extends Fragment {

    private String TAG = "WeekWeatherFragment";
    private ArrayList<CurrentWeather> arrayWeather = new ArrayList<>();
    private WeatherAdapter weatherAdapter = new WeatherAdapter(arrayWeather, getContext());
    private RecyclerView recyclerView2;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayoutManager linearLayoutManager;
    private TextView txt_time, txt_temp;
    private ImageView img_weather;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeekWeatherFragment() {
        // Required empty public constructor
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

        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView2.setLayoutManager(linearLayoutManager);

        recyclerView2.setAdapter(weatherAdapter);

//        CurrentWeather currentWeather = new CurrentWeather(1, null, null, R.drawable.icon_cloud, 24, 36.0);
//        arrayWeather.add(currentWeather);
//        weatherAdapter.notifyDataSetChanged();

        for(int i=1; i<=10; i++){
            switch (i%5){
                case 0:
                    arrayWeather.add(new CurrentWeather(null, null, null, R.drawable.icon_cloud,"12", "21"));
                    break;
                case 1:
                    arrayWeather.add(new CurrentWeather(null, null, null, R.drawable.icon_rain,"13", "22"));
                    break;
                case 2:
                    arrayWeather.add(new CurrentWeather(null, null, null, R.drawable.icon_snow,"14", "23"));
                    break;
                case 3:
                    arrayWeather.add(new CurrentWeather(null, null, null, R.drawable.icon_sun,"15", "24"));
                    break;
                case 4:
                    arrayWeather.add(new CurrentWeather(null, null, null, R.drawable.icon_sun,"16", "25"));
                    break;
            }
        }
        weatherAdapter.notifyDataSetChanged();

        return view;
    }

    public void init(View view) {
        recyclerView2 = view.findViewById(R.id.recyclerview2);
        txt_temp = view.findViewById(R.id.txt_temp);
        txt_time = view.findViewById(R.id.txt_time);
        img_weather = view.findViewById(R.id.img_weather);

    }
}