package com.example.hermon.wildcatmenu.touch;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class FoodTouchHelperCallback extends ItemTouchHelper.Callback{
    private FoodTouchHelper foodTouchHelper;

    public FoodTouchHelperCallback(FoodTouchHelper foodTouchHelper) {
        this.foodTouchHelper = foodTouchHelper;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        foodTouchHelper.onItemMove(viewHolder.getAdapterPosition(),
                target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        foodTouchHelper.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
