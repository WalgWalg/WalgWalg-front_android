package com.example.walgwalg_front_android.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.member.DTO.WalkCalendarResponse;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdater extends RecyclerView.Adapter<CalendarAdater.CustomViewHolder> {
    private String TAG = "CalendarAdapter";

    private WalkCalendarResponse walkCalendarResponse;
//    private RecordData recordData;

    private Context mContext;
    private String selectDay;
    private int cnt = 0;
    private List<Integer> num = new ArrayList<>();

    public CalendarAdater(WalkCalendarResponse walkCalendarResponse, Context mContext, String selectDay){
        this.walkCalendarResponse = walkCalendarResponse;
        this.mContext = mContext;
        this.selectDay = selectDay;
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
        Log.d(TAG, "찾을 날짜 : " + selectDay);

        // TODO: 데이터 개수만큼 돌지만 조건이 성립하는 아이템만 생성되도록 만들기

        if (walkCalendarResponse != null){

            String recordDay = walkCalendarResponse.walkCalendarList.get(position).getWalkDate();

            int idx = recordDay.indexOf("T");
            String check = recordDay.substring(0, idx);

            if (selectDay.equals(check)) {

                int idxFront = recordDay.indexOf("T");
                int idxEnd = recordDay.indexOf(".");
                Log.d(TAG, "통과");

                holder.txt_startTime.setText(recordDay.substring(idxFront + 1, idxEnd));
                holder.txt_recordStep.setText(String.valueOf(walkCalendarResponse.walkCalendarList.get(position).getStepCount()));
                holder.txt_recordDistance.setText(String.valueOf(walkCalendarResponse.walkCalendarList.get(position).getDistance()));
                holder.txt_recordCalorie.setText(String.valueOf(walkCalendarResponse.walkCalendarList.get(position).getStepCount() / 300));
            }
            else{
                holder.txt_startTime.setText("00:00");
                holder.txt_recordStep.setText("0");
                holder.txt_recordDistance.setText("0");
                holder.txt_recordCalorie.setText("0");
            }
            if (cnt == 0) {
                holder.txt_startTime.setText("00:00");
                holder.txt_recordStep.setText("0");
                holder.txt_recordDistance.setText("0");
                holder.txt_recordCalorie.setText("0");

                Log.d(TAG, "일치하는 값이 없을 때 임시수치");
            }
        }
        else {
            holder.txt_startTime.setText("00:00");
            holder.txt_recordStep.setText("0");
            holder.txt_recordDistance.setText("0");
            holder.txt_recordCalorie.setText("0");
            Log.d(TAG, "비어있을 때 임시수치");
        }

//        holder.txt_startTime.setText(arrayList.get(position).getStartTime());
//        holder.txt_recordStep.setText(arrayList.get(position).getRecordStep());
//        holder.txt_recordDistance.setText(arrayList.get(position).getRecordDistance());
//        holder.txt_recordCalorie.setText(arrayList.get(position).getRecordCalorie());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount(){
        if (walkCalendarResponse != null) {
            for (int i = 0; i < walkCalendarResponse.walkCalendarList.size(); i++) {

                String recordDay = walkCalendarResponse.walkCalendarList.get(i).getWalkDate();

                int idx = recordDay.indexOf("T");
                String check = recordDay.substring(0, idx);

                if (selectDay.equals(check)) {
                    cnt += 1;
                    num.add(i);
                }

            }
            Log.d(TAG, "인덱스 : " + num);
            return cnt;
        }
        return 1;
    }

    private void remove(int position){
        try {
            walkCalendarResponse.walkCalendarList.remove(position);
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
