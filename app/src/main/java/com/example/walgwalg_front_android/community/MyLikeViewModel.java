package com.example.walgwalg_front_android.community;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.walgwalg_front_android.member.DTO.MyLikePojo;

import java.util.ArrayList;

public class MyLikeViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<MyLikePojo>> myLikeData = new MutableLiveData<>();

    public void SaveMyLikeData(ArrayList<MyLikePojo> myLikeData){
        this.myLikeData.setValue(myLikeData);
    }

    public MutableLiveData<ArrayList<MyLikePojo>> getMyLikeData() {
        return myLikeData;
    }
}
