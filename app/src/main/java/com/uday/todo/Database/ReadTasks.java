package com.uday.todo.Database;

import android.content.Context;
import android.database.Cursor;

import androidx.lifecycle.MutableLiveData;

import com.uday.todo.models.Taskpojo;

import java.util.ArrayList;
import java.util.List;

public class ReadTasks {

    private Database db;
    private static ReadTasks instance = null;

    public static ReadTasks init(Context context){
        if(instance == null){
            instance = new ReadTasks(context);
        }
        return instance;
    }

    public ReadTasks(Context context){
        this.db = new Database(context);
    }

    public List<Taskpojo> readTasks() {
        Cursor cursor = db.getALLItems();
        List<Taskpojo> tasks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Taskpojo temptask = new Taskpojo(cursor.getString(cursor.getColumnIndex("task")),
                        Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("status"))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
                tasks.add(temptask);
            } while (cursor.moveToNext());


        }
        return tasks;
    }

    public MutableLiveData<List<Taskpojo>> readMutableExpenses() {
        MutableLiveData<List<Taskpojo>> data = new MutableLiveData<>();
        data.setValue(readTasks());
        return data;
    }
}
