package com.example.kakaomap_personal;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HelpMeActivity extends AppCompatActivity {

    private ListView listView;
    private UserListAdapter2 adapter;
    private List<User2> userList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpme);


        Intent intent = getIntent();

        listView = (ListView)findViewById(R.id.ListView);
        userList1 = new ArrayList<User2>();

        //어댑터 초기화부분 userList와 어댑터를 연결해준다.
        adapter = new UserListAdapter2(getApplicationContext(), userList1,this);
        listView.setAdapter(adapter);

        try{
            //intent로 값을 가져옵니다 이때 JSONObject타입으로 가져옵니다
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("userList"));


            //List.php 웹페이지에서 response라는 변수명으로 JSON 배열을 만들었음..
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            //String userID;
            //String userPassword;
            String userName;
            String userTotal;
            int userCount=0;
            //JSON 배열 길이만큼 반복문을 실행
            while(count < jsonArray.length()){
                //count는 배열의 인덱스를 의미
                JSONObject object = jsonArray.getJSONObject(count);

                userName = object.getString("userName");
                userTotal = object.getString("total");

                User2 user2 = new User2(userName, userTotal);
                if(!userName.equals("관리자"))
                    userList1.add(user2);//리스트뷰에 값을 추가해줍니다
                count++;

            }


        }catch(Exception e){
            e.printStackTrace();
        }

    }

}

