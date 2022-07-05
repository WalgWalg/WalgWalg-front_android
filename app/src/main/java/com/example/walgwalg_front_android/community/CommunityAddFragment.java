package com.example.walgwalg_front_android.community;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.member.DTO.CommunityAddRequest;
import com.example.walgwalg_front_android.member.DTO.CommunityAddResponse;
import com.example.walgwalg_front_android.member.Interface.CommunityAddInterface;
import com.example.walgwalg_front_android.member.PreferenceHelper;
import com.example.walgwalg_front_android.member.Retrofit.ServiceGenerator;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityAddFragment extends Fragment {

    private CommunityAddRequest communityAddRequest;
    private CommunityAddInterface communityAddInterface;
    private PreferenceHelper preferenceHelper;
    private String authToken;
    private TextInputEditText edt_title, edt_hashtag, edt_location, edt_content;
    private MaterialToolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community_add, container, false);

        init(view);


//        String time = null;
//        try {
//            time = convertTimeZone("2019-03-15'T'00:11:33.SSS'Z'");
//            Toast.makeText(getContext(), time, Toast.LENGTH_SHORT).show();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        Calendar calendar = Calendar.getInstance();
//        Date date = calendar.getTime();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
//
//        String text = sdf.format(date);
//        Log.d("MyPageFragment", "Asia/Seoul 시간" + text);

//        String text = sdf.format(date);
//         Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
//         Log.d("MyPageFragment", "시간 string" + sdf.getClass());


//        try {
//            Log.d("MyPageFragment", "시간 date" + );
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            LocalDateTime currentDateTime = LocalDateTime.now();
//            Date date2 = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
//            Log.d("MyPageFragment", "시간 string" + currentDateTime);
//            Log.d("MyPageFragment", String.valueOf(date2));
//
//        }

//        String str = "2019-03-05T05:59:52+00:00";
//
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

//        try {
//            Date temp = df.parse(str);
//            Log.d("MyPageFragment", String.valueOf(temp));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        preferenceHelper = new PreferenceHelper(getContext());

        communityAddInterface = ServiceGenerator.createService(CommunityAddInterface.class, "PMAK-62c2bb573eaf6129f000481d-7a7ec096d3dd182e419dc0fb7e0473d544");

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.finish:
                        Log.d("MyPageFragment", "버튼 누름");
                        String title = edt_title.getText().toString();
                        String[] hashTag = {"동백", "호수", "공원", "이쁘다", "산책스타그램"};
                        String location = edt_location.getText().toString();
                        String contents = edt_content.getText().toString();

                        communityAddRequest = new CommunityAddRequest(location, title, hashTag, contents);
                        communityAddInterface.getCommunityAddResponse(communityAddRequest)
                                .enqueue(new Callback<CommunityAddResponse>() {
                                    @Override
                                    public void onResponse(Call<CommunityAddResponse> call, Response<CommunityAddResponse> response) {
                                        if (response.isSuccessful()) {
                                            CommunityAddResponse result = (CommunityAddResponse) response.body();
                                            Log.d("MyPageFragment", "응답" + response.isSuccessful());
                                            Log.d("MyPageFragment", "응답" + result.status);
                                        } else {
                                            try {
                                                Log.d("MyPageFragment REST FAILED MESSAGE", response.errorBody().string());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            Log.d("MyPageFragment REST FAILED MESSAGE", response.message());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<CommunityAddResponse> call, Throwable
                                            t) {
                                        Log.d("MyPageFragment REST ERROR!", t.getMessage());
                                    }
                                });

                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        return view;
    }

    private void init(View view) {
        edt_title = view.findViewById(R.id.edt_title);
        edt_hashtag = view.findViewById(R.id.edt_hashtag);
        edt_location = view.findViewById(R.id.edt_location);
        edt_content = view.findViewById(R.id.edt_content);
        toolbar = view.findViewById(R.id.toolbar);
    }

//    public String convertTimeZone(String time) throws ParseException {
//
//
//        String form = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
//
//
//        SimpleDateFormat inputFormat = new SimpleDateFormat
//                (form, Locale.KOREA);
//        inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
//
//        SimpleDateFormat outputFormat = new SimpleDateFormat(form);
//        // Adjust locale and zone appropriately
//        Date date = inputFormat.parse(time);
//        String outputText = outputFormat.format(date);
//        return outputText;
//    }
}