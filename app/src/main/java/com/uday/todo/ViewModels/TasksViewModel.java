package com.uday.todo.ViewModels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.uday.todo.Database.ReadTasks;
import com.uday.todo.models.Taskpojo;

import java.util.List;

public class TasksViewModel extends ViewModel {

    private MutableLiveData<List<Taskpojo>> mtasks;
    private ReadTasks readTasks;

    public MutableLiveData<List<Taskpojo>> getMtasks() {
        return mtasks;
    }

    public void init(Context context){
        if(mtasks != null){
            return;
        }
        readTasks = ReadTasks.init(context);
        mtasks = readTasks.readMutableExpenses();

    }
    public void addNewTask(List<Taskpojo> tasks){
        mtasks.postValue(tasks);
    }
}
