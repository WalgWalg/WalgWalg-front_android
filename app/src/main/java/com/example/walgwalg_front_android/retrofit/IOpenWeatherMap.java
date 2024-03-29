package com.example.walgwalg_front_android.retrofit;

import com.example.walgwalg_front_android.model.WeatherForecstResult;
import com.example.walgwalg_front_android.model.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {

    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                                 @Query("lon") String lng,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit,
                                                 @Query("lang") String lang);

    @GET("onecall")
    Observable<WeatherForecstResult> getForecastWeatherByLatLng (@Query("lat") String lat,
                                                                @Query("lon") String lng,
                                                                @Query("appid") String appid,
                                                                @Query("units") String units,
                                                                 @Query("lang") String lang);

//    @GET("forecast/hourly")
//    Observable<WeatherForecstHourlyResult> getForecastWeatherHourlyByLatLng (@Query("lat") String lat,
//                                                                 @Query("lon") String lng,
//                                                                 @Query("appid") String appid,
//                                                                 @Query("units") String unit,
//                                                                 @Query("lang") String lang);
}
