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

import java.util.ArrayList;

public class drive2Fragment extends Fragment {

    RecyclerView recyclerView_scrap;
    LinearLayoutManager linearLayoutManager;
    RecyclerScrapAdapter recyclerScrapAdapter;

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
