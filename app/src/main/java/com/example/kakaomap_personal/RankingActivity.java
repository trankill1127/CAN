package com.example.kakaomap_personal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RankingActivity extends AppCompatActivity {

    private Intent intent;
    private String userID;
    private String userPassword;
    private String userName;
    private int step_count;
    private int trash_count;
    private int total;
    private int best_rank;
    private int now_step_count;

    private TextView layout_userName;
    private TextView layout_bestRank;
    private TextView layout_trashCnt;
    private TextView layout_stepCnt;
    private TextView layout_nowStepCnt;
    private Button rankingButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        initView();
        rankingButton = findViewById(R.id.ranking_button);
        rankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new BackgroundTask().execute();
                }
        });

    }
class BackgroundTask extends AsyncTask<Void, Void, String> {
    String target;

    @Override
    protected void onPreExecute() {
        //List.php은 파싱으로 가져올 웹페이지
        target = "http://seyeonbb.dothome.co.kr/List.php";
    }

    @Override
    protected String doInBackground(Void... voids) {

        try{
            URL url = new URL(target);//URL 객체 생성

            //URL을 이용해서 웹페이지에 연결하는 부분
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

            //바이트단위 입력스트림 생성 소스는 httpURLConnection
            InputStream inputStream = httpURLConnection.getInputStream();

            //웹페이지 출력물을 버퍼로 받음 버퍼로 하면 속도가 더 빨라짐
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp;

            //문자열 처리를 더 빠르게 하기 위해 StringBuilder클래스를 사용함
            StringBuilder stringBuilder = new StringBuilder();

            //한줄씩 읽어서 stringBuilder에 저장함
            while((temp = bufferedReader.readLine()) != null){
                stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
            }

            //사용했던 것도 다 닫아줌
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        Intent intent = new Intent(RankingActivity.this,HelpMeActivity.class);
        intent.putExtra("userList", result);//파싱한 값을 넘겨줌
        RankingActivity.this.startActivity(intent);//ManagementActivity로 넘어감

    }

}
    private void initView() {

        //mainActivity에서 전달한 정보들 받아오기
        intent = getIntent();
        userID = intent.getStringExtra("userID");
        userPassword = intent.getStringExtra("userPassword");
        userName = intent.getStringExtra("userName");
        step_count = intent.getIntExtra("step_count", 0);
        trash_count = intent.getIntExtra("trash_count", 0);
        total = intent.getIntExtra("total", 0);
        best_rank = intent.getIntExtra("best_rank", 0);
        now_step_count = intent.getIntExtra("now_step_count", 0);

        layout_userName = findViewById(R.id.ranking_userName);
        layout_userName.setText(userName);

        layout_bestRank = findViewById(R.id.ranking_bestRank);
        layout_bestRank.setText(best_rank+"위");

        layout_trashCnt = findViewById(R.id.ranking_trashCnt);
        layout_trashCnt.setText(trash_count+"회");

        layout_stepCnt = findViewById(R.id.ranking_stepCnt);
        layout_stepCnt.setText(step_count+"보");
        layout_nowStepCnt = findViewById(R.id.ranking_nowStepCnt);
        layout_nowStepCnt.setText("(+"+now_step_count+")");


    }
}