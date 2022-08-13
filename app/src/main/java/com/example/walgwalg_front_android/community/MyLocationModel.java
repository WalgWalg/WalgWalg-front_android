package com.example.walgwalg_front_android.community;

public class MyLocationModel {

    Double latitude;    // 위도
    Double longitude;   // 경고

    String Si_Do;
    String Si_Gun_Gu;
    String Eup_Myeon_Dong;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getSi_Do() {
        return Si_Do;
    }

    public void setSi_Do(String si_Do) {
        Si_Do = si_Do;
    }

    public String getSi_Gun_Gu() {
        return Si_Gun_Gu;
    }

    public void setSi_Gun_Gu(String si_Gun_Gu) {
        Si_Gun_Gu = si_Gun_Gu;
    }

    public String getEup_Myeon_Dong() {
        return Eup_Myeon_Dong;
    }

    public void setEup_Myeon_Dong(String eup_Myeon_Dong) {
        Eup_Myeon_Dong = eup_Myeon_Dong;
    }
}
