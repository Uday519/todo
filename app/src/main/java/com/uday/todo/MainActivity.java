package com.uday.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uday.todo.Database.Database;
import com.uday.todo.Database.ReadTasks;
import com.uday.todo.ViewModels.TasksViewModel;
import com.uday.todo.adapters.RecycleAdapter;
import com.uday.todo.models.Taskpojo;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecycleAdapter.OnTaskListener {

    private RecyclerView rv;
    private RecycleAdapter recycleAdapter;
    private Dialog dialog;
    private ReadTasks readTasks;
    private Database db;
    private TasksViewModel mViewModel;
    private EditText task_edittext;
    boolean isUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new Dialog(this);
        db = new Database(this.getApplicationContext());
        readTasks = ReadTasks.init(getApplicationContext());
        mViewModel = ViewModelProviders.of(this).get(TasksViewModel.class);
        mViewModel.init(getApplicationContext());
        mViewModel.getMtasks().observe(this, new Observer<List<Taskpojo>>() {
            @Override
            public void onChanged(@Nullable List<Taskpojo> expenses) {
                recycleAdapter.setTaskslist(expenses);
                recycleAdapter.notifyDataSetChanged();
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUpdate = false;
                addNewTask("",0);
            }
        });
        initRecyclerView();
    }

    private void addNewTask(final String task_detail, final int id) {
        Button close_dialog, submit;
        dialog.setContentView(R.layout.custom_pop_up);
        task_edittext = dialog.findViewById(R.id.task);
        submit = dialog.findViewById(R.id.submit);
        task_edittext.setText(task_detail);
        close_dialog = dialog.findViewById(R.id.close_dialog);
        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isUpdate){
                    if(db.updateTask(task_edittext.getText().toString(),id)){
                        mViewModel.addNewTask(readTasks.readTasks());
                        Toast toast = Toast.makeText(getApplicationContext(), "Task Updated Successfully", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else{
                    if(db.addNewTask(task_edittext.getText().toString())){
                        task_edittext.setText("");
                        mViewModel.addNewTask(readTasks.readTasks());
                        Toast toast = Toast.makeText(getApplicationContext(), "Task Added Successfully", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                }
            }
        });
        dialog.show();
    }

    public void initRecyclerView(){
        rv = findViewById(R.id.todo_recycleview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        recycleAdapter = new RecycleAdapter(mViewModel.getMtasks().getValue(), this, this);
        rv.setAdapter(recycleAdapter);
        rv.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                List<Taskpojo> expenses_temp = mViewModel.getMtasks().getValue();
                int id = expenses_temp.get(viewHolder.getAdapterPosition()).id;
                db.deleteTask(id);
                mViewModel.addNewTask(readTasks.readTasks());
                recycleAdapter.notifyDataSetChanged();

            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rv);
    }

    @Override
    public void onExpenseClick(int index) {
        isUpdate = true;
        List<Taskpojo> taskpojoList = mViewModel.getMtasks().getValue();
        addNewTask(taskpojoList.get(index).task,taskpojoList.get(index).id);
    }
}
