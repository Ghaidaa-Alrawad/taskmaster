package com.tasks.taskmanager.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.tasks.taskmanager.R;
import com.tasks.taskmanager.activity.adapter.TasksListRecyclerViewAdapter;
import com.tasks.taskmanager.activity.database.TasksDatabase;
import com.tasks.taskmanager.activity.model.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String DATABASE_NAME = "taskDb";

    private TasksDatabase tasksDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpTasksRecyclerView();

        Button addTask = findViewById(R.id.addTaskButton);

        addTask.setOnClickListener(v -> {
            Intent goToAddTaskPage = new Intent(MainActivity.this, AddTask.class);
            startActivity(goToAddTaskPage);
        });

        Button allTasks = findViewById(R.id.allTasksButton);

        allTasks.setOnClickListener(v -> {
            Intent goToAllTasksPage = new Intent(MainActivity.this, AllTasks.class);
            startActivity(goToAllTasksPage);
        });

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent goToSettings = new Intent(MainActivity.this, Settings.class);
            startActivityForResult(goToSettings, 1);
        });

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", "");

        TextView usernameTextView = findViewById(R.id.usernameTextView);
        usernameTextView.setText(username + "'s tasks");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String username = data.getStringExtra("username");

            TextView usernameTextView = findViewById(R.id.usernameTextView);
            usernameTextView.setText(username + "'s tasks");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", "");

        TextView usernameTextView = findViewById(R.id.usernameTextView);
        usernameTextView.setText(username + "'s tasks");
    }

    private void setUpTasksRecyclerView() {
        RecyclerView tasksListRecyclerView = findViewById(R.id.tasksListRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        tasksListRecyclerView.setLayoutManager(layoutManager);

        tasksDatabase = Room.databaseBuilder(
                        getApplicationContext(),
                        TasksDatabase.class,
                        DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        List<Task> tasks = tasksDatabase.taskDao().findAll();

        TasksListRecyclerViewAdapter adapter = new TasksListRecyclerViewAdapter(tasks, this);
        tasksListRecyclerView.setAdapter(adapter);
    }
}
