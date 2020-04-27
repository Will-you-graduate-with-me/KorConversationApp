package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ConvActivity extends AppCompatActivity {

    ImageButton back,speaker,restart,grade,check;
    Button repeat;
    TextView eng,kor;
    String str_eng, str_kor,part_no,unit_no;
    Handler hand;
    TextView part_unit_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);

        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        setContentView(R.layout.activity_conv);
        back=(ImageButton)findViewById(R.id.backbtn);
        speaker=(ImageButton)findViewById(R.id.btn_speak);
//        restart=(ImageButton)findViewById(R.id.btn_restart);
//        grade=(ImageButton)findViewById(R.id.btn_grade);
//        check=(ImageButton)findViewById(R.id.btn_check);
//        repeat=(Button)findViewById(R.id.btn_repeat);

        part_unit_no=(TextView)findViewById(R.id.part_unit_no);
        eng=(TextView)findViewById(R.id.eng);
        kor=(TextView)findViewById(R.id.kor);

        Intent intent=new Intent(this.getIntent());
        str_eng=intent.getStringExtra("eng");
        str_kor=intent.getStringExtra("kor");
        part_no=intent.getStringExtra("part_no");
        unit_no=intent.getStringExtra("unit_no");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hand.removeCallbacksAndMessages( null );
                finish();
            }
        });
        part_unit_no.setText("Part "+part_no+" > Unit "+unit_no);
        eng.setText(str_eng);
        kor.setText(str_kor);

//        hand = new Handler();
//
//        hand.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                Intent i = new Intent(ConvActivity.this, AnalysisActivity.class);
//                startActivity(i);
//                finish();
//            }
//        }, 3000);


        }
}