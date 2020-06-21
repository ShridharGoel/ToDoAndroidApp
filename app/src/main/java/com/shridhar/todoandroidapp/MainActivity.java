package com.shridhar.todoandroidapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ToDoAppDatabase toDoAppDatabase;
    RecyclerView recyclerView;
    Adapter adapter;
    List<ToDo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toDoAppDatabase = Room.databaseBuilder(getApplicationContext(), ToDoAppDatabase.class, ToDoAppDatabase.DB_NAME).fallbackToDestructiveMigration().build();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(toDoAppDatabase);
        recyclerView.setAdapter(adapter);

        loadAllTodos();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_task_menu_option, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadAllTodosAndFilter(newText);
                return true;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_task:
                showAddTaskDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final TextInputLayout textInputLayout = new TextInputLayout(this);

        final EditText newTask = new EditText(this);
        newTask.setHint("Enter task here....");
        alert.setTitle("Add Task");

        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(50, 10, 50, 10);

        textInputLayout.setLayoutParams(params);

        textInputLayout.addView(newTask);
        container.addView(textInputLayout);

        alert.setView(container);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newTaskText = newTask.getText().toString();
                if (newTaskText.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Task cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                ToDo todo = new ToDo();
                todo.name = newTaskText;

                insertRow(todo);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    @SuppressLint("StaticFieldLeak")
    private void insertRow(final ToDo todo) {
        new AsyncTask<ToDo, Void, Long>() {
            @Override
            protected Long doInBackground(ToDo... params) {
                return toDoAppDatabase.toDoAppDao().insertTodo(params[0]);
            }

            @Override
            protected void onPostExecute(Long id) {
                todo.setTodoID(id);
                adapter.addRow(todo);
            }
        }.execute(todo);
    }

    @SuppressLint("StaticFieldLeak")
    private void loadAllTodos() {
        new AsyncTask<String, Void, List<ToDo>>() {
            @Override
            protected List<ToDo> doInBackground(String... params) {
                return toDoAppDatabase.toDoAppDao().fetchAllTodos();
            }

            @Override
            protected void onPostExecute(List<ToDo> todoList) {
                list = todoList;
                adapter.updateTodoList(todoList);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadAllTodosAndFilter(final String newText) {
        new AsyncTask<String, Void, List<ToDo>>() {
            @Override
            protected List<ToDo> doInBackground(String... params) {
                return toDoAppDatabase.toDoAppDao().fetchAllTodos();
            }

            @Override
            protected void onPostExecute(List<ToDo> todoList) {
                list = todoList;
                final List<ToDo> filteredModelList = adapter.filter(list, newText);
                adapter.updateTodoList(filteredModelList);
                recyclerView.scrollToPosition(0);
            }
        }.execute();
    }
}
