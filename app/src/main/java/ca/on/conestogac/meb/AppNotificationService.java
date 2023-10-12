package ca.on.conestogac.meb;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class AppNotificationService extends Service {
    public AppNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        final Timer timer = new Timer(true);

        final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final Intent intent = new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        final Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.notificationName))
                .setContentText(getString(R.string.notificationDetails))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();


        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                manager.notify(1, notification);
                timer.cancel();
                stopSelf();
            }
        }, 10000);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}