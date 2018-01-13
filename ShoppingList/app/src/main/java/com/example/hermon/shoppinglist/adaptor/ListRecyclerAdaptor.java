package com.example.hermon.shoppinglist.adaptor;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hermon.shoppinglist.EditActivity;
import com.example.hermon.shoppinglist.MainActivity;
import com.example.hermon.shoppinglist.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.example.hermon.shoppinglist.data.Item;
import io.realm.Realm;
import io.realm.RealmResults;
import com.example.hermon.shoppinglist.touch.ShoppingTouchHelperAdapter;

public class ListRecyclerAdaptor
        extends RecyclerView.Adapter<ListRecyclerAdaptor.ViewHolder>
        implements ShoppingTouchHelperAdapter{

    private List<Item> ShoppingList;
    private Context context;

    private Realm realmItemList;

    public ListRecyclerAdaptor(Context context, Realm realmItems){
        this.context = context;

        realmItemList = realmItems;

        RealmResults<Item> shoppingListResult  = realmItemList.where(Item.class).findAll();

        ShoppingList = new ArrayList<Item>();

        for (int i = 0; i < shoppingListResult.size(); i++) {
            ShoppingList.add(shoppingListResult.get(i));
        }
    }
    @Override
    public ListRecyclerAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.shopping_item, parent, false);

        ListRecyclerAdaptor.ViewHolder itemHolder = new ViewHolder(itemView);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(final ListRecyclerAdaptor.ViewHolder holder, int position) {
        holder.tv_name.setText(ShoppingList.get(position).getName());
        holder.tv_price.setText(ShoppingList.get(position).getPriceEST());
        holder.cb_purchase.setChecked(ShoppingList.get(position).isPurchased());
        switch (ShoppingList.get(position).getCategory()){
            case "Food and Drinks":
                holder.iv_category.setImageResource(R.drawable.fd);
                break;
            case "Electronics":
                holder.iv_category.setImageResource(R.drawable.e);
                break;
            case "Clothing":
                holder.iv_category.setImageResource(R.drawable.shoe);
                break;
            case "Office Supplies":
                holder.iv_category.setImageResource(R.drawable.os2);
                break;
            default:
                holder.iv_category.setImageResource(R.drawable.shop);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).openEditActivity(holder.getAdapterPosition(),
                        ShoppingList.get(holder.getAdapterPosition()).getItemID());
            }
        });


        holder.cb_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realmItemList.beginTransaction();
                ShoppingList.get(holder.getAdapterPosition()).
                        setPurchased(holder.cb_purchase.isChecked());
                realmItemList.commitTransaction();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ShoppingList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        // remove from realm
        realmItemList.beginTransaction();
        ShoppingList.get(position).deleteFromRealm();
        realmItemList.commitTransaction();
        // remove from list
        ShoppingList.remove(position);
        notifyItemRemoved(position);

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(ShoppingList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(ShoppingList, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);

    }

    public void addToList(String itemID) {
        realmItemList.beginTransaction();
        Item ItemToAdd = realmItemList.where(Item.class).equalTo("ItemID",itemID).findFirst();
        realmItemList.commitTransaction();

        ShoppingList.add(0, ItemToAdd);
        notifyItemInserted(0);
    }

    public void updateItem(String itemId, int positionToEdit) {
        Item item = realmItemList.where(Item.class)
                .equalTo("ItemID", itemId)
                .findFirst();

        ShoppingList.set(positionToEdit, item);

        notifyItemChanged(positionToEdit);
    }

    public void clearList(){
        realmItemList.beginTransaction();
        RealmResults<Item> allItems  = realmItemList.where(Item.class).findAll();
        allItems.deleteAllFromRealm();
        realmItemList.commitTransaction();

        ShoppingList.clear();
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cb_purchase;
        private TextView tv_name,tv_price;
        private ImageView iv_category;

        public ViewHolder(final View itemView) {
            super(itemView);
            cb_purchase = (CheckBox)itemView.findViewById(R.id.cb_purchase);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            iv_category = (ImageView) itemView.findViewById(R.id.iv_category);
        }

    }
}
