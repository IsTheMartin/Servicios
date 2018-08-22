package com.mcuadrada.servicios;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Intent serviceIntent;
    private android.widget.Button btnFlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnFlash = (Button) findViewById(R.id.btnFlash);

        btnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceIntent = new Intent(MainActivity.this, FlashService.class);
                if (checkSelfPermission(Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            20);
                } else {
                    if (btnFlash.getText().equals("Encender")) {
                        startService(serviceIntent);
                        //ContextCompat.startForegroundService(MainActivity.this, serviceIntent);
                        //startForegroundService(serviceIntent);
                        btnFlash.setText("Apagar");
                    } else {
                        stopService(serviceIntent);
                        btnFlash.setText("Encender");
                    }
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(serviceIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
