package com.example.walgwalg_front_android.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.model.CurrentWeather;
import com.example.walgwalg_front_android.model.WeatherForecstResult;
import com.example.walgwalg_front_android.model.WeatherResult;
import com.example.walgwalg_front_android.weather.Common_Weather;
import com.kakao.util.maps.helper.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.CustomViewHolder> {

    private String TAG = "WeatherAdapter";
    private Context mContext;
    WeatherForecstResult weatherForecstResult;

    public WeatherAdapter(Context mContext, WeatherForecstResult weatherForecstResult){
        this.mContext = mContext;
        this.weatherForecstResult = weatherForecstResult;
    }

    @NonNull
    @Override
    public WeatherAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_weather, parent, false);
        WeatherAdapter.CustomViewHolder holder = new WeatherAdapter.CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.CustomViewHolder holder, int position){

        // Load image - 현재 지역 기후에 따른 아이콘 변경
//        holder.img_weather.setImageResource(Utility.getArtResourceForWeatherCondition(weatherForecstResult.hourly.get(position).weather.get(0).getId()));
//        Log.d(TAG, String.valueOf(weatherForecstResult.timezone_offset));

        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/wn/")
                .append(weatherForecstResult.hourly.get(position).weather.get(0).getIcon())
                .append("@2x.png").toString()).into(holder.img_weather);
        Log.d(TAG, "getIcon " + weatherForecstResult.hourly.get(position).weather.get(0).getIcon());

        holder.txt_time.setText(new StringBuilder(Common_Weather.convertUnixToHour(weatherForecstResult.hourly.get(position).dt)));
        holder.txt_temp.setText(new StringBuilder(String.valueOf(weatherForecstResult.hourly.get(position).getTemp())).append("°C"));

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 날씨 클릭 시
            }
        });
    }

    @Override
    public int getItemCount() {
        return 24;
    }

    public void remove(int position){
        try{
            weatherForecstResult.hourly.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_time, txt_temp;
        private ImageView img_weather;
        private int id;

        public CustomViewHolder(@NonNull View itemView){
            super(itemView);

            txt_temp = itemView.findViewById(R.id.txt_temp);
            txt_time = itemView.findViewById(R.id.txt_time);
            img_weather = itemView.findViewById(R.id.img_weather);

        }
    }

}
