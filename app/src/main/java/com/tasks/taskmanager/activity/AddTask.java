package com.tasks.taskmanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.appsync.ModelMetadata;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.State;
import com.google.android.material.snackbar.Snackbar;
import com.tasks.taskmanager.R;
//import com.tasks.taskmanager.activity.model.State;
//import com.tasks.taskmanager.activity.model.Task;

import java.util.Date;

public class AddTask extends AppCompatActivity {

    public static final String TAG = "AddTaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

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
//            Task newTask = new Task(
//                    ((EditText) findViewById(R.id.addTaskInput)).getText().toString(),
//                    ((EditText) findViewById(R.id.taskDiscriptionInput)).getText().toString(),
//                    new Date(),
//                    State.fromString(taskStateSpinner.getSelectedItem().toString())
//            );

            String title = ((EditText) findViewById(R.id.addTaskInput)).getText().toString();
            String body = ((EditText) findViewById(R.id.taskDiscriptionInput)).getText().toString();
            String currentDate = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());

            Task newTask = Task.builder()
                    .title(title)
                    .body(body)
                    .dateCreated(new Temporal.DateTime(new Date(), 0))
                    .state((State) taskStateSpinner.getSelectedItem()).build();

            Amplify.API.mutate(
                    ModelMutation.create(newTask),
                    successRes -> Log.i(TAG, "AddTaskActivity.onCreate(): made a task successfully"),
                    failureRes -> Log.e(TAG, "AddTaskActivity.onCreate(): failed with this res" + failureRes)
            );

//            tasksDatabase.taskDao().insertTask(newTask);

            snackbar.show();
        });
    }
}
