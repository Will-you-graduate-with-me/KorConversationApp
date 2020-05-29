package com.cookandroid.korconversationapp;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerHistoryAdapter extends RecyclerView.Adapter<RecyclerHistoryAdapter.ViewHolder> {

    private ArrayList<HistoryModel> history;
    private Fragment fragment;
    View view;

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
        ImageView back;

        public ViewHolder(View itemView) {
            super(itemView);
            category = (TextView) itemView.findViewById(R.id.category);
            situation = (TextView) itemView.findViewById(R.id.situation);
            back = (ImageView) itemView.findViewById(R.id.back);
        }
    }

    public RecyclerHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, null);
        RecyclerHistoryAdapter.ViewHolder viewHolder = new RecyclerHistoryAdapter.ViewHolder(view);
        return viewHolder;
    }

    // 재활용 되는 View가 호출, Adapter가 해당 position에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(final RecyclerHistoryAdapter.ViewHolder holder, int position) {
        final HistoryModel data = history.get(position);

        String base_url="https://bucket-test-sy.s3.us-east-2.amazonaws.com/android_image/";

        String img_back_url=base_url+data.getSituation_back()+".png";

        // 데이터 결합
        holder.category.setText("PART " + data.getCategory() +". "+data.getPart_name());
        holder.situation.setText(data.getUnit_name());
        Glide.with(fragment).load(img_back_url).centerCrop().into(holder.back);



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(),LoadingActivity.class);
                intent.putExtra("kor","이건 뭐지");
                intent.putExtra("eng","나중에 정리해야겠다");
                intent.putExtra("part_no",data.getCategory());
                System.out.println("part_no : " + data.getCategory());
                intent.putExtra("unit_no",data.getSituation());
                System.out.println("unit_no : " + data.getSituation());

                fragment.startActivity(intent);

            }
        });

    }
}