package com.mcuadrada.servicios;

import android.Manifest;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements UpdateCounterServiceListener{

    Intent flashServiceIntent;
    Intent counterServiceIntent;
    private android.widget.Button btnFlash;
    private android.widget.Button btnCounter;
    private TextView tvCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnFlash = (Button) findViewById(R.id.btnFlash);
        this.btnCounter = (Button) findViewById(R.id.btnCounter);
        this.tvCounter = (TextView) findViewById(R.id.txvCounter);

        CounterService.setUpdateCounterListener(MainActivity.this);

        btnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    launchFlashService();
                }
            }
        });

        btnCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCounterService();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stopService(serviceIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 20) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                AlertDialog.Builder cameraDialog = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Permisos de cámara")
                                .setMessage("Esta App necesita usar la cámara solo para encender y apagar el flash")
                                .setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkCameraPermission();
                                    }
                                });
                cameraDialog.show();
            }
        }
    }

    private boolean checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        20);
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private void launchFlashService() {
        flashServiceIntent = new Intent(MainActivity.this, FlashService.class);
        if (btnFlash.getText().equals("Encender")) {
            startService(flashServiceIntent);
            btnFlash.setText("Apagar");
        } else {
            stopService(flashServiceIntent);
            btnFlash.setText("Encender");
        }
    }

    private void launchCounterService(){
        counterServiceIntent = new Intent(MainActivity.this, CounterService.class);
        if(btnCounter.getText().equals("Comenzar")){
            startService(counterServiceIntent);
            btnCounter.setText("Detener");
        } else {
            stopService(counterServiceIntent);
            btnCounter.setText("Comenzar");
        }
    }

    @Override
    public void updateCounter(int counter) {
        tvCounter.setText("CONTEO: " + counter);
    }
}
