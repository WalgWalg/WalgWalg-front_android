package com.example.walgwalg_front_android.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.member.PreferenceHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import net.daum.mf.map.api.MapPolyline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationInfoActivity extends AppCompatActivity implements AutoPermissionsListener {

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private TextView btn_location, txt_km, txt_time, txt_step, txt_kcal,txt_start,txt_end,txt_starttime,txt_endtime;
    private String distance, walkTime, stepCount, calorie,park,walkid;
    private Location location;
    private LocationManager manager;
//    GPSListener gpsListener;
    private MapPolyline polyline;
    private List<Polyline> polylines = new ArrayList();
    private LatLng startLatLng = new LatLng(0, 0);
    private LatLng endLatLng = new LatLng(0, 0);
    private static double latitude,longitude;
    private int gpssize;
    private double startgps,endgps;

    SupportMapFragment mapFragment;
    GoogleMap map;

    Marker myMarker;
    MarkerOptions myLocationMarker;
    Circle circle;
    CircleOptions circle1KM;

    List<Lo_InfoGps> gpsInfo;
    Lo_InfoItem dataList;
    Lo_InfoData dataList2;
    private ApiClient apiClient;
    private static String usertoken;
    private String region;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);


        btn_location = findViewById(R.id.btn_location);
        txt_km = (TextView) findViewById(R.id.txt_km);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txt_step = (TextView) findViewById(R.id.txt_step);
        txt_kcal = (TextView) findViewById(R.id.txt_kcal);
        txt_start = (TextView) findViewById(R.id.txt_start);
        txt_end = (TextView) findViewById(R.id.txt_end);
        txt_starttime = (TextView) findViewById(R.id.txt_starttime);
        txt_endtime = (TextView) findViewById(R.id.txt_endtime);
        apiClient=new ApiClient(usertoken);

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }
        // 활동 퍼미션 체크
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        polyline = new MapPolyline();
        polyline.setLineColor(Color.RED);
//        gpsListener = new GPSListener();

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.i("LocationInfoActivity", "지도 준비됨");
                map = googleMap;
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

//                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
                map.setMyLocationEnabled(true);
                AutoPermissions.Companion.loadAllPermissions(LocationInfoActivity.this, 101);
                mapFragment.getView().setVisibility(View.VISIBLE);
                startLocationService();

            }
        });

        intent=getIntent();
        walkid=intent.getStringExtra("id");

        gpsInfo = new ArrayList<>();

        Lo_InfoInterface lo_infoInterface = apiClient.getClient().create(Lo_InfoInterface.class);
        Call<Lo_InfoItem> call = lo_infoInterface.getInfoData(walkid);

        call.enqueue(new Callback<Lo_InfoItem>() {

            @Override
            public void onResponse(Call<Lo_InfoItem> call, Response<Lo_InfoItem> response) {

                dataList = response.body();
                Log.d("LocationInfoActivity", dataList.getMessage());

                dataList2=dataList.getList();
                gpsInfo=dataList2.getList();
                gpssize=gpsInfo.size();
                Log.d("LocationInfoActivity", String.valueOf(gpssize));

                if(gpssize>=2) {
                    startLatLng = new LatLng(Float.parseFloat(gpsInfo.get(0).getLatitude()), Float.parseFloat(gpsInfo.get(0).getLongitude()));
                    endLatLng = new LatLng(Float.parseFloat(gpsInfo.get(1).getLatitude()), Float.parseFloat(gpsInfo.get(1).getLongitude()));
                    drawPath();
                    if(gpssize>=3) {
                        for (int i = 2; i < gpssize; i++) {
                            startLatLng = new LatLng(Float.parseFloat(gpsInfo.get(i-1).getLatitude()), Float.parseFloat(gpsInfo.get(i-1).getLongitude()));
                            endLatLng = new LatLng(Float.parseFloat(gpsInfo.get(i).getLatitude()), Float.parseFloat(gpsInfo.get(i).getLongitude()));
                            drawPath();
                            Log.d("LocationInfoActivity", startLatLng+","+endLatLng);
                        }
                    }
                }

                stepCount=String.valueOf(dataList2.getStepCount());
                calorie=String.valueOf(dataList2.getCalorie());
                distance=String.valueOf(dataList2.getDistance());
                walkTime=dataList2.getWalkTime();

                txt_step.setText(stepCount);
                txt_kcal.setText(calorie);
                txt_km.setText(distance);
                txt_time.setText(walkTime);

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
                String getTime = dateFormat.format(date);
                String[] time = getTime.split(":");
                int hour=Integer.parseInt(time[0]);
                int minute=Integer.parseInt(time[1]);
                Log.d("ttt",String.valueOf(hour)+":"+String.valueOf(minute));

                String[] plus_time = walkTime.split(" : ");
                int plus_hour=Integer.parseInt(plus_time[0]);
                int plus_minute=Integer.parseInt(plus_time[1]);
                Log.d("ttt",String.valueOf(plus_hour)+":"+String.valueOf(plus_minute));

                hour+=plus_hour;
                minute+=plus_minute;
                String result_time= String.valueOf(hour)+":"+String.valueOf(minute);
                if(hour<10)
                    result_time="0"+result_time;

                txt_starttime.setText(getTime);
                txt_endtime.setText(result_time);

                Geocoder g = new Geocoder(getApplicationContext(),Locale.getDefault());
                List<Address> start_address = null;
                List<Address> end_address = null;

                try {
                    start_address = g.getFromLocation(Float.parseFloat(gpsInfo.get(0).getLatitude()), Float.parseFloat(gpsInfo.get(0).getLongitude()), 10);
                    end_address = g.getFromLocation(Float.parseFloat(gpsInfo.get(gpssize-1).getLatitude()), Float.parseFloat(gpsInfo.get(gpssize-1).getLongitude()), 10);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("LocationInfoActivity", "입출력오류");
                }

                String start_region = start_address.get(0).getAddressLine(0);
                String end_region = end_address.get(0).getAddressLine(0);

                Log.d("LocationInfoActivity", String.valueOf(start_address.get(0)));


                start_region = start_region.substring(5);
                String[] arr = start_region.split(" ");
                String result;
                result = arr[3];
                result += arr[4];
                Log.d("LocationInfoActivity", result);
                txt_start.setText(result);

                end_region = end_region.substring(5);
                String[] arr2 = end_region.split(" ");
                String result2;
                result2 = arr2[3];
                result2 += arr[4];
                Log.d("LocationInfoActivity", result2);
                txt_end.setText(result2);

            }

            @Override
            public void onFailure(Call<Lo_InfoItem> call, Throwable t) {
                Log.d("LocationInfoActivity", t.toString());
            }
        });


        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationService();
            }
        });

    }

    public void drawPath() {        //polyline을 그려주는 메소드
        PolylineOptions options = new PolylineOptions().add(startLatLng).add(endLatLng).width(15).color(Color.RED).geodesic(true);
        polylines.add(map.addPolyline(options));
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 18));
    }

    public void gettoken(String token){
        usertoken=token;
        Log.d("locationtoken","로그인에서 받아온 토큰: "+usertoken);
    }

    public void startLocationService() {
        try {
            location = null;

            long minTime = 0;        // 0초마다 갱신 - 바로바로갱신
            float minDistance = 0;

            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    String message = "최근 위치1 -> Latitude : " + latitude + " Longitude : " + longitude;

                    showCurrentLocation(latitude, longitude);

                    Log.i("LocationInfoActivity", message);
                }

//                //위치 요청하기
//                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
//                //manager.removeUpdates(gpsListener);
////                Toast.makeText(getApplicationContext(), "내 위치1확인 요청함", Toast.LENGTH_SHORT).show();
//                Log.i("LocationInfoActivity", "requestLocationUpdates() 내 위치1에서 호출시작 ~~ ");

            } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    String message = "최근 위치2 -> Latitude : " + latitude + "\n Longitude : " + longitude;

                    Log.i("RecordActivity", "최근 위치1 " + message);
                    showCurrentLocation(latitude, longitude);

                    Log.i("RecordActivity", "최근 위치2 호출");
                }


//                //위치 요청하기
//                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, gpsListener);
//                //manager.removeUpdates(gpsListener);
////                Toast.makeText(getApplicationContext(), "내 위치2확인 요청함", Toast.LENGTH_SHORT).show();
//                Log.i("RecordActivity", "requestLocationUpdates() 내 위치2에서 호출시작 ~~ ");
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

//    class GPSListener implements LocationListener {
//
//        // 위치 확인되었을때 자동으로 호출됨 (일정시간 and 일정거리)
//        @Override
//        public void onLocationChanged(Location location) {
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
//
//            if (myMarker != null) myMarker.remove();
//            location = location;
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(new LatLng(latitude, longitude));
//            myMarker = map.addMarker(markerOptions);
//
//            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
//            showCurrentLocation(latitude, longitude);
//
//        }
//
//        private void drawPath() {        //polyline을 그려주는 메소드
//            PolylineOptions options = new PolylineOptions().add(startLatLng).add(endLatLng).width(15).color(Color.RED).geodesic(true);
//            polylines.add(map.addPolyline(options));
////            map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 18));
//        }
//
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();

        // GPS provider를 이용전에 퍼미션 체크
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            Toast.makeText(getApplicationContext(), "접근 권한이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {

//            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
//                //manager.removeUpdates(gpsListener);
//            } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gpsListener);
//                //manager.removeUpdates(gpsListener);
//            }

            if (map != null) {
                map.setMyLocationEnabled(true);
            }
            Log.i("MyLocTest", "onResume에서 requestLocationUpdates() 되었습니다.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        manager.removeUpdates(gpsListener);

        if (map != null) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(false);
        }
        Log.i("RecordActivity", "onPause에서 removeUpdates() 되었습니다.");
    }

    private void showCurrentLocation(double latitude, double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 17));
        showMyLocationMarker(curPoint);
    }

    private void showMyLocationMarker(LatLng curPoint) {
        if (myLocationMarker == null) {
            myLocationMarker = new MarkerOptions(); // 마커 객체 생성
            myLocationMarker.position(curPoint);
            myLocationMarker.title("최근위치 \n");
            myLocationMarker.snippet("*GPS로 확인한 최근위치");
//            myLocationMarker.icon(BitmapDescriptorFactory.fromResource((R.drawable.logo)));
            myMarker = map.addMarker(myLocationMarker);
        } else {
            myMarker.remove(); // 마커삭제
            myLocationMarker.position(curPoint);
            myMarker = map.addMarker(myLocationMarker);
        }

        // 반경추가
        if (circle1KM == null) {
            circle1KM = new CircleOptions().center(curPoint) // 원점
                    .radius(1000)       // 반지름 단위 : m
                    .strokeWidth(1.0f);    // 선너비 0f : 선없음
            //.fillColor(Color.parseColor("#1AFFFFFF")); // 배경색
            circle = map.addCircle(circle1KM);

        } else {
            circle.remove(); // 반경삭제
            circle1KM.center(curPoint);
            circle = map.addCircle(circle1KM);
        }


    }

    @Override
    public void onDenied(int requestCode, String[] permissions) {
//        Toast.makeText(getApplicationContext(),"permissions denied : " + permissions.length, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGranted(int requestCode, String[] permissions) {
//        Toast.makeText(getApplicationContext(),"permissions granted : " + permissions.length, Toast.LENGTH_SHORT).show();
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LocationInfoActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
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
    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(LocationInfoActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED ) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(LocationInfoActivity.this, REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(getApplicationContext(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(LocationInfoActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(LocationInfoActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    private void onFinishReverseGeoCoding(String result) {
//        Toast.makeText(LocationDemoActivity.this, "Reverse Geo-coding : " + result, Toast.LENGTH_SHORT).show();
    }

    // ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        AutoPermissions.Companion.parsePermissions(LocationInfoActivity.this, permsRequestCode, permissions, this);

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
                Log.d("@@@", "start");
                //위치 값을 가져올 수 있음

            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있다
                if (ActivityCompat.shouldShowRequestPermissionRationale(LocationInfoActivity.this, REQUIRED_PERMISSIONS[0])) {
                    Toast.makeText(getApplicationContext(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}


