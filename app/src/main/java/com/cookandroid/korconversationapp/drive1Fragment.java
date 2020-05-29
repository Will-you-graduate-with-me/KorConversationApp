package com.cookandroid.korconversationapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
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

public class drive1Fragment extends Fragment {
    RecyclerView recyclerView_history;
    GridLayoutManager gridLayoutManager;
    RecyclerHistoryAdapter recyclerHistoryAdapter;
    private FirebaseAuth mAuth ;
    ArrayList<HistoryModel> historyArray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.drive_history, container, false);

        recyclerView_history = (RecyclerView)v.findViewById(R.id.recyclerView_history);
        gridLayoutManager = new GridLayoutManager(v.getContext(), 2);

        recyclerView_history.setLayoutManager(gridLayoutManager);
        ScrapDecoration spaceDecoration = new ScrapDecoration(60);
        recyclerView_history.addItemDecoration(spaceDecoration);

        mAuth = FirebaseAuth.getInstance();
        String historyInfo = "";

        try {
            Map<String, String> shistoryparams = new HashMap<String, String>();
            shistoryparams.put("user_id", mAuth.getUid());

            Task Taskforshistory = new Task("selectUserHistory", shistoryparams);

            historyInfo = Taskforshistory.execute(shistoryparams).get();
            System.out.println("Taskforshistory : " + historyInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            JSONArray jsonArrayHistory = new JSONArray(historyInfo);
            historyArray = new ArrayList<>();

            String part_name, unit_name, part_no, unit_no, situation_back;

            for (int i = 0; i < jsonArrayHistory.length(); i++) {
                JSONObject scrapped = (JSONObject) jsonArrayHistory.get(i);
                System.out.println("받아온 정보 : " + scrapped);

                part_name = scrapped.get("part_name").toString();
                unit_name = scrapped.get("unit_name").toString();
                part_no = scrapped.get("part_no").toString();
                unit_no = scrapped.get("unit_no").toString();
                situation_back = scrapped.get("situation_back").toString();

                // ArrayList에 person 객체(이름과 번호) 넣기
                historyArray.add(new HistoryModel(part_name, unit_name, part_no, unit_no, situation_back));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Adapter생성
        recyclerHistoryAdapter = new RecyclerHistoryAdapter(this,historyArray);
        recyclerView_history.setAdapter(recyclerHistoryAdapter);

        return v;
    }
}
