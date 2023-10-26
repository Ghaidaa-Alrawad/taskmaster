package com.tasks.taskmanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.tasks.taskmanager.R;
import com.tasks.taskmanager.activity.database.TasksDatabase;
import com.tasks.taskmanager.activity.model.State;
import com.tasks.taskmanager.activity.model.Task;

import java.util.Date;

public class AddTask extends AppCompatActivity {

    TasksDatabase tasksDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        tasksDatabase = Room.databaseBuilder(
                        getApplicationContext(),
                        TasksDatabase.class,
                        "taskDb")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        Button back = (Button) findViewById(R.id.backButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack = new Intent(AddTask.this, MainActivity.class);
                startActivity(goBack);
            }
        });

        Spinner taskStateSpinner = (Spinner) findViewById(R.id.stateSpinner);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                State.values()));

        Button addTask = (Button) findViewById(R.id.addTask);

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Your Task is added :D", Snackbar.LENGTH_LONG);
        addTask.setOnClickListener(v -> {
            Task newTask = new Task(
                    ((EditText) findViewById(R.id.addTaskInput)).getText().toString(),
                    ((EditText) findViewById(R.id.taskDiscriptionInput)).getText().toString(),
                    new Date(),
                    State.fromString(taskStateSpinner.getSelectedItem().toString())
            );

            tasksDatabase.taskDao().insertTask(newTask);

            snackbar.show();
        });
    }
}