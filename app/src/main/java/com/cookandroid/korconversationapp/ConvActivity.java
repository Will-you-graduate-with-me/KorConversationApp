package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConvActivity extends AppCompatActivity {

    CustomExoPlayerView customExoPlayerView;
    ImageButton back, speaker, restart, grade, check;
    Button repeat;
    TextView kor_script;
    TextView[] textview_kor = new TextView[3];
    TextView[] textview_eng = new TextView[3];
    String str_eng, str_kor, part_no, unit_no;
    TextView part_unit_no;
    int caseCount;
    int caseNumber;
    JSONArray jsonArrayKor;
    String[] scriptK, scriptE;
    int num = 0;
    Intent i;
    SpeechRecognizer mRecognizer;
    int hand_num = 1;
    Thread thread;
    Runnable task;
    boolean flag = true;
    String part_unit_no_to_string;
    final String url_header = "https://bucket-test-sy.s3.us-east-2.amazonaws.com/android_video/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);

        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        setContentView(R.layout.activity_conv);

        // Video Player
        customExoPlayerView = findViewById(R.id.player_view);

        back = (ImageButton) findViewById(R.id.backbtn);
        speaker = (ImageButton) findViewById(R.id.btn_speak);
//        restart=(ImageButton)findViewById(R.id.btn_restart);
//        grade=(ImageButton)findViewById(R.id.btn_grade);
//        check=(ImageButton)findViewById(R.id.btn_check);
//        repeat=(Button)findViewById(R.id.btn_repeat);

        part_unit_no = (TextView) findViewById(R.id.part_unit_no);
        textview_eng[0] = (TextView) findViewById(R.id.eng);
        textview_kor[0] = (TextView) findViewById(R.id.kor);
        textview_eng[1] = (TextView) findViewById(R.id.eng2);
        textview_kor[1] = (TextView) findViewById(R.id.kor2);
        textview_eng[2] = (TextView) findViewById(R.id.eng3);
        textview_kor[2] = (TextView) findViewById(R.id.kor3);
        kor_script = (TextView) findViewById(R.id.kor_script);
        kor_script.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = new Intent(this.getIntent());
        str_eng = intent.getStringExtra("eng");
        str_kor = intent.getStringExtra("kor");
        part_no = intent.getStringExtra("part_no");
        unit_no = intent.getStringExtra("unit_no");

        // ex) p1_u4_ : 동영상 url 써먹기 편하라고! 나머지 뒤에 인자들은 어디서 갖고 오는 지 모르것네요 허허;;
        part_unit_no_to_string = "p"+part_no+"_"+"u"+unit_no+"_";

        /*

        @@@ Video Player @@@

        // 가장 처음 문장 (인공지능이 말하는) 실행시에는 아래 문장 하나만 실행하면 됨
        // 비디오 플레이어의 첫 실행이라서 releasePlayer (기존에 존재하는 플레이어 삭제하는거임) 없이 바로 initializeplayer만 하면 됨!
        // url 구성은 url_header 변수랑 part_unit_no_to_string 변수 갖고 쓰면 편할듯..
        // 지금 S3에는 https://bucket-test-sy.s3.us-east-2.amazonaws.com/android_video/p1_u4_a2_1_k.mp4 밖에 없음..! 참고!!
        // 테스트 해보고 싶으면 영상 다운받아서 이름 여러개로 바꿔서 올리고 걔네 다 퍼블릭 설정 해두고 테스트하면 되는데.. 아마 영상 잘 나올거임
        // 귀찮으니까 밑에 BigBuckBunny로 테스트해보삼요!

        customExoPlayerView.initializePlayer("https://bucket-test-sy.s3.us-east-2.amazonaws.com/android_video/p1_u1_a1_1_k.mp4");


        // 사용자가 말하고 나서 다시 인공지능이 받아치는 두 번째 실행부터는 이미 플레이어를 사용하고 난 뒤라서 꼭 지워주고 다시 만들어야함!
        1. 기존 플레이어 지우기
        customExoPlayerView.releasePlayer();
        // 2. 새로운 인스턴스 다시 만들어서 영상 연결하기
        customExoPlayerView.initializePlayer("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");


         */

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thread.interrupt();
                flag = false;
                finish();
            }
        });
        part_unit_no.setText("Part " + part_no + " > Unit " + unit_no);

        String scriptInfoKor = "";
        String scriptInfoEng = "";
        String caseInfo = "";
        try {

            Map<String, String> userparams_forKor = new HashMap<String, String>();
            userparams_forKor.put("part_no", part_no);
            userparams_forKor.put("unit_no", unit_no);
            userparams_forKor.put("language", "k");

            Map<String, String> userparams_forEng = new HashMap<String, String>();
            userparams_forEng.put("part_no", part_no);
            userparams_forEng.put("unit_no", unit_no);
            userparams_forEng.put("language", "e");

            Map<String, String> countparams = new HashMap<String, String>();
            countparams.put("part_no", part_no);
            countparams.put("unit_no", unit_no);

            Task TaskforKor = new Task("selectScript", userparams_forKor);
            Task TaskforEng = new Task("selectScript", userparams_forEng);
            Task TaskforCase = new Task("selectUnitInfo", countparams);

            scriptInfoKor = TaskforKor.execute(userparams_forKor).get();
            scriptInfoEng = TaskforEng.execute(userparams_forEng).get();
            caseInfo = TaskforCase.execute(countparams).get();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String sentence_no1 = "";
            String sentence_no2 = "";
            String sentence_no_final = "";
            jsonArrayKor = new JSONArray(scriptInfoKor);
            JSONArray jsonArrayEng = new JSONArray(scriptInfoEng);
            JSONArray jsonArrayCase = new JSONArray(caseInfo);
            scriptK = new String[jsonArrayKor.length()];
            scriptE = new String[jsonArrayEng.length()];

            for (int i = 0; i < jsonArrayKor.length(); i++) {
                JSONObject scriptKor = (JSONObject) jsonArrayKor.get(i);
                scriptK[i] = scriptKor.get("sentence").toString();
            }

            for (int i = 0; i < jsonArrayEng.length(); i++) {
                JSONObject scriptEng = (JSONObject) jsonArrayEng.get(i);
                scriptE[i] = scriptEng.get("sentence").toString();
            }

            for (int i = 0; i < jsonArrayCase.length(); i++) {
                JSONObject info_case = (JSONObject) jsonArrayCase.get(i);
                System.out.print("case정보:" + info_case);
                caseCount = Integer.parseInt(info_case.get("case_CT").toString());
                caseNumber = Integer.parseInt(info_case.get("case_SQ").toString());
            }


            textview_kor[0].setText(scriptK[0]);
            textview_eng[0].setText(scriptE[0]);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 1) {
                    textview_kor[1].setVisibility(View.GONE);
                    textview_eng[1].setVisibility(View.GONE);
                    textview_kor[2].setVisibility(View.GONE);
                    textview_eng[2].setVisibility(View.GONE);

                    textview_kor[0].setText(scriptK[num]);
                    textview_eng[0].setText(scriptE[num]);
                    if(num%2==1){

                        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

                        mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                        mRecognizer.setRecognitionListener(listener);
                        mRecognizer.startListening(i);

                    }
                    num++;
                } else if(msg.what == 2) {
                    if(caseCount == 2) {
                        textview_kor[1].setVisibility(View.VISIBLE);
                        textview_eng[1].setVisibility(View.VISIBLE);
                    } else if (caseCount == 3) {
                        textview_kor[1].setVisibility(View.VISIBLE);
                        textview_eng[1].setVisibility(View.VISIBLE);
                        textview_kor[2].setVisibility(View.VISIBLE);
                        textview_eng[2].setVisibility(View.VISIBLE);
                    }

                    for (int i = num; i < num + caseCount; i++) {
                        textview_kor[i-num].setText(scriptK[i]);
                        textview_eng[i-num].setText(scriptE[i]);
                    }
                    if(num%2==1){

                        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

                        mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                        mRecognizer.setRecognitionListener(listener);
                        mRecognizer.startListening(i);

                    }
                    num = num + caseCount;
                }
            }

        };

        task = new Runnable() {
            @Override
            public void run() {
                hand_num = 1;

                while(flag) {
                    if (num <= jsonArrayKor.length() - 1) {
                        if (num != caseNumber-1) {
                            if(num % 2 == 1) {
                                handler.sendEmptyMessage(1);
                                try {
                                    Thread.sleep(20000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            } else{
                                handler.sendEmptyMessage(1);
                            }
                        } else {
                            if(num % 2 == 1) {
                                handler.sendEmptyMessage(2);
                                try {
                                    Thread.sleep(20000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            } else{
                                handler.sendEmptyMessage(2);
                            }
                        }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        thread.interrupt();
                        break;
                    }
                }
                if(num > jsonArrayKor.length()-1) {
                    // 결과 화면 보여주기
                    Intent i = new Intent(ConvActivity.this, AnalysisActivity.class);
                    i.putExtra("length", jsonArrayKor.length());
                    for (int j = 0; j < jsonArrayKor.length(); j++) {
                        i.putExtra("kor_" + j, scriptK[j]);
                    }
                    startActivity(i);
                    finish();
                }
            }
        };

        thread = new Thread(task);
        thread.start();


    }

    private final RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {

            Toast.makeText(getApplicationContext(), "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }

            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String recText_org = "";
            recText_org = matches.get(0); //인식된 음성정보
            if (recText_org!="") {
                kor_script.setText(recText_org);
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable()  {
                    public void run() {
                        // 시간 지난 후 실행할 코딩
                        thread.interrupt();
                        kor_script.setText("");
                    }
                }, 2000); // 0.5초후
            }

            /*
            recText = recText_org.replace(" ", ""); //인식된 음성정보 공백제거

            // 배열로 선언
            String[] lyrics_org = new String[count];
            String[] lyrics = new String[count];
            int[] rightCount = new int[count];
            int max = 0;
            int max_num = 0;

            SpannableStringBuilder sb = new SpannableStringBuilder(recText_org);

            // 문장 개수 따라 검사
            for(int i=0; i< count; i++) {
                rightCount[i] = 0;

                lyrics_org[i] = kor[i]; //가사 정보
                lyrics[i] = lyrics_org[i].replace(" ", ""); //가사정보 공백제거
                lyrics[i] = lyrics[i].replace(".", ""); //가사정보 온점제거
                lyrics[i] = lyrics[i].replace("!", ""); //가사정보 느낌표제거

                int length = (recText.length() > lyrics[i].length()) ? recText.length() : lyrics[i].length();
                for (int j = 0; j < length; j++) {
                    try {
                        if ((recText.charAt(j)) == (lyrics[i].charAt(j))) {  //음성정보와 가사와 비교
                            rightCount[i]++; // 맞은 개수 체크
                        } else {
                            continue;
                        }
                    } catch (Exception e) {
                        continue;

                    }
                }

                // 맞은 개수가 가장 많은 문장 찾기
                if(rightCount[i] > max) {
                    max = rightCount[i];
                    // 그 문장의 번호를 저장
                    max_num = i;
                }

            }

            // 해당 문장과 발음한 문장 비교
            int length = (recText.length()>lyrics[max_num].length())?recText.length():lyrics[max_num].length();
            for (int j = 0; j < length; j++) {
                try {
                    if ((recText.charAt(j)) == (lyrics[max_num].charAt(j))) {  //음성정보와 가사와 비교
                        continue;
                    } else {
                        wrong[wrong_num] = j; // 틀린 부분 저장
                        wrong_num++;
                    }
                } catch (Exception e) {
                    wrong[wrong_num] = j; // 틀린 부분 저장
                    wrong_num++;
                }
            }

            for(int j=0; j<wrong_num; j++) {
                for(int k=0; k<recText_org.length(); k++) {
                    try {
                        if((recText_org.charAt(k)) == (recText.charAt(wrong[j]))) { // 기존 예시에서 틀린 부분 찾기
                            // 틀린 부분 색깔 바꾸기
                            ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#FFFA5252"));
                            sb.setSpan(span, k, k+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    } catch (Exception e) {
                        break;
                    }
                }

            }

            // 해당되는 문장 색 바꾸기
            textview_kor[max_num].setTextColor(Color.parseColor("#FF52B4FA"));
            textview_eng[max_num].setTextColor(Color.parseColor("#FF52B4FA"));
            tv_wrong.setText(sb);
            */

        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if(customExoPlayerView != null){
            customExoPlayerView.releasePlayer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(customExoPlayerView != null){
            customExoPlayerView.releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(customExoPlayerView != null){
            customExoPlayerView.releasePlayer();
        }
    }
}