package com.example.siestasiestabitirme;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.app.Notification;



import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyForegroundService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Burada arka planda yapılacak işleri gerçekleştirin.

        // Servis, kullanıcıya bildirim göndererek arka planda çalıştığını belirtir.
        startForeground(1, createNotification());

        // START_STICKY, servisin sistem tarafından öldürülmesi durumunda otomatik olarak yeniden başlatılmasını sağlar.
        return START_STICKY;
    }

    private Notification createNotification() {
        // Bildirim oluşturma
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle("Uygulama çalışıyor")
                .setContentText("Uygulama arka planda çalışıyor.")
                .setSmallIcon(R.drawable.enterscrane);

        return builder.build();
    }
}
