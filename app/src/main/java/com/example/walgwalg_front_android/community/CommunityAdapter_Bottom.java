package com.example.walgwalg_front_android.community;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.ViewModel.MyLikeViewModel;
import com.example.walgwalg_front_android.member.DTO.CommunityPojo;
import com.example.walgwalg_front_android.member.DTO.MyLikePojo;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class CommunityAdapter_Bottom extends RecyclerView.Adapter<CommunityAdapter_Bottom.ViewHolder> {

    private String TAG = "CommunityAdapter_Bottom";

    private ArrayList<CommunityPojo> localDataSet;
    private ArrayList<MyLikePojo> myLikeData;

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

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Navigation.createNavigateOnClickListener(R.id.action_communityFragment_to_postFragment);
//                }
//            });

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

    public CommunityAdapter_Bottom(ArrayList<CommunityPojo> dataSet, ArrayList<MyLikePojo> myLikeData) {
        this.localDataSet = dataSet;
        this.myLikeData = myLikeData;
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
        String date = localDataSet.get(position).date;
        int likes = localDataSet.get(position).likes;

        for(int i =0; i<myLikeData.size();i++){
            if(localDataSet.get(position).boardId.equals(myLikeData.get(i).boardId)){
                viewHolder.btn_like.setIconResource(R.drawable.ic_favorite_filled_24);
            }
        }

        date = date.substring(0, 10);

        viewHolder.getTv_title().setText(title);
        viewHolder.getTv_hashtag().setText(hashTag);
        viewHolder.getBtn_like().setText(like);
        viewHolder.getTv_datetime().setText(date);
        viewHolder.getBtn_like().setText(String.valueOf(likes));

        Glide.with(viewHolder.itemView)
                .load(localDataSet.get(position).image)
                .into(viewHolder.iv_map);


        Bundle bundle = new Bundle();
        bundle.putString("boardId", localDataSet.get(position).boardId);
        viewHolder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_communityFragment_to_postFragment, bundle));


    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
