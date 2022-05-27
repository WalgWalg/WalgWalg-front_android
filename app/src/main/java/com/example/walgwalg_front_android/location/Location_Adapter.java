package com.example.walgwalg_front_android.location;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walgwalg_front_android.R;

import java.util.ArrayList;

public class Location_Adapter extends RecyclerView.Adapter<Location_Adapter.ViewHolder> {

    private ArrayList<LocationItem> arrayList;

    @NonNull
    @Override
    public Location_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Location_Adapter.ViewHolder holder, int position) {
//        holder.iv_map.setBackgroundResource(arrayList.get(position).getIv_map());
//        holder.tv_distance.setText(arrayList.get(position).getIv_map());
        holder.onBind(arrayList.get(position));
    }

    public void setArrayList(ArrayList<LocationItem> list){
        this.arrayList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_map;
        TextView tv_distance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_map = (ImageView) itemView.findViewById(R.id.iv_map);
            tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);
        }
        void onBind(LocationItem item){
            iv_map.setImageResource(item.getIv_map());
            tv_distance.setText(item.getTv_distance());
        }
    }
}
