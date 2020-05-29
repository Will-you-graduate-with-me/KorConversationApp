package com.cookandroid.korconversationapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnalysisActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerAnalysisAdapter recyclerAnalysisAdapter;
    Button btn_OK, btn_RE;
    private FirebaseAuth mAuth ;
    public static Context Acontext;
    ArrayList<String> scriptArray = new ArrayList<>();
    String part_no, unit_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        Acontext = this;

        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this,linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent i = getIntent();
        int length = i.getExtras().getInt("length");
        String[] kor = new String[length];
        String[] script_id_kor = new String[length];
        String[] script_id_eng = new String[length];

        for(int j=0; j<length; j++) {
            kor[j] = i.getExtras().getString("kor_"+j);
            script_id_kor[j] = i.getExtras().getString("scriptidkor_"+j);
            script_id_eng[j] = i.getExtras().getString("scriptideng_"+j);
        }
        part_no = i.getStringExtra("part_no");
        unit_no = i.getStringExtra("unit_no");
        ArrayList<AnalysisModel> sentence = new ArrayList<>();

        for(int j=0; j<length; j++) {
            sentence.add(new AnalysisModel(kor[j], script_id_kor[j], script_id_eng[j]));
            System.out.println(script_id_kor[j] + " / " + script_id_eng[j]);
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

                mAuth = FirebaseAuth.getInstance();
                String historyInfo = "";

                try {
                    Map<String, String> historyparams = new HashMap<String, String>();
                    Map<String, String> historyparams2 = new HashMap<String, String>();

                    historyparams.put("user_id", mAuth.getUid());
                    historyparams.put("part_no", part_no);
                    historyparams.put("unit_no", unit_no);
                    historyparams.put("grade", "B");

                    historyparams2.put("user_id", mAuth.getUid());
                    historyparams2.put("part_no", part_no);
                    historyparams2.put("unit_no", unit_no);

                    System.out.println(mAuth.getUid()+"/"+ part_no + "/" + unit_no);
                    Task Taskforhistory = new Task("insertHistoryByCase", historyparams);
                    Task Taskforhistory2 = new Task("updateRecentID", historyparams2);

                    historyInfo = Taskforhistory.execute(historyparams).get();
                    Taskforhistory2.execute(historyparams2).get();
                    System.out.println("historyInfo : " + historyInfo);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for(int i=0; i<scriptArray.size(); i++) {
                    System.out.println("출력:" + scriptArray.get(i));

                    try {

                        Map<String, String> scriptparams = new HashMap<String, String>();
                        scriptparams.put("user_id", mAuth.getUid());
                        scriptparams.put("script_id", scriptArray.get(i));


                        Task Taskforscript = new Task("insertScrappedScript", scriptparams);

                        Taskforscript.execute(scriptparams).get();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                finish();
            }
        });
    }

}
