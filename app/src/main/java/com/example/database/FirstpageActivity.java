package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class FirstpageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);

        Handler timer = new Handler();
        //타이머를 사용하여 1초뒤에 로그인 페이지가 띄워지게 하기
        timer.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(FirstpageActivity.this, LoginActivity.class);
                startActivity(in);
                finish();
            }
        },1000);
    }
}
