package com.example.walgwalg_front_android.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.walgwalg_front_android.member.DTO.MyInfoPojo;
import com.example.walgwalg_front_android.member.DTO.MyLikePojo;

import java.util.ArrayList;

public class MyInfoViewModel extends ViewModel {

    private final MutableLiveData<MyInfoPojo> myInfoData = new MutableLiveData<>();

    public void saveMyInfo(MyInfoPojo myInfoData){
        this.myInfoData.setValue(myInfoData);
    }

    public MutableLiveData<MyInfoPojo> getMyInfoData() {
        return myInfoData;
    }
}
