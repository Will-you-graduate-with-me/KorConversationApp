package com.cookandroid.korconversationapp;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Task extends AsyncTask< Map<String, String>, Integer, String> {
    String subUrl;
    String LoadData;
    Map<String, String> maps;
    public String getSubUrl() {
        return subUrl;
    }

    public void setSubUrl(String subUrl) {
        this.subUrl = subUrl;
    }

    public Task(String subUrl) {
        this.subUrl=subUrl;
    }
    public Task(String subUrl, Map<String, String> maps) {
        this.subUrl=subUrl;
        this.maps=maps;
    }
    public Task(){}
    public String getString(){return LoadData;}
    public static String ip = "http://3.23.22.107:8080/AndroidDBConnection/"; // 자신의 IP주소를 쓰시면 됩니다.
    //doInBackground 전에 동작
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //원래 protected
    @Override
    public String doInBackground( Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

        // Http 요청 준비 작업
        HttpClient.Builder http = new HttpClient.Builder
                ("POST", ip+this.getSubUrl()); //포트번호,서블릿주소
        // Parameter 를 전송한다.

        http.addAllParameters(maps[0]);
        //http.addOrReplace("user_id","유저아이디");
        //Http 요청 전송

        HttpClient post = http.create();
        post.request();

        // 응답 상태코드 가져오기
        int statusCode = post.getHttpStatusCode();

        // 응답 본문 가져오기
        LoadData = post.getBody();



        return LoadData;
    }

    @Override
    protected void onPostExecute(String s) { //서블릿으로부터 값을 받을 함수

        Log.d("JSON_RESULT", s);


    }
}