package com.example.hermon.tictac;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import view.TicTacView;

public class MainActivity extends AppCompatActivity {

    private TextView tvData;
    private Chronometer timer,timer2;
    private long LAST_STOP1 = 0,LAST_STOP2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvData = (TextView) findViewById(R.id.tvData);
        timer = (Chronometer) findViewById(R.id.timer);
        timer2 = (Chronometer) findViewById(R.id.timer2);

        final TicTacView gameView = (TicTacView)findViewById(R.id.tictacview);

        Button btnClear = (Button)findViewById(R.id.btnClear);

        btnClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gameView.reset();
            }
        });

        ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout)findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmerAnimation();
        startTimer(0);
    }

    public void setMessage(String text) {
        tvData.setText(text);
    }

    public void restartTimer(int which){
        if (which%2 == 0) {
            timer.setBase(SystemClock.elapsedRealtime());
            LAST_STOP1 = 0;
        }
        else{
            timer2.setBase(SystemClock.elapsedRealtime());
            LAST_STOP2 = 0;
        }
    }

    public void startTimer(int which){
        if (which %2 == 0){
            if (LAST_STOP1 == 0) {
                timer.setBase(SystemClock.elapsedRealtime());
            }
            else {
                long interval = (SystemClock.elapsedRealtime() - LAST_STOP1);
                timer.setBase( timer.getBase() + interval );
            }
            timer.start();
        }
        else{
            if (LAST_STOP2 == 0) {
                timer2.setBase(SystemClock.elapsedRealtime());
            } else {
                long interval = (SystemClock.elapsedRealtime() - LAST_STOP2);
                timer2.setBase(timer2.getBase() + interval);
            }
            timer2.start();
        }
    }

    public void stopTimer(int which){
        if (which %2 == 0){
            LAST_STOP1 = SystemClock.elapsedRealtime();
            timer.stop();
        }
        else{
            LAST_STOP2 = SystemClock.elapsedRealtime();
            timer2.stop();
        }
    }
}
