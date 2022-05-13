package com.example.kakaomap_personal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import net.daum.mf.map.api.MapView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        initView();

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