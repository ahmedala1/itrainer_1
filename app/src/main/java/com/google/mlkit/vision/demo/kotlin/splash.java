package com.google.mlkit.vision.demo.kotlin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.demo.R;

public class splash extends AppCompatActivity {
    Animation anim;


    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView=findViewById(R.id.splash);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim); // Create the animation.

        imageView.setAnimation(anim);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash.this, login.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
        imageView.startAnimation(anim);

    }

}