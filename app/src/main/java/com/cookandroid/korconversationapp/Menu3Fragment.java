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

import org.w3c.dom.Text;

public class Menu3Fragment extends Fragment {

    private FirebaseAuth mAuth ;
    TextView logout,signout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.page_my, container, false);

        mAuth = FirebaseAuth.getInstance();


        logout=(TextView)v.findViewById(R.id.logOut);
        signout=(TextView)v.findViewById(R.id.signOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

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