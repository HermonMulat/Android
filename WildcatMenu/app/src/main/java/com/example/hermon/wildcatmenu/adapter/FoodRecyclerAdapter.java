package com.example.hermon.wildcatmenu.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.hermon.wildcatmenu.ConfirmActivity;
import com.example.hermon.wildcatmenu.R;
import com.example.hermon.wildcatmenu.data.Food;
import com.example.hermon.wildcatmenu.touch.FoodTouchHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class FoodRecyclerAdapter
        extends RecyclerView.Adapter<FoodRecyclerAdapter.ViewHolder>
        implements FoodTouchHelper{

    private List<Food> menu;
    private List<Boolean> checked;
    private Context context;

    public FoodRecyclerAdapter(Context context, Realm realmMenu) {
        this.context = context;

        RealmResults<Food> foodResults = realmMenu.where(Food.class).findAll();

        menu = new ArrayList<Food>();
        checked = new ArrayList<Boolean>();

        for (int i = 0; i < foodResults.size(); i++) {
            menu.add(foodResults.get(i));
            checked.add(false);
        }
    }

    public FoodRecyclerAdapter(Context context, Realm realmMenu, List<String> IDs){
        this.context = context;
        menu = new ArrayList<Food>();

        Food currFood;
        for (int i=0; i<IDs.size(); i++){
            currFood = realmMenu.where(Food.class).equalTo("FoodId",IDs.get(i)).findFirst();
            menu.add(currFood);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.food_row,parent,false);

        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Food currItem = menu.get(position);
        holder.tvPrice.setText(currItem.getPrice());
        holder.tvDescription.setText(currItem.getDescription());
        holder.tvName.setText(currItem.getName());
        if (context instanceof ConfirmActivity){
            holder.cbAdd.setVisibility(View.INVISIBLE);
        }

        holder.cbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked.set(position,!checked.get(position));
            }
        });
        holder.setBackground(currItem.getCategory(),context);
    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    @Override
    public void onItemDismiss(int position) {
        menu.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int from, int to) {
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(menu, i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(menu, i, i - 1);
            }
        }

        notifyItemMoved(from, to);
    }

    public ArrayList<String> getCheckedIds() {
        ArrayList<String> Ids = new ArrayList<String>();
        Food f;
        for (int i=0; i<menu.size(); i++){
            f = menu.get(i);
            if (checked.get(i)) {
                Ids.add(f.getFoodID());
            }
        }
        return Ids;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName,tvDescription,tvPrice;
        private CheckBox cbAdd;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            cbAdd = (CheckBox) itemView.findViewById(R.id.cbAdd);

        }
        public void setBackground(String category,Context c){
            System.out.println("Setting background color: "+ category);
            if (category.equals(c.getResources().getString(R.string.deli))){
                itemView.setBackgroundColor(ContextCompat.getColor(c, R.color.Deli));
            }
            else if (category.equals(c.getResources().getString(R.string.oven))){
                itemView.setBackgroundColor(ContextCompat.getColor(c, R.color.Oven));
            }
            else if (category.equals(c.getResources().getString(R.string.grill))){
                itemView.setBackgroundColor(ContextCompat.getColor(c, R.color.Grill));
            }


        }
    }
}
