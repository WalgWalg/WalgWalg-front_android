package com.example.walgwalg_front_android.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.model.CurrentWeather;
import com.example.walgwalg_front_android.model.WeatherResult;
import com.example.walgwalg_front_android.retrofit.IOpenWeatherMap;
import com.example.walgwalg_front_android.retrofit.RetrofitClient;
import com.example.walgwalg_front_android.weather.Common_Weather;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class WeatherFragment extends Fragment {

    private String TAG = "WeatherFragment";
    //    private MaterialToolbar tb_weather;
    FragmentManager fragmentManager;
    //    public FragmentManager fragmentManager = HomeFragment.fragmentManager.findFragmentById(R.id.homeFragment);
    TodayWeatherFragment todayWeatherFragment;
    WeekWeatherFragment weekWeatherFragment;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabPagerAdapter tabPagerAdapter;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private TextView txt_dateview, txt_cityview, txt_tempview;
    private Double cur_latitude, cur_longitue;
    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;
    private LocationManager lm;

    public WeatherFragment() {
        // Required empty public constructor
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }

    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        init(view);
        hideBottomNavigation(true);
        setHasOptionsMenu(true);
        tabLayout.setupWithViewPager(viewPager);

        tabPagerAdapter = new TabPagerAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabPagerAdapter.addFragment(new TodayWeatherFragment(), "Today");
        tabPagerAdapter.addFragment(new WeekWeatherFragment(), "Week");
        viewPager.setAdapter(tabPagerAdapter);

        //Request permission
//        Dexter.withActivity(getActivity())
//                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.ACCESS_FINE_LOCATION)
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        buildLocationRequest();
//                        buildLocationCallBack();
//
//                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                            return;
//                        }
//                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
//                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
//                        getWeatherInformation();
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//
//                    }
//                }).check();

        // 위치 받아오기
        if(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getContext().getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) getActivity().getApplicationContext(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        } else{
            LocationManager lm = (LocationManager) getContext().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            String provider = location.getProvider();
            double now_longitude = location.getLongitude();
            double now_latitude = location.getLatitude();
            double now_altitude = location.getAltitude();

            cur_latitude = now_latitude;
            cur_longitue = now_longitude;

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000,
                    1,
                    gpsLocationListener);

            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    3000,
                    1,
                    gpsLocationListener);
        }

        getWeatherInformation();


        // Toolbar 활성화

//        fragmentManager = getActivity().getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.weatherFragment, new HomeFragment()).addToBackStack(null).commit();

//        ((AppCompatActivity)getActivity()).setSupportActionBar(tb_weather);
//        Objects.requireNonNull(((AppCompatActivity)getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(null); // Toolbar 제목 제거

        return view;
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            String provider = location.getProvider();
            double now_logitude = location.getLongitude();
            double now_latitude = location.getLatitude();
            double now_altitude = location.getAltitude();

            cur_latitude = now_latitude;
            cur_longitue = now_logitude;

            LocationManager lm = (LocationManager) getContext().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

            if(ActivityCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return;
            }
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    3000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000,
                    1,
                    gpsLocationListener);
        }

        public void onStatusChanged(String provider, int status, Bundle extras){

        }
        public void onProviderEnabled(String provider){

        }
        public void onProviderDisabled(String provider){

        }
    };


    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Common_Weather.current_location = locationResult.getLastLocation();
                cur_latitude = locationResult.getLastLocation().getLatitude();
                cur_longitue = locationResult.getLastLocation().getLongitude();

                Log.d("Location", locationResult.getLastLocation().getLatitude() + "/" + locationResult.getLastLocation().getLongitude());

            }
        };
    }

    private void buildLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10.0f);
    }

    // 날씨 정보 받아오기
    private void getWeatherInformation() {
        //TODO: 나갔다 돌아오면 화면 사라지는 오류 고치기
        Log.d(TAG, cur_latitude + "/" + cur_longitue);

//        compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common_Weather.current_location.getLatitude()),
//                                String.valueOf(Common_Weather.current_location.getLongitude()),
        compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(cur_latitude),
                                String.valueOf(cur_longitue),
                                Common_Weather.APP_ID,
                                "metric",
                                "kr")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<WeatherResult>() {
                            @Override
                            public void accept(WeatherResult weatherResult) throws Exception {
                                //Load image - 현재 지역 기후에 따른 아이콘 변경
//                        Picasso.get().load(new StringBuilder("http://openweathermap.org/img/wn/")
//                                .append(weatherResult.getWeather().get(0).getIcon())
//                                .append(".png").toString()).into(이미지 변수);
                                String name=weatherResult.getName();
                                if(name.equals("Dongjinwon")){
                                    name="Yongin-si";
                                }
                                txt_cityview.setText(name);
                                txt_tempview.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());
                                txt_dateview.setText(Common_Weather.convertUnixToDate(weatherResult.getDt()));

                                //날씨 상태
                                //변수.setText(new StringBuilder("Weather in ").append(weatherResult.getName()).toString());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
        );
    }


    // Toolbar 뒤로가기 버튼 활성화 코드
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
//                Log.d("WeatherFragment","BackButton");
////                Navigation.findNavController(getView()).navigate(R.id.action_weatherFragment_to_homeFragment);
////                fragmentManager = getActivity().getSupportFragmentManager();
////                fragmentManager.beginTransaction().remove(WeatherFragment.this).commit();
////                fragmentManager.popBackStack();
//                fragmentManager.popBackStack();
//                return true;
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void init(View view) {
//        tb_weather = view.findViewById(R.id.tb_weather);
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);
        txt_cityview = view.findViewById(R.id.txt_cityview);
        txt_dateview = view.findViewById(R.id.txt_dateview);
        txt_tempview = view.findViewById(R.id.txt_tempview);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideBottomNavigation(false);
    }

    // 하단 탭 제거 함수
    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottomNavigationView);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }
}