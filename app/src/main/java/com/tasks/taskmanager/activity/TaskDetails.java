package com.tasks.taskmanager.activity;

import static com.tasks.taskmanager.activity.MainActivity.TASK_ID_TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.tasks.taskmanager.R;

import java.io.File;

public class TaskDetails extends AppCompatActivity {

    public String taskImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Button back = findViewById(R.id.backDetails);

        back.setOnClickListener(v -> {
            Intent goBack = new Intent(TaskDetails.this, MainActivity.class);
            startActivity(goBack);
        });


        Intent intent = getIntent();
        String taskTitle = intent.getStringExtra("taskTitle");
        String taskBody = intent.getStringExtra("taskBody");
        String taskState = intent.getStringExtra("taskState");
        String teamTask = intent.getStringExtra("teamName");
        taskImg = intent.getStringExtra("taskS3Uri");

        TextView titleTextView = findViewById(R.id.taskDetailTitle);
        titleTextView.setText(taskTitle);

        TextView stateTextView = findViewById(R.id.taskState);
        stateTextView.setText(taskState);

        TextView descriptionTextView = findViewById(R.id.taskDetailDescription);
        descriptionTextView.setText(taskBody);

        TextView teamTaskTextView = findViewById(R.id.teamTask);
        teamTaskTextView.setText(teamTask);

        updateUI();
    }

    private void updateUI() {
        if (taskImg != null) {
            ImageView image = findViewById(R.id.imageViewDetails);
            Amplify.Storage.downloadFile(
                    taskImg,
                    new File(getApplicationContext().getFilesDir(), "downloaded_image.jpg"),
                    result -> {
                        Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getPath());
                        runOnUiThread(() -> {
                            Bitmap bitmap = BitmapFactory.decodeFile(result.getFile().getPath());
                            image.setImageBitmap(bitmap);
                        });
                    },
                    error -> Log.e("MyAmplifyApp", "Download failed", error)
            );
        }
    }

}
