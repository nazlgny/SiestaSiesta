package com.example.siestasiestabitirme;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.TextView;

import java.util.Locale;

public class BackgroundService extends Service {


    public Handler umbrellaHandler = new Handler();
    public Handler chairHandler = new Handler();
    public TextView timerTextView,timerTextView2;
    public long umbrellaTimeLeftInMillis = 0;
    public long chairTimeLeftInMillis = 0;
    public static boolean umbrellaTimerRunning;
    public static boolean chairTimerRunning;
    public long umbrellaStartTimeInMillis;
    public long chairStartTimeInMillis;


    @Override
    public IBinder onBind(Intent intent) {
        // Servis bağlantı noktası gerektirmediği için null döndürüyoruz.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startUmbrellaTimer();
        startChairTimer();

        // Servis başlatıldığında yapılacak işlemleri buraya yazabilirsiniz.
        // Örneğin, zamanlayıcıları başlatma, veritabanı işlemleri, ağ çağrıları vb.
        return START_STICKY; // Servis durdurulduğunda otomatik olarak yeniden başlaması için START_STICKY kullanılabilir.
    }

    @Override
    public void onDestroy() {
        // Servis sonlandırıldığında yapılacak işlemleri buraya yazabilirsiniz.
    }



    public void startChairTimer() {
        chairStartTimeInMillis = System.currentTimeMillis();

        chairTimerRunning = true;
        chairHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (chairTimerRunning) {
                    long currentTimeInMillis = System.currentTimeMillis();
                    long chairTimeElapsedInMillis = currentTimeInMillis - chairStartTimeInMillis;
                    chairTimeLeftInMillis = chairTimeElapsedInMillis;

                    updateChairTimerText();

                    chairHandler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }



    public void startUmbrellaTimer() {
        umbrellaStartTimeInMillis = System.currentTimeMillis();

        umbrellaTimerRunning = true;
        umbrellaHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (umbrellaTimerRunning) {
                    long currentTimeInMillis = System.currentTimeMillis();
                    long umbrellaTimeElapsedInMillis = currentTimeInMillis - umbrellaStartTimeInMillis;
                    umbrellaTimeLeftInMillis = umbrellaTimeElapsedInMillis;

                    updateUmbrellaTimerText();

                    umbrellaHandler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    public static void stopUmbrellaTimer() {
        umbrellaTimerRunning = false;
    }

    public static void stopChairTimer() {
        chairTimerRunning = false;
    }

    public void updateUmbrellaTimerText() {
        long hours = umbrellaTimeLeftInMillis / (1000 * 60 * 60);
        long minutes = (umbrellaTimeLeftInMillis % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (umbrellaTimeLeftInMillis % (1000 * 60)) / 1000;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    public void updateChairTimerText() {
        long hours = chairTimeLeftInMillis / (1000 * 60 * 60);
        long minutes = (chairTimeLeftInMillis % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (chairTimeLeftInMillis % (1000 * 60)) / 1000;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        timerTextView2.setText(timeLeftFormatted);
    }







}
