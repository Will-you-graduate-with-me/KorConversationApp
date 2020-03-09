package com.cookandroid.korconversationapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Menu1Fragment extends Fragment {
    ArrayList<SituationModel> allSampleData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.page_home, container, false);
        allSampleData=new ArrayList<SituationModel>();
        createDummyData();

        RecyclerView my_recycler_view = (RecyclerView)v.findViewById(R.id.my_recycler_view);
        my_recycler_view.setHasFixedSize(true);
        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(this, allSampleData);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(adapter);

        return v;


    }

    public void createDummyData() {
        for (int i = 1; i <= 3; i++) {

            SituationModel situationModel = new SituationModel();

            situationModel.setSituation("Situation " + i);

            ArrayList<ConvModel> singleItem = new ArrayList<ConvModel>();
            singleItem.add(new ConvModel("종업원 부를 때", "img1", "Excuse me","여기 주문할게요" ));
            singleItem.add(new ConvModel("음식 주문할 때", "img2", "I'd like the coke","콜라 하나 주세요" ));
            singleItem.add(new ConvModel("종업원 부를 때", "img1", "Excuse me","여기 주문할게요" ));
            singleItem.add(new ConvModel("음식 주문할 때", "img2", "I'd like the coke","콜라 하나 주세요" ));
            singleItem.add(new ConvModel("종업원 부를 때", "img1", "Excuse me","여기 주문할게요" ));
            singleItem.add(new ConvModel("음식 주문할 때", "img2", "I'd like the coke","콜라 하나 주세요" ));

            situationModel.setAllItemsInSection(singleItem);

            allSampleData.add(situationModel);

        }
    }
}