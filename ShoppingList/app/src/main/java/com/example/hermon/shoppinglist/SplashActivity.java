package com.example.hermon.shoppinglist;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Animation animation = AnimationUtils.loadAnimation(SplashActivity.this,
                R.anim.play_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent mainInt = new Intent(SplashActivity.this,MainActivity.class);
                mainInt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainInt);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        final ImageView load = (ImageView) findViewById(R.id.iv_loading);
        load.setAnimation(animation);
    }
}
