package com.cookandroid.korconversationapp;

public class ConvModel {

    private String when;
    private String image;
    private String kor;
    private String eng;

    public ConvModel(){

    }

    public ConvModel( String when, String image, String kor, String eng) {
        this.when = when;
        this.image = image;
        this.kor = kor;
        this.eng = eng;
    }


    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
