package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class UserInfoActivity extends AppCompatActivity {
    Button btn_infoOK;
    Spinner ageSpinner, genderSpinner;
    ArrayAdapter ageAdapter, genderAdapter;
    String user_id,nickname,gender,age;
    int character_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        Intent preIntent=getIntent();
        user_id=preIntent.getStringExtra("user_id");
        nickname=preIntent.getStringExtra("nickname");
        character_id=preIntent.getIntExtra("character_id",3);

        ageSpinner = (Spinner)findViewById(R.id.age_box);
        ageAdapter = ArrayAdapter.createFromResource(this,
                R.array.age, R.layout.spinner_item);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(ageAdapter);

        genderSpinner = (Spinner)findViewById(R.id.gender_box);
        genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender, R.layout.spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        age= ageSpinner.getSelectedItem().toString();
        gender=genderSpinner.getSelectedItem().toString();

        btn_infoOK=(Button)findViewById(R.id.btn_infoOK);
        btn_infoOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                intent.putExtra("character_id",character_id);
                intent.putExtra("nickname",nickname);
                intent.putExtra("user_id",user_id);
                intent.putExtra("gender",gender);
                intent.putExtra("age",age);

                startActivityForResult(intent,1001); //다른 액티비티를 띄우기 위한 요청코드(상수)
                finish();
            }
        });

    }
}
