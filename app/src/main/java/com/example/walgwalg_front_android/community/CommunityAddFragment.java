package com.example.walgwalg_front_android.community;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.walgwalg_front_android.ProgressDialog;
import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.member.DTO.CommunityAddRequest;
import com.example.walgwalg_front_android.member.DTO.CommunityAddResponse;
import com.example.walgwalg_front_android.member.DTO.EditPostRequest;
import com.example.walgwalg_front_android.member.DTO.EditPostResponse;
import com.example.walgwalg_front_android.member.DTO.MyWalkPojo;
import com.example.walgwalg_front_android.member.DTO.MyWalkRequest;
import com.example.walgwalg_front_android.member.DTO.MyWalkResponse;
import com.example.walgwalg_front_android.member.Interface.CommunityAddInterface;
import com.example.walgwalg_front_android.member.Interface.EditPostInterface;
import com.example.walgwalg_front_android.member.Interface.MyWalkInterface;
import com.example.walgwalg_front_android.member.PreferenceHelper;
import com.example.walgwalg_front_android.member.Retrofit.ServiceGenerator;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityAddFragment extends Fragment {

    private String TAG = "CommunityAddFragmentTAG";


    private CommunityAddRequest communityAddRequest;
    private CommunityAddInterface communityAddInterface;
    private EditPostInterface editPostInterface;
    private EditPostRequest editPostRequest;
    private EditPostResponse editPostResponse;
    private MyWalkRequest myWalkRequest;
    private MyWalkResponse myWalkResponse;
    private MyWalkInterface myWalkInterface;
    private PreferenceHelper preferenceHelper;

    private String authToken;
    private String walkId;
    private String boardId, title, hashtag, location, contents;
    private TextInputLayout layout_location;
    private TextInputEditText edt_title, edt_hashtag, edt_location, edt_content;
    private MaterialToolbar toolbar;

    private ArrayList<MyWalkPojo> myWalkData;

    Bundle bundle = new Bundle();

    ProgressDialog customProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community_add, container, false);

        customProgressDialog = new ProgressDialog(getContext());
        //로딩창을 투명하게
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        init(view);
//        Log.d(TAG, getArguments().getString("title"));

        if (!getArguments().isEmpty()) {
            edt_location.setEnabled(false);
            layout_location.setHint("산책 이름(수정 불가)");
            boardId = getArguments().getString("boardId");
            title = getArguments().getString("title");
            hashtag = getArguments().getString("hashtag");
            location = getArguments().getString("location");
            contents = getArguments().getString("contents");
            edt_title.setText(title);
            edt_hashtag.setText(hashtag);
            edt_location.setText(location);
            edt_content.setText(contents);
        }

        preferenceHelper = new PreferenceHelper(getContext());

        myWalkInterface = ServiceGenerator.createService(MyWalkInterface.class, preferenceHelper.getAccessToken());

        edt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.show();
                myWalkRequest = new MyWalkRequest();
                myWalkInterface.getMyWalkResponse().enqueue(new Callback<MyWalkResponse>() {
                    @Override
                    public void onResponse(Call<MyWalkResponse> call, Response<MyWalkResponse> response) {
                        if (response.isSuccessful()) {
                            MyWalkResponse myWalkResponse = response.body();
                            myWalkData = response.body().myWalkPojo;
                            Log.d(TAG, String.valueOf(myWalkData.size()));
                            Log.d(TAG, String.valueOf(myWalkData.get(0).location));

                            ArrayList<HashMap<String, String>> list = new ArrayList<>();

                            HashMap<String, String> item01 = new HashMap<>();

                            for (int i = 0; i < myWalkData.size(); i++) {
                                item01.put("walkDate", myWalkData.get(i).walkDate);
                                Log.d(TAG + i, String.valueOf(myWalkData.get(i).walkDate));
                                Log.d(TAG + i, String.valueOf(myWalkData.get(i).location));
                                item01.put("location", myWalkData.get(i).location);
//                                item01.put("walkTime", myWalkData.get(i).walkTime);
//                                item01.put("calorie", String.valueOf(myWalkData.get(i).calorie));
//                                item01.put("distance", String.valueOf(myWalkData.get(i).distance));
//                                item01.put("stepCount", String.valueOf(myWalkData.get(i).stepCount));
                                list.add(item01);
                            }
//
                            customProgressDialog.dismiss();
                            showAlertDialog(list);
                        } else {
                            try {
                                Log.d(TAG, response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyWalkResponse> call, Throwable t) {
                        Log.d("MyPageFragment REST ERROR!", t.getMessage());
                    }
                });
            }
        });


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.finish:
                        Log.d("MyPageFragment", "버튼 누름");
                        String title = edt_title.getText().toString();
                        String[] hashTag = {"벚꽃", "산책"};
                        String location = edt_location.getText().toString();
                        String contents = edt_content.getText().toString();
                        if (getArguments().isEmpty()) {
                            communityAddInterface = ServiceGenerator.createService(CommunityAddInterface.class, preferenceHelper.getAccessToken());

                            communityAddRequest = new CommunityAddRequest(walkId, title, hashTag, contents);
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
                        } else if (!getArguments().isEmpty()) {
                            editPostInterface = ServiceGenerator.createService(EditPostInterface.class, preferenceHelper.getAccessToken());
                            editPostRequest = new EditPostRequest(boardId, title, hashTag, contents);
                            editPostInterface.EDIT_POST_RESPONSE_CALL(editPostRequest).enqueue(new Callback<EditPostResponse>() {
                                @Override
                                public void onResponse(Call<EditPostResponse> call, Response<EditPostResponse> response) {
                                    if (response.isSuccessful()) {
                                        editPostResponse = response.body();
                                        if (editPostResponse.status.equals("200")) {
                                            Toast.makeText(getContext(), "게시물이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("boardId", boardId);
                                            Navigation.findNavController(getView()).navigate(R.id.action_communityAddFragment_to_postFragment, bundle);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<EditPostResponse> call, Throwable t) {

                                }
                            });
                        }


                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        return view;
    }

    private void showAlertDialog(ArrayList<HashMap<String, String>> list) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.community_add_location_dialog, null);
        builder.setView(view);

        String[] from = {"walkDate", "location"};
        int[] to = new int[]{R.id.tv_datetime, R.id.tv_location};

        final ListView listview = (ListView) view.findViewById(R.id.listview_alterdialog_list);
        final AlertDialog dialog = builder.create();

        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), list,
                R.layout.community_add_location_dialog_row,
                from, to);

        listview.setAdapter(simpleAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, myWalkData.get(position).location + "을 선택함");
                walkId = myWalkData.get(position).id;
                edt_location.setText(myWalkData.get(position).location);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void init(View view) {
        myWalkData = new ArrayList<>();
        edt_title = view.findViewById(R.id.edt_title);
        edt_hashtag = view.findViewById(R.id.edt_hashtag);
        layout_location = view.findViewById(R.id.layout_location);
        edt_location = view.findViewById(R.id.edt_location);
        edt_content = view.findViewById(R.id.edt_content);
        toolbar = view.findViewById(R.id.toolbar);
    }
}