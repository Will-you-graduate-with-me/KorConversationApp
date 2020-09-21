package com.cookandroid.korconversationapp;

import android.content.Context;
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
    String[] scrap, scrap_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.drive_scrap, container, false);

        recyclerView_scrap = (RecyclerView)v.findViewById(R.id.recyclerView_scrap);
        linearLayoutManager = new LinearLayoutManager(v.getContext());

        /* 구분선 필요없음
        recyclerView_scrap.addItemDecoration(
                new DividerItemDecoration(v.getContext(),linearLayoutManager.getOrientation()));*/

        recyclerView_scrap.setLayoutManager(linearLayoutManager);
        ScrapDecoration spaceDecoration = new ScrapDecoration(60);
        recyclerView_scrap.addItemDecoration(spaceDecoration);

        String sentenceInfo = "";

        mAuth = FirebaseAuth.getInstance();

        try {

            Map<String, String> params_scrapped = new HashMap<String, String>();
            params_scrapped.put("user_id", mAuth.getUid());
            System.out.println("유저아이디 : " + mAuth.getUid());

            Task TaskforScrap = new Task("selectUserScrappedScriptInfo", params_scrapped);

            sentenceInfo = TaskforScrap.execute(params_scrapped).get();
            System.out.println("문장정보 : " + sentenceInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            JSONArray jsonArrayScrap = new JSONArray(sentenceInfo);

            scrap = new String[jsonArrayScrap.length()];
            scrap_id = new String[jsonArrayScrap.length()];

            for (int i = 0; i < jsonArrayScrap.length(); i++) {
                JSONObject scrapped = (JSONObject) jsonArrayScrap.get(i);
                System.out.println("받아온 정보 : " + scrapped);

                scrap[i] = scrapped.get("sentence").toString();
                scrap_id[i] = scrapped.get("script_id").toString();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // ArrayList에 person 객체(이름과 번호) 넣기
        ArrayList<ScrapModel> scrapArray = new ArrayList<>();

        for (int i = 0; i < scrap.length; i = i+2) {
            scrapArray.add(new ScrapModel(scrap[i+1],scrap[i], scrap_id[i+1], scrap_id[i]));
        }
        // Adapter생성
        recyclerScrapAdapter = new RecyclerScrapAdapter(this,scrapArray);
        recyclerView_scrap.setAdapter(recyclerScrapAdapter);

        return v;
    }
}