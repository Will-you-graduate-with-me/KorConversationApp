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
    int caseCount=0;
    int caseNumber=-1;
    JSONArray jsonArrayKor;
    String[] scriptK;
    String[] scriptE;
    int num=1;


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
        back = (ImageButton) findViewById(R.id.backbtn);
        speaker = (ImageButton) findViewById(R.id.btn_speak);
//        restart=(ImageButton)findViewById(R.id.btn_restart);
//        grade=(ImageButton)findViewById(R.id.btn_grade);
//        check=(ImageButton)findViewById(R.id.btn_check);
//        repeat=(Button)findViewById(R.id.btn_repeat);

        part_unit_no = (TextView) findViewById(R.id.part_unit_no);
        eng = (TextView) findViewById(R.id.eng);
        kor = (TextView) findViewById(R.id.kor);
        eng_script = (TextView) findViewById(R.id.eng_script);
        kor_script = (TextView) findViewById(R.id.kor_script);
        eng_script.setMovementMethod(new ScrollingMovementMethod());
        kor_script.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = new Intent(this.getIntent());
        str_eng = intent.getStringExtra("eng");
        str_kor = intent.getStringExtra("kor");
        part_no = intent.getStringExtra("part_no");
        unit_no = intent.getStringExtra("unit_no");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hand.removeCallbacksAndMessages( null );
                finish();
            }
        });
        part_unit_no.setText("Part " + part_no + " > Unit " + unit_no);

        String scriptInfoKor = "";
        String scriptInfoEng = "";
        try {

            Map<String, String> userparams_forKor = new HashMap<String, String>();
            userparams_forKor.put("part_no", part_no);
            userparams_forKor.put("unit_no", unit_no);
            userparams_forKor.put("language", "k");

            Map<String, String> userparams_forEng = new HashMap<String, String>();
            userparams_forEng.put("part_no", part_no);
            userparams_forEng.put("unit_no", unit_no);
            userparams_forEng.put("language", "e");

            Task TaskforKor = new Task("selectScript", userparams_forKor);
            Task TaskforEng = new Task("selectScript", userparams_forEng);

            scriptInfoKor = TaskforKor.execute(userparams_forKor).get();
            scriptInfoEng = TaskforEng.execute(userparams_forEng).get();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String sentence_no1 = "";
            String sentence_no2 = "";
            String sentence_no_final = "";
            jsonArrayKor = new JSONArray(scriptInfoKor);
            JSONArray jsonArrayEng = new JSONArray(scriptInfoEng);
            scriptK = new String[jsonArrayKor.length()];
            scriptE = new String[jsonArrayEng.length()];

            for (int i = 0; i < jsonArrayKor.length(); i++) {
                JSONObject scriptKor = (JSONObject) jsonArrayKor.get(i);
                scriptK[i] = scriptKor.get("sentence").toString();
            }

            for (int i = 0; i < jsonArrayEng.length(); i++) {
                JSONObject scriptEng = (JSONObject) jsonArrayEng.get(i);
                scriptE[i] = scriptEng.get("sentence").toString();
            }

            kor.setText(scriptK[0]);
            eng.setText(scriptE[0]);

            // case 개수 알아내기
            for (int i = 0; i < jsonArrayKor.length(); i++) {
                JSONObject scriptEng = (JSONObject) jsonArrayKor.get(i);
                sentence_no1 = scriptEng.get("sentence_no").toString();
                if (sentence_no1.equals(sentence_no2)) {
                    sentence_no_final = sentence_no2;
                    caseNumber = i-1;
                    for (int j = 0; j < jsonArrayKor.length(); j++) {
                        JSONObject scriptEng2 = (JSONObject) jsonArrayKor.get(j);
                        sentence_no1 = scriptEng2.get("sentence_no").toString();
                        if (sentence_no1.equals(sentence_no_final))
                            caseCount++;
                        else
                            continue;
                    }
                } else {
                    sentence_no2 = sentence_no1;
                }

        }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 2초마다 다른 문장으로 넘어감
        hand = new Handler();

        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (num<jsonArrayKor.length()-1) {
                    hand.postDelayed(this,2000);
                } else {
                    hand.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(ConvActivity.this, AnalysisActivity.class);
                            i.putExtra("length", jsonArrayKor.length());
                            for(int j=0; j<jsonArrayKor.length(); j++) {
                                i.putExtra("kor_" + j, scriptK[j]);
                            }
                            startActivity(i);
                            finish();
                        }
                    }, 2000);

                }
                if(num != caseNumber) {
                    kor.setText(scriptK[num]);
                    eng.setText(scriptE[num]);
                    num++;
                } else {
                    kor.setText("");
                    eng.setText("");
                    for(int i=num; i<num+caseCount; i++) {
                        kor.append(scriptK[i]+"\n");
                        eng.append(scriptE[i]+"\n");
                    }
                    num = num+caseCount;
                }
            }
        }, 2000);


    }

}