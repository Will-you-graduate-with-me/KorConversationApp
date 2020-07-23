package com.cookandroid.korconversationapp;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class DialogActivity extends AppCompatActivity {

    Button btn_aud, btn_mic;
    TextView tv_kor, tv_eng, tv_wrong, part_unit;
    Button btn_cancel;
    Intent i;
    SpeechRecognizer mRecognizer;
    int rightCount;
    String recText;
    String lyrics;
    int[] wrong = new int[20];
    int wrong_num;
    public static String url;
    MediaPlayer player;
    public String kor_id, part_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customdialog);

        btn_cancel = findViewById(R.id.btn_cancel); // 취소 버튼
        btn_aud = findViewById(R.id.btn_aud); // 듣기 버튼
        btn_mic = findViewById(R.id.btn_mic); // 재생 버튼

        tv_kor = findViewById(R.id.tv_kor); // 누르면 시작
        tv_eng = findViewById(R.id.tv_eng); // 예시 문장
        tv_wrong = findViewById(R.id.tv_wrong); // 인식된 문장
        part_unit = findViewById(R.id.part_unit); // 파트, 유닛 정보

        Intent intent = getIntent();

        String kor = intent.getExtras().getString("kor");
        String eng = intent.getExtras().getString("eng");
        kor_id = intent.getExtras().getString("kor_id");
        part_no = intent.getExtras().getString("part_no");
        String unit_no = intent.getExtras().getString("unit_no");
        tv_kor.setText(kor);
        tv_eng.setText(eng);
        part_unit.setText("Part "+part_no+" > "+"Unit "+unit_no);

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_aud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://bucket-test-sy.s3.us-east-2.amazonaws.com/android_voice/p"+part_no+"/"+kor_id+".mp3";
                System.out.println(url);
                playAudio();
            }
        });

        btn_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wrong_num=0;
                rightCount=0;

                mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                mRecognizer.setRecognitionListener(listener);
                mRecognizer.startListening(i);

            }
        });
    }

    private void playAudio() {
        try {
            closePlayer();

            player = new MediaPlayer();
            player.setDataSource(url);
            player.prepare();
            player.start();

            Toast.makeText(this, "재생 시작됨.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 녹음 시 마이크 리소스 제한. 누군가가 lock 걸어놓으면 다른 앱에서 사용할 수 없음.
     * 따라서 꼭 리소스를 해제해주어야함. */
    public void closePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        // 바깥레이어 클릭해도 닫히지 않도록
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
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
            String recText_org = matches.get(0); //인식된 음성정보
            recText = recText_org.replace(" ", ""); //인식된 음성정보 공백제거

            SpannableStringBuilder sb = new SpannableStringBuilder(recText_org);

            String lyrics_org = tv_kor.getText().toString(); //가사 정보

            String[] test = new String[recText_org.length()];

            int num = 0;

            test = new String[recText_org.length()];

            // '/'로 전부 바꾸기
            for (int j = 0; j < lyrics_org.length(); j++) {
                if((lyrics_org.charAt(j)) == ' ' || (lyrics_org.charAt(j)) == '.'  || (lyrics_org.charAt(j)) == '!' || (lyrics_org.charAt(j)) == '?') {
                    lyrics = lyrics_org.replace(" ", "/"); //가사정보 공백제거
                    lyrics = lyrics.replace(".", "/"); //가사정보 온점제거
                    lyrics = lyrics.replace("!", "/"); //가사정보 느낌표제거
                    lyrics = lyrics.replace("?", "/"); //가사정보 물음표제거
                    test[num] = String.valueOf(j);
                    num++;
                }

                for (int k=num; k<recText_org.length(); k++) {
                    test[k] = String.valueOf("");
                }
            }

            for (int k=0; k<test.length; k++) {
                for (int j = 0; j<recText.length(); j++) {
                    if(test[k].equals("")) {
                        continue;
                    } else {
                        if (test[k].equals(String.valueOf(j))) {
                            recText = recText.substring(0,j) +"/"+ recText.substring(j);
                        }
                    }
                }
            }

            // 마지막에 붙은 특수문자때문에 '/'를 추가해줌
            recText = recText + "/";

            System.out.println("문장 : "+recText);

            // 해당 문장과 발음한 문장 비교
            int length = (recText.length()>lyrics.length())?recText.length():lyrics.length();
            for (int j = 0; j < length; j++) {
                try {
                    if ((recText.charAt(j)) == (lyrics.charAt(j))) {  //음성정보와 가사와 비교
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

            tv_wrong.setText(sb);

        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

}