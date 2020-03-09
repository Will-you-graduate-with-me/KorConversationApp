package com.cookandroid.korconversationapp;

import java.util.ArrayList;

public class SituationModel {

    private String situation;
    private ArrayList<ConvModel> allItemsInSection;

    public SituationModel(){

    }

    public SituationModel(String situation, ArrayList<ConvModel> allItemsInSection) {
        this.situation = situation;
        this.allItemsInSection = allItemsInSection;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public ArrayList<ConvModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<ConvModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }
}
