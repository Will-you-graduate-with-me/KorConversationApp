package com.cookandroid.korconversationapp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;
    private ArrayList<SituationModel> dataList;
    private Fragment mfragment;
    private Context mContext;

    public RecyclerViewDataAdapter(Fragment fragment, ArrayList<SituationModel> dataList) {
        this.dataList = dataList;
        this.mfragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        View v;
        RecyclerView.ViewHolder holder;

        if (viewType == TYPE_HEADER) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.page_home_header, viewGroup, false);
            holder = new HeaderViewHolder(v);
        } else {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
            holder = new ItemRowHolder(v);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        } else {
            ItemRowHolder itemRowHolder = (ItemRowHolder) holder;


            final String sectionName = dataList.get(i-1).getSituation();

            ArrayList singleSectionItems = dataList.get(i-1).getAllItemsInSection();

            itemRowHolder.itemTitle.setText(sectionName);

            SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mfragment, singleSectionItems);

            itemRowHolder.recycler_view_list.setHasFixedSize(true);
            itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);

        }

    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;
        protected RecyclerView recycler_view_list;
        protected Button btnMore;


        public ItemRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);


        }

    }
    class HeaderViewHolder extends RecyclerView.ViewHolder {

        HeaderViewHolder(View headerView) {
            super(headerView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

}

