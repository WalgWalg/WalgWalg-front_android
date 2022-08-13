package com.example.walgwalg_front_android.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.walgwalg_front_android.community.MyLocationModel;
import com.example.walgwalg_front_android.member.DTO.MyLikePojo;

import java.util.ArrayList;

public class MyLocationViewModel extends ViewModel {

    private final MutableLiveData<MyLocationModel> myLocationData = new MutableLiveData<>();

    public void saveMyLocation(MyLocationModel myLocationData){
        this.myLocationData.setValue(myLocationData);
    }

    public MutableLiveData<MyLocationModel> getMyLocationData() {
        return myLocationData;
    }
}
