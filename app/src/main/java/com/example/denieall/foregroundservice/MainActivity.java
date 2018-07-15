package com.example.denieall.foregroundservice;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_ID = "fs_1";
    public static final String CHANNEL_NAME = "Foreground Service Channel";

    public Intent serviceIntent;

    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel(CHANNEL_ID, CHANNEL_NAME);

        input = findViewById(R.id.input_et);
    }

    private void createNotificationChannel(String id, String name) {

        // Ask foreground service permission for android 8.0 and above
        if (Build.VERSION.SDK_INT >= 28) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.FOREGROUND_SERVICE}, 555);

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel nc = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(nc);
            }

        }

    }

    // Handle permission pop up
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 555) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createNotificationChannel(CHANNEL_ID, CHANNEL_NAME);
            } else {
                System.exit(0);
            }

        }
    }

    public void startService(View view) {
        serviceIntent = new Intent(this, TestService.class);
        serviceIntent.putExtra("Input", input.getText().toString());
        startService(serviceIntent);
    }

    public void stopService(View view) {
        stopService(serviceIntent);
    }
}
