package com.example.siestasiestabitirme;



import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.util.Timer;
import java.util.TimerTask;

public class MyForegroundService extends Service {
    @Override
    public IBinder onBind(Intent p0) {
        return null;
    }

    private final Timer timer = new Timer();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        double time = intent.getDoubleExtra(TIME_EXTRA, 0.0);
        timer.scheduleAtFixedRate(new TimeTask(time), 0, 1000);
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private class TimeTask extends TimerTask {
        private double time;

        TimeTask(double time) {
            this.time = time;
        }

        @Override
        public void run() {
            Intent intent = new Intent(TIMER_UPDATED);
            time++;
            intent.putExtra(TIME_EXTRA, time);
            sendBroadcast(intent);
        }
    }

    public static final String TIMER_UPDATED = "timerUpdated";
    public static final String TIME_EXTRA = "timeExtra";
}