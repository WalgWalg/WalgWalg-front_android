package com.example.walgwalg_front_android.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.walgwalg_front_android.R;

import java.util.ArrayList;
import java.util.List;

public class Location_Adapter extends RecyclerView.Adapter<Location_Adapter.ViewHolder> {
    private Context c;
    private List<LocationData> dataList;

    public Location_Adapter(Context c, List<LocationData> dataList) {
        this.c = c;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public Location_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);

        return new Location_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Location_Adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

//        holder.tv_distance.setText(dataList.get(position).getDistance());
        Glide.with(holder.itemView.getContext()).load(dataList.get(position).getCourse()).into(holder.iv_map);

        holder.btn_detail.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Log.d("post","어댑터: "+dataList.get(position).getBoardId()+" "+dataList.get(position).getTitle());

            }
        });


    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_distance;
        Button btn_detail;
        ImageView iv_map;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);
            btn_detail = (Button) itemView.findViewById(R.id.btn_detail);
            iv_map=(ImageView) itemView.findViewById(R.id.iv_map);

        }

    }
}
