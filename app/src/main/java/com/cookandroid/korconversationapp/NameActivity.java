package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class NameActivity extends AppCompatActivity {

    Button btn_nameOk;
    private FirebaseAuth mAuth ;
    EditText nameEdit;
    String user_id="";
    String nickname="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        Intent preIntent=getIntent();
        user_id=preIntent.getStringExtra("user_id");

        mAuth = FirebaseAuth.getInstance();
        nameEdit=(EditText)findViewById(R.id.nameEdit);
        btn_nameOk=(Button)findViewById(R.id.btn_nameOk);
        btn_nameOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( nameEdit.getText().toString().length() == 0 ) {

                } else {
                    Intent intent = new Intent(getApplicationContext(),CharacterActivity.class);
                    nickname=nameEdit.getText().toString();
                    intent.putExtra("nickname",nickname);
                    intent.putExtra("user_id",user_id);
                    startActivityForResult(intent,1001); //다른 액티비티를 띄우기 위한 요청코드(상수)
                    finish();


                }

            }
        });



    }
}
