package com.example.walgwalg_front_android.home;

import static android.graphics.BlendMode.COLOR;

import android.os.Bundle;

import androidx.compose.ui.graphics.Color;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.walgwalg_front_android.R;
import com.google.android.material.button.MaterialButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MaterialCalendarView materialCalendarView;
    private MaterialButton btn_weather;
    private Button btn_start;
    private static FragmentManager fragmentManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);

//        CalendarView.setTop(R.color.mainColor);

//        materialCalendarView.setColor;

//        materialCalendarView.state().edit()
//                .setFirstDayOfWeek(Calendar.SUNDAY)
//                .setMinimumDate(CalendarDay.from(2021, 0,1))
//                .setMaximumDate(CalendarDay.from(2030,12,31))
//                .setCalendarDisplayMode(CalendarMode.MONTHS)
//                .commit();

        btn_weather.setOnClickListener(task -> {
            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.homeFragment, new WeatherFragment()).addToBackStack(null).commit();

            Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_weatherFragment);
        });

        btn_start.setOnClickListener(task ->{
            Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_recordFragment);
        });

        return view;
    }

    public void init(View view) {
//        materialCalendarView = view.findViewById(R.id.calendar);
        btn_weather = view.findViewById(R.id.btn_weather);
        btn_start = view.findViewById(R.id.btn_start);
    }
}