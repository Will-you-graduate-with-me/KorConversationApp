package com.cookandroid.korconversationapp;

public class ScrapModel {
    String kor;
    String eng;
    String scriptid_kor;
    String scriptid_eng;

    public ScrapModel(String kor, String eng, String scriptid_kor, String scriptid_eng) {
        this.kor = kor;
        this.eng = eng;
        this.scriptid_kor = scriptid_kor;
        this.scriptid_eng = scriptid_eng;
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

    public String getScriptIDKor() {
        return scriptid_kor;
    }

    public void setScriptIDKor(String script_id) {
        this.scriptid_kor = scriptid_kor;
    }

    public String getScriptIDEng() {
        return scriptid_eng;
    }

    public void setScriptIDEng(String script_id) {
        this.scriptid_eng = scriptid_eng;
    }
}
