package com.cookandroid.korconversationapp;

public class HistoryModel {
    String category;
    String situation;

    public HistoryModel(String category, String situation) {
        this.category = category;
        this.situation = situation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }
}
