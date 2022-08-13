package com.example.walgwalg_front_android.member;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.walgwalg_front_android.GpsTracker;
import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.ViewModel.MyInfoViewModel;
import com.example.walgwalg_front_android.ViewModel.MyLikeViewModel;
import com.example.walgwalg_front_android.ViewModel.MyLocationViewModel;
import com.example.walgwalg_front_android.community.MyLocationModel;
import com.example.walgwalg_front_android.home.HomeFragment;
import com.example.walgwalg_front_android.location.LocationFragment;
import com.example.walgwalg_front_android.location.LocationInfoActivity;
import com.example.walgwalg_front_android.location.RecordActivity;
import com.example.walgwalg_front_android.member.DTO.LoginRequest;
import com.example.walgwalg_front_android.member.DTO.LoginResponse;
import com.example.walgwalg_front_android.member.DTO.MyInfoRequest;
import com.example.walgwalg_front_android.member.DTO.MyInfoResponse;
import com.example.walgwalg_front_android.member.DTO.MyLikeRequest;
import com.example.walgwalg_front_android.member.DTO.MyLikeResponse;
import com.example.walgwalg_front_android.member.Interface.LoginInterface;
import com.example.walgwalg_front_android.member.Interface.MyInfoInterface;
import com.example.walgwalg_front_android.member.Interface.MyLikeInterface;
import com.example.walgwalg_front_android.member.Retrofit.RetrofitClient;
import com.example.walgwalg_front_android.member.Retrofit.ServiceGenerator;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private String TAG = "LoginFragmentTAG";

    private RetrofitClient retrofitClient;
    private LoginInterface loginInterface;
    private MyInfoInterface myInfoInterface;
    private MyInfoRequest myInfoRequest;
    private MyInfoResponse myInfoResponse;
    private MyLikeInterface myLikeInterface;
    private MyLikeRequest myLikeRequest;
    private MyLikeResponse myLikeResponse;
    private PreferenceHelper preferenceHelper;

    private MyInfoViewModel myInfoViewModel;
    private MyLikeViewModel myLikeViewModel;
    private MyLocationViewModel myLocationViewModel;

    private Button btn_login;
    private MaterialCheckBox cb_autoLogin;
    private EditText edt_idInput, edt_pwInput;

    private String status;
    private String dateTime;
    private String accessToken;
    private String refreshToken;

    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

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

        myLocationViewModel = new ViewModelProvider(requireActivity()).get(MyLocationViewModel.class);

//        // 자동 로그인 코드
//        if (!getPreferenceString("autoLoginId").equals("") && !getPreferenceString("autoLoginPw").equals("")) {
//            cb_autoLogin.setChecked(true);
//            checkAutoLogin(getPreferenceString("autoLoginId"));
//        }
        Log.d(TAG, "자동 로그인 전");
        if (preferenceHelper.getAutoLogin() != null && preferenceHelper.getAutoLogin() && !preferenceHelper.getUserid().equals("") && !preferenceHelper.getPassword().equals("")) {
            Log.d(TAG, "자동 로그인 코드");
            cb_autoLogin.setChecked(true);
            LoginResponse(preferenceHelper.getUserid(), preferenceHelper.getPassword());
        }

        // 버튼 로그인 코드
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
                //로그인 통신
                LoginResponse(id, pw);
                // setPreference(cb_autoLogin.isChecked(), id, pw, dateTime, accessToken, refreshToken);
                // Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_homeFragment);
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
                    status = result.getStatus();
                    // 토큰 발급 시간
                    dateTime = result.getDateTime();
                    //받은 토큰 저장
                    accessToken = result.getTokenPOJO().accessToken;
                    refreshToken = result.getTokenPOJO().refreshToken;
                    Log.d(TAG, "access: " + accessToken + "\nrefresh: " + refreshToken);

                    String success = "200"; //로그인 성공

                    if (status.equals(success)) {
                        setPreference(cb_autoLogin.isChecked(), userID, userPassword, dateTime, accessToken, refreshToken);
                        //자동 로그인 여부
//                        if (cb_autoLogin.isChecked()) {
//                            setPreference("autoLoginId", userID);
//                            setPreference("autoLoginPw", userPassword);
//                        } else {
//                            setPreference("autoLoginId", "");
//                            setPreference("autoLoginPw", "");
//                        }

                        Toast.makeText(getContext(), userID + "님 환영합니다.", Toast.LENGTH_LONG).show();
//                        Toast.makeText(getContext(), "토큰 값 : " + getPreferenceString(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, getPreferenceString());
                        RecordActivity recordActivity = new RecordActivity();
                        recordActivity.gettoken(getPreferenceString());
                        LocationFragment locationFragment = new LocationFragment();
                        locationFragment.gettoken(getPreferenceString());
                        LocationInfoActivity locationInfoActivity = new LocationInfoActivity();
                        locationInfoActivity.gettoken(getPreferenceString());
                        HomeFragment homeFragment = new HomeFragment();
                        homeFragment.gettoken(getPreferenceString());

                        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
//마지막 위치 받아오기
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
//                        Location loc_Current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        gpsTracker = new GpsTracker(getContext());

                        double latitude = gpsTracker.getLatitude();
                        double longitude = gpsTracker.getLongitude();

                        String address = getCurrentAddress(latitude, longitude);

                        String[] split_address = address.split("\\s");

                        MyLocationModel myLocationModel = new MyLocationModel();
                        myLocationModel.setLatitude(latitude);
                        myLocationModel.setLongitude(longitude);
                        myLocationModel.setSi_Do(split_address[1]);
                        myLocationModel.setSi_Gun_Gu(split_address[2]);
                        myLocationModel.setEup_Myeon_Dong(split_address[3]);

                        Log.d(TAG, "위도 : " + latitude + " 경도 : " + longitude +
                                " 시도 : " + split_address[1] + " 시군구 : " + split_address[2] + " 읍면동 : " + split_address[3]);

                        myLocationViewModel.saveMyLocation(myLocationModel);


                        Toast.makeText(getContext(), "현재위치 : " + address, Toast.LENGTH_LONG).show();

                        MyInfoResponse();
                    } else {
                        showError(status);
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

    public void MyInfoResponse() {

        preferenceHelper = new PreferenceHelper(getContext());
        myInfoInterface = ServiceGenerator.createService(MyInfoInterface.class, preferenceHelper.getAccessToken());
        myInfoRequest = new MyInfoRequest();
        myInfoInterface.GET_MY_INFO().enqueue(new Callback<MyInfoResponse>() {
            @Override
            public void onResponse(Call<MyInfoResponse> call, Response<MyInfoResponse> response) {
                if (response.isSuccessful()) {
                    MyInfoResponse result = response.body();
                    myInfoViewModel = new ViewModelProvider(requireActivity()).get(MyInfoViewModel.class);
                    myInfoViewModel.saveMyInfo(result.myInfoPojo);
                    MyLikeResponse();
                }
            }

            @Override
            public void onFailure(Call<MyInfoResponse> call, Throwable t) {

            }
        });

    }

    public void MyLikeResponse() {

        preferenceHelper = new PreferenceHelper(getContext());
        myLikeInterface = ServiceGenerator.createService(MyLikeInterface.class, preferenceHelper.getAccessToken());
        myLikeRequest = new MyLikeRequest();
        myLikeInterface.GetMyLike().enqueue(new Callback<MyLikeResponse>() {
            @Override
            public void onResponse(Call<MyLikeResponse> call, Response<MyLikeResponse> response) {
                if (response.isSuccessful()) {
                    if (response.isSuccessful()) {
                        MyLikeResponse result = response.body();
                        myLikeViewModel = new ViewModelProvider(requireActivity()).get(MyLikeViewModel.class);
                        myLikeViewModel.SaveMyLikeData(result.myLikePojo);
//                        Log.d(TAG, "MyLikeData : " + result.myLikePojo.get(0).title);
                        Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_homeFragment);
                    } else {
                        Log.d(TAG, "Fail : " + response.errorBody());
                    }
                }
            }

            @Override
            public void onFailure(Call<MyLikeResponse> call, Throwable t) {
                Log.d(TAG, "Failure : " + t.getMessage());
            }
        });

    }

    //데이터를 내부 저장소에 저장하기
    public void setPreference(Boolean AutoLogin, String userid, String password, String dateTime, String accessToken, String refreshToken) {
        preferenceHelper.saveAutoLogin(AutoLogin);
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

    public void showError(String eCode) {
        setPreference(null, "", "", "", "", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("알림");
        switch (eCode) {
            case "404":
                builder.setMessage("아이디와 비밀번호를 확인하시기 바랍니다.");
            default:
                builder.setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.");
        }
        builder.setPositiveButton("확인", null)
                .create()
                .show();
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                //위치 값을 가져올 수 있음
                ;
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(getContext(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
//                    finish();


                } else {

                    Toast.makeText(getContext(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(getContext(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    public String getCurrentAddress(double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(getContext(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(getContext(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(getContext(), "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString() + "\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}

