package com.example.hermon.guessgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    public static final String KEY_RAND = "KEY_RAND";
    private int RANDOM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final EditText etGuess = (EditText) findViewById(R.id.etGuess);
        Button btnGuess = (Button) findViewById(R.id.btnGuess);
        final TextView tvStatus = (TextView) findViewById(R.id.tvStatus);

        if (savedInstanceState != null &&
                savedInstanceState.containsKey(KEY_RAND)) {
            RANDOM = savedInstanceState.getInt(KEY_RAND);
        } else {
            generateRandom();
        }

        btnGuess.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    if (!etGuess.getText().toString().equals("")) {
                        int myNumber = Integer.parseInt(etGuess.getText().toString());
                        if (myNumber == RANDOM) {
                            //tvStatus.setText("You Win!");
                            startActivity(new Intent(GameActivity.this, ResultActivity.class));
                        } else if (RANDOM > myNumber) {
                            tvStatus.setText("ur # is Smaller");
                        } else {
                            tvStatus.setText("ur # is Bigger");
                        }
                    }
                    else{
                        etGuess.setError("Don't be an idiot");
                    }
                } catch(NumberFormatException e){
                    etGuess.setError("only +ve integers");
                }

            }
        });

    }

    private void generateRandom(){
        RANDOM = new Random(System.currentTimeMillis()).nextInt(3);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_RAND,RANDOM);
    }
}
