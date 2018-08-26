package com.mcuadrada.servicios;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

public class CounterService extends Service {

    Timer timer;
    private int counter;
    private static final long UPDATE_INTERVAL = 1000;
    public static UpdateCounterServiceListener UPDATE_COUNTER_LISTENER = null;

    public CounterService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        counter = 0;
        startCounter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void startCounter() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                counter++;
                handler.sendEmptyMessage(0);
            }
        }, 0, UPDATE_INTERVAL);
    }

    public static void setUpdateCounterListener(UpdateCounterServiceListener listener) {
        UPDATE_COUNTER_LISTENER = listener;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (UPDATE_COUNTER_LISTENER != null)
                UPDATE_COUNTER_LISTENER.updateCounter(counter);
        }
    };
}


