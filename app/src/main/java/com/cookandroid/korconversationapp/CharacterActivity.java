package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class CharacterActivity extends AppCompatActivity {

    Button btn_charOK;
    ImageButton char1,char2, char3, char4;
    int character_id=0;
    String user_id;
    String nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char);

        Intent preIntent=getIntent();
        user_id=preIntent.getStringExtra("user_id");
        nickname=preIntent.getStringExtra("nickname");

        char1=(ImageButton)findViewById(R.id.img1);
        char2=(ImageButton)findViewById(R.id.img2);
        char3=(ImageButton)findViewById(R.id.img3);
        char4=(ImageButton)findViewById(R.id.img4);
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
        char3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character_id=3;

            }
        });
        char4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character_id=4;

            }
        });
        btn_charOK=(Button)findViewById(R.id.btn_charOK);
        btn_charOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(character_id==0){

            }
            else {
                Intent intent = new Intent(getApplicationContext(),UserInfoActivity.class);
                intent.putExtra("video_id",character_id);
                intent.putExtra("nickname",nickname);
                intent.putExtra("user_id",user_id);
                startActivityForResult(intent,1001); //다른 액티비티를 띄우기 위한 요청코드(상수)
                finish();
            }

            }
        });

    }

}
