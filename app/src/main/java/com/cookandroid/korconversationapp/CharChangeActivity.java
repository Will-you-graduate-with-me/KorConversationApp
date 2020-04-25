package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class CharChangeActivity extends AppCompatActivity {


    Button btn_charOK;
    ImageButton char1,char2;
    int character_id=0;
    String user_id="";
    private FirebaseAuth mAuth ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char);
        mAuth = FirebaseAuth.getInstance();

        char1=(ImageButton)findViewById(R.id.img1);
        char2=(ImageButton)findViewById(R.id.img2);
        char1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character_id=1;
            }
        });
        char2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character_id=2;

            }
        });
        btn_charOK=(Button)findViewById(R.id.btn_charOK);
        btn_charOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(character_id==0){

                }
                else {
                    try{


                        //User정보 가져오기
                        Map<String, String> userparams = new HashMap<String, String>();
                        userparams.put("user_id",mAuth.getUid());
                        userparams.put("character_id",Integer.toString(character_id));

                        Task Task=new Task("updateCharacter",userparams);
                        Task.execute(userparams).get();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    finish();
                }

            }
        });

    }
}
