package com.tasks.taskmanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.tasks.taskmanager.R;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

public class TaskDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Button back = (Button) findViewById(R.id.backDetails);

        back.setOnClickListener(v -> {
            Intent goBack = new Intent(TaskDetails.this, MainActivity.class);
            startActivity(goBack);
        });

        Intent intent = getIntent();
        String taskTitle = intent.getStringExtra("taskTitle");
        String taskBody = intent.getStringExtra("taskBody");
        String taskState = intent.getStringExtra("taskState");


        TextView titleTextView = findViewById(R.id.taskDetailTitle);
        titleTextView.setText(taskTitle);

        TextView stateTextView = findViewById(R.id.taskState);
        stateTextView.setText(taskState);

        TextView descriptionTextView = findViewById(R.id.taskDetailDescription);
        descriptionTextView.setText(taskBody);
    }

//    private String getLoremIpsum() {
//        Lorem lorem = LoremIpsum.getInstance();
//        return lorem.getParagraphs(2, 4);
//    }
}