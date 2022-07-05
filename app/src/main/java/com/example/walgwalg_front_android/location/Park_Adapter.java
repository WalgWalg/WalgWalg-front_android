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

import com.example.walgwalg_front_android.R;

import java.util.ArrayList;
import java.util.List;

public class Park_Adapter extends RecyclerView.Adapter<Park_Adapter.ViewHolder> {
    private Context c;
    private List<Data> dataList;

    public Park_Adapter(Context c, List<Data> dataList) {
        this.c = c;
        this.dataList = dataList;
    }

    //아이템 클릭 리스너 인터페이스
    interface OnItemClickListener{
        void onItemClick(View v, int position); //뷰와 포지션값
    }
    //리스너 객체 참조 변수
    private OnItemClickListener mListener = null;
    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
    @NonNull
    @Override
    public Park_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_park, parent, false);

        return new Park_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Park_Adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

//        if("35.8599253741".equals(dataList.get(position).getLatitude())){
            holder.tv_parkname.setText(dataList.get(position).getParkName());
            holder.tv_address.setText(dataList.get(position).getNumberAddress());

            holder.btn_park.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    RecordActivity recordActivity=new RecordActivity();
                    recordActivity.parkpost(dataList.get(position).getParkName(),dataList.get(position).getNumberAddress());
                    Log.d("post","어댑터: "+dataList.get(position).getParkName()+" "+dataList.get(position).getNumberAddress());

                    if (position!=RecyclerView.NO_POSITION){
                        if (mListener!=null){
                            mListener.onItemClick (view,position);
                        }
                    }
                }
            });
//        }

    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_parkname,tv_address;
        Button btn_park;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_parkname = (TextView) itemView.findViewById(R.id.tv_parkname);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            btn_park = (Button) itemView.findViewById(R.id.btn_park);

        }

    }
}
