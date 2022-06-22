package com.example.walgwalg_front_android.member;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.walgwalg_front_android.R;
import com.example.walgwalg_front_android.member.DTO.RegisterRequest;
import com.example.walgwalg_front_android.member.DTO.RegisterResponse;
import com.example.walgwalg_front_android.member.Interface.RegisterInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    String TAG = "RegisterFragmentTAG";

    private Button btn_login;
    private EditText edt_idInput, edt_pwInput, edt_pwCheckInput, edt_nicknameInput, edt_addressInput;

    RetrofitClient retrofitClient;
    RegisterInterface registerInterface;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        init(view);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = edt_idInput.getText().toString();
                String pw = edt_pwInput.getText().toString();
                String repw = edt_pwCheckInput.getText().toString();
                String nickname = edt_nicknameInput.getText().toString();
                String address = edt_addressInput.getText().toString();

                //로그인 정보 미입력 시
                if (id.trim().length() == 0 || pw.trim().length() == 0 || repw.trim().length() == 0 || nickname.trim().length() == 0 || address.trim().length() == 0 ||
                        id == null || pw == null || repw == null || nickname == null || address == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("알림")
                            .setMessage("회원가입 정보를 입력바랍니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }else if(!pw.equals(repw)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("알림")
                            .setMessage("비밀번호가 일치하지 않습니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    //로그인 통신
                    RegisterResponse(id, pw, nickname, address);
                }

                if ((edt_idInput.getText().toString().trim().isEmpty()
                        && edt_pwInput.getText().toString().trim().isEmpty()
                        && edt_pwCheckInput.getText().toString().trim().isEmpty()
                        && edt_nicknameInput.getText().toString().trim().isEmpty()
                        && edt_addressInput.getText().toString().trim().isEmpty())) {
                    Toast.makeText(getContext(), "입력란이 비어있습니다.", Toast.LENGTH_SHORT);
                } else {

                }
            }
        });


        return view;
    }

    public void RegisterResponse(String userID, String userPassword, String nickname, String address) {

        //loginRequest에 사용자가 입력한 id와 pw를 저장
        RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, nickname, address);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        registerInterface = RetrofitClient.getRegistRetrofitInterface();

        //loginRequest에 저장된 데이터와 함께 init에서 정의한 getLoginResponse 함수를 실행한 후 응답을 받음
        registerInterface.getRegisterResponse(registerRequest).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                Log.d("retrofit", "Data fetch success");

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    RegisterResponse result = response.body();

                    //받은 코드 저장
                    String status = result.getStatus();

                    String success = "200"; //회원가입 성공

                    if (status.equals(success)) {
                        Toast.makeText(getContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
//                        Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_homeFragment);

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    private void init(View view) {
        btn_login = view.findViewById(R.id.btn_login);
        edt_idInput = view.findViewById(R.id.edt_idInput);
        edt_pwInput = view.findViewById(R.id.edt_pwInput);
        edt_pwCheckInput = view.findViewById(R.id.edt_pwCheckInput);
        edt_nicknameInput = view.findViewById(R.id.edt_nicknameInput);
        edt_addressInput = view.findViewById(R.id.edt_addressInput);
    }
}