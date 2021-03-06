package com.cookandroid.korconversationapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAnalysisAdapter extends RecyclerView.Adapter<RecyclerAnalysisAdapter.ViewHolder> {

    private ArrayList<AnalysisModel> sentence;
    private Activity activity;
    private AnalysisActivity ac;


    public RecyclerAnalysisAdapter(Activity activity, ArrayList<AnalysisModel> sentence) {
        this.sentence = sentence;
        this.activity = activity;
    }

    @Override
    public int getItemCount() {
        return sentence.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox analysis_check;
        TextView tv_scrap;

        public ViewHolder(View itemView) {
            super(itemView);
            analysis_check = (CheckBox) itemView.findViewById(R.id.analysis_check);
            tv_scrap = (TextView) itemView.findViewById(R.id.tv_scrap);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "click " +
                            sentence.get(getAdapterPosition()).getKor(), Toast.LENGTH_SHORT).show();
                }
            });*/

            analysis_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (analysis_check.isChecked()) {
                        System.out.println(sentence.get(getAdapterPosition()).getScript_id_kor());
                        System.out.println(sentence.get(getAdapterPosition()).getScript_id_eng());
                        ((AnalysisActivity)AnalysisActivity.Acontext).scriptArray.add(sentence.get(getAdapterPosition()).getScript_id_kor());
                        ((AnalysisActivity)AnalysisActivity.Acontext).scriptArray.add(sentence.get(getAdapterPosition()).getScript_id_eng());
                    } else {
                        for(int i=0; i<((AnalysisActivity)AnalysisActivity.Acontext).scriptArray.size(); i++) {
                            if(((AnalysisActivity)AnalysisActivity.Acontext).scriptArray.get(i).equals(sentence.get(getAdapterPosition()).getScript_id_kor())) {
                                // 한국어 삭제
                                System.out.println("체크취소 : " + ((AnalysisActivity)AnalysisActivity.Acontext).scriptArray.get(i));
                                ((AnalysisActivity) AnalysisActivity.Acontext).scriptArray.remove(i);
                                // 영어 삭제
                                System.out.println("체크취소 : " + ((AnalysisActivity)AnalysisActivity.Acontext).scriptArray.get(i));
                                ((AnalysisActivity) AnalysisActivity.Acontext).scriptArray.remove(i);

                            }
                            else
                                continue;
                        }
                    }
                }
            });

        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_analysis, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // 재활용 되는 View가 호출, Adapter가 해당 position에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        AnalysisModel data = sentence.get(position);

        // 데이터 결합
        holder.tv_scrap.setText(data.getKor());
    }


}
