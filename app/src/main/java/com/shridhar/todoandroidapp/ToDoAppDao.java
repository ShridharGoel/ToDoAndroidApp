package com.shridhar.todoandroidapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ToDoAppDao {

    @Insert
    long insertTodo(ToDo todo);

    @Insert
    void insertTodoList(List<ToDo> todoList);

    @Query("SELECT * FROM " + ToDoAppDatabase.TABLE_NAME_TODO)
    List<ToDo> fetchAllTodos();

    @Query("SELECT * FROM " + ToDoAppDatabase.TABLE_NAME_TODO + " WHERE todoID = :todoId")
    ToDo fetchTodoListById(int todoId);

    @Update
    int updateTodo(ToDo todo);

    @Delete
    int deleteTodo(ToDo todo);
}
