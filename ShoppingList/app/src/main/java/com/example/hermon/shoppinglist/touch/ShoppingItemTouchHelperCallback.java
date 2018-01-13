package com.example.hermon.shoppinglist.touch;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.hermon.shoppinglist.adaptor.ListRecyclerAdaptor;

public class ShoppingItemTouchHelperCallback extends
        ItemTouchHelper.Callback {

    private ListRecyclerAdaptor myTouchHelperAdapter;

    public ShoppingItemTouchHelperCallback(ListRecyclerAdaptor myTouchHelperAdapter){
        this.myTouchHelperAdapter = myTouchHelperAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        myTouchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        myTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());

    }
}
