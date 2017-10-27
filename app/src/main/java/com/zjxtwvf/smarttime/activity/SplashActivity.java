package com.zjxtwvf.smarttime.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zjxtwvf.smarttime.R;

/**
 * Created by zjxtwvf on 2017/7/17.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_splash);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(SplashActivity.this,GestureVerifyActivity.class);
                startActivity(intent);
                finish();
            }
        }).start();
    }
}
