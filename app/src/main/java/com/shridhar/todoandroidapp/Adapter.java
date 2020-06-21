package com.shridhar.todoandroidapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<ToDo> todoList;
    private ToDoAppDatabase toDoAppDatabase;

    public Adapter(ToDoAppDatabase toDoAppDatabase) {
        this.toDoAppDatabase = toDoAppDatabase;
        todoList = new ArrayList<>();
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        Adapter.ViewHolder viewHolder = new Adapter.ViewHolder(view, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final Adapter.ViewHolder holder, int position) {
        final ToDo todo = todoList.get(position);
        holder.name.setText(todo.name);
        holder.important.setChecked(todo.getImportant());
        if (todo.getImportant()) {
            holder.important.setTextColor(Color.parseColor("#E50000"));
        } else {
            holder.important.setTextColor(Color.DKGRAY);
        }
        if (todo.getDone()) {
            holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.check.setVisibility(View.GONE);
            holder.important.setVisibility(View.GONE);
            holder.undo.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.name.setPaintFlags(holder.name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.check.setVisibility(View.VISIBLE);
            holder.important.setVisibility(View.VISIBLE);
            holder.undo.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todo.setDone(true);
                notifyDataSetChanged();
                updateTodo(todo);
            }
        });
        holder.undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todo.setDone(false);
                notifyDataSetChanged();
                updateTodo(todo);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTodo(todo);
                deleteRow(todo);
                updateTodo(todo);
            }
        });

        holder.important.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    todo.setImportant(true);
                    holder.important.setTextColor(Color.parseColor("#E50000"));
                } else {
                    todo.setImportant(false);
                    holder.important.setTextColor(Color.DKGRAY);
                }

                updateTodo(todo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void updateTodoList(List<ToDo> data) {
        todoList.clear();
        todoList.addAll(data);
        notifyDataSetChanged();
    }

    public void addRow(ToDo data) {
        todoList.add(data);
        notifyDataSetChanged();
    }

    public void deleteRow(ToDo data) {
        todoList.remove(data);
        notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteTodo(ToDo todo) {
        new AsyncTask<ToDo, Void, Integer>() {
            @Override
            protected Integer doInBackground(ToDo... params) {
                return toDoAppDatabase.toDoAppDao().deleteTodo(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);
            }
        }.execute(todo);
    }

    public List<ToDo> filter(List<ToDo> todoList, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<ToDo> filteredModelList = new ArrayList<>();
        for (ToDo todo : todoList) {
            final String text = todo.getName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(todo);
            }
        }
        return filteredModelList;
    }

    @SuppressLint("StaticFieldLeak")
    private void updateTodo(ToDo todo) {
        new AsyncTask<ToDo, Void, Integer>() {
            @Override
            protected Integer doInBackground(ToDo... params) {
                return toDoAppDatabase.toDoAppDao().updateTodo(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);
            }
        }.execute(todo);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageButton check;
        public ImageButton undo;
        public ImageButton delete;
        public ToggleButton important;
        public CardView cardView;

        public ViewHolder(View view, Adapter adapter) {
            super(view);

            name = view.findViewById(R.id.name);
            check = view.findViewById(R.id.check);
            undo = view.findViewById(R.id.undo);
            delete = view.findViewById(R.id.delete);
            important = view.findViewById(R.id.important);
            cardView = view.findViewById(R.id.cardView);
        }
    }
}
