package com.cookandroid.korconversationapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class Menu2Fragment extends Fragment {


    //탭2안의 탭
    private drive1Fragment drive1Fragment = new drive1Fragment();
    private drive2Fragment drive2Fragment = new drive2Fragment();
    private drive3Fragment drive3Fragment = new drive3Fragment();

    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getChildFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_drivemenu1:
                    fragmentTransaction.replace(R.id.drive_layout, drive1Fragment).commit();
                    return true;
                case R.id.navigation_drivemenu2:
                    fragmentTransaction.replace(R.id.drive_layout, drive2Fragment).commit();
                    return true;
                case R.id.navigation_drivemenu3:
                    fragmentTransaction.replace(R.id.drive_layout, drive3Fragment).commit();
                    return true;
            }
            return false;
        }

    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.page_drive, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView navigation =  getActivity().findViewById(R.id.top_navigation_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

}