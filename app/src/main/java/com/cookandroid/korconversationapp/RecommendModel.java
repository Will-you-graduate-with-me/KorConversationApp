package com.cookandroid.korconversationapp;

import java.util.ArrayList;

public class RecommendModel {

    private ArrayList<ConvModel> recommendItems;

    public RecommendModel(){

    }

    public RecommendModel(ArrayList<ConvModel> recommendItems) {
        this.recommendItems = recommendItems;
    }


    public ArrayList<ConvModel> getRecommendItems() {
        return recommendItems;
    }

    public void setRecommendItems(ArrayList<ConvModel> recommendItems) {
        this.recommendItems = recommendItems;
    }
}