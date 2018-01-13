package com.example.hermon.andwallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import adaptor.ItemRecyclerAdapter;

public class SummaryActivity extends AppCompatActivity {

    private TextView incomeVal,expenseVal,finalBalance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        incomeVal = (TextView) findViewById(R.id.tvIncometotal);
        expenseVal = (TextView) findViewById(R.id.tvExpensetotal);
        finalBalance = (TextView) findViewById(R.id.tvFinalBalance);

        setText();
    }

    public void setText(){
        incomeVal.setText(getIntent().getStringExtra(MainActivity.INCOMETOTAL));
        expenseVal.setText(getIntent().getStringExtra(MainActivity.EXPTOTAL));
        finalBalance.setText(getIntent().getStringExtra(MainActivity.BALANCE));
    }
}
