package com.cookandroid.korconversationapp;


import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;

public class Task extends AsyncTask< Map<String, String>, Integer, String> {
    String subUrl;

    public String getSubUrl() {
        return subUrl;
    }

    public void setSubUrl(String subUrl) {
        this.subUrl = subUrl;
    }


    public static String ip = "192.168.219.106"; // 자신의 IP주소를 쓰시면 됩니다.
    //doInBackground 전에 동작
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground( Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

        // Http 요청 준비 작업

        HttpClient.Builder http = new HttpClient.Builder
                ("POST", "http://192.168.219.106:8080/"+this.getSubUrl()); //포트번호,서블릿주소
        System.out.println("URL:: "+http.getUrl());
        // Parameter 를 전송한다.
        http.addAllParameters(maps[0]);

        //Http 요청 전송
        HttpClient post = http.create();
        post.request();

        // 응답 상태코드 가져오기
        int statusCode = post.getHttpStatusCode();

        // 응답 본문 가져오기
        String body = post.getBody();
        return body;
    }

    @Override
    protected void onPostExecute(String s) { //서블릿으로부터 값을 받을 함수
        Log.d("JSON_RESULT", s);
        System.out.println("데이터 "+s);
        Gson gson = new Gson();
        Member data = gson.fromJson(s, Member.class);

        //System.out.println("아이디 : "+data.getId());
        //System.out.println("이름 : "+data.getName());
    }
}