package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Menu3Fragment extends Fragment {

    private FirebaseAuth mAuth ;
    TextView logout,signout,point1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.page_my, container, false);

        mAuth = FirebaseAuth.getInstance();

        logout=(TextView)v.findViewById(R.id.logOut);
        signout=(TextView)v.findViewById(R.id.signOut);
        point1=(TextView)v.findViewById(R.id.point1); //닉네임

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        String UserInfo="";


        try{
            //User정보 가져오기
            Map<String, String> userparams = new HashMap<String, String>();
            userparams.put("user_id",mAuth.getUid());
            Task Task=new Task("selectUserInfo",userparams);
            UserInfo=Task.execute(userparams).get();

        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            JSONObject jsonvar = new JSONObject(UserInfo);
            point1.setText(jsonvar.get("nickname").toString());

        }catch (JSONException e) {
            e.printStackTrace();
        }


        //회원 탈퇴
//        signout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                revokeAccess();
//
//            }
//        });

        return v;
    }

    private void signOut() {
        System.out.println("로그아웃됨");
        FirebaseAuth.getInstance().signOut();
    }

    //회원탈퇴
    private void revokeAccess() {
        System.out.println("회원탈퇴됨");
        mAuth.getCurrentUser().delete();
    }
}