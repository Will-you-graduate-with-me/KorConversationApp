package com.cookandroid.korconversationapp;

public class ScrapModel {
    String kor;
    String eng;

    public ScrapModel(String kor, String eng) {
        this.kor = kor;
        this.eng = eng;
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
