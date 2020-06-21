package com.shridhar.todoandroidapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ToDo.class}, version = 1, exportSchema = false)
public abstract class ToDoAppDatabase extends RoomDatabase {

    public static final String DB_NAME = "to_do_app_db";
    public static final String TABLE_NAME_TODO = "todo";

    public abstract ToDoAppDao toDoAppDao();
}

