package hu.ait.wildcatmenu.staff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hu.ait.wildcatmenu.MainApplication;
import hu.ait.wildcatmenu.R;

import hu.ait.wildcatmenu.data.Food;
import hu.ait.wildcatmenu.data.Order;
import io.realm.Realm;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Orders. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link OrderDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class OrderListActivity extends AppCompatActivity {

    public static final String REMOVE_KEY = "key";

    public boolean mTwoPane;
    private RecyclerView recyclerOrder;
    private OrderRecyclerAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        ((MainApplication)getApplication()).openRealm();

        recyclerOrder = (RecyclerView) findViewById(R.id.recyclerOrder);
        setupRecyclerView();

        if (findViewById(R.id.order_detail_container) != null) {
            mTwoPane = true;
        }

        if (getIntent().hasExtra(REMOVE_KEY)) {
            String key = getIntent().getStringExtra(REMOVE_KEY);
            orderAdapter.removeOrder(key);
        }
        initOrdersListener();

    }

    private void initOrdersListener() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        ordersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Order newOrder = dataSnapshot.getValue(Order.class);
                orderAdapter.addOrder(newOrder, dataSnapshot.getKey());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerOrder.setLayoutManager(layoutManager);
        orderAdapter = new OrderRecyclerAdapter(this, ((MainApplication)getApplication()).getRealmDB());
        recyclerOrder.setAdapter(orderAdapter);
    }

    public List<Food> getFoodFromID(List<String> foodID) {
        List<Food> menu = new ArrayList<Food>();

        Food currFood;
        Realm realmMenu = ((MainApplication)getApplication()).getRealmDB();
        for (int i=0; i<foodID.size(); i++){
            currFood = realmMenu.where(Food.class).equalTo("FoodID",foodID.get(i)).findFirst();
            menu.add(currFood);
        }

        return menu;
    }

    public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder>{

        private List<Order> orderList = new ArrayList<Order>();
        private List<String> keys = new ArrayList<String>();
        private Context context;
        private DatabaseReference ordersRef;
        private Realm realmMenu;

        public OrderRecyclerAdapter(Context context, Realm realmMenu) {
            this.context = context;
            ordersRef = FirebaseDatabase.getInstance().getReference("orders");
            this.realmMenu = realmMenu;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_list_content, parent, false);
            return new ViewHolder(rowView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Order currOrder = orderList.get(position);
            holder.order = currOrder;
            holder.tvName.setText(currOrder.getCustomerName());
            holder.tvNumber.setText(""+currOrder.getMenuItems().size());
            List<Food> foodItems = getFoodFromID(currOrder.getMenuItems());
            String items = makeDescription(foodItems);
            holder.tvItems.setText(items);

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putStringArrayList(OrderDetailFragment.ARG_ITEM_ID,
                                (ArrayList<String>) currOrder.getMenuItems());
                        arguments.putString(OrderDetailFragment.ARG_NAME,
                                currOrder.getCustomerName());
                        arguments.putString(OrderDetailFragment.ARG_KEY, keys.get(position));
                        OrderDetailFragment fragment = new OrderDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.order_detail_container, fragment)
                                .commit();
                    } else {
                        Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                        intent.putStringArrayListExtra(OrderDetailFragment.ARG_ITEM_ID,
                                (ArrayList<String>) currOrder.getMenuItems());
                        intent.putExtra(OrderDetailFragment.ARG_NAME, currOrder.getCustomerName());
                        intent.putExtra(OrderDetailFragment.ARG_KEY, keys.get(position));
                        startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }

        public void addOrder(Order order, String key) {
            orderList.add(order);
            keys.add(key);
            notifyDataSetChanged();
        }

        public void removeOrder(String key) {
            ordersRef.child(key).removeValue();
            int index = keys.indexOf(key);
            if (index != -1) {
                orderList.remove(index);
                keys.remove(index);
                notifyItemRemoved(index);
            }
        }

        private String makeDescription(List<Food> foodItems) {
            String desc = foodItems.get(0).getName();
            for (int i = 1; i < foodItems.size(); i++) {
                desc = desc + ", " + foodItems.get(i).getName();
            }
            return desc;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private View view;
            private TextView tvName;
            private TextView tvItems;
            private TextView tvNumber;
            private Order order;

            public ViewHolder(View itemView) {
                super(itemView);

                view = itemView;
                tvName = (TextView) itemView.findViewById(R.id.tvCustomerName);
                tvItems = (TextView) itemView.findViewById(R.id.tvItems);
                tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            }
        }
    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmDB();
    }

    @Override
    protected void onDestroy() {
        ((MainApplication)getApplication()).closeRealm();
        super.onDestroy();
    }
}
