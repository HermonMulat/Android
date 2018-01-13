package com.example.hermon.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.hermon.shoppinglist.data.Item;

import org.w3c.dom.Text;

import butterknife.BindView;
import io.realm.Realm;

public class EditActivity extends AppCompatActivity {
    public static final String KEY_ITEM_EDIT = "KEY_ITEM_EDIT";
    private Item itemToEdit;
    private EditText et_name,et_price,et_description;
    private AutoCompleteTextView ac_tv_category;
    private CheckBox cb_purchased;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view);
        if (getIntent().hasExtra(MainActivity.KEY_ITEM_ID)) {
            String itemID = getIntent().getStringExtra(MainActivity.KEY_ITEM_ID);
            itemToEdit = getRealm().where(Item.class)
                    .equalTo("ItemID", itemID)
                    .findFirst();
        }

        bindViews();
        Button btnSave = (Button) findViewById(R.id.btn_saveChanges);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });

        if (itemToEdit != null) {
            fillFields();
        }
    }

    private void bindViews() {
        et_name = (EditText)findViewById(R.id.et_editName);
        et_price = (EditText)findViewById(R.id.et_editPrice);
        et_description = (EditText)findViewById(R.id.et_editDescription);
        ac_tv_category = (AutoCompleteTextView) findViewById(R.id.ac_tv_editCategory);
        cb_purchased = (CheckBox)findViewById(R.id.cb_editPurchased);
    }

    private void fillFields() {
        et_price.setText(itemToEdit.getPrice());
        et_name.setText(itemToEdit.getName());
        et_description.setText(itemToEdit.getDescription());
        ac_tv_category.setText(itemToEdit.getCategory());
        cb_purchased.setChecked(itemToEdit.isPurchased());
    }


    private void saveChanges() {

        Intent intentResult = new Intent();

        getRealm().beginTransaction();

        itemToEdit.setName(et_name.getText().toString());
        itemToEdit.setDescription(et_description.getText().toString());
        itemToEdit.setCategory(ac_tv_category.getText().toString());
        itemToEdit.setPriceEST(et_price.getText().toString());
        itemToEdit.setPurchased(cb_purchased.isChecked());

        getRealm().commitTransaction();

        intentResult.putExtra(KEY_ITEM_EDIT, itemToEdit.getItemID());
        setResult(RESULT_OK, intentResult);
        finish();

    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmItemList();
    }
}
