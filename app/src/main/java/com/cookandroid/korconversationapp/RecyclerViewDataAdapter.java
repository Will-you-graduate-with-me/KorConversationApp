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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_HEADER = 0;
    private final int TYPE_RECOMMEND = 1;
    private final int TYPE_ITEM = 2;

    private ArrayList<SituationModel> dataList;
    private ArrayList<RecommendModel> recommendList;
    private Fragment mfragment;
    private Context mContext;

    public RecyclerViewDataAdapter(Fragment fragment, ArrayList<SituationModel> dataList, ArrayList<RecommendModel> recommendList) {
        this.dataList = dataList;
        this.mfragment = fragment;
        this.recommendList = recommendList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        View v;
        RecyclerView.ViewHolder holder;

        if (viewType == TYPE_HEADER) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.page_home_header, viewGroup, false);
            holder = new HeaderViewHolder(v);
        } else if (viewType == TYPE_RECOMMEND) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_recommend, null);
            holder = new RecommendRowHolder(v);
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
        } else if (holder instanceof RecommendRowHolder) {
            RecommendRowHolder recommendRowHolder = (RecommendRowHolder) holder;

            ArrayList singleSectionItems = recommendList.get(i-1).getRecommendItems();

            RecommendListDataAdapter recommendListDataAdapter = new RecommendListDataAdapter(mfragment, singleSectionItems);

            recommendRowHolder.recycler_view_recommend.setHasFixedSize(true);
            recommendRowHolder.recycler_view_recommend.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            recommendRowHolder.recycler_view_recommend.setAdapter(recommendListDataAdapter);

        } else {
            ItemRowHolder itemRowHolder = (ItemRowHolder) holder;

            final String sectionName = dataList.get(i-2).getSituation();

            ArrayList singleSectionItems = dataList.get(i-2).getAllItemsInSection();

            itemRowHolder.itemTitle.setText(sectionName);

            SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mfragment, singleSectionItems);

            itemRowHolder.recycler_view_list.setHasFixedSize(true);
            itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);

        }

    }

    @Override
    public int getItemCount() {
        return dataList.size() + recommendList.size() +1;
    }

    public class RecommendRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;
        protected RecyclerView recycler_view_recommend;


        public RecommendRowHolder(View view) {
            super(view);

            this.recycler_view_recommend = (RecyclerView) view.findViewById(R.id.recycler_view_recommend);

        }

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

        protected TextView today_script_kor,today_script_eng;

        HeaderViewHolder(View headerView) {

            super(headerView);
            this.today_script_eng=(TextView) headerView.findViewById(R.id.today_script_eng);
            this.today_script_kor=(TextView)headerView.findViewById(R.id.today_script_kor);


            String todayScriptInfo="";
            try{
                Task networkTask = new Task("today_script");
                Map<String, String> params = new HashMap<String, String>();
                todayScriptInfo=networkTask.execute(params).get();


            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                JSONArray jsonArray=new JSONArray(todayScriptInfo);
                SimpleDateFormat format = new SimpleDateFormat("dd");
                Date time=new Date();
                String todaytime=format.format(time);

                int random_id=0;
                int last=Integer.parseInt(todaytime.substring(todaytime.length()-1));
                random_id=last+1;

                for(int i=0; i<jsonArray.length(); i++)
                {
                    JSONObject todayScriptObject=(JSONObject)jsonArray.get(i);
                    if(Integer.parseInt(todayScriptObject.get("today_id").toString())==random_id){
                        today_script_kor.setText(todayScriptObject.get("today_script_kor").toString());
                        today_script_eng.setText(todayScriptObject.get("today_script_eng").toString());
                }

                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else if (position == 1)
            return TYPE_RECOMMEND;
        else
            return TYPE_ITEM;
    }

}

