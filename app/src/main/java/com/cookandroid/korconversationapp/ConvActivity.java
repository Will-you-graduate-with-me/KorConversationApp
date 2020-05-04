package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConvActivity extends AppCompatActivity {

    ImageButton back,speaker,restart,grade,check;
    Button repeat;
    TextView eng,kor,eng_script,kor_script;
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
        eng_script=(TextView)findViewById(R.id.eng_script);
        kor_script=(TextView)findViewById(R.id.kor_script);
        eng_script.setMovementMethod(new ScrollingMovementMethod());
        kor_script.setMovementMethod(new ScrollingMovementMethod());

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

        String scriptInfoKor="";
        String scriptInfoEng="";
        try{

            Map<String, String> userparams_forKor = new HashMap<String, String>();
            userparams_forKor.put("part_no",part_no);
            userparams_forKor.put("unit_no",unit_no);
            userparams_forKor.put("language","k");

            Map<String, String> userparams_forEng = new HashMap<String, String>();
            userparams_forEng.put("part_no",part_no);
            userparams_forEng.put("unit_no",unit_no);
            userparams_forEng.put("language","e");

            Task TaskforKor=new Task("selectScript",userparams_forKor);
            Task TaskforEng=new Task("selectScript",userparams_forEng);

            scriptInfoKor=TaskforKor.execute(userparams_forKor).get();
            scriptInfoEng=TaskforEng.execute(userparams_forEng).get();

        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            String scriptE="";
            String scriptK="";
            JSONArray jsonArrayKor=new JSONArray(scriptInfoKor);
            JSONArray jsonArrayEng=new JSONArray(scriptInfoEng);

            for(int i=0; i<jsonArrayKor.length(); i++) {
                JSONObject scriptKor=(JSONObject)jsonArrayKor.get(i);
                scriptK=scriptK+scriptKor.get("sentence").toString()+"\n";
            }

            for(int i=0; i<jsonArrayEng.length(); i++) {
                JSONObject scriptEng=(JSONObject)jsonArrayEng.get(i);
                scriptE=scriptE+scriptEng.get("sentence").toString()+"\n";

            }
            kor_script.setText(scriptK);
            eng_script.setText(scriptE);
        }catch (JSONException e) {
            e.printStackTrace();
        }

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