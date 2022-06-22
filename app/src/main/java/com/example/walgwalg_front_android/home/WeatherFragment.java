package com.example.walgwalg_front_android.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.walgwalg_front_android.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class WeatherFragment extends Fragment {

//    private MaterialToolbar tb_weather;
    FragmentManager fragmentManager;
//    public FragmentManager fragmentManager = HomeFragment.fragmentManager.findFragmentById(R.id.homeFragment);
    TodayWeatherFragment todayWeatherFragment;
    WeekWeatherFragment weekWeatherFragment;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabPagerAdapter tabPagerAdapter;


    public WeatherFragment() {
        // Required empty public constructor
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


        // Toolbar 활성화

//        fragmentManager = getActivity().getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.weatherFragment, new HomeFragment()).addToBackStack(null).commit();

//        ((AppCompatActivity)getActivity()).setSupportActionBar(tb_weather);
//        Objects.requireNonNull(((AppCompatActivity)getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(null); // Toolbar 제목 제거

        return view;
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