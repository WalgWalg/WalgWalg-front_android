package com.example.walgwalg_front_android.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WeatherViewModel extends ViewModel {

    private final MutableLiveData<Double> cur_longitude = new MutableLiveData<>();
    private final MutableLiveData<Double> cur_latitude = new MutableLiveData<>();

    public void save(Double cur_longitude, Double cur_latitude){
        this.cur_longitude.setValue(cur_longitude);
        this.cur_latitude.setValue(cur_latitude);
    }

    public MutableLiveData<Double> getCur_longitude() {
        return cur_longitude;
    }

    public MutableLiveData<Double> getCur_latitude() {
        return cur_latitude;
    }
}
