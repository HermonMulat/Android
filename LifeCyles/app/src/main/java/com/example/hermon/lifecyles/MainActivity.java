package com.example.hermon.lifecyles;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public static final String TAG_LIFE = "TAG_LIFE";
    private int score = 7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG_LIFE,"ON Create CALL");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG_LIFE,"ON Start CALL");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG_LIFE,"ON Resume CALL");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG_LIFE,"ON Pause CALL");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG_LIFE,"ON Stop CALL");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG_LIFE,"ON Destroy CALL");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt("Score",score);

    }
}
