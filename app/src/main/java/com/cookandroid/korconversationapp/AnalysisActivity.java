package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class AnalysisActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerAnalysisAdapter recyclerAnalysisAdapter;
    Button btn_OK, btn_RE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this,linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent i = getIntent();
        int length = i.getExtras().getInt("length");
        String[] kor = new String[length];

        for(int j=0; j<length; j++) {
            kor[j] = i.getExtras().getString("kor_"+j);
        }
        ArrayList<AnalysisModel> sentence = new ArrayList<>();

        for(int j=0; j<length; j++) {
            sentence.add(new AnalysisModel(kor[j]));
        }

        // Adapter생성
        recyclerAnalysisAdapter = new RecyclerAnalysisAdapter(this,sentence);
        recyclerView.setAdapter(recyclerAnalysisAdapter);

        btn_RE = findViewById(R.id.btn_RE);
        btn_RE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AnalysisActivity.this, ConvActivity.class);
                startActivity(i);
                finish();
            }
        });

        btn_OK = findViewById(R.id.btn_OK);
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
