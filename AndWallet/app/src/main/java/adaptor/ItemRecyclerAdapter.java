package adaptor;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.hermon.andwallet.MainActivity;
import com.example.hermon.andwallet.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import data.Item;
import touch.ItemTouchHelperAdapter;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private List<Item> itemList;
    private static double balance;
    private Context context;

    public ItemRecyclerAdapter(Context context) {
        this.context = context;
        balance = 0;
        itemList = new ArrayList<Item>();
    }

    @Override
    public ItemRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ItemRecyclerAdapter.ViewHolder holder, int position) {
        holder.tvItem.setText(itemList.get(position).getTitle());
        holder.tvPrice.setText(itemList.get(position).getPriceDisplay());
        if (itemList.get(position).isIncome()){
            holder.ivIcon.setImageResource(R.drawable.inc);
        }
        else{
            holder.ivIcon.setImageResource(R.drawable.exp);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        Item removed = itemList.remove(position);
        if (removed.isIncome()){
            balance -= removed.getPrice();
        }
        else{
            balance += removed.getPrice();
        }
        ((MainActivity) context).updateBalance(balance);

        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
//        itemList.add(to, itemList.get(from));
//        itemList.remove(from);
//
//        notifyItemMoved(from, to);

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(itemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(itemList, i, i - 1);
            }
        }


        notifyItemMoved(fromPosition, toPosition);

    }
    public String getIncome(){
        double total = 0.0;

        for (Item i: itemList) {
            if (i.isIncome()) {
                total += i.getPrice();
            }
        }
        return String.format("%.2f",total)+"$";

    }
    public String getExpenses(){
        double total = 0.0;

        for (Item i: itemList) {
            if (!i.isIncome()) {
                total += i.getPrice();
            }
        }
        return String.format("%.2f",total)+"$";
    }
    public void clear(){
        itemList.clear();
        balance = 0;
        ((MainActivity) context).updateBalance(balance);
        notifyDataSetChanged();


    }

    public void addAnItem(Item anItem){
        if (anItem.isIncome()){
            balance += anItem.getPrice();
        }
        else{
            balance -= anItem.getPrice();
        }

        itemList.add(0,anItem);
        notifyItemInserted(0);
    }

    public static double getBalance(){
        return balance;
    }
    public static String getBalanceStr(){
        return String.format("%.2f",balance)+"$";
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivIcon;
        private TextView tvItem;
        private TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById((R.id.ivIcon));
            tvItem = (TextView) itemView.findViewById((R.id.tvItem));
            tvPrice = (TextView) itemView.findViewById((R.id.tvPrice));
        }
    }
}
