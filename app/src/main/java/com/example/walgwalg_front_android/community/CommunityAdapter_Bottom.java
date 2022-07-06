package com.example.walgwalg_front_android.community;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.member.DTO.CommunityPojo;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class CommunityAdapter_Bottom extends RecyclerView.Adapter<CommunityAdapter_Bottom.ViewHolder> {

    private ArrayList<CommunityPojo> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_map;
        private final TextView tv_title, tv_hashtag, tv_datetime;
        private final MaterialButton btn_like;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            iv_map = (ImageView) view.findViewById(R.id.iv_map);
            tv_title = view.findViewById(R.id.tv_title);
            tv_hashtag = view.findViewById(R.id.tv_hashtag);
            tv_datetime = view.findViewById(R.id.tv_datetime);
//            btn_scrap = view.findViewById(R.id.btn_scrap);
            btn_like = view.findViewById(R.id.btn_like);

        }

        public ImageView getIv_map() {
            return iv_map;
        }

        public TextView getTv_title() {
            return tv_title;
        }

        public TextView getTv_hashtag() {
            return tv_hashtag;
        }

        public TextView getTv_datetime() {
            return tv_datetime;
        }

//        public MaterialButton getBtn_scrap() {
//            return btn_scrap;
//        }

        public MaterialButton getBtn_like() {
            return btn_like;
        }
    }

    public CommunityAdapter_Bottom(ArrayList<CommunityPojo> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public CommunityAdapter_Bottom.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_community_bottom, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        String title = localDataSet.get(position).title;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < localDataSet.get(position).hashTags.length; i++) {
            if (i != localDataSet.get(position).hashTags.length) {
                sb.append("#");
            }
            sb.append(localDataSet.get(position).hashTags[i]);
        }
        String hashTag = sb.toString();
        String like = String.valueOf(localDataSet.get(position).likes);

        viewHolder.getTv_title().setText(title);
        viewHolder.getTv_hashtag().setText(hashTag);
        viewHolder.getBtn_like().setText(like);
        viewHolder.getTv_datetime().setText("2022.04.15");

        Glide.with(viewHolder.itemView)
                .load(localDataSet.get(position).course)
                .into(viewHolder.iv_map);

    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
