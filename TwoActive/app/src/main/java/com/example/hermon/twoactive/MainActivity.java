package com.example.hermon.twoactive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static String KEY_DATA = "Hello WOrld!";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText etData = (EditText) findViewById(R.id.etData);
        Button btnOk = (Button) findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start the the 2nd activity
                Intent intentShowSecond = new Intent();
                intentShowSecond.setClass(MainActivity.this, Main2Activity.class);

                intentShowSecond.putExtra(KEY_DATA,etData.getText().toString());

                startActivity(intentShowSecond);
            }
        });
    }
}
