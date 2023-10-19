package com.tasks.taskmanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.tasks.taskmanager.R;

public class Settings extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String USERNAME_KEY = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button back = (Button) findViewById(R.id.backSitting);

        back.setOnClickListener(v -> {
            Intent goBack = new Intent(Settings.this, MainActivity.class);
            startActivity(goBack);
        });

        EditText usernameEditText = findViewById(R.id.usernameEditText);
        Button saveButton = findViewById(R.id.saveButton);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        saveButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            prefs.edit().putString(USERNAME_KEY, username).apply();

            showSnackbar("Username saved: " + username);

        });
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}