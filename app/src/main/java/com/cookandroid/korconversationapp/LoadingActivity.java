package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoadingActivity extends AppCompatActivity {
    ProgressBar progressBar;
    String str_eng, str_kor,part_no,unit_no, loading_script, loading_explanation;
    Handler handler;
    int progress_percent;
    TextView text1, text2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);

        Intent intent=new Intent(this.getIntent());
        str_eng=intent.getStringExtra("eng");
        str_kor=intent.getStringExtra("kor");
        part_no=intent.getStringExtra("part_no");
        unit_no=intent.getStringExtra("unit_no");

        // 로딩 데이터
        String loadingInfo="";
        try{
            Task networkTask = new Task("loading_script");
            Map<String, String> params = new HashMap<String, String>();
            loadingInfo=networkTask.execute(params).get();
            System.out.println(loadingInfo);

        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            JSONArray jsonArray=new JSONArray(loadingInfo);

            int random_id = (int)(Math.random()*10)+1;

            for(int j=0; j<jsonArray.length(); j++)
            {
                JSONObject loadingScriptObject=(JSONObject)jsonArray.get(j);
                if(Integer.parseInt(loadingScriptObject.get("loading_id").toString())==random_id){

                    loading_script = loadingScriptObject.get("loading_script").toString();
                    loading_explanation = loadingScriptObject.get("loading_explanation").toString();

                }

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text1.setText("#"+loading_script);
        text2.setText(loading_explanation);

        new Thread() {
            public void run() {
                while(true) {
                    try {
                        while(!Thread.currentThread().isInterrupted()) {
                            progress_percent += 25;
                            Thread.sleep(1000);
                            progressBar.setProgress(progress_percent);

                            if(progress_percent >= 100) {
                                Intent new_intent = new Intent(LoadingActivity.this, ConvActivity.class);
                                new_intent.putExtra("kor",str_kor);
                                new_intent.putExtra("eng",str_eng);
                                new_intent.putExtra("part_no",part_no);
                                new_intent.putExtra("unit_no",unit_no);
                                startActivity(new_intent);
                                finish();
                                currentThread().interrupt();
                            }
                        }
                    } catch (Throwable t) {

                    } finally {

                    }
                }
            }
        }.start();


    }

}
