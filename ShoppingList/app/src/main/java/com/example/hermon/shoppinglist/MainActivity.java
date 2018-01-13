package com.example.hermon.shoppinglist;

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
import android.widget.Toast;

import com.example.hermon.shoppinglist.adaptor.ListRecyclerAdaptor;
import com.example.hermon.shoppinglist.touch.ShoppingItemTouchHelperCallback;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_ID = "KEY_ITEM_ID";
    public static final int REQUEST_CODE_ADD = 101;
    public static final int REQUEST_CODE_EDIT = 102;

    private ListRecyclerAdaptor shoppingListAdaptor;
    private RecyclerView recyclerShoppingList;
    private  Toolbar toolbar;

    private int positionToEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((MainApplication)getApplication()).openRealm();

        setupUI();
    }

    private void setupUI() {
        setUpToolBar();
        setupRecyclerView();
    }

    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupRecyclerView() {
        recyclerShoppingList = (RecyclerView) findViewById(R.id.recyclerShoppingList);
        recyclerShoppingList.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerShoppingList.setLayoutManager(layoutManager);

        shoppingListAdaptor = new ListRecyclerAdaptor(this,
                                ((MainApplication)getApplication()).getRealmItemList());
        recyclerShoppingList.setAdapter(shoppingListAdaptor);

        // adding touch support
        ItemTouchHelper.Callback callback = new ShoppingItemTouchHelperCallback(shoppingListAdaptor);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerShoppingList);
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
            case R.id.action_addItem:
                startAddItemActivity();
                break;
            case R.id.action_deleteAll:
                shoppingListAdaptor.clearList();
                break;
            default:
                break;
        }


        return true;
    }


    private void startAddItemActivity() {
        Intent intentAddItemForm = new Intent();
        intentAddItemForm.setClass(MainActivity.this, AddActivity.class);
        startActivityForResult(intentAddItemForm, REQUEST_CODE_ADD);

    }

    public void openEditActivity(int index, String itemID) {
        positionToEdit = index;
        Intent startEdit = new Intent(this, EditActivity.class);
        startEdit.putExtra(KEY_ITEM_ID, itemID);
        startActivityForResult(startEdit, REQUEST_CODE_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == REQUEST_CODE_ADD) {
                    String itemID = data.getStringExtra(
                            AddActivity.KEY_ITEM);

                    shoppingListAdaptor.addToList(itemID);
                }
                else{
                    String itemID = data.getStringExtra(
                            EditActivity.KEY_ITEM_EDIT);
                    shoppingListAdaptor.updateItem(itemID,positionToEdit);
                }
                break;
            case RESULT_CANCELED:
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainApplication)getApplication()).closeRealm();
    }


}
