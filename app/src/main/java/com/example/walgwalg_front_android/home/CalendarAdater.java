package com.example.walgwalg_front_android.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walgwalg_front_android.R;

import java.util.ArrayList;

public class CalendarAdater extends RecyclerView.Adapter<CalendarAdater.CustomViewHolder> {
    private String TAG = "CalendarAdapter";

    private ArrayList<RecordData> arrayList;
    private Context mContext;

    public CalendarAdater(ArrayList<RecordData> arrayList, Context mContext){
        this.arrayList = arrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CalendarAdater.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_record, parent, false);
        CalendarAdater.CustomViewHolder holder = new CalendarAdater.CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdater.CustomViewHolder holder, int position){
        holder.txt_startTime.setText(arrayList.get(position).getStartTime());
        holder.txt_recordStep.setText(arrayList.get(position).getRecordStep());
        holder.txt_recordDistance.setText(arrayList.get(position).getRecordDistance());
        holder.txt_recordCalorie.setText(arrayList.get(position).getRecordCalorie());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount(){
        return (null != arrayList ? arrayList.size() : 0);
    }

    private void remove(int position){
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_startTime, txt_recordStep, txt_recordDistance,txt_recordCalorie;

        public CustomViewHolder(@NonNull View itemView){
            super(itemView);

            txt_startTime = itemView.findViewById(R.id.txt_startTime);
            txt_recordStep = itemView.findViewById(R.id.txt_recordStep);
            txt_recordDistance = itemView.findViewById(R.id.txt_recordDistance);
            txt_recordCalorie = itemView.findViewById(R.id.txt_recordCalorie);

        }
    }
}
