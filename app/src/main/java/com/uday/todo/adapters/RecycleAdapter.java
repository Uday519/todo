package com.uday.todo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uday.todo.Database.Database;
import com.uday.todo.R;
import com.uday.todo.models.Taskpojo;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecyclerViewHolder> {
    List<Taskpojo> taskslist;
    Context context;
    LayoutInflater inflater;
    private OnTaskListener onTaskListener;
    private Database db;

    public RecycleAdapter(List<Taskpojo> readExpenses, Context context, OnTaskListener onTaskListener) {
        this.taskslist = readExpenses;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.onTaskListener = onTaskListener;
        this.db = new Database(context);
    }

    public void setTaskslist(List<Taskpojo> taskslist) {
        this.taskslist = taskslist;
    }

    @NonNull
    @Override
    public RecycleAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.todo_row,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, onTaskListener);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapter.RecyclerViewHolder holder, final int position) {
        holder.task_textview.setText(taskslist.get(position).task);
        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.updateStatus(taskslist.get(position).id)){
                    notifyItemRemoved(position);
                    taskslist.remove(position);
                    notifyItemRangeChanged(position,taskslist.size());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskslist.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView task_textview;
        OnTaskListener onTaskListener;
        Button done;

        public RecyclerViewHolder(@NonNull View itemView, OnTaskListener onExpenseListener) {
            super(itemView);
            this.task_textview = itemView.findViewById(R.id.task);
            this.onTaskListener = onExpenseListener;
            this.done = itemView.findViewById(R.id.done);
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View view) {
            onTaskListener.onExpenseClick(getAdapterPosition());
        }
    }

    public interface OnTaskListener{
        void onExpenseClick(int index);
    }
}
