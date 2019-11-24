package com.uday.todo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todo";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "todo_tasks";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (\n" +
                "    " + "id" + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + "task" + " TEXT NOT NULL,\n" +
                "    " + "status" + " TINYINT NOT NULL\n" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(sql);
        onCreate(db);
    }

    public boolean addNewTask(String task){

        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        contentValues.put("status",1);
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_NAME,null, contentValues) != -1;

    }

    public Cursor getALLItems(){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM todo_tasks where status = ?", new String[]{String.valueOf(1)});
    }

    public boolean updateTask(String task, int id){

        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        SQLiteDatabase db = getWritableDatabase();
        return db.update(TABLE_NAME, contentValues, "id =?",new String[]{String.valueOf(id)}) == 1;

    }

    public boolean updateStatus(int id){

        ContentValues contentValues = new ContentValues();
        contentValues.put("status", 0);
        SQLiteDatabase db = getWritableDatabase();
        return db.update(TABLE_NAME, contentValues, "id =?",new String[]{String.valueOf(id)}) == 1;

    }


    public boolean deleteTask(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, "id =?", new String[]{String.valueOf(id)}) == 1;
    }
}