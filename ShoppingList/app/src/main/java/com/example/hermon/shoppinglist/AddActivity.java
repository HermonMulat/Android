package com.example.hermon.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.hermon.shoppinglist.data.Item;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class AddActivity extends AppCompatActivity {
    public static final String KEY_ITEM = "KEY_ITEM";
    private AutoCompleteTextView categoryList;

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_price)
    EditText et_price;
    @BindView(R.id.et_description)
    EditText et_description;
    @BindView(R.id.ac_tv_category)
    AutoCompleteTextView ac_tv_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        System.out.println("Here");
        setupDropdown();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_addItem)
    public void addShoppingItem(Button btn){
        Intent intentResult = new Intent();

        getRealm().beginTransaction();
        Item newItem = getRealm().createObject(Item.class, UUID.randomUUID().toString());
        newItem.setName(et_name.getText().toString());
        newItem.setDescription(et_description.getText().toString());
        newItem.setCategory(ac_tv_category.getText().toString());
        newItem.setPriceEST(et_price.getText().toString());
        newItem.setPurchased(false);
        getRealm().commitTransaction();

        intentResult.putExtra(KEY_ITEM, newItem.getItemID());
        setResult(RESULT_OK, intentResult);

        finish();
    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmItemList();
    }

    private void setupDropdown() {
        categoryList = (AutoCompleteTextView) findViewById(R.id.ac_tv_category);
        String[] categories = getResources().getStringArray(R.array.category_selection);
        ArrayAdapter<String> categoryAdaptor = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, categories);

        categoryList.setAdapter(categoryAdaptor);
    }
}