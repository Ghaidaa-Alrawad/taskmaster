package com.tasks.taskmanager.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.appsync.ModelMetadata;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.State;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import com.tasks.taskmanager.R;
//import com.tasks.taskmanager.activity.model.State;
//import com.tasks.taskmanager.activity.model.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTask extends AppCompatActivity {

    public static final String TAG = "gggAddTaskActivity";

    Spinner teamSpinner = null;

    Spinner taskStateSpinner = null;

    CompletableFuture<List<Team>> teamFuture = new CompletableFuture<>();

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private ImageView selectedImageView;
    private String filePath;

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

//        Spinner taskStateSpinner = (Spinner) findViewById(R.id.stateSpinner);
//        taskStateSpinner.setAdapter(new ArrayAdapter<>(
//                this,
//                android.R.layout.simple_spinner_item,
//                State.values()));

        teamFuture = new CompletableFuture<>();
        setTeamSpinner();
        setUpAddButton();

        selectedImageView = findViewById(R.id.imageView3);

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        try {
                            selectedImageView.setImageURI(uri);
                            filePath = getRealPathFromURI(uri);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("PHTACT", "Error setting image URI: " + e.getMessage());
                        }
                    } else {
                        Log.d("PHTACT", "No media selected");
                        filePath = null;
                    }
                });

    }

    public void onAddImageButtonClicked(View view) {
        if (pickMedia != null) {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        } else {
            Log.e("PhotoPicker", "pickMedia is null");
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
    }

    private void setTeamSpinner(){
        teamSpinner = (Spinner) findViewById(R.id.teamSpinner);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, " Reading Teams success");
                    ArrayList<String> teamName = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();
                    for (Team team : success.getData()){
                        teams.add(team);
                        teamName.add(team.getName());
                    }
                    teamFuture.complete(teams);

                    runOnUiThread(()-> {
                        teamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                (android.R.layout.simple_spinner_item),
                                teamName
                        ));
                    });
                },
                failure -> {
                    teamFuture.complete(null);
                    Log.i(TAG, "Did not read any team");
                }
        );

        taskStateSpinner = (Spinner) findViewById(R.id.stateSpinner);

        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                (android.R.layout.simple_spinner_item),
                State.values()
        ));
    }

    private void setUpAddButton(){

        Button addTask = (Button) findViewById(R.id.addTask);

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Your Task is added :D", Snackbar.LENGTH_LONG);
        addTask.setOnClickListener(v -> {

            if (filePath != null && !filePath.isEmpty()){
                File imageFile = new File(filePath);
                String key = "images/" + imageFile.getName();

                String title = ((EditText) findViewById(R.id.addTaskInput)).getText().toString();
                String body = ((EditText) findViewById(R.id.taskDiscriptionInput)).getText().toString();
                String currentDate = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
                String selectedTeamString = teamSpinner.getSelectedItem().toString();

                List<Team> teams = null;
                try {
                    teams = teamFuture.get();
                }catch (InterruptedException ie){
                    Log.e(TAG, "InterruptedException while getting the teams");
                }catch (ExecutionException ee){
                    Log.e(TAG, "ExecutionException while getting the teams");
                }

                Team selectedTeam = teams.stream().filter(t -> t.getName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);

                Task newTask = Task.builder()
                        .title(title)
                        .body(body)
                        .dateCreated(new Temporal.DateTime(new Date(), 0))
                        .state((State) taskStateSpinner.getSelectedItem())
                        .teamTask(selectedTeam)
                        .taskS3Uri(key)
                        .build();

                Amplify.Storage.uploadFile(key,
                        imageFile,
                        result -> {
                            Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey());
                        },
                        error -> {
                            Log.e("MyAmplifyApp", "Upload failed", error);
                        });

                Amplify.API.mutate(
                        ModelMutation.create(newTask),
                        successRes -> Log.i(TAG, "AddTaskActivity.onCreate(): made a task successfully"),
                        failureRes -> Log.e(TAG, "AddTaskActivity.onCreate(): failed with this res" + failureRes)
                );

                snackbar.show();

            }else {
                String title = ((EditText) findViewById(R.id.addTaskInput)).getText().toString();
                String body = ((EditText) findViewById(R.id.taskDiscriptionInput)).getText().toString();
                String currentDate = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
                String selectedTeamString = teamSpinner.getSelectedItem().toString();

                List<Team> teams = null;
                try {
                    teams = teamFuture.get();
                }catch (InterruptedException ie){
                    Log.e(TAG, "InterruptedException while getting the teams");
                }catch (ExecutionException ee){
                    Log.e(TAG, "ExecutionException while getting the teams");
                }

                Team selectedTeam = teams.stream().filter(t -> t.getName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);

                Task newTask = Task.builder()
                        .title(title)
                        .body(body)
                        .dateCreated(new Temporal.DateTime(new Date(), 0))
                        .state((State) taskStateSpinner.getSelectedItem())
                        .teamTask(selectedTeam)
                        .build();


                Amplify.API.mutate(
                        ModelMutation.create(newTask),
                        successRes -> Log.i(TAG, "AddTaskActivity.onCreate(): made a task successfully"),
                        failureRes -> Log.e(TAG, "AddTaskActivity.onCreate(): failed with this res" + failureRes)
                );

                snackbar.show();

            }

        });
    }
}
