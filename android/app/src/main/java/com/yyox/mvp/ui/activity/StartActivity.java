package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.message.PushAgent;
import com.yyox.R;

/**
 * Created by 95 on 2017/6/6.
 */

public class StartActivity extends AppCompatActivity {
    private Button mBtn_home;
    private TextView mTv_time_welcome;
    private SmsCount smsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //友盟设置
        PushAgent.getInstance(this).onAppStart();
        initView();
    }

    private void initView() {
        mBtn_home = (Button) findViewById(R.id.btn_home);
        mTv_time_welcome = (TextView) findViewById(R.id.tv_time_welcome);
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFirstRun) {
            editor.putBoolean("isFirstRun", false);
            editor.commit();
            mBtn_home.setVisibility(View.VISIBLE);
            init();
        } else {
            smsCount = new StartActivity.SmsCount(2 * 1000, 100);
            smsCount.start();
            mBtn_home.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StartActivity.this, HomeActivity.class);
                    startActivity(intent);
                    StartActivity.this.finish();
                }
            }, 2000);
        }
    }

    public void init() {
        mBtn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, HomeActivity.class);
                startActivity(intent);
                StartActivity.this.finish();
            }
        });
    }

    /*
         *倒计时类
         */
    class SmsCount extends CountDownTimer {

        public SmsCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
        }

        @Override
        public void onTick(long millisUntilFinished) {
            ((TextView) findViewById(R.id.tv_time_welcome)).setText((millisUntilFinished / 1000 % 60)+1 + "秒");
        }
    }
}
