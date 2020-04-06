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

        // ArrayList에 person 객체(이름과 번호) 넣기
        ArrayList<AnalysisModel> sentence = new ArrayList<>();
        sentence.add(new AnalysisModel("안녕하세요."));
        sentence.add(new AnalysisModel("어떻게 지내세요."));
        sentence.add(new AnalysisModel("저는 잘 지냅니다."));
        sentence.add(new AnalysisModel("그럼 다음에 만나요."));

        // Adapter생성
        recyclerAnalysisAdapter = new RecyclerAnalysisAdapter(this,sentence);
        recyclerView.setAdapter(recyclerAnalysisAdapter);

        btn_RE = findViewById(R.id.btn_RE);
        btn_RE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AnalysisActivity.this, ConvActivity.class);
                startActivity(i);
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
