package hu.ait.wildcatmenu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.ait.wildcatmenu.adapter.FoodRecyclerAdapter;

public class StudentActivity extends AppCompatActivity {
    public static final String KEY_IDS = "foodIDs";
    private FoodRecyclerAdapter foodRecyclerAdapter;
    private RecyclerView recyclerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        ((MainApplication)getApplication()).openRealm();

        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        setRecyclerView();
    }

    @OnClick(R.id.btn_continue)
    public void continueOrder(){
        ArrayList<String> IDs = foodRecyclerAdapter.getCheckedIds();
        startConfirmActivity(IDs);
    }

    private void startConfirmActivity(ArrayList<String> IDs) {
        Intent confirmAct = new Intent(this,ConfirmActivity.class);
        confirmAct.putStringArrayListExtra(KEY_IDS,IDs);
        startActivity(confirmAct);
    }

    private void setRecyclerView() {
        recyclerMenu = (RecyclerView) findViewById(R.id.recyclerFood);
        recyclerMenu.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);

        foodRecyclerAdapter = new FoodRecyclerAdapter(this,
                ((MainApplication)getApplication()).getRealmDB());

        recyclerMenu.setAdapter(foodRecyclerAdapter);
    }
}
