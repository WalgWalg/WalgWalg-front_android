package com.example.walgwalg_front_android.member;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.walgwalg_front_android.MainActivity;
import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.home.HomeFragment;
import com.example.walgwalg_front_android.location.LocationFragment;
import com.example.walgwalg_front_android.location.RecordActivity;
import com.example.walgwalg_front_android.member.DTO.LoginRequest;
import com.example.walgwalg_front_android.member.DTO.LoginResponse;
import com.example.walgwalg_front_android.member.Interface.LoginInterface;
import com.example.walgwalg_front_android.member.Retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.checkbox.MaterialCheckBox;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private String TAG = "LoginFragmentTAG";

    private Button btn_login;
    private MaterialCheckBox cb_autoLogin;
    private EditText edt_idInput, edt_pwInput;
    private RetrofitClient retrofitClient;
    private LoginInterface loginInterface;
    private PreferenceHelper preferenceHelper;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        hideBottomNavigation(true);
        init(view);


//        if (!getPreferenceString("autoLoginId").equals("") && !getPreferenceString("autoLoginPw").equals("")) {
//            cb_autoLogin.setChecked(true);
//            checkAutoLogin(getPreferenceString("autoLoginId"));
//        }

        btn_login.setOnClickListener(task -> {

            String id = edt_idInput.getText().toString();
            String pw = edt_pwInput.getText().toString();

            //로그인 정보 미입력 시
            if (id.trim().length() == 0 || pw.trim().length() == 0 || id == null || pw == null) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("알림")
                        .setMessage("로그인 정보를 입력바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            } else {
                /**
                 * 로그인 통신 건너뛰고 싶으면
                 * LoginResponse(id, pw) 주석 처리후
                 * Navigation.find~~ 주석 풀기
                 */

                //로그인 통신
                LoginResponse(id, pw);
                //화면 넘어가기
                //Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_homeFragment);
            }
        });

        return view;
    }

    public void LoginResponse(String userID, String userPassword) {

        //loginRequest에 사용자가 입력한 id와 pw를 저장
        LoginRequest loginRequest = new LoginRequest(userID, userPassword);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        loginInterface = RetrofitClient.getLoginRetrofitInterface();

        //loginRequest에 저장된 데이터와 함께 init에서 정의한 getLoginResponse 함수를 실행한 후 응답을 받음
        loginInterface.getLoginResponse(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                Log.d("retrofit", "Data fetch success");

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    LoginResponse result = response.body();

                    // 받은 코드 저장
                    String status = result.getStatus();

                    // 토큰 발급 시간
                    String dateTime = result.getDateTime();

                    //받은 토큰 저장
                    String accessToken = result.getTokenPOJO().accessToken;
                    String refreshToken = result.getTokenPOJO().refreshToken;

                    Log.d(TAG, "access: " + accessToken + "\nrefresh: " + refreshToken);

                    String success = "200"; //로그인 성공
                    String errorId = "404"; //아이디 일치x
                    String errorPw = "404"; //비밀번호 일치x


                    if (status.equals(success)) {
                        String userID = edt_idInput.getText().toString();
                        String userPassword = edt_pwInput.getText().toString();

                        // 사용자 정보와 서버로부터 받은 정보 저장
                        setPreference(userID, userPassword, dateTime, accessToken, refreshToken);

                        //자동 로그인 여부
//                        if (cb_autoLogin.isChecked()) {
//                            setPreference("autoLoginId", userID);
//                            setPreference("autoLoginPw", userPassword);
//                        } else {
//                            setPreference("autoLoginId", "");
//                            setPreference("autoLoginPw", "");
//                        }

                        Toast.makeText(getContext(), userID + "님 환영합니다.", Toast.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "토큰 값 : " + getPreferenceString(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, getPreferenceString());
                        RecordActivity recordActivity=new RecordActivity();
                        recordActivity.gettoken(getPreferenceString());
                        LocationFragment locationFragment=new LocationFragment();
                        locationFragment.gettoken(getPreferenceString());
                        HomeFragment homeFragment = new HomeFragment();
                        homeFragment.gettoken(getPreferenceString());
                        Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_homeFragment);

                    } else if (status.equals(errorId)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("알림")
                                .setMessage("아이디가 존재하지 않습니다.\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } else if (status.equals(errorPw)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("알림")
                                .setMessage("비밀번호가 일치하지 않습니다.\n 고객" +
                                        "센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();

                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    //데이터를 내부 저장소에 저장하기
    public void setPreference(String userid, String password, String dateTime, String accessToken, String refreshToken) {
        preferenceHelper.saveUserid(userid);
        preferenceHelper.savePassword(password);
        preferenceHelper.saveDateTime(dateTime);
        preferenceHelper.saveAccessToken(accessToken);
        preferenceHelper.saveRefreshToken(refreshToken);
    }

    //내부 저장소에 저장된 데이터 가져오기
    public String getPreferenceString() {
        return preferenceHelper.getAccessToken();
    }


    //키보드 숨기기
//    private void hideKeyboard()
//    {
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(edt_idInput.getWindowToken(), 0);
//        imm.hideSoftInputFromWindow(edt_pwInput.getWindowToken(), 0);
//    }

    //화면 터치 시 키보드 내려감
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        View focusView = getCurrentFocus();
//        if (focusView != null) {
//            Rect rect = new Rect();
//            focusView.getGlobalVisibleRect(rect);
//            int x = (int) ev.getX(), y = (int) ev.getY();
//            if (!rect.contains(x, y)) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                if (imm != null)
//                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
//                focusView.clearFocus();
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    //자동 로그인 유저
//    public void checkAutoLogin(String id){
//
//        Toast.makeText(getContext(), id + "님 환영합니다.", Toast.LENGTH_LONG).show();
//          Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_homeFragment);
//
//    }

    //뒤로 가기 버튼 2번 클릭시 종료
//    @Override public void onBackPressed() {
//        //super.onBackPressed();
//        backPressCloseHandler.onBackPressed();
//    }

    public void init(View view) {

        preferenceHelper = new PreferenceHelper(view.getContext());

        cb_autoLogin = view.findViewById(R.id.cb_autoLogin);
        btn_login = view.findViewById(R.id.btn_login);
        edt_idInput = view.findViewById(R.id.edt_idInput);
        edt_pwInput = view.findViewById(R.id.edt_pwInput);
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