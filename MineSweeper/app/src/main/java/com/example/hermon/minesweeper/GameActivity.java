package com.example.hermon.minesweeper;

import android.graphics.Typeface;
import android.media.Image;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import View.FieldView;

import Model.GameModel;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // initialize game model
        int n = MainActivity.DIFFICULTY_MODE;
        GameModel.initializeModel(n);

        final FieldView mineField = (FieldView) findViewById(R.id.fieldView);

        final ImageView ivReset = (ImageView) findViewById(R.id.iVReset);
        ivReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivReset.setImageResource(R.drawable.normal);
                mineField.reset();
            }
        });

        TextView counter = (TextView) findViewById(R.id.mineCount);
        Chronometer timer = (Chronometer) findViewById(R.id.timer);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/DS-DIGI.TTF");

        counter.setTypeface(custom_font);
        timer.setTypeface(custom_font);

    }

    public void changeImage(boolean winCondition){
        ImageView ivReset = (ImageView) findViewById(R.id.iVReset);
        if (winCondition) {
            ivReset.setImageResource(R.drawable.win);
            Toast.makeText(this, "You Won!", Toast.LENGTH_SHORT).show();
        }
        else{
            ivReset.setImageResource(R.drawable.loss);
            Toast.makeText(this, "You lost!", Toast.LENGTH_SHORT).show();
        }
    }


    public void resetTimer(){
        Chronometer timer = (Chronometer) findViewById(R.id.timer);
        timer.stop();
        timer.setBase(SystemClock.elapsedRealtime());
    }

    public void stopTimer(){
        Chronometer timer = (Chronometer) findViewById(R.id.timer);
        timer.stop();

    }
    public void startTimer(){
        Chronometer timer = (Chronometer) findViewById(R.id.timer);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
    }

    public void setMineCount(int n){
        TextView mineCount = (TextView) findViewById(R.id.mineCount);
        mineCount.setText(String.format("%03d", n));
    }
}
