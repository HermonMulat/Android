package com.example.hermon.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static int  TRIVIAL = 6;
    private static int EASY = 7;
    private static int MEDIUM = 8;
    private static int HARD = 9;

    public static int DIFFICULTY_MODE = EASY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnTrivialMode = (Button) findViewById(R.id.btnTrivial);
        Button btnEasyMode = (Button) findViewById(R.id.btnEasy);
        Button btnMediumMode = (Button) findViewById(R.id.btnMedium);
        Button btnHardMode = (Button) findViewById(R.id.btnHard);


        btnTrivialMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start game
                DIFFICULTY_MODE = TRIVIAL;
                startActivity(new Intent().setClass(MainActivity.this, GameActivity.class));
            }
        });

        btnEasyMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start game
                DIFFICULTY_MODE = EASY;
                startActivity(new Intent().setClass(MainActivity.this, GameActivity.class));
            }
        });

        btnMediumMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start game
                DIFFICULTY_MODE = MEDIUM;
                startActivity(new Intent().setClass(MainActivity.this, GameActivity.class));
            }
        });

        btnHardMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start game
                DIFFICULTY_MODE = HARD;
                startActivity(new Intent().setClass(MainActivity.this, GameActivity.class));
            }
        });
    }
}
