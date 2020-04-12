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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu1Fragment extends Fragment {
    ArrayList<SituationModel> allSampleData;
    ArrayList<RecommendModel> allRecommendData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.page_home, container, false);
        allSampleData=new ArrayList<SituationModel>();
        allRecommendData=new ArrayList<RecommendModel>();
        createData();
        recommendData();

        RecyclerView my_recycler_view = (RecyclerView)v.findViewById(R.id.my_recycler_view);
        my_recycler_view.setHasFixedSize(true);
        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(this, allSampleData, allRecommendData);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(adapter);

        return v;


    }

    //part 별 데이터
    public void createData() {

        String partInfo="";
        try{
            Task networkTask = new Task("part");
            //networkTask.setSubUrl("unit");
            Map<String, String> params = new HashMap<String, String>();
            partInfo=networkTask.execute(params).get();
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            JSONArray jsonArray=new JSONArray(partInfo);
            PartInfoVO[] partInfos = new PartInfoVO[jsonArray.length()];


            for(int i=0; i<jsonArray.length(); i++)
            {
                JSONObject partInfoObject=(JSONObject)jsonArray.get(i);
                System.out.println("객체"+partInfoObject);
                System.out.println(partInfoObject.get("part_name"));

                SituationModel situationModel = new SituationModel();

                situationModel.setSituation("PART "+(i+1)+". "+partInfoObject.get("part_name"));

                ArrayList<ConvModel> singleItem = new ArrayList<ConvModel>();
                singleItem.add(new ConvModel("종업원 부를 때", "img1", "여기 주문할게요" ,"Excuse me"));
                singleItem.add(new ConvModel("음식 주문할 때", "img2", "콜라 하나 주세요","I'd like the coke" ));
                singleItem.add(new ConvModel("종업원 부를 때", "img1", "여기 주문할게요" ,"Excuse me"));
                singleItem.add(new ConvModel("음식 주문할 때", "img2", "콜라 하나 주세요","I'd like the coke" ));
                singleItem.add(new ConvModel("종업원 부를 때", "img1", "여기 주문할게요" ,"Excuse me"));
                singleItem.add(new ConvModel("음식 주문할 때", "img2", "콜라 하나 주세요","I'd like the coke" ));
                situationModel.setAllItemsInSection(singleItem);

                allSampleData.add(situationModel);


            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
//         하드코딩(삭제하지 말것/유지용)
//        for (int i = 1; i <= 3; i++) {
//
//            SituationModel situationModel = new SituationModel();
//
//            situationModel.setSituation("Situation " + i);
//
//            ArrayList<ConvModel> singleItem = new ArrayList<ConvModel>();
//            singleItem.add(new ConvModel("종업원 부를 때", "img1", "여기 주문할게요" ,"Excuse me"));
//            singleItem.add(new ConvModel("음식 주문할 때", "img2", "콜라 하나 주세요","I'd like the coke" ));
//            singleItem.add(new ConvModel("종업원 부를 때", "img1", "여기 주문할게요" ,"Excuse me"));
//            singleItem.add(new ConvModel("음식 주문할 때", "img2", "콜라 하나 주세요","I'd like the coke" ));
//            singleItem.add(new ConvModel("종업원 부를 때", "img1", "여기 주문할게요" ,"Excuse me"));
//            singleItem.add(new ConvModel("음식 주문할 때", "img2", "콜라 하나 주세요","I'd like the coke" ));
//            situationModel.setAllItemsInSection(singleItem);
//
//            allSampleData.add(situationModel);
//
//        }
    }

    //추천 데이터
    public void recommendData() {

        RecommendModel recommendModel = new RecommendModel();

        ArrayList<ConvModel> singleItem_recommend = new ArrayList<ConvModel>();
        singleItem_recommend.add(new ConvModel("쇼핑 할 때", "img1", "이거 얼마에요?" ,"How much is it?"));
        singleItem_recommend.add(new ConvModel("감사 할 때", "img2", "고마워요","Thank you" ));
        singleItem_recommend.add(new ConvModel("쇼핑 할 때", "img1", "이거 얼마에요?" ,"How much is it?"));
        singleItem_recommend.add(new ConvModel("감사 할 때", "img2", "고마워요","Thank you" ));
        recommendModel.setRecommendItems(singleItem_recommend);

        allRecommendData.add(recommendModel);

    }
}