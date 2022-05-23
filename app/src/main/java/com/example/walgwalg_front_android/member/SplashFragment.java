package com.example.walgwalg_front_android.member;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.walgwalg_front_android.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SplashFragment extends Fragment {

    private Button btn_login, btn_register;

    public SplashFragment() {
        // Required empty public constructor
        // 빈 상태의 구조체가 필수적으로 필요합니다.
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        // 하단 탭 제거 - true : 숨기기, false : 보이기
        hideBottomNavigation(true);

        // 레이아웃 아이템 연결 및 초기화 함수
        init(view);

        // 로그인 버튼 눌렀을때
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // splash에서 login으로 넘어가는 코드
                Navigation.findNavController(getView()).navigate(R.id.action_splashFragment_to_loginFragment);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // splash에서 register으로 넘어가는 코드
                Navigation.findNavController(getView()).navigate(R.id.action_splashFragment_to_registerFragment);
            }
        });

        return view;
    }

    // 레이아웃 아이템 연결 및 초기화 함수
    private void init(View view){
        btn_login = view.findViewById(R.id.btn_login);
        btn_register = view.findViewById(R.id.btn_register);
    }

    // SplashFragment 화면이 사라지면 작동할 코드
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