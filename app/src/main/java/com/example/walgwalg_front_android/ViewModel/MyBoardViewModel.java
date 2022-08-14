package com.example.walgwalg_front_android.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.walgwalg_front_android.member.DTO.CommunityPojo;
import com.example.walgwalg_front_android.member.DTO.MyInfoPojo;

import java.util.ArrayList;

public class MyBoardViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<CommunityPojo>> myBoardData = new MutableLiveData<>();

    public void saveMyBoard(ArrayList<CommunityPojo> myBoardData) {
        this.myBoardData.setValue(myBoardData);
    }

    public MutableLiveData<ArrayList<CommunityPojo>> getMyBoardData() {
        return myBoardData;
    }
}
