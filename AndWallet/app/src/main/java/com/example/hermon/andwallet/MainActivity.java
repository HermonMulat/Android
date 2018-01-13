package com.example.hermon.andwallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import adaptor.ItemRecyclerAdapter;
import data.Item;
import touch.ItemTouchHelperCallback;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public static String INCOMETOTAL = "inc";
    public static String EXPTOTAL = "exp";
    public static String BALANCE = "bal";

    RecyclerView recycleItemList;
    ItemRecyclerAdapter itemRecyclerAdapter;

    EditText etItemTitle,etValue;
    ToggleButton tbIsIncome;
    TextView tvBalance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // add support for tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // init edit views
        etItemTitle = (EditText) findViewById(R.id.etItemTitle);
        etValue = (EditText) findViewById(R.id.etValue);
        tbIsIncome = (ToggleButton) findViewById(R.id.tbIsIncome);
        tvBalance = (TextView) findViewById(R.id.tvBalance);

        // init recycler and adaptor
        recycleItemList = (RecyclerView) findViewById(R.id.recycleItemList);
        recycleItemList.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleItemList.setLayoutManager(layoutManager);

        itemRecyclerAdapter  = new ItemRecyclerAdapter(this);
        recycleItemList.setAdapter(itemRecyclerAdapter );

        // enable touch
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(itemRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recycleItemList);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSave)
    public void saveItem(){
        try {
            String itemTitle = etItemTitle.getText().toString();
            double itemValue;
            boolean isIncome = tbIsIncome.isChecked();

            if (!itemTitle.equals("")) {
                itemValue = Double.parseDouble(etValue.getText().toString());
                itemRecyclerAdapter.addAnItem(new Item(itemTitle,itemValue,isIncome));
                recycleItemList.scrollToPosition(0);
                updateBalance(ItemRecyclerAdapter.getBalance());

            } else {
                if (itemTitle.equals("")) {
                    etItemTitle.setError("Please Provide Item Name !");
                }
            }
        }
        catch(NumberFormatException e){
            etValue.setError("Please use positive numbers only !");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                itemRecyclerAdapter.clear();
                break;
            case R.id.action_summary:
                Intent intentShowSummary = new Intent();
                intentShowSummary.setClass(MainActivity.this, SummaryActivity.class);

                intentShowSummary.putExtra(INCOMETOTAL,itemRecyclerAdapter.getIncome());
                intentShowSummary.putExtra(EXPTOTAL,itemRecyclerAdapter.getExpenses());
                intentShowSummary.putExtra(BALANCE,ItemRecyclerAdapter.getBalanceStr());
                startActivity(intentShowSummary);
            default:
                break;
        }

        return true;
    }

    public void updateBalance(double value){
        tvBalance.setText(String.format("%.2f",value) + "$");
    }
}
