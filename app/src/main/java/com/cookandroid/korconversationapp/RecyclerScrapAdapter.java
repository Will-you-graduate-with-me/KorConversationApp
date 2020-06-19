package com.cookandroid.korconversationapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerScrapAdapter extends RecyclerView.Adapter<RecyclerScrapAdapter.ViewHolder> {

    private ArrayList<ScrapModel> scrap;
    private Fragment fragment;
    ScrapModel data;
    private FirebaseAuth mAuth ;
    int current;

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
        Button btn_play, btn_cancel;

        public ViewHolder(View itemView) {
            super(itemView);
            sentence_kor = (TextView) itemView.findViewById(R.id.sentence_kor);
            sentence_eng = (TextView) itemView.findViewById(R.id.sentence_eng);
            btn_play = (Button) itemView.findViewById(R.id.btn_play);
            btn_cancel = (Button) itemView.findViewById(R.id.btn_cancel);

        }
    }

    public RecyclerScrapAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_scrap, null);
        RecyclerScrapAdapter.ViewHolder viewHolder = new RecyclerScrapAdapter.ViewHolder(view);
        return viewHolder;
    }

    // 재활용 되는 View가 호출, Adapter가 해당 position에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(final RecyclerScrapAdapter.ViewHolder holder, final int position) {
        data = scrap.get(position);

        // 데이터 결합
        holder.sentence_kor.setText(data.getKor());
        holder.sentence_eng.setText(data.getEng());

        holder.btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = scrap.get(position).getScriptIDKor();
                String part_no, unit_no;

                part_no = String.valueOf(number.charAt(1));
                unit_no = String.valueOf(number.charAt(4));
                System.out.println("part_no : "+part_no +" unit_no : "+unit_no);

                Intent intent = new Intent(fragment.getContext(), DialogActivity.class);
                intent.putExtra("kor",holder.sentence_kor.getText());
                intent.putExtra("eng",holder.sentence_eng.getText());
                intent.putExtra("kor_id",scrap.get(position).getScriptIDKor());
                intent.putExtra("part_no",part_no);
                intent.putExtra("unit_no",unit_no);
                fragment.startActivity(intent);
            }
        });

        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth = FirebaseAuth.getInstance();

                try {

                    Map<String, String> scrapparams_kor = new HashMap<String, String>();
                    scrapparams_kor.put("user_id", mAuth.getUid());
                    scrapparams_kor.put("script_id", scrap.get(position).getScriptIDKor());
                    System.out.println("scriptid_kor : " + scrap.get(position).getScriptIDKor());

                    Map<String, String> scrapparams_eng = new HashMap<String, String>();
                    scrapparams_eng.put("user_id", mAuth.getUid());
                    scrapparams_eng.put("script_id", scrap.get(position).getScriptIDEng());
                    System.out.println("scriptid_eng : " + scrap.get(position).getScriptIDEng());

                    Task Taskforscript = new Task("deleteScrappedScript", scrapparams_kor);
                    Task Taskforscript2 = new Task("deleteScrappedScript", scrapparams_eng);

                    Taskforscript.execute(scrapparams_kor).get();
                    Taskforscript2.execute(scrapparams_eng).get();

                    scrap.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, scrap.size());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}