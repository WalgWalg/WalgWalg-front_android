package com.example.walgwalg_front_android.community;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.member.DTO.CommunityTopRankPojo;

import java.util.ArrayList;

public class CommunityAdapter_Top extends RecyclerView.Adapter<CommunityAdapter_Top.ViewHolder> {

    private ArrayList<CommunityTopRankPojo> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_map;
        private final TextView tv_title;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            iv_map = (ImageView) view.findViewById(R.id.iv_map);
            tv_title = (TextView) view.findViewById(R.id.tv_title);

        }

        public ImageView getIv_map() {
            return iv_map;
        }

        public TextView getTv_title() {
            return tv_title;
        }
    }

    public CommunityAdapter_Top(ArrayList<CommunityTopRankPojo> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public CommunityAdapter_Top.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_community_top, parent, false);

        return new CommunityAdapter_Top.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String link = localDataSet.get(position).image;
        String title = localDataSet.get(position).parkName;

        Glide.with(viewHolder.itemView)
                .load(link)
                .error(R.drawable.ic_error_24)
                .fallback(R.drawable.ic_error_24)
                .into(viewHolder.getIv_map());
        Log.d("CommunityAdapter_TOP", title);
        viewHolder.getTv_title().setText(title);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
