package com.example.kakaomap_personal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    private EditText edit_id;
    private EditText edit_pw;
    private EditText edit_name;
    private ImageButton signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //뷰와 객체 연결
        edit_id = findViewById(R.id.signUp_id_Edit);
        edit_pw = findViewById(R.id.signUp_pw_Edit);
        edit_name = findViewById(R.id.signUp_name_Edit);
        signup_button = findViewById(R.id.signUp_signinButton);


        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userID = edit_id.getText().toString();
                String userPassword = edit_pw.getText().toString();
                String userName = edit_name.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            boolean success = jsonObject.getBoolean("success");

                            if (success) { // 회원등록에 성공한 경우

                                Toast.makeText(getApplicationContext(),"회원 가입에 성공했습니다.",Toast.LENGTH_SHORT).show(); //회원 가입성공 알림림

                                //로그인 페이지로 이동
                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intent);

                            } else { // 회원등록에 실패한 경우
                                Toast.makeText(getApplicationContext(),"회원 등록에 실패했습니다.",Toast.LENGTH_SHORT).show(); //회원가입 실패 알림
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                SignUpRequest signUpRequest = new SignUpRequest(userID,userPassword,userName,responseListener);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(signUpRequest);

            }
        });
    }
}