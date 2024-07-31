package com.my.audio_video_fm;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        View textView = findViewById(R.id.textsplace);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(textView, "scaleX", 0.5f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(textView, "scaleY", 0.5f, 1.0f);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(textView, "translationY", 500f, 0f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f);

        // Combine the animations in an AnimatorSet
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, translateY, fadeIn);
        animatorSet.setDuration(SPLASH_DURATION);
        animatorSet.start();

        // Start main activity after the splash duration
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }


}