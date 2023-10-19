package com.tasks.taskmanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tasks.taskmanager.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addTask = (Button) findViewById(R.id.addTaskButton);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("welcome to add task page");

                Intent goToAddTaskPage = new Intent(MainActivity.this, AddTask.class);
                startActivity(goToAddTaskPage);
            }
        });

        Button allTasks = (Button) findViewById(R.id.allTasksButton);

        allTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAllTasksPage = new Intent(MainActivity.this, AllTasks.class);
                startActivity(goToAllTasksPage);
            }
        });
    }
}