package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URL;
import java.util.ArrayList;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<ConvModel> itemsList;
    private Fragment mfragment;
    View v;

    public SectionListDataAdapter(Fragment fragment, ArrayList<ConvModel> itemsList) {
        this.itemsList = itemsList;
        this.mfragment = fragment;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        //final ConvModel singleItem = itemsList.get(i);
        ConvModel singleItem = itemsList.get(i);
        //final String k=singleItem.getKor();

        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        final ConvModel singleItem = itemsList.get(i);
        String base_url="https://bucket-test-sy.s3.us-east-2.amazonaws.com/android_image/";

        String img_back_url=base_url+singleItem.getSituation_back()+".png";

        holder.when.setText(singleItem.getWhen());
        holder.eng.setText(singleItem.getEng());
        holder.kor.setText(singleItem.getKor());
        Glide.with(mfragment).load(img_back_url).centerCrop().into(holder.back);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(v.getContext(),LoadingActivity.class);
                intent.putExtra("kor",singleItem.getKor());
                intent.putExtra("eng",singleItem.getEng());
                intent.putExtra("part_no",singleItem.getPart_no());
                intent.putExtra("unit_no",singleItem.getUnit_no());
                mfragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView when;
        protected TextView kor;
        protected TextView eng;
        protected ImageView back;


        public SingleItemRowHolder(View view) {
            super(view);

            this.when = (TextView) view.findViewById(R.id.when);
            this.kor=(TextView)view.findViewById(R.id.kor);
            this.eng=(TextView)view.findViewById(R.id.eng);
            this.back=(ImageView)view.findViewById(R.id.unit_back);

        }

    }

}
