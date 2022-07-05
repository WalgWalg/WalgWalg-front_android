package com.example.walgwalg_front_android.community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.walgwalg_front_android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CommunityFragment extends Fragment {

    private FloatingActionButton btn_add;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        init(view);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_communityFragment_to_communityAddFragment);
            }
        });

        return view;
    }

    private void init(View view){
        btn_add = view.findViewById(R.id.btn_add);
    }
}