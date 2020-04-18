package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class NameActivity extends AppCompatActivity {

    Button btn_nameOk, btn_nameSkip;
    private FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        mAuth = FirebaseAuth.getInstance();

        btn_nameOk=(Button)findViewById(R.id.btn_nameOk);
        btn_nameSkip=(Button)findViewById(R.id.btn_nameSkip);
        btn_nameOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CharacterActivity.class);
                startActivityForResult(intent,1001); //다른 액티비티를 띄우기 위한 요청코드(상수)
                finish();
            }
        });
        btn_nameSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CharacterActivity.class);
                startActivityForResult(intent,1001); //다른 액티비티를 띄우기 위한 요청코드(상수)
                finish();
            }
        });
    }
}
