package com.cookandroid.korconversationapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnalysisActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerAnalysisAdapter recyclerAnalysisAdapter;
    Button btn_OK, btn_RE;
    TextView tv_grade;
    private FirebaseAuth mAuth ;
    public static Context Acontext;
    ArrayList<String> scriptArray = new ArrayList<>();
    String part_no, unit_no, grade;
    int all_count, wrong_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;

        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

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

        tv_grade = findViewById(R.id.grade);
        all_count = Integer.parseInt(i.getStringExtra("all_count"));
        wrong_count = Integer.parseInt(i.getStringExtra("wrong_count"));
        if(((all_count-wrong_count)/all_count)*100>=90)
            grade = "A";
        else if(((all_count-wrong_count)/all_count)*100>=80)
            grade = "B";
        else
            grade = "C";
        System.out.println("all_count : "+all_count+" / wrong_count : "+wrong_count+" / grade : "+grade);
        tv_grade.setText(grade);

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
                Intent i = new Intent(AnalysisActivity.this, LoadingActivity.class);
                i.putExtra("part_no",part_no);
                i.putExtra("unit_no",unit_no);
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
                    Map<String, String> updateparams = new HashMap<String, String>();

                    historyparams.put("user_id", mAuth.getUid());
                    historyparams.put("part_no", part_no);
                    historyparams.put("unit_no", unit_no);
                    historyparams.put("grade", grade);

                    // 업데이트 된 history count, recent_id를 바탕으로 새로운 interest_id 갱신
                    updateparams.put("user_id", mAuth.getUid());

                    System.out.println(mAuth.getUid()+"/"+ part_no + "/" + unit_no);
                    Task Taskforhistory = new Task("insertHistoryByCase", historyparams);
                    TaskForFlask TaskforUpdateInterestID = new TaskForFlask("cfRecommend", updateparams);

                    historyInfo = Taskforhistory.execute(historyparams).get();
                    TaskforUpdateInterestID.execute(updateparams);
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

                onResume();
                finish();
            }
        });
    }

}
