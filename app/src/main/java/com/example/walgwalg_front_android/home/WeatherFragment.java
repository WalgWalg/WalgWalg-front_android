package com.example.walgwalg_front_android.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.walgwalg_front_android.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class WeatherFragment extends Fragment {

    private MaterialToolbar tb_weather;
    private static FragmentManager fragmentManager;


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

        // Toolbar 활성화

//        fragmentManager = getActivity().getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.weatherFragment, new HomeFragment()).addToBackStack(null).commit();

        ((AppCompatActivity)getActivity()).setSupportActionBar(tb_weather);
        Objects.requireNonNull(((AppCompatActivity)getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(null); // Toolbar 제목 제거

        return view;
    }

    // Toolbar 뒤로가기 버튼 활성화 코드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                Log.d("WeatherFragment","BackButton");
//                getActivity().finish();

                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(WeatherFragment.this).commit();
                fragmentManager.popBackStack();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void init(View view) {
        tb_weather = view.findViewById(R.id.tb_weather);
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