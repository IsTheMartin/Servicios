package com.mcuadrada.servicios;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

public class Flash2Service extends Service {

    NotificationManager notificationManager;
    Vibrator vibrator;
    private static final int ID_NOTIFICATION = 829;

    public Flash2Service() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationCompat.Builder notification;
        NotificationChannel channel;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel1";
            channel = new NotificationChannel("CHANNEL1", name,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notification = new NotificationCompat.Builder(this, "CHANNEL1");
            notification.setChannelId("CHANNEL1");
            notificationManager.createNotificationChannel(channel);
        } else {
            notification = new NotificationCompat.Builder(this);
        }

        notification.setWhen(System.currentTimeMillis())
                .setContentTitle("Servicios")
                .setContentText("Aplicación para encender el flash")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLights(Color.argb(1, 255, 170, 65), 2000, 1000);

        long[] vibrationPattern = {200, 400, 500, 1000};
        vibrator.vibrate(vibrationPattern, -1);

        notificationManager.notify(ID_NOTIFICATION, notification.build());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(ID_NOTIFICATION);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
