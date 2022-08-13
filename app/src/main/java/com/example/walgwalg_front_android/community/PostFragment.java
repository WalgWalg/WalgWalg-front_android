package com.example.walgwalg_front_android.community;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.member.DTO.AddLikeRequest;
import com.example.walgwalg_front_android.member.DTO.AddLikeResponse;
import com.example.walgwalg_front_android.member.DTO.DelLikeRequest;
import com.example.walgwalg_front_android.member.DTO.DelLikeResponse;
import com.example.walgwalg_front_android.member.DTO.DelPostRequest;
import com.example.walgwalg_front_android.member.DTO.DelPostResponse;
import com.example.walgwalg_front_android.member.DTO.MyLikePojo;
import com.example.walgwalg_front_android.member.DTO.MyLikeRequest;
import com.example.walgwalg_front_android.member.DTO.MyLikeResponse;
import com.example.walgwalg_front_android.member.DTO.PostRequest;
import com.example.walgwalg_front_android.member.DTO.PostResponse;
import com.example.walgwalg_front_android.member.Interface.AddLikeInterface;
import com.example.walgwalg_front_android.member.Interface.DelLikeInterface;
import com.example.walgwalg_front_android.member.Interface.DelPostInterface;
import com.example.walgwalg_front_android.member.Interface.MyLikeInterface;
import com.example.walgwalg_front_android.member.Interface.PostInterface;
import com.example.walgwalg_front_android.member.MyInfoViewModel;
import com.example.walgwalg_front_android.member.PreferenceHelper;
import com.example.walgwalg_front_android.member.Retrofit.ServiceGenerator;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    private String TAG = "PostFragmentTAG";

    private TextView tv_name, tv_title, tv_location, tv_contents, tv_step, tv_distance, tv_calorie, tv_favorite, tv_hashtag;
    private ImageView img_route;
    private MaterialButton btn_favorite, btn_share, btn_track, btn_menu;

    private PostInterface postInterface;
    private PostRequest postRequest;
    private PostResponse postResponse;
    private MyLikeInterface myLikeInterface;
    private MyLikeRequest myLikeRequest;
    private MyLikeResponse myLikeResponse;
    private AddLikeInterface likeInterface;
    private AddLikeRequest likeRequest;
    private AddLikeResponse likeResponse;
    private DelLikeInterface delLikeInterface;
    private DelLikeRequest delLikeRequest;
    private DelLikeResponse delLikeResponse;
    private DelPostInterface delPostInterface;
    private DelPostRequest delPostRequest;
    private DelPostResponse delPostResponse;
    private PreferenceHelper preferenceHelper;

    private MyInfoViewModel myInfoViewModel;
    private MyLikeViewModel myLikeViewModel;

    private String name, boardId;
    private Boolean like_status = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        boardId = getArguments().getString("boardId");
        Log.d(TAG, boardId);

        init(view);

        myInfoViewModel = new ViewModelProvider(requireActivity()).get(MyInfoViewModel.class);
        myLikeViewModel = new ViewModelProvider(requireActivity()).get(MyLikeViewModel.class);

        name = myInfoViewModel.getMyInfoData().getValue().nickname;

        myLikeViewModel.getMyLikeData().observe(getActivity(), new Observer<ArrayList<MyLikePojo>>() {
            @Override
            public void onChanged(ArrayList<MyLikePojo> myLikePojos) {
                Log.d(TAG, "ViewModel 관찰중");
                for (int i = 0; i < myLikePojos.size(); i++) {
                    if (boardId.equals(myLikePojos.get(i).boardId)) {
                        like_status = true;
                        Log.d(TAG, boardId);
                        Log.d(TAG, myLikePojos.get(i).boardId);
                    }
                }
                if (like_status) {
                    btn_favorite.setIconResource(R.drawable.ic_favorite_filled_24);
                } else {
                    btn_favorite.setIconResource(R.drawable.ic_like_empty_24);
                }
            }
        });


        preferenceHelper = new PreferenceHelper(getContext());
        postInterface = ServiceGenerator.createService(PostInterface.class, preferenceHelper.getAccessToken());
        postRequest = new PostRequest();
        postInterface.getPost(boardId).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()) {
                    PostResponse result = response.body();
                    tv_name.setText(result.postPojo.nickname);
                    if (result.postPojo.nickname.equals(name)) {
                        btn_menu.setVisibility(View.VISIBLE);
                    }
                    tv_title.setText(result.postPojo.title);
                    String str_img = result.postPojo.course;
                    Log.d(TAG, str_img);
                    tv_location.setText(result.postPojo.location);
                    Glide.with(getView()).load(str_img).into(img_route);
                    tv_step.setText(String.valueOf(result.postPojo.step_count));
                    tv_distance.setText(String.valueOf(result.postPojo.distance));
                    tv_calorie.setText(String.valueOf(result.postPojo.calorie));
                    tv_contents.setText(result.postPojo.contents);
                    tv_favorite.setText(String.valueOf(result.postPojo.likes));
                    String[] favorite = result.postPojo.hashTags;
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < favorite.length; i++) {
                        sb.append("#");
                        sb.append(favorite[i]);
                    }
                    tv_hashtag.setText(sb);
                } else {
                    try {
                        Log.d(TAG + " REST FAILED MESSAGE", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d(TAG + " REST ERROR!", t.getMessage());
            }
        });

        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!like_status) {
                    likeInterface = ServiceGenerator.createService(AddLikeInterface.class, preferenceHelper.getAccessToken());
                    likeRequest = new AddLikeRequest(boardId);
                    likeInterface.PostAddLike(likeRequest).enqueue(new Callback<AddLikeResponse>() {
                        @Override
                        public void onResponse(Call<AddLikeResponse> call, Response<AddLikeResponse> response) {
                            if (response.isSuccessful()) {
                                AddLikeResponse result = response.body();
                                if (result.status.equals("200")) {
                                    Toast.makeText(getContext(), "좋아요를 클릭하셨습니다.", Toast.LENGTH_SHORT).show();
//                                btn_like.setIconResource(R.drawable.ic_favorite_filled_24);
                                    MyLikeResponse();
                                }
                            } else {
                                try {
                                    Log.d(TAG + " REST FAILED MESSAGE", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AddLikeResponse> call, Throwable t) {
                            Log.d(TAG + " REST ERROR!", t.getMessage());
                        }
                    });

                } else {
                    delLikeInterface = ServiceGenerator.createService(DelLikeInterface.class, preferenceHelper.getAccessToken());
                    delLikeRequest = new DelLikeRequest();
                    delLikeInterface.DEL_LIKE_RESPONSE_CALL(boardId).enqueue(new Callback<DelLikeResponse>() {
                        @Override
                        public void onResponse(Call<DelLikeResponse> call, Response<DelLikeResponse> response) {
                            if (response.isSuccessful()) {
                                DelLikeResponse result = response.body();
                                if (result.status.equals("200")) {
                                    Toast.makeText(getContext(), "좋아요를 취소하셨습니다.", Toast.LENGTH_SHORT).show();
//                                btn_like.setIconResource(R.drawable.ic_favorite_filled_24);
                                    MyLikeResponse();
                                }
                            } else {
                                try {
                                    Log.d(TAG + " REST FAILED MESSAGE", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DelLikeResponse> call, Throwable t) {
                            Log.d(TAG + " REST ERROR!", t.getMessage());
                        }
                    });
                    like_status = false;
                }
            }
        });

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("게시물을");

                builder.setItems(R.array.PostMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
//                        String[] items = getResources().getStringArray(R.array.PostMenu);
                        if (pos == 0) {
                            // 삭제
                            DelPostResponse(boardId);
                        } else if (pos == 1) {
                            // 수정
                            Bundle bundle = new Bundle();
                            bundle.putString("boardId", boardId);
                            bundle.putString("title", tv_title.getText().toString());
                            bundle.putString("hashtag", tv_hashtag.getText().toString());
                            bundle.putString("location", tv_location.getText().toString());
                            bundle.putString("contents", tv_contents.getText().toString());
                            Navigation.findNavController(getView()).navigate(R.id.action_postFragment_to_communityAddFragment, bundle);
                        }
//                        Toast.makeText(getContext(), items[pos], Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        return view;
    }

    public void DelPostResponse(String boardId) {
        preferenceHelper = new PreferenceHelper(getContext());
        delPostInterface = ServiceGenerator.createService(DelPostInterface.class, preferenceHelper.getAccessToken());
        delPostRequest = new DelPostRequest();
        delPostInterface.DEL_POST_RESPONSE_CALL(boardId).enqueue(new Callback<DelPostResponse>() {
            @Override
            public void onResponse(Call<DelPostResponse> call, Response<DelPostResponse> response) {
                if (response.isSuccessful()) {
                    DelPostResponse result = response.body();
                    if (result.status.equals("200")) {
                        Toast.makeText(getContext(), "게시물이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getView()).navigate(R.id.action_postFragment_to_communityFragment);
                    }
                } else {
                    Log.d(TAG, "Fail : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<DelPostResponse> call, Throwable t) {
                Log.d(TAG, "Failure : " + t.getMessage());
            }
        });
    }

    public void MyLikeResponse() {
        Log.d(TAG, "MyLikeResponse 실행");
        preferenceHelper = new PreferenceHelper(getContext());
        myLikeInterface = ServiceGenerator.createService(MyLikeInterface.class, preferenceHelper.getAccessToken());
        myLikeRequest = new MyLikeRequest();
        myLikeInterface.GetMyLike().enqueue(new Callback<MyLikeResponse>() {
            @Override
            public void onResponse(Call<MyLikeResponse> call, Response<MyLikeResponse> response) {
                if (response.isSuccessful()) {
                    if (response.isSuccessful()) {
                        MyLikeResponse result = response.body();
                        myLikeViewModel = new ViewModelProvider(requireActivity()).get(MyLikeViewModel.class);
                        myLikeViewModel.SaveMyLikeData(result.myLikePojo);
//                        Log.d(TAG, "MyLikeData : " + result.myLikePojo.get(0).title);
//                        Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_homeFragment);
                    } else {
                        Log.d(TAG, "Fail : " + response.errorBody());
                    }
                }
            }

            @Override
            public void onFailure(Call<MyLikeResponse> call, Throwable t) {
                Log.d(TAG, "Failure : " + t.getMessage());
            }
        });

    }

    private void init(View view) {
        tv_name = view.findViewById(R.id.tv_name);
        tv_title = view.findViewById(R.id.tv_title);
        tv_location = view.findViewById(R.id.tv_location);
        img_route = view.findViewById(R.id.img_route);
        tv_contents = view.findViewById(R.id.tv_contents);
        tv_step = view.findViewById(R.id.tv_step);
        tv_distance = view.findViewById(R.id.tv_distance);
        tv_calorie = view.findViewById(R.id.tv_calorie);
        tv_favorite = view.findViewById(R.id.tv_favorite);
        tv_hashtag = view.findViewById(R.id.tv_hashtag);
        btn_favorite = view.findViewById(R.id.btn_favorite);
        btn_share = view.findViewById(R.id.btn_share);
        btn_track = view.findViewById(R.id.btn_track);
        btn_menu = view.findViewById(R.id.btn_menu);
    }
}