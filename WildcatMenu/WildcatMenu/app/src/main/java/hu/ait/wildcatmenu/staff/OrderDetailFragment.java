package hu.ait.wildcatmenu.staff;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import hu.ait.wildcatmenu.MainApplication;
import hu.ait.wildcatmenu.R;
import hu.ait.wildcatmenu.adapter.FoodRecyclerAdapter;
import hu.ait.wildcatmenu.data.Food;
import io.realm.Realm;

/**
 * A fragment representing a single Order detail screen.
 * This fragment is either contained in a {@link OrderListActivity}
 * in two-pane mode (on tablets) or a {@link OrderDetailActivity}
 * on handsets.
 */
public class OrderDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_NAME = "name";
    public static final String ARG_KEY = "key";

    private List<String> foodID;
    private String name;
    private String key;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID) && getArguments().containsKey(ARG_NAME)) {

            name = getArguments().getString(ARG_NAME);
            foodID = getArguments().getStringArrayList(ARG_ITEM_ID);
            key = getArguments().getString(ARG_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_detail, container, false);

        RecyclerView recyclerFood = (RecyclerView) rootView.findViewById(R.id.recyclerFood);
        Button btnComplete = (Button) rootView.findViewById(R.id.btnComplete);
        TextView tvOrderName = (TextView) rootView.findViewById(R.id.tvOrderName);

        tvOrderName.setText(name + "'s Order");

        setupRecyclerView(recyclerFood);

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrderListActivity.class);
                intent.putExtra(OrderListActivity.REMOVE_KEY, key);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void setupRecyclerView(RecyclerView recyclerFood) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerFood.setLayoutManager(layoutManager);

        Realm realmMenu;
        if (getContext() instanceof OrderListActivity)
            realmMenu = ((OrderListActivity)getContext()).getRealm();
        else {
            realmMenu = ((OrderDetailActivity)getContext()).getRealm();
        }
        FoodRecyclerAdapter foodAdapter = new FoodRecyclerAdapter(getContext(),
                realmMenu,
                foodID);
        recyclerFood.setAdapter(foodAdapter);
    }
}
