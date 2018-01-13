package com.example.hermon.helloword;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.R.attr.onClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = (Button) findViewById(R.id.btn);
        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //
                View contentLayout = findViewById(R.id.activity_main);
                Snackbar s = Snackbar.make(contentLayout,"Hello Again world",
                        Snackbar.LENGTH_LONG);
                ((TextView)s.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.MAGENTA);
                s.show();

            }
        });
    }
}
