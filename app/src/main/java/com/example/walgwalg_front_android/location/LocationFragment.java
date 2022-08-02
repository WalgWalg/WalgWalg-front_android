package com.example.walgwalg_front_android.location;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.walgwalg_front_android.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment {
    private ImageView iv_map;
    private Button btn_detail;
    private TextView tv_distance, tv_location,tv_ment;
    private RecyclerView recyclerView;
    private Location_Adapter location_adapter;
    private static double latitude, longitude;
    List<LocationData> dataInfo;
    LocationItem dataList;
    private ApiClient apiClient;
    private static String usertoken;
    private String region;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        init(view);

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return view;
        }
        Location loc_Current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        latitude=loc_Current.getLatitude();
        longitude=loc_Current.getLongitude();


        Geocoder g = new Geocoder(getContext());
        List<Address> address = null;


        try {
//                    first_latitude=35.8767887;    //대구
//                    first_longitude=128.5962455;
            Log.d("LocationFragment", String.valueOf(latitude) + "  " + String.valueOf(longitude));
            address = g.getFromLocation(latitude, longitude, 10);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("LocationFragment", "입출력오류");
        }

        region = address.get(0).getAddressLine(0);
        region = region.substring(5);
        String[] arr = region.split(" ");
        String result;
        result = arr[2];
        result += " " + arr[1];
        result += " " + arr[0];
        Log.d("LocationFragment", result);
        tv_location.setText("region");
        tv_ment.setText(region+"에서");

        dataInfo = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        LocationInterface locationInterface = apiClient.getClient().create(LocationInterface.class);
        Call<LocationItem> call = locationInterface.getLocationData(result);

        call.enqueue(new Callback<LocationItem>() {

            @Override
            public void onResponse(Call<LocationItem> call, Response<LocationItem> response) {

                dataList = response.body();

                Log.d("LocationFragment", dataList.tolist());
                Log.d("LocationFragment", dataList.getMessage());

                dataInfo = dataList.list;

                location_adapter = new Location_Adapter(getContext(), dataInfo);

                recyclerView.setAdapter(location_adapter);

            }

            @Override
            public void onFailure(Call<LocationItem> call, Throwable t) {
                Log.d("LocationFragment", t.toString());
            }
        });

        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location loc_Current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                latitude=loc_Current.getLatitude();
                longitude=loc_Current.getLongitude();
                tv_location.setText(region);
                tv_ment.setText(region+"에서");
            }
        });

        return view;
    }

    public void gettoken(String token){
        usertoken=token;
        Log.d("LocationFragment","로그인에서 받아온 토큰: "+usertoken);
    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        btn_detail=(Button) view.findViewById(R.id.btn_detail);
        apiClient=new ApiClient(usertoken);
        tv_ment=(TextView) view.findViewById(R.id.tv_ment);
    }


}
