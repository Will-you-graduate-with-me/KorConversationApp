package com.cookandroid.korconversationapp;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    int PERMISSION;
    Button btn_login;
    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들
    private Menu1Fragment menu1Fragment = new Menu1Fragment();
    private Menu2Fragment menu2Fragment = new Menu2Fragment();
    private Menu3Fragment menu3Fragment = new Menu3Fragment();
    private FirebaseAuth mAuth ;

    String user_id,nickname;
    int character_id,stay_duration,age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        Intent preIntent=getIntent();
        user_id=preIntent.getStringExtra("user_id");
        nickname=preIntent.getStringExtra("nickname");
        stay_duration=preIntent.getIntExtra("stay_duration",999);
        character_id=preIntent.getIntExtra("video_id",999);
        age=preIntent.getIntExtra("age",999);

        String userInfo = "";
        mAuth = FirebaseAuth.getInstance();

        // 유저 아이디 확인
        try {
            Map<String, String> params_userid = new HashMap<String, String>();
            params_userid.put("user_id", mAuth.getUid());

            com.cookandroid.korconversationapp.Task TaskforUser = new com.cookandroid.korconversationapp.Task("whetherUserInfo", params_userid);

            userInfo = TaskforUser.execute(params_userid).get();
            System.out.println("유저반환값 : " + userInfo);

            if(userInfo.equals("0")) {
                //insert
                //만약 작동 안한다면 try-catch 없애고 밑에 주석표시한 부분만 밖으로 빼내기
                Map<String, String> userparams = new HashMap<String, String>();
                Map<String, String> userparamsForFlask = new HashMap<>();

                userparams.put("user_id",user_id);
                userparamsForFlask.put("user_id", user_id);
                System.out.println("유저아이디 :" + user_id);
                userparams.put("nickname",nickname);
                userparams.put("stay_duration",Integer.toString(stay_duration));
                userparamsForFlask.put("stay_duration",Integer.toString(stay_duration));
                userparams.put("video_id",Integer.toString(character_id));
                userparams.put("age",Integer.toString(age));
                userparamsForFlask.put("age",Integer.toString(age));

                Task insertTask=new Task("insertUserID",userparams);
                TaskForFlask insertInterestIDTask=new TaskForFlask("fuzzyRecommend",userparamsForFlask);

                insertTask.execute(userparams);
                insertInterestIDTask.execute(userparamsForFlask);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, menu1Fragment).commitAllowingStateLoss();


        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_menu1: {
                        transaction.replace(R.id.frame_layout, menu1Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu2: {
                        transaction.replace(R.id.frame_layout, menu2Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu3: {
                        transaction.replace(R.id.frame_layout, menu3Fragment).commitAllowingStateLoss();
                        break;
                    }

                }

                return true;
            }
        });

    }


}
