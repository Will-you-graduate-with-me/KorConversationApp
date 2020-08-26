package com.cookandroid.korconversationapp;

public class HistoryModel {
    String part_name;
    String unit_name;
    String category;
    String situation;
    String situation_back;
    String grade;

    public HistoryModel(String part_name, String unit_name, String category, String situation, String situation_back,String grade) {
        this.part_name = part_name;
        this.unit_name = unit_name;
        this.category = category;
        this.situation = situation;
        this.situation_back = situation_back;
        this.grade=grade;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPart_name() {
        return part_name;
    }

    public void setPart_name(String part_name) {
        this.part_name = part_name;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
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

    public String getSituation_back() {
        return situation_back;
    }

    public void setSituation_back(String situation_back) {
        this.situation_back = situation_back;
    }
}
