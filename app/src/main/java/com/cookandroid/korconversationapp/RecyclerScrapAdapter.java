package com.cookandroid.korconversationapp;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerScrapAdapter extends RecyclerView.Adapter<RecyclerScrapAdapter.ViewHolder> {

    private ArrayList<ScrapModel> scrap;
    private Fragment fragment;

    public RecyclerScrapAdapter(Fragment fragment, ArrayList<ScrapModel> scrap) {
        this.scrap = scrap;
        this.fragment = fragment;
    }

    @Override
    public int getItemCount() {
        return scrap.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView sentence_kor;
        TextView sentence_eng;

        public ViewHolder(View itemView) {
            super(itemView);
            sentence_kor = (TextView) itemView.findViewById(R.id.sentence_kor);
            sentence_eng = (TextView) itemView.findViewById(R.id.sentence_eng);

        }
    }

    public RecyclerScrapAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_scrap, null);
        RecyclerScrapAdapter.ViewHolder viewHolder = new RecyclerScrapAdapter.ViewHolder(view);
        return viewHolder;
    }

    // 재활용 되는 View가 호출, Adapter가 해당 position에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(final RecyclerScrapAdapter.ViewHolder holder, int position) {
        ScrapModel data = scrap.get(position);

        // 데이터 결합
        holder.sentence_kor.setText(data.getKor());
        holder.sentence_eng.setText(data.getEng());

    }
}