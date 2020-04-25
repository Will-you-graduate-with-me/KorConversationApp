package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NameChangeActivity  extends AppCompatActivity {

    Button btn_nameChange;
    private FirebaseAuth mAuth ;
    EditText nameChangeEdit;
    String user_id="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_change);
        Intent preIntent=getIntent();
        user_id=preIntent.getStringExtra("user_id");

        mAuth = FirebaseAuth.getInstance();
        nameChangeEdit=(EditText)findViewById(R.id.nameChangeEdit);
        btn_nameChange=(Button)findViewById(R.id.btn_nameChangeOk);

        String UserInfo="";


//        try{
//            JSONObject jsonvar = new JSONObject(UserInfo);
//            point1.setText(jsonvar.get("nickname").toString());
//
//        }catch (JSONException e) {
//            e.printStackTrace();
//        }


        btn_nameChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( nameChangeEdit.getText().toString().length() == 0 ) {

                } else {
                    try{
                        //User정보 가져오기
                        Map<String, String> userparams = new HashMap<String, String>();
                        userparams.put("user_id",mAuth.getUid());
                        userparams.put("nickname",nameChangeEdit.getText().toString());

                        Task Task=new Task("updateNickname",userparams);
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
