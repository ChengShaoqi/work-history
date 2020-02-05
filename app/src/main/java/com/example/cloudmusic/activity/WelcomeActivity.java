package com.example.cloudmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cloudmusic.R;

import java.util.Timer;
import java.util.TimerTask;

//1、延迟三秒
//2、跳转页面
public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "csqWelcomeActivity";
    private static final int WELCOME_SHOW_TIME = 1000;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        init();
    }

    /**
     * 经历WELCOME_SHOW_TIME时间后执行toMainActivity()
     */
    private void init() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "run: 当前线程为：" + Thread.currentThread());
                toMainActivity();
            }
        }, WELCOME_SHOW_TIME);
    }

    /**
     * 跳转到MainActivity
     */
    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
