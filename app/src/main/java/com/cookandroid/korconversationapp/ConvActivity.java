package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.Player;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ConvActivity extends AppCompatActivity {

    CustomExoPlayerView customExoPlayerView;
    ImageButton back, speaker, restart, grade, check;
    TextView kor_script;
    TextView[] textview_kor = new TextView[3];
    TextView[] textview_eng = new TextView[3];
    String part_no, unit_no;
    TextView part_unit_no, speak;
    int caseCount, caseNumber, num=0, rightCount;
    JSONArray jsonArrayKor;
    String[] scriptK, scriptE, script_id_kor, script_id_eng, sentence_no;
    Intent i;
    SpeechRecognizer mRecognizer;
    int wrong_num;
    Thread thread;
    Runnable task;
    boolean flag = true, video_init = true;
    final String url_header = "@string/video_url_header";
    private FirebaseAuth mAuth ;
    Boolean singleResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;

        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        setContentView(R.layout.activity_conv);

        // Video Player
        customExoPlayerView = (CustomExoPlayerView)findViewById(R.id.player_view);

        back = (ImageButton) findViewById(R.id.backbtn);
        speaker = (ImageButton) findViewById(R.id.btn_speak);

        speak = (TextView) findViewById(R.id.speak);
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
        part_no = intent.getStringExtra("part_no");
        unit_no = intent.getStringExtra("unit_no");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thread.interrupt();
                flag = false;
                onStop();
                if (mRecognizer != null) {
                    mRecognizer.destroy();
                    mRecognizer.cancel();
                }

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

            jsonArrayKor = new JSONArray(scriptInfoKor);
            JSONArray jsonArrayEng = new JSONArray(scriptInfoEng);
            JSONArray jsonArrayCase = new JSONArray(caseInfo);
            scriptK = new String[jsonArrayKor.length()];
            scriptE = new String[jsonArrayEng.length()];
            script_id_kor = new String[jsonArrayKor.length()];
            script_id_eng = new String[jsonArrayEng.length()];
            sentence_no = new String[jsonArrayKor.length()];

            for (int i = 0; i < jsonArrayKor.length(); i++) {
                JSONObject scriptKor = (JSONObject) jsonArrayKor.get(i);
                scriptK[i] = scriptKor.get("sentence").toString();
                script_id_kor[i] = scriptKor.get("script_id").toString();
                sentence_no[i] = String.valueOf(scriptKor.get("sentence_no").toString().charAt(0));
            }

            for (int i = 0; i < jsonArrayEng.length(); i++) {
                JSONObject scriptEng = (JSONObject) jsonArrayEng.get(i);
                scriptE[i] = scriptEng.get("sentence").toString();
                script_id_eng[i] = scriptEng.get("script_id").toString();
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
                kor_script.setText("");
                kor_script.setVisibility(View.GONE);

                // 메세지 1번 실행
                if(msg.what == 1) {
                    // 메세지 1번은 케이스가 나뉘지 않는 문장들임

                    // 텍스트뷰 (케이스가 여러개일 때 각각 보여주는 공간) 숨기기
                    textview_kor[1].setVisibility(View.GONE);
                    textview_eng[1].setVisibility(View.GONE);
                    textview_kor[2].setVisibility(View.GONE);
                    textview_eng[2].setVisibility(View.GONE);

                    textview_kor[0].setText(scriptK[num]);
                    textview_eng[0].setText(scriptE[num]);

                    // 사용자가 말할 차례라면
                    if(sentence_no[num].equals("b")){
                        speaker.setVisibility(View.GONE);

                        singleResult = true;

                        // 음성인식 시작 코드들
                        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

                        mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                        mRecognizer.setRecognitionListener(listener);
                        mRecognizer.startListening(i);

                    } else{
                        speaker.setVisibility(View.VISIBLE);
                        speak.setVisibility(View.GONE);
                        // 인공지능이 말할 단계: 합성된 영상 보여주기
                        if(!video_init){
                            // 1. 기존 플레이어 지우기
                            customExoPlayerView.releasePlayer();
                            // 2. 새 링크 설정
                            customExoPlayerView.initializePlayer("https://bucket-test-sy.s3.us-east-2.amazonaws.com/android_video/p"+part_no+"/"+script_id_kor[num]+".mp4");
                            customExoPlayerView.getPlayer().addListener(eventListener);
                        }
                        else{
                            // 처음으로 비디오 플레이어 사용하는 경우
                            customExoPlayerView.initializePlayer("https://bucket-test-sy.s3.us-east-2.amazonaws.com/android_video/p"+part_no+"/"+script_id_kor[num]+".mp4");
                            video_init = !video_init;
                            customExoPlayerView.getPlayer().addListener(eventListener);
                        }
                    }
                    num++; // 문장 처리하고 나서 숫자 추가
                } else if(msg.what == 2) { // 메세지 2번 실행
                    speaker.setVisibility(View.GONE);

                    singleResult = true;

                    // 메세지 2번은 케이스가 나뉘는 문장들임
                    if(caseCount == 2) { // 케이스가 2개라면
                        // 추가로 텍스트뷰 하나만 더 보여줌
                        textview_kor[1].setVisibility(View.VISIBLE);
                        textview_eng[1].setVisibility(View.VISIBLE);
                    } else if (caseCount == 3) { // 케이스가 3개라면
                        // 추가로 텍스트뷰 두개를 더 보여줌
                        textview_kor[1].setVisibility(View.VISIBLE);
                        textview_eng[1].setVisibility(View.VISIBLE);
                        textview_kor[2].setVisibility(View.VISIBLE);
                        textview_eng[2].setVisibility(View.VISIBLE);
                    }

                    // 케이스 개수대로 텍스트뷰에 나타내는 것
                    for (int i = num; i < num + caseCount; i++) {
                        textview_kor[i-num].setText("case"+(i-num+1)+"> "+scriptK[i]);
                        textview_eng[i-num].setText(scriptE[i]);
                    }

                    // 케이스는 사용자 차례일 때만 발생
                    i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

                    mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                    mRecognizer.setRecognitionListener(listener);
                    mRecognizer.startListening(i);

                    /*
                    // 사용자라면 음성인식 시작
                    if(sentence_no[num].equals("b")){

                        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

                        mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                        mRecognizer.setRecognitionListener(listener);
                        mRecognizer.startListening(i);

                    } else{
                        // 인공지능이 말할 단계: 합성된 영상 보여주기
                        if(!video_init) {
                            // 1. 기존 플레이어 지우기
                            customExoPlayerView.releasePlayer();
                            // 2. 새 링크 설정
                            customExoPlayerView.initializePlayer("https://bucket-test-sy.s3.us-east-2.amazonaws.com/android_video/p" + part_no + "/" + script_id_kor[num] + ".mp4");
                            customExoPlayerView.getPlayer().addListener(eventListener);
                        }
                        else{
                            // 처음으로 비디오 플레이어 사용하는 경우
                            customExoPlayerView.initializePlayer("https://bucket-test-sy.s3.us-east-2.amazonaws.com/android_video/p"+part_no+"/"+script_id_kor[num]+".mp4");
                            video_init = !video_init;
                            customExoPlayerView.getPlayer().addListener(eventListener);
                        }
                    }
                    */
                    // 케이스개수만큼 뛰어넘기위해 num+caseCount를 해줌
                    num = num + caseCount;
                }
            }

        };

        // 학습할 때 사용하는 task
        task = new Runnable() {
            @Override
            public void run() {

                // flag 설정으로 task 실행을 조절함
                // 기본은 true이고 뒤로가기 버튼을 눌렀을 때 flag가 false가 되면서 멈춤
                while(flag) {
                    // 음성인식용 변수, 신경쓰지 않아도 됨
                    wrong_num=0;
                    rightCount=0;

                    // 배열의 크기를 벗어나지 않을 때 실행 (학습할 문장들이 남아 있을 때)
                    if (num <= jsonArrayKor.length() - 1) {
                        // 케이스가 발생하는(갈라지는) 번호가 아닐 때
                        if (num != caseNumber-1) { // num은 시작할 때 값이 0임
                            // b라면 (사용자라면)
                            if(sentence_no[num].equals("b")) {
                                // 1번 메세지를 넘겨줌 => 위에서 msg.what==1을 실행
                                handler.sendEmptyMessage(1);

                            } else{ // a 라면 (영상이라면)
                                // 바로 실행 (스레드를 재우게되면 딜레이가 생기기때문에 여기서는 재우지않고 넘어감)
                                handler.sendEmptyMessage(1);
                            }
                        } else { // 케이스가 발생하는 번호일 때
                            // 케이스 발생은 b에서만 발생
                            // 2번 메세지를 넘겨줌 => 위에서 msg.what==2를 실행
                            handler.sendEmptyMessage(2);
                            /*
                            // b라면 (사용자라면)
                            if(sentence_no[num].equals("b")) {
                                // 2번 메세지를 넘겨줌 => 위에서 msg.what==2를 실행
                                handler.sendEmptyMessage(2);
                                try {
                                    Thread.sleep(5000); // 원래 20000
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            } else{
                                // a라면
                                // 바로 실행
                                handler.sendEmptyMessage(2);
                            }
                            */
                        }

                        try {
                            // a,b 사용없이 15초동안 스레드 재우기
                            // 어차피 interrupt 쓰기 때문에 재워도 상관없음
                            Thread.sleep(15000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } else {
                        // 더 학습할 스크립트가 남아있지 않을 때
                        thread.interrupt();
                        break;
                    }
                }
                // 배열이 끝나면 더 학습할 스크립트가 남아있지 않으므로 결과창으로 넘어감
                if(num > jsonArrayKor.length()-1) {

                    // 결과화면 보여주기 전에 먼저 recent_id 업데이트
                    mAuth = FirebaseAuth.getInstance();

                    Map<String, String> historyparams2 = new HashMap<String, String>();

                    historyparams2.put("user_id", mAuth.getUid());
                    historyparams2.put("part_no", part_no);
                    historyparams2.put("unit_no", unit_no);

                    System.out.println(mAuth.getUid()+"/"+ part_no + "/" + unit_no);

                    Task Taskforhistory2 = new Task("updateRecentID", historyparams2);

                    Taskforhistory2.execute(historyparams2);

                    // 결과 화면 보여주기
                    Intent i = new Intent(ConvActivity.this, AnalysisActivity.class);
                    i.putExtra("length", jsonArrayKor.length());
                    for (int j = 0; j < jsonArrayKor.length(); j++) {
                        i.putExtra("kor_" + j, scriptK[j]);
                        i.putExtra("scriptidkor_"+j, script_id_kor[j]);
                        i.putExtra("scriptideng_"+j, script_id_eng[j]);
                        i.putExtra("part_no", part_no);
                        i.putExtra("unit_no", unit_no);
                    }
                    startActivity(i);
                    finish();
                }
            }
        };

        // 위의 task를 시작해주는 코드
        thread = new Thread(task);
        thread.start();


    }



    private final Player.EventListener eventListener = new Player.EventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case Player.STATE_IDLE: // 1
                    //재생 실패
                    break;
                case Player.STATE_BUFFERING: // 2
                    // 재생 준비
                    break;
                case Player.STATE_READY: // 3
                    // 재생 준비 완료
                    break;
                case Player.STATE_ENDED: // 4
                    // 완료
                    thread.interrupt();
                    break;
                default:
                    break;
            }
        }
    };



    private final RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            speak.setVisibility(View.VISIBLE);
            //Toast.makeText(getApplicationContext(), "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            thread.interrupt();

        }

        @Override
        public void onResults(Bundle results) {
                // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
                kor_script.setVisibility(View.VISIBLE);
                ArrayList<String> matches =
                        results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String recText_org = matches.get(0); //인식된 음성정보
                String recText = recText_org.replace(" ", ""); //인식된 음성정보 공백제거

                String lyrics_org = textview_kor[0].getText().toString(); //가사 정보
                String lyrics = lyrics_org.replace(" ", ""); //가사정보 공백제거
                lyrics = lyrics.replace(".", ""); //가사정보 온점제거
                lyrics = lyrics.replace("!", ""); //가사정보 느낌표제거

                SpannableStringBuilder sb = new SpannableStringBuilder(recText_org);

                // 음성이 인식되면 넘어가기
                if (recText_org != "") {
                /* 틀린거 찾고 색깔 바꿔주는 부분 (오류 뜰까봐 주석처리)
                int length = (recText.length() > lyrics.length()) ? recText.length() : lyrics.length();
                int[] wrong = new int[20];

                for (int i = 0; i < length; i++) {
                    try {
                        if ((recText.charAt(i)) == (lyrics.charAt(i))) {  //음성정보와 가사와 비교
                            rightCount++; // 맞은 개수 체크
                        } else {
                            wrong[wrong_num] = i; // 틀린 부분 저장
                            wrong_num++;
                        }
                    } catch (Exception e) {
                        wrong[wrong_num] = i; // 틀린 부분 저장
                        wrong_num++;
                    }
                }

                for (int j = 0; j < wrong_num; j++) {
                    for (int i = 0; i < recText_org.length(); i++) {
                        try {
                            if ((recText_org.charAt(i)) == (recText.charAt(wrong[j]))) { // 기존 예시에서 틀린 부분 찾기
                                // 틀린 부분 색깔 바꾸기
                                ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#FFFA5252"));
                                sb.setSpan(span, i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        } catch (Exception e) {
                            break;
                        }
                    }

                }
                */

                    kor_script.setText(sb);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //여기에 딜레이 후 시작할 작업들을 입력
                            if(singleResult) {
                                System.out.println("반복하는 중인가");
                                singleResult = false;
                                thread.interrupt();
                            }
                        }
                    }, 2000);
                }

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