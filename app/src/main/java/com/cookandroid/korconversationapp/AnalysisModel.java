package com.cookandroid.korconversationapp;

public class AnalysisModel {
    String kor;
    String script_id_kor;
    String script_id_eng;

    public AnalysisModel (String kor, String script_id_kor, String script_id_eng) {
        this.kor = kor;
        this.script_id_kor = script_id_kor;
        this.script_id_eng = script_id_eng;
    }
    public String getKor() {
        return kor;
    }
    public void setKor(String kor) {
        this.kor = kor;
    }

    public String getScript_id_kor() {
        return script_id_kor;
    }
    public void setScript_id_kor(String script_id_kor) {
        this.script_id_kor = script_id_kor;
    }

    public String getScript_id_eng() {
        return script_id_eng;
    }
    public void setScript_id_eng(String script_id_eng) {
        this.script_id_eng = script_id_eng;
    }
}