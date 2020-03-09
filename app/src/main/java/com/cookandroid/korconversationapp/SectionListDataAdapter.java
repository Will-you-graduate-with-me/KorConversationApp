package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<ConvModel> itemsList;
    private Fragment mfragment;

    public SectionListDataAdapter(Fragment fragment, ArrayList<ConvModel> itemsList) {
        this.itemsList = itemsList;
        this.mfragment = fragment;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(v.getContext(),ConvActivity.class);
                mfragment.startActivity(intent);
            }
        });
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        ConvModel singleItem = itemsList.get(i);

        holder.when.setText(singleItem.getWhen());
        holder.eng.setText(singleItem.getEng());
        holder.kor.setText(singleItem.getKor());

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView when;
        protected TextView kor;
        protected TextView eng;


        public SingleItemRowHolder(View view) {
            super(view);

            this.when = (TextView) view.findViewById(R.id.when);
            this.kor=(TextView)view.findViewById(R.id.kor);
            this.eng=(TextView)view.findViewById(R.id.eng);


        }

    }

}
