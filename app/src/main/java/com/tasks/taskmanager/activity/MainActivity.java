package com.tasks.taskmanager.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.tasks.taskmanager.R;
import com.tasks.taskmanager.activity.adapter.TasksListRecyclerViewAdapter;
import com.amplifyframework.datastore.generated.model.State;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String DATABASE_NAME = "taskDb";

    public static final String TAG = "MainActivity";

    List<Task> tasks = new ArrayList<>();

    TasksListRecyclerViewAdapter adapter;

    public static final String TASK_ID_TAG = "Task ID Tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpTasksRecyclerView();

//        Intent intent = getIntent();
//        String selectedTeam = intent.getStringExtra("selectedTeam");

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

        Amplify.API.query(
                ModelQuery.list(Task.class),
                success ->{
                    Log.i("anything", "Read Task successfully");
                    tasks.clear();
                    for (Task databaseTask: success.getData()){
                        tasks.add(databaseTask);
                    }
                    runOnUiThread(() ->{
                        adapter.notifyDataSetChanged();
                    });
                },
                failure -> Log.i("anything", "Did not red Task")
        );
//        Team team1 = Team.builder()
//                         .name("Team 1")
//                         .build();
//
//        Team team2 = Team.builder()
//                .name("Team 2")
//                .build();
//
//        Team team3 = Team.builder()
//                .name("Team 3")
//                .build();
//
//        Team team4 = Team.builder()
//                .name("Team 4")
//                .build();
//
//        Amplify.API.mutate(
//                ModelMutation.create(team1),
//                successRes -> Log.i(TAG, "MainActivity, made a team successfully"),
//                failureRes -> Log.i(TAG, "MainActivity, failed making a team")
//        );
//
//        Amplify.API.mutate(
//                ModelMutation.create(team2),
//                successRes -> Log.i(TAG, "MainActivity, made a team successfully"),
//                failureRes -> Log.i(TAG, "MainActivity, failed making a team")
//        );
//
//        Amplify.API.mutate(
//                ModelMutation.create(team3),
//                successRes -> Log.i(TAG, "MainActivity, made a team successfully"),
//                failureRes -> Log.i(TAG, "MainActivity, failed making a team")
//        );
//
//        Amplify.API.mutate(
//                ModelMutation.create(team4),
//                successRes -> Log.i(TAG, "MainActivity, made a team successfully"),
//                failureRes -> Log.i(TAG, "MainActivity, failed making a team")
//        );

//        Amplify.API.query(
//                ModelQuery.list(Task.class),
//                success ->{
//                    Log.i(TAG, "Read Task successfully");
//                    tasks.clear();
//                    for (Task databaseTask : success.getData()){
//                        if (databaseTask.getTeamTask().getName().equals(selectedTeam)) {
//                            tasks.add(databaseTask);
//                        }
//                    }
//                    runOnUiThread(() ->{
//                        adapter.notifyDataSetChanged();
//                    });
//                },
//                failure -> Log.i(TAG, "Did not red Task")
//        );

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

        readTasks();
    }

    private void setUpTasksRecyclerView() {
        RecyclerView tasksListRecyclerView = findViewById(R.id.tasksListRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        tasksListRecyclerView.setLayoutManager(layoutManager);

        adapter = new TasksListRecyclerViewAdapter(tasks, this);
        tasksListRecyclerView.setAdapter(adapter);
    }

    private void readTasks() {
        Intent intent = getIntent();
        String selectedTeam = intent.getStringExtra("selectedTeam");

        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "Read Task successfully");
                    Log.i(TAG, "success.getData()"+success.getData().toString());
                    tasks.clear();
                    for (Task databaseTask : success.getData()) {
                        if (databaseTask.getTeamTask().getName().equals(selectedTeam)) {
                            tasks.add(databaseTask);
                        }
                    }
                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                    });
                },
                failure -> Log.i(TAG, "Did not read Task")
        );
    }
}
