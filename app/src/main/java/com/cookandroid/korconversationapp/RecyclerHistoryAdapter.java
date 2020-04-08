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

public class RecyclerHistoryAdapter extends RecyclerView.Adapter<RecyclerHistoryAdapter.ViewHolder> {

    private ArrayList<HistoryModel> history;
    private Fragment fragment;

    public RecyclerHistoryAdapter(Fragment fragment, ArrayList<HistoryModel> history) {
        this.history = history;
        this.fragment = fragment;
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView category;
        TextView situation;

        public ViewHolder(View itemView) {
            super(itemView);
            category = (TextView) itemView.findViewById(R.id.category);
            situation = (TextView) itemView.findViewById(R.id.situation);

        }
    }

    public RecyclerHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, null);
        RecyclerHistoryAdapter.ViewHolder viewHolder = new RecyclerHistoryAdapter.ViewHolder(view);
        return viewHolder;
    }

    // 재활용 되는 View가 호출, Adapter가 해당 position에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(final RecyclerHistoryAdapter.ViewHolder holder, int position) {
        HistoryModel data = history.get(position);

        // 데이터 결합
        holder.category.setText(data.getCategory());
        holder.situation.setText(data.getSituation());

    }
}