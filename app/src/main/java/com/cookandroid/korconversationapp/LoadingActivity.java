package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

public class LoadingActivity extends AppCompatActivity {
    ProgressBar progressBar;
    String str_eng, str_kor;
    Handler handler;
    int progress_percent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);

        Intent intent=new Intent(this.getIntent());
        str_eng=intent.getStringExtra("eng");
        str_kor=intent.getStringExtra("kor");

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
