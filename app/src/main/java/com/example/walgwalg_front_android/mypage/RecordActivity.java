package com.example.walgwalg_front_android.mypage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walgwalg_front_android.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RecordActivity extends AppCompatActivity implements AutoPermissionsListener, SensorEventListener {
    private TextView btn_location,txt_km,txt_time,txt_step,txt_kcal;
    private Button btn_end;
    private Location location;
    private LocationManager manager;
    GPSListener gpsListener;
    private MapPolyline polyline;
    private List<Polyline> polylines=new ArrayList();
    private  LatLng  startLatLng  =  new  LatLng(0,  0);                //polyline  시작점  
    private  LatLng  endLatLng  =  new  LatLng(0,  0);                //polyline  끝점

    private int distance,predistance=0;
    private Timer timer;
    private TimerTask timerTask;
    private Timer km;
    private TimerTask kmTask;
    private Timer kcal;
    private TimerTask kcalTask;
    private int one=300;
    private int kcals=0;
    private double time=0.000;
    //현재 걸음 수
    private int mSteps = 0;
    //리스너가 등록되고 난 후의 step count
    private int mCounterSteps = 0;
    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    private int userloca=0;


    SupportMapFragment mapFragment;
    GoogleMap map;

    Marker myMarker;
    MarkerOptions myLocationMarker;
    Circle circle;
    CircleOptions circle1KM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        btn_location = findViewById(R.id.btn_location);
        btn_end=(Button)findViewById(R.id.btn_end);
        txt_km=(TextView) findViewById(R.id.txt_km);
        txt_time=(TextView) findViewById(R.id.txt_time);
        txt_step=(TextView) findViewById(R.id.txt_step);
        txt_kcal=(TextView) findViewById(R.id.txt_kcal);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        // 디바이스에 걸음 센서의 존재 여부 체크
        if (stepCountSensor == null) {
            Toast.makeText(getApplicationContext(), "No Step Sensor", Toast.LENGTH_SHORT).show();
        }

        timer = new Timer();
        startTimer();

        km=new Timer();
        startKm();

        kcal=new Timer();
        startKcal();


        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //폴리라인 그리기
        polyline = new MapPolyline();
        polyline.setLineColor(Color.RED); // Polyline 컬러 지정.
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
            }
        });

        // 최초 지도 숨김
//        mapFragment.getView().setVisibility(View.GONE);


        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationService();
            }
        });
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerTask.cancel();
                kmTask.cancel();
                kcalTask.cancel();
                manager.removeUpdates(gpsListener);
                map.snapshot(new GoogleMap.SnapshotReadyCallback()
                {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap)
                    {
                        SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMddHHmmss");
                        Date currentTime_1=new Date();
                        String dateString=formatter.format(currentTime_1);
                        String filename = dateString+"screenshot.png";

                        File file = new File(Environment.getExternalStorageDirectory()+"/Pictures", filename);
                        Log.i("RecordActivity",Environment.getExternalStorageDirectory()+"/Pictures");
                        try{
                            FileOutputStream os = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os); //PNG파일로 만들기
                            Log.d("RecordActivity","저장성공");
                            os.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        if(file!=null){
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                            Toast.makeText(getApplicationContext(),"캡쳐성공",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }

    //km
    private void startKm()
    {
        kmTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(predistance!=distance){
                            txt_km.setText(String.valueOf(distance));
                        }
                        predistance=distance;
                    }
                });
            }

        };
        km.scheduleAtFixedRate(kmTask, 0 ,10000);
    }

    //kcal
    private void startKcal()
    {
        kcalTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(mSteps>=one){
                            kcals=kcals+1;
                            txt_kcal.setText(String.valueOf(kcals));
                            one=one*2;
                        }
                    }
                });
            }

        };
        kcal.scheduleAtFixedRate(kcalTask, 0 ,100000);
    }

    //timer
    private void startTimer()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        time++;
                        txt_time.setText(getTimerText());
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
    }
    private String getTimerText()
    {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours)
    {
        return String.format("%02d",hours) + " : " + String.format("%02d",minutes);
    }

    public void startLocationService() {
        try {
            location = null;

            long minTime = 0;        // 0초마다 갱신 - 바로바로갱신
            float minDistance = 0;

            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
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
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    String message = "최근 위치2 -> Latitude : " + latitude + "\n Longitude : " + longitude;

                    Log.i("RecordActivity", "최근 위치1 "+message);
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
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){

            //stepcountsenersor는 앱이 꺼지더라도 초기화 되지않는다. 그러므로 우리는 초기값을 가지고 있어야한다.
            if (mCounterSteps < 1) {
                // initial value
                mCounterSteps = (int) event.values[0];
            }
            //리셋 안된 값 + 현재값 - 리셋 안된 값
            mSteps = (int)event.values[0] - mCounterSteps;
            Log.d("::::::::::::::::","step "+mSteps);
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
            double  latitude  =  location.getLatitude(),  longtitude  =  location.getLongitude();

            if  (myMarker  !=  null)  myMarker.remove();
            location  =  location;
            MarkerOptions  markerOptions  =  new  MarkerOptions();
            markerOptions.position(new  LatLng(latitude,  longtitude));
            myMarker  =    map.addMarker(markerOptions);

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new  LatLng(location.getLatitude(),  location.getLongitude()),  18));
            endLatLng  =  new  LatLng(latitude,  longtitude);//현재  위치를  끝점으로  설정
            drawPath();//polyline  그리기
            startLatLng  =  new  LatLng(latitude,  longtitude);//시작점을  끝점으로  다시  설정
            Log.d("RecordActivity","위도 "+latitude+" 경도 "+longtitude);
            distance=distance+1;
            Log.d("RecordActivity","km "+distance);
            showCurrentLocation(latitude, longtitude);

        }
    private void drawPath(){        //polyline을 그려주는 메소드
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
    };

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
            sensorManager.registerListener(this,stepCountSensor,SensorManager.SENSOR_DELAY_UI);
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
        Log.i("RecordActivity","onPause에서 removeUpdates() 되었습니다.");
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


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(RecordActivity.this, requestCode, permissions, this);
//        Toast.makeText(getApplicationContext(), "requestCode : "+requestCode+"  permissions : "+permissions+"  grantResults :"+grantResults, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDenied(int requestCode, String[] permissions) {
//        Toast.makeText(getApplicationContext(),"permissions denied : " + permissions.length, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGranted(int requestCode, String[] permissions) {
//        Toast.makeText(getApplicationContext(),"permissions granted : " + permissions.length, Toast.LENGTH_SHORT).show();
    }
}

