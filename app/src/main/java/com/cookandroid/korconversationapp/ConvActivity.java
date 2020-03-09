package com.cookandroid.korconversationapp;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ConvActivity extends AppCompatActivity {

    ImageButton back,speaker,restart,grade,check;
    Button repeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conv);
        back=(ImageButton)findViewById(R.id.backbtn);
        speaker=(ImageButton)findViewById(R.id.btn_speak);
        restart=(ImageButton)findViewById(R.id.btn_restart);
        grade=(ImageButton)findViewById(R.id.btn_grade);
        check=(ImageButton)findViewById(R.id.btn_check);
        repeat=(Button)findViewById(R.id.btn_repeat);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
