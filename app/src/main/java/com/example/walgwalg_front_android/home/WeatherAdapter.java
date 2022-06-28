package com.example.walgwalg_front_android.home;

import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.CustomViewHolder> {
    private ArrayList<CurrentWeather> arrayList;
    private String TAG = "WeatherAdapter";
    private Context mContext;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentStateAdapter fragmentStateAdapter;
    WeatherForecstResult weatherForecstResult;

    public WeatherAdapter(ArrayList<CurrentWeather> arrayList, Context mContext){
        this.arrayList = arrayList;
        this.mContext = mContext;
    }

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
//        holder.img_weather.setImageResource(arrayList.get(position).getIcon());   //TODO: 이미지 타입 확인
//        holder.txt_temp.setText( arrayList.get(position).getTemp());                           //TODO: 온도 타입 확인
//        holder.txt_time.setText(arrayList.get(position).getDt());

        // Load image - 현재 지역 기후에 따른 아이콘 변경
        //Todo: (13:53)
        Picasso.get().load(new StringBuilder("http://openweathermapmap.org/img/wn/")
                .append(weatherForecstResult.getHourly().getWeather().get(position).getIcon())
                .append(".png").toString()).into(holder.img_weather);

        holder.txt_time.setText(new StringBuilder(Common_Weather.convertUnixToHour(weatherForecstResult.getHourly().getDt())));
        holder.txt_temp.setText(new StringBuilder(String.valueOf(weatherForecstResult.getHourly().getTemp())).append("°C"));

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
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position){
        try{
            arrayList.remove(position);
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
