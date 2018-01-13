package com.example.hermon.wildcatmenu;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Button;


import com.example.hermon.wildcatmenu.adapter.FoodRecyclerAdapter;
import com.example.hermon.wildcatmenu.data.Order;
import com.example.hermon.wildcatmenu.touch.FoodTouchHelperCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmActivity extends AppCompatActivity {

    @BindView(R.id.btnPlaceOrder)
    Button btnPlaceOrder;

    private RecyclerView recyclerFood;
    private FoodRecyclerAdapter foodAdapter;

    private List<String> foodId = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        ButterKnife.bind(this);

        ((MainApplication)getApplication()).openRealm();

        foodId = getIntent().getStringArrayListExtra(StudentActivity.KEY_IDS);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerFood = (RecyclerView) findViewById(R.id.recyclerFood);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerFood.setLayoutManager(layoutManager);

        foodAdapter = new FoodRecyclerAdapter(this,
                ((MainApplication)getApplication()).getRealmDB(),
                foodId);
        recyclerFood.setAdapter(foodAdapter);

        ItemTouchHelper.Callback callback = new FoodTouchHelperCallback(foodAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerFood);
    }

    @OnClick(R.id.btnPlaceOrder)
    public void placeOrderClick() {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        String key = database.child("orders").push().getKey();

        Order order =  new Order(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                foodId,
                ((MainApplication)getApplication()).getRealmDB()
        );
        order.setCustomerName(FirebaseAuth.getInstance()
                .getCurrentUser().getDisplayName());

        database.child("orders").child(key).setValue(order);

        finish();
    }

    @Override
    protected void onDestroy() {
        ((MainApplication)getApplication()).closeRealm();
        super.onDestroy();
    }
}

