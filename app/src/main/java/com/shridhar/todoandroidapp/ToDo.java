package com.shridhar.todoandroidapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = ToDoAppDatabase.TABLE_NAME_TODO)
public class ToDo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public Long todoID;

    public String name;

    public boolean isDone;

    public boolean important;

    public Long getTodoID() {
        return todoID;
    }

    public void setTodoID(Long todoID) {
        this.todoID = todoID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean getImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }
}