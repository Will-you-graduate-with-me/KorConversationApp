package com.cookandroid.korconversationapp;

public class ConvModel {

    private String when;
    private String kor;
    private String eng;
    private String part_no;
    private String unit_no;
    private String situation_back;

    public String getSituation_back() {
        return situation_back;
    }

    public void setSituation_back(String situation_back) {
        this.situation_back = situation_back;
    }


    public ConvModel( String when, String image, String kor, String eng) {
        this.when = when;
        this.kor = kor;
        this.eng = eng;
    }
    public ConvModel( String when, String kor, String eng,String part_no, String unit_no,String situation_back) {
        this.when = when;
        this.kor = kor;
        this.eng = eng;
        this.part_no=part_no;
        this.unit_no=unit_no;
        this.situation_back=situation_back;
    }

    public String getPart_no() {
        return part_no;
    }

    public void setPart_no(String part_no) {
        this.part_no = part_no;
    }

    public String getUnit_no() {
        return unit_no;
    }

    public void setUnit_no(String unit_no) {
        this.unit_no = unit_no;
    }

    public ConvModel(){

    }



    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }


    public String getKor() {
        return kor;
    }

    public void setKor(String kor) {
        this.kor = kor;
    }

    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }
}
