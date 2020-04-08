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

import java.util.ArrayList;

public class drive1Fragment extends Fragment {
    RecyclerView recyclerView_history;
    GridLayoutManager gridLayoutManager;
    RecyclerHistoryAdapter recyclerHistoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.drive_history, container, false);

        recyclerView_history = (RecyclerView)v.findViewById(R.id.recyclerView_history);
        gridLayoutManager = new GridLayoutManager(v.getContext(), 2);

        recyclerView_history.setLayoutManager(gridLayoutManager);
        ScrapDecoration spaceDecoration = new ScrapDecoration(60);
        recyclerView_history.addItemDecoration(spaceDecoration);


        // ArrayList에 person 객체(이름과 번호) 넣기
        ArrayList<HistoryModel> history = new ArrayList<>();
        history.add(new HistoryModel("Category1","Situation1"));
        history.add(new HistoryModel("Category2","Situation2"));
        history.add(new HistoryModel("Category3","Situation3"));
        history.add(new HistoryModel("Category4","Situation4"));
        history.add(new HistoryModel("Category5","Situation5"));
        history.add(new HistoryModel("Category6","Situation6"));

        // Adapter생성
        recyclerHistoryAdapter = new RecyclerHistoryAdapter(this,history);
        recyclerView_history.setAdapter(recyclerHistoryAdapter);

        return v;
    }
}
