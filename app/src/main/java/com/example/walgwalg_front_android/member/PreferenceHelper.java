package com.example.walgwalg_front_android.member;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    // TODO: token(access, refresh) getter 만들어야됨. - 20220621-18:21

    private final String USERID = "userid";
    private final String PASSWORD = "password";
    private final String DATETIME = "dateTime";
    private final String ACCESSTOKEN = "accessToken";
    private final String REFRESHTOKEN = "refreshToken";
    private final String AUTOLOGIN = "AUTOLOGIN";
    private SharedPreferences user_prefs;
    private Context context;

    public PreferenceHelper(Context context) {
        user_prefs = context.getSharedPreferences("shared", 0);
        this.context = context;
    }

    public void saveAutoLogin(Boolean isAutoLogin) {
        SharedPreferences.Editor edit = user_prefs.edit();
        edit.putBoolean(AUTOLOGIN, isAutoLogin);
        edit.apply();
    }

    public Boolean getAutoLogin() {return user_prefs.getBoolean(AUTOLOGIN, false);}

    public void saveUserid(String userId) {
        SharedPreferences.Editor edit = user_prefs.edit();
        edit.putString(USERID, userId);
        edit.apply();
    }

    public String getUserid() {
        return user_prefs.getString(USERID, "");
    }

    public void savePassword(String passWord) {
        SharedPreferences.Editor edit = user_prefs.edit();
        edit.putString(PASSWORD, passWord);
        edit.apply();
    }

    public String getPassword() {
        return user_prefs.getString(PASSWORD, "");
    }

    public void saveDateTime(String dateTime) {
        SharedPreferences.Editor edit = user_prefs.edit();
        edit.putString(DATETIME, dateTime);
        edit.apply();
    }

    public String getDateTime() {
        return user_prefs.getString(DATETIME, "");
    }

    public void saveAccessToken(String accessToken) {
        SharedPreferences.Editor edit = user_prefs.edit();
        edit.putString(ACCESSTOKEN, accessToken);
        edit.apply();
    }

    public String getAccessToken() {
        return user_prefs.getString(ACCESSTOKEN, "");
    }

    public void saveRefreshToken(String refreshToken) {
        SharedPreferences.Editor edit = user_prefs.edit();
        edit.putString(REFRESHTOKEN, refreshToken);
        edit.apply();
    }

    public String getRefreshToken() {
        return user_prefs.getString(REFRESHTOKEN, "");
    }
}
