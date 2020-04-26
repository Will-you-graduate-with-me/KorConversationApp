package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class UserInfoActivity extends AppCompatActivity {
    Button btn_infoOK;
    String user_id,nickname;
    int stay_duration,age, character_id;
    EditText age_edit,stay_duration_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        Intent preIntent=getIntent();
        user_id=preIntent.getStringExtra("user_id");
        nickname=preIntent.getStringExtra("nickname");
        character_id=preIntent.getIntExtra("character_id",3);


        age_edit=(EditText)findViewById(R.id.age_edit);
        stay_duration_edit=(EditText)findViewById(R.id.stay_duration_edit);
        btn_infoOK=(Button)findViewById(R.id.btn_infoOK);
        btn_infoOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if ( age_edit.getText().length() == 0 ) {

                } else {
                    age=Integer.parseInt(age_edit.getText().toString());
                }
                if ( stay_duration_edit.getText().length() == 0 ) {

                } else {
                    stay_duration=Integer.parseInt(stay_duration_edit.getText().toString());
                }
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                intent.putExtra("character_id",character_id);
                intent.putExtra("nickname",nickname);
                intent.putExtra("user_id",user_id);
                intent.putExtra("stay_duration",stay_duration);
                intent.putExtra("age",age);

                startActivityForResult(intent,1001); //다른 액티비티를 띄우기 위한 요청코드(상수)
                finish();
            }
        });

    }
}
