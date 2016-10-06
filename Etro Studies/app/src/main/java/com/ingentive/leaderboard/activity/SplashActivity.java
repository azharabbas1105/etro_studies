package com.ingentive.leaderboard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ingentive.leaderboard.R;
import android.os.Handler;

/**
 * Created by PC on 04-10-2016.
 */
public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
