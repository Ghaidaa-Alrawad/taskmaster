package com.tasks.taskmanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tasks.taskmanager.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Button taskButton1 = findViewById(R.id.taskButton1);
        Button taskButton2 = findViewById(R.id.taskButton2);
        Button taskButton3 = findViewById(R.id.taskButton3);

        taskButton1.setOnClickListener(v -> openTaskDetails("Task 1"));
        taskButton2.setOnClickListener(v -> openTaskDetails("Task 2"));
        taskButton3.setOnClickListener(v -> openTaskDetails("Task 3"));

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

    private void openTaskDetails(String taskTitle) {
        Intent goToTaskDetails = new Intent(MainActivity.this, TaskDetails.class);
        goToTaskDetails.putExtra("taskTitle", taskTitle);
        startActivity(goToTaskDetails);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", "");

        TextView usernameTextView = findViewById(R.id.usernameTextView);
        usernameTextView.setText(username + "'s tasks");
    }
}