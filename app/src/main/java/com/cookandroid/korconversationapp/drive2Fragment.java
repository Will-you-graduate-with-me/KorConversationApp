package com.cookandroid.korconversationapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class drive2Fragment extends Fragment {

    RecyclerView recyclerView_scrap;
    LinearLayoutManager linearLayoutManager;
    RecyclerScrapAdapter recyclerScrapAdapter;
    private FirebaseAuth mAuth ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.drive_scrap, container, false);

        recyclerView_scrap = (RecyclerView)v.findViewById(R.id.recyclerView_scrap);
        linearLayoutManager = new LinearLayoutManager(v.getContext());

        recyclerView_scrap.addItemDecoration(
                new DividerItemDecoration(v.getContext(),linearLayoutManager.getOrientation()));
        recyclerView_scrap.setLayoutManager(linearLayoutManager);
        ScrapDecoration spaceDecoration = new ScrapDecoration(90);
        recyclerView_scrap.addItemDecoration(spaceDecoration);

        String scrappedInfo = "";
        String RecommendInfo = "";
        String sentenceInfo = "";

        mAuth = FirebaseAuth.getInstance();

        try {

            Map<String, String> params_scrapped = new HashMap<String, String>();
            params_scrapped.put("user_id", mAuth.getUid());
            System.out.println("유저아이디 : " + mAuth.getUid());

            Task TaskforScrap = new Task("selectUserScrappedScript", params_scrapped);
            Task TaskforRecommend = new Task("selectUserRecommend", params_scrapped);
            Task TaskforSentence = new Task("selectUserScrappedScriptSentence", params_scrapped);


            /*
            scrappedInfo = TaskforScrap.execute(params_scrapped).get();
            System.out.println("스크랩정보 : " + scrappedInfo);

            RecommendInfo = TaskforRecommend.execute(params_scrapped).get();
            System.out.println("추천정보 : " + RecommendInfo);
            */

            sentenceInfo = TaskforSentence.execute(params_scrapped).get();
            System.out.println("문장정보 : " + sentenceInfo);


        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            JSONArray jsonArrayScrap = new JSONArray(sentenceInfo);

            String[] scrap;
            scrap = new String[jsonArrayScrap.length()];

            for (int i = 0; i < jsonArrayScrap.length(); i++) {
                JSONObject scrapped = (JSONObject) jsonArrayScrap.get(i);
                System.out.println("받아온 정보 : " + scrapped);

                scrap[i] = scrapped.get("sentence").toString();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // ArrayList에 person 객체(이름과 번호) 넣기
        ArrayList<ScrapModel> scrap = new ArrayList<>();
        scrap.add(new ScrapModel("안녕하세요","Hi"));
        scrap.add(new ScrapModel("오랜만이에요","Long time no see"));
        scrap.add(new ScrapModel("어떻게 지내요?","How are you?"));
        scrap.add(new ScrapModel("좋은 아침","Good morning"));

        // Adapter생성
        recyclerScrapAdapter = new RecyclerScrapAdapter(this,scrap);
        recyclerView_scrap.setAdapter(recyclerScrapAdapter);

        return v;
    }
}
