package com.example.walgwalg_front_android.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();

    public TabPagerAdapter(@NonNull FragmentManager fm, int behavior){
        super(fm,behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
//        switch (position){
//            case 0:
//                HomeAlltabFragment homeAlltabFragment = new HomeAlltabFragment();
//                return homeAlltabFragment;
//
//            case 1:
//                HomeBesttabFragment homeBesttabFragment = new HomeBesttabFragment();
//                return homeBesttabFragment;
//
//            case 2:
//                HomeNewtabFragment homeNewtabFragment = new HomeNewtabFragment();
//                return homeNewtabFragment;
//
//            default:
//                return null;
//        }

        return fragmentArrayList.get(position);

    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment, String title){
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        return fragmentTitle.get(position);
    }
}
