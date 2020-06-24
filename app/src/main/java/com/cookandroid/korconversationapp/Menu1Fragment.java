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

import com.google.firebase.auth.FirebaseAuth;
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

    String UserRecommendInfo="";
    private FirebaseAuth mAuth ;
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

        // adapter.notifyDataSetChanged();

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        //View v=inflate(R.layout.page_home, container, false);

        allSampleData=new ArrayList<SituationModel>();
        allRecommendData=new ArrayList<RecommendModel>();

        createData();
        recommendData();

        RecyclerView my_recycler_view = (RecyclerView) getView().findViewById(R.id.my_recycler_view);
        my_recycler_view.setHasFixedSize(true);
        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(this, allSampleData, allRecommendData);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(getView().getContext(), LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(adapter);
    }

    //part 별 데이터, unit 별 데이터
    public void createData() {

        String partInfo="";
        String partialUnitInfo="";
        try{
            Task networkTask = new Task("part");
            Task networkTask2 = new Task("unit");

            //networkTask.setSubUrl("unit");
            Map<String, String> params = new HashMap<String, String>();
            partInfo=networkTask.execute(params).get();
            partialUnitInfo=networkTask2.execute(params).get();
            System.out.println("partInfo : " + partInfo);
            System.out.println("partialUnitInfo : " + partialUnitInfo);

//            //insert
//            Map<String, String> userparams = new HashMap<String, String>();
//            userparams.put("user_id","abcdefg123");
//            Task insertTask=new Task("insertUserID",userparams);
//            insertTask.execute(userparams);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            JSONArray jsonArray=new JSONArray(partInfo);
            JSONArray jsonArray2=new JSONArray(partialUnitInfo);

            for(int i=0; i<jsonArray.length(); i++)
            {
                JSONObject partInfoObject=(JSONObject)jsonArray.get(i);

                SituationModel situationModel = new SituationModel();

                situationModel.setSituation("PART "+(i+1)+". "+partInfoObject.get("part_name"));
                ArrayList<ConvModel> singleItem = new ArrayList<ConvModel>();

                for(int j=0;j<jsonArray2.length();j++){
                    JSONObject partialUnitInfoObj=(JSONObject)jsonArray2.get(j);
                    if(Integer.parseInt(partialUnitInfoObj.get("part_no").toString())==(i+1)){
                        singleItem.add(new ConvModel(partialUnitInfoObj.get("unit_name").toString(), partialUnitInfoObj.get("main_sentence_kor").toString() ,
                                partialUnitInfoObj.get("main_sentence_eng").toString(),partialUnitInfoObj.get("part_no").toString(),partialUnitInfoObj.get("unit_no").toString(),partialUnitInfoObj.get("situation_back").toString()));

                    }
                }

                situationModel.setAllItemsInSection(singleItem);
                allSampleData.add(situationModel);

            }
        } catch (JSONException e) {
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

        try{
            //User정보 가져오기
            mAuth = FirebaseAuth.getInstance();

            Map<String, String> userparams = new HashMap<String, String>();
            userparams.put("user_id",mAuth.getUid());
            Task networkTask=new Task("selectUserRecommend",userparams);
            UserRecommendInfo=networkTask.execute(userparams).get();

        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            JSONArray jsonArray=new JSONArray(UserRecommendInfo);

            RecommendModel recommendModel = new RecommendModel();
            ArrayList<ConvModel> singleItem_recommend = new ArrayList<ConvModel>();

            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject recommendObject=(JSONObject)jsonArray.get(i);

                singleItem_recommend.add(new ConvModel(recommendObject.get("unit_name").toString(),
                     recommendObject.get("main_sentence_kor").toString(),
                     recommendObject.get("main_sentence_eng").toString(),
                     recommendObject.get("part_no").toString(),
                     recommendObject.get("unit_no").toString(),
                     recommendObject.get("situation_back").toString()));
            }

            recommendModel.setRecommendItems(singleItem_recommend);
            allRecommendData.add(recommendModel);

        }catch (JSONException e) {
            e.printStackTrace();
        }

    }
}