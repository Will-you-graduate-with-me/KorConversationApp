package com.cookandroid.korconversationapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class drive3Fragment extends Fragment {

    PieChart pieChart;
    private FirebaseAuth mAuth ;
    float gradeA=0,gradeB=0,gradeC=0;
    float percentA=0,percentB=0,percentC=0;
    int part=0,unit=0;
    TextView tv,tv_result;
    String[] unit_no, part_no;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drive_analysis, container, false);
        pieChart = (PieChart)v.findViewById(R.id.piechart);
        mAuth = FirebaseAuth.getInstance();
        tv=(TextView)v.findViewById(R.id.text2);
        tv_result=(TextView)v.findViewById(R.id.tv_result);

        String lowGrade="";

        try {

            Map<String, String> params_scrapped = new HashMap<String, String>();
            params_scrapped.put("user_id", mAuth.getUid());

            Task TaskforGrade = new Task("selectLowGraded", params_scrapped);

            lowGrade = TaskforGrade.execute(params_scrapped).get();

        } catch (Exception e) {
            e.printStackTrace();
        }
        String part_unit_no="";
        try {

            JSONArray jsonArrayGradeInfo = new JSONArray(lowGrade);

            unit_no = new String[jsonArrayGradeInfo.length()];
            part_no = new String[jsonArrayGradeInfo.length()];

            JSONObject gradeInfo2 = (JSONObject) jsonArrayGradeInfo.get(0);


            for (int i = 0; i < jsonArrayGradeInfo.length(); i++) {
                JSONObject gradeInfo = (JSONObject) jsonArrayGradeInfo.get(i);
                part_no[i] = gradeInfo.get("part_no").toString();
                unit_no[i] = gradeInfo.get("unit_no").toString();
                part_unit_no+=" PART "+part_no[i]+" - UNIT "+unit_no[i] +"\n";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(lowGrade.equals("[]") ){
            tv.setText("없습니다.");
        }else{
            tv.setText(part_unit_no);
        }


        String lowPart="";
        try {

            Map<String, String> param = new HashMap<String, String>();
            param.put("user_id", mAuth.getUid());

            Task TaskforLowHistoryPart = new Task("selectLowHistory", param);

            lowPart = TaskforLowHistoryPart.execute(param).get();

        } catch (Exception e) {
            e.printStackTrace();
        }
        String a="";
        try {

            JSONArray jsonArrayLowPart = new JSONArray(lowPart);


            for (int i = 0; i < jsonArrayLowPart.length(); i++) {
                JSONObject gradeInfo = (JSONObject) jsonArrayLowPart.get(i);

                a = gradeInfo.get("part_no").toString();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        tv_result.setText("PART "+ a+"입니다.");

        //회원별 등급개수
        try{
            Task networkTask_A = new Task("countGrade");
            Map<String, String> params_B = new HashMap<String, String>();
            params_B.put("user_id", mAuth.getUid());
            params_B.put("grade","A");
            gradeA=(float)Integer.parseInt(networkTask_A.execute(params_B).get());
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Task networkTask_B = new Task("countGrade");
            Map<String, String> params_B = new HashMap<String, String>();
            params_B.put("user_id", mAuth.getUid());
            params_B.put("grade","B");
            gradeB=(float)Integer.parseInt(networkTask_B.execute(params_B).get());
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Task networkTask_C = new Task("countGrade");
            Map<String, String> params_C = new HashMap<String, String>();
            params_C.put("user_id", mAuth.getUid());
            params_C.put("grade","C");
            gradeC=(float)Integer.parseInt(networkTask_C.execute(params_C).get());
        }catch (Exception e){
            e.printStackTrace();
        }

        //PieChart
        percentA=gradeA/(gradeA+gradeB+gradeC)*100;
        percentB=gradeB/(gradeA+gradeB+gradeC)*100;
        percentC=gradeC/(gradeA+gradeB+gradeC)*100;

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        if(percentA!=0)
            yValues.add(new PieEntry(percentA,"A"));

        if(percentB!=0)
            yValues.add(new PieEntry(percentB,"B"));

        if(percentC!=0)
            yValues.add(new PieEntry(percentC,"C"));

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"grade");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);

        return v;
    }
}
