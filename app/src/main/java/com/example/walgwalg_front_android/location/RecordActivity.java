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

public class RecordActivity extends AppCompatActivity implements AutoPermissionsListener, SensorEventListener {

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private TextView btn_location, txt_km, txt_time, txt_step, txt_kcal;
    private Button btn_end;
    private SlidingDrawer sliding_drawer,select_drawer;
    private Location location;
    private LocationManager manager;
    GPSListener gpsListener;
    private MapPolyline polyline;
    private List<Polyline> polylines = new ArrayList();
    private LatLng startLatLng = new LatLng(0, 0);
    private LatLng endLatLng = new LatLng(0, 0);
    private double latitude,longitude;

    private static int distance, predistance = 0;
    private Timer timer;
    private TimerTask timerTask;
    private static String walkTime="0";
    private Timer km;
    private TimerTask kmTask;
    private Timer kcal;
    private TimerTask kcalTask;
    private int one = 300;
    private static int kcals = 0;
    private double time = 0.000;
    private static int mSteps = 0;
    private int mCounterSteps = 0;
    private SensorManager sensorManager;
    private Sensor stepCountSensor;

    private Timer gpsapi=new Timer();
    private TimerTask gpsapiTask;

    SupportMapFragment mapFragment;
    GoogleMap map;

    Marker myMarker;
    MarkerOptions myLocationMarker;
    Circle circle;
    CircleOptions circle1KM;

    TestItem dataList;
    List<Data> dataInfo;
    List<Data> dataresult= new ArrayList<>();
    private RecyclerView recyclerView;
    private Park_Adapter park_adapter;

    Post post;
    private String park_name,park_address;
    private PreferenceHelper preferenceHelper;
    private double first_latitude,first_longitude;
    private static String usertoken;
    private Date resultdate;
    private ApiClient apiClient;

    Gps gps;
    private static String walkId="1";

    Finish finish;
    requestDto requestdto;
    MultipartBody.Part file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);


        btn_location = findViewById(R.id.btn_location);
        btn_end = (Button) findViewById(R.id.btn_end);
        txt_km = (TextView) findViewById(R.id.txt_km);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txt_step = (TextView) findViewById(R.id.txt_step);
        txt_kcal = (TextView) findViewById(R.id.txt_kcal);
        sliding_drawer = findViewById(R.id.sliding_drawer);
        sliding_drawer.setVisibility(View.GONE);
        select_drawer = findViewById(R.id.select_drawer);
        select_drawer.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        preferenceHelper = new PreferenceHelper(getApplicationContext());
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

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepCountSensor == null) {
            Toast.makeText(getApplicationContext(), "No Step Sensor", Toast.LENGTH_SHORT).show();
        }

        timer = new Timer();
        startTimer();

        km = new Timer();
        startKm();

        kcal = new Timer();
        startKcal();


        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        polyline = new MapPolyline();
        polyline.setLineColor(Color.RED);
        gpsListener = new GPSListener();

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.i("RecordActivity", "지도 준비됨");
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
                map.setMyLocationEnabled(true);
                AutoPermissions.Companion.loadAllPermissions(RecordActivity.this, 101);
                mapFragment.getView().setVisibility(View.VISIBLE);
                startLocationService();

                Geocoder g = new Geocoder(getApplicationContext());
                List<Address> address=null;
                String region;

                try {
                    Log.d("test",String.valueOf(latitude)+"  "+String.valueOf(longitude));
                    address = g.getFromLocation(latitude,longitude,10);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("test","입출력오류");
                }

                region=address.get(0).getAddressLine(0);
                region=region.substring(5);
                String [] arr=region.split(" ");
                String result;
                result=arr[0];
                result+=" "+arr[1];
                result+=" "+arr[2];
                Log.d("test",result);


                dataInfo=new ArrayList<>();
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

                ApiInterface apiInterface = apiClient.getClient().create(ApiInterface.class);
                Call<TestItem> call = apiInterface.getData(result);

                call.enqueue(new Callback<TestItem>() {

                    @Override
                    public void onResponse(Call<TestItem> call, Response<TestItem> response) {

                        dataList = response.body();

                        Log.d("RecordActivity", dataList.tolist());

                        dataInfo=dataList.list;
                        park_adapter = new Park_Adapter(getApplicationContext(), dataInfo);

                        recyclerView.setAdapter(park_adapter);

                        park_adapter.setOnItemClickListener(new Park_Adapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                                select_drawer.setVisibility(View.GONE);
                                sliding_drawer.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<TestItem> call, Throwable t) {
                        Log.d("Park", t.toString());
                    }
                });

            }
        });



        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationService();
            }
        });
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gpsapiTask != null){
                    gpsapiTask.cancel();
                    gpsapiTask = null;
                }
                timerTask.cancel();
                kmTask.cancel();
                kcalTask.cancel();
                manager.removeUpdates(gpsListener);

                map.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date currentTime_1 = new Date();
                        String dateString = formatter.format(currentTime_1);
                        String filename = dateString + "screenshot.png";

                        File infile = new File(Environment.getExternalStorageDirectory() + "/Pictures", filename);
                        Log.i("finish", Environment.getExternalStorageDirectory() + "/Pictures");
                        try {
                            FileOutputStream os = new FileOutputStream(infile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);

                            RequestBody requestFile=RequestBody.create(MediaType.parse("image/png"),infile);
                            file = MultipartBody.Part.createFormData("file", infile.getName(), requestFile);

                            Log.d("finish", infile.getName());
                            Log.d("finish", String.valueOf(requestFile.contentType()));
                            Log.d("finish", "캡쳐성공");
                            Log.d("finish", String.valueOf(file.body().contentType()));

                            requestdto = new requestDto(walkId,mSteps,distance,kcals,walkTime);
                            Log.d("requestdto",requestdto.getWalkId()+" "+requestdto.getStep_count()+" "+requestdto.getDistance()+" "
                                    +requestdto.getCalorie()+" "+requestdto.getWalkTime());

                            finish=new Finish(file,requestdto);

                            FinishInterface finishInterface = apiClient.getClient().create(FinishInterface.class);
                            Call<FinishResponse> call = finishInterface.CreateWalk(file,requestdto);

                            call.enqueue(new Callback<FinishResponse>() {
                                @Override
                                public void onResponse(Call<FinishResponse> call, Response<FinishResponse> response) {
                                    if(response.isSuccessful()){
                                        FinishResponse finishResponse = response.body();

                                        String content = "success: " + finishResponse.getMessage();
                                        Log.d("finish", content);
                                    }
                                    if (!response.isSuccessful()) {
                                        try {
                                            String body=response.errorBody().string();

                                            Log.d("finish", "response fail : "+body);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<FinishResponse> call, Throwable t) {
                                    Log.d("finish", t.getMessage());
                                }
                            });
                            os.close();
                            Toast.makeText(getApplicationContext(),"end",Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        });

    }

    public void postintent_parkname(String title){
        park_name=title;
    }
    public void postintent_parkaddress(String num){
        park_address=num;
    }

    public void gettoken(String token){
        usertoken=token;
        Log.d("post","로그인에서 받아온 토큰: "+usertoken);
    }

    public void parkpost(String name, String numaddress){

        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ", Locale.KOREA);
        TimeZone tz;
        tz=TimeZone.getTimeZone("GMT");
        mFormat.setTimeZone(tz);
        Date mDate= new Date();
        String date=mFormat.format(mDate);

        post = new Post(date,name,numaddress);

        Log.d("post",post.getWalkDate()+post.getLocation()+post.getAddress());
        PostInterface postInterface = apiClient.getClient().create(PostInterface.class);
        Call<PostResponse> call = postInterface.CreatePost(post);

        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.isSuccessful()){
                    PostResponse postResponse = response.body();

                    String content = "success: " + postResponse.getMessage();
                    walkId=postResponse.getList().getWalkId();
                    Log.d("gps walkId", walkId);
                    Log.d("post", content);
                    gpsapi();
                }
                if (!response.isSuccessful()) {
                    try {
                        String body=response.errorBody().string();

                        Log.d("post", "response fail : "+body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d("post", t.getMessage());
            }
        });
    }
    private GpsInterface gpsInterface = apiClient.getClient().create(GpsInterface.class);

    private void gpsapi() {
        gpsapiTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gps = new Gps(walkId,String.valueOf(latitude),String.valueOf(longitude));
                        Log.d("gps",gps.getWalkId()+" latitude: "+gps.getLatitude()+"longitude: "+gps.getLongitude());
                        Call<GpsResponse> call = gpsInterface.CreateGps(gps);

                        call.enqueue(new Callback<GpsResponse>() {
                            @Override
                            public void onResponse(Call<GpsResponse> call, Response<GpsResponse> response) {
                                if(response.isSuccessful()){
                                    GpsResponse gpsResponse = response.body();

                                    String content = "success: " + gpsResponse.getMessage();

                                    Log.d("gps", content);
                                }
                                if (!response.isSuccessful()) {
                                    try {
                                        String body=response.errorBody().string();

                                        Log.d("gps", "response fail : "+body);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<GpsResponse> call, Throwable t) {
                                Log.d("gps", t.getMessage());
                            }
                        });
                    }
                });
            }

        };
        gpsapi.scheduleAtFixedRate(gpsapiTask, 0, 200000);
    }

    //km
    private void startKm() {
        kmTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (predistance != distance) {
                            txt_km.setText(String.valueOf(distance));
                        }
                        predistance = distance;
                    }
                });
            }

        };
        km.scheduleAtFixedRate(kmTask, 0, 10000);
    }

    //kcal
    private void startKcal() {
        kcalTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mSteps >= one) {
                            kcals = kcals + 1;
                            txt_kcal.setText(String.valueOf(kcals));
                            one = one * 2;
                        }
                    }
                });
            }

        };
        kcal.scheduleAtFixedRate(kcalTask, 0, 100000);
    }

    //timer
    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        txt_time.setText(getTimerText());
                        walkTime=getTimerText();
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private String getTimerText() {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours) {
        return String.format("%02d", hours) + " : " + String.format("%02d", minutes);
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

                    Log.i("RecordActivity", message);
                }

                //위치 요청하기
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
                //manager.removeUpdates(gpsListener);
//                Toast.makeText(getApplicationContext(), "내 위치1확인 요청함", Toast.LENGTH_SHORT).show();
                Log.i("RecordActivity", "requestLocationUpdates() 내 위치1에서 호출시작 ~~ ");

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


                //위치 요청하기
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, gpsListener);
                //manager.removeUpdates(gpsListener);
//                Toast.makeText(getApplicationContext(), "내 위치2확인 요청함", Toast.LENGTH_SHORT).show();
                Log.i("RecordActivity", "requestLocationUpdates() 내 위치2에서 호출시작 ~~ ");
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            //stepcountsenersor는 앱이 꺼지더라도 초기화 되지않는다. 그러므로 우리는 초기값을 가지고 있어야한다.
            if (mCounterSteps < 1) {
                // initial value
                mCounterSteps = (int) event.values[0];
            }
            //리셋 안된 값 + 현재값 - 리셋 안된 값
            mSteps = (int) event.values[0] - mCounterSteps;
            Log.d("::::::::::::::::", "step " + mSteps);
            txt_step.setText(Integer.toString(mSteps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    class GPSListener implements LocationListener {

        // 위치 확인되었을때 자동으로 호출됨 (일정시간 and 일정거리)
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            if (myMarker != null) myMarker.remove();
            location = location;
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(latitude, longitude));
            myMarker = map.addMarker(markerOptions);

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
            endLatLng = new LatLng(latitude, longitude);//현재  위치를  끝점으로  설정
            drawPath();//polyline  그리기
            startLatLng = new LatLng(latitude, longitude);//시작점을  끝점으로  다시  설정
            Log.d("RecordActivity", "위도 " + latitude + " 경도 " + longitude);
            distance = distance + 1;
            Log.d("RecordActivity", "km " + distance);
            showCurrentLocation(latitude, longitude);

        }

        private void drawPath() {        //polyline을 그려주는 메소드
            PolylineOptions options = new PolylineOptions().add(startLatLng).add(endLatLng).width(15).color(Color.RED).geodesic(true);
            polylines.add(map.addPolyline(options));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 18));
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    ;

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

            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
                //manager.removeUpdates(gpsListener);
            } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gpsListener);
                //manager.removeUpdates(gpsListener);
            }

            if (map != null) {
                map.setMyLocationEnabled(true);
            }
            Log.i("MyLocTest", "onResume에서 requestLocationUpdates() 되었습니다.");
            sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.removeUpdates(gpsListener);

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
        sensorManager.unregisterListener(this);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
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
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(RecordActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED ) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(RecordActivity.this, REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(getApplicationContext(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(RecordActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(RecordActivity.this, REQUIRED_PERMISSIONS,
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
        AutoPermissions.Companion.parsePermissions(RecordActivity.this, permsRequestCode, permissions, this);

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
                if (ActivityCompat.shouldShowRequestPermissionRationale(RecordActivity.this, REQUIRED_PERMISSIONS[0])) {
                    Toast.makeText(getApplicationContext(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}


