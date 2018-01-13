package adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.hermon.recylerdemo.R;

import java.util.ArrayList;
import java.util.List;
import data.ToDo;
import touch.TodoTouchHelperAdapter;

public class ToDoRecyclerAdaptor
        extends RecyclerView.Adapter<ToDoRecyclerAdaptor.ViewHolder>
        implements TodoTouchHelperAdapter{

    private List<ToDo> todoList;
    private Context context;

    public ToDoRecyclerAdaptor(Context context){
        todoList = new ArrayList<ToDo> ();
        this.context = context;

        for (int i = 0; i < 20; i++) {
            todoList.add(new ToDo("TODO"+i,false));
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(
                R.layout.todo_row, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvTodo.setText(todoList.get(position).getTodoText());
        holder.cbDone.setChecked(todoList.get(position).isDone());

        holder.cbDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoList.get(holder.getAdapterPosition()).setDone(holder.cbDone.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        todoList.remove(position);
        // refreshes the whole list
        //notifyDataSetChanged();
        notifyItemRemoved(position); // refreshes only relevant part
    }

    @Override
    public void onItemMove(int from, int to) {
        todoList.add(to, todoList.get(from));
        todoList.remove(from);

        notifyItemMoved(from, to);

    }


    public void addTodo(ToDo todo){
        todoList.add(0,todo);
        notifyItemInserted(0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

            private CheckBox cbDone;
            private TextView tvTodo;

            public ViewHolder (View itemView){
                super(itemView);
                cbDone = (CheckBox) itemView.findViewById((R.id.cbDone));
                tvTodo = (TextView) itemView.findViewById((R.id.tvTodo));
            }
    }

}
