package rekab.app.background_locator;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import io.flutter.view.FlutterNativeView;

public class IsolateHolderService extends Service {
        public static String ACTION_SHUTDOWN = "SHUTDOWN";
        public static String ACTION_START = "START";
        public static String WAKELOCK_TAG = "IsolateHolderService::WAKE_LOCK";
        public static String CHANNEL_ID = "app.rekab/locator_plugin";

        public static FlutterNativeView _backgroundFlutterView = null;
        public static void setBackgroundFlutterView(FlutterNativeView view) {
            _backgroundFlutterView = view;
        }
        public static boolean isRunning = false;

    String notificationTitle = "Location Tracking";
    String notificationMsg = "Track location in background";
    int icon = 0;
    long wakeLockTime = 60 * 60 * 1000L; // 1 hour default wake lock time

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    private void start() {
        if (isRunning) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Notification channel is available in Android O and up
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Flutter Locator Plugin",
                    NotificationManager.IMPORTANCE_LOW);
            ((NotificationManager)(getSystemService(Context.NOTIFICATION_SERVICE))).createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, getMainActivityClass(this));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMsg)
                .setSmallIcon(icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build();
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG);
        wakeLock.setReferenceCounted(false);
        wakeLock.acquire(wakeLockTime);
        // Starting Service as foreground with a notification prevent service from closing
        startForeground(1, notification);

        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("IsolateHolderService", "onStartCommand...intent = "+(intent!=null));
        if (intent!=null && ACTION_SHUTDOWN.equals(intent.getAction())) {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG);
            if(wakeLock.isHeld())
                wakeLock.release();
            stopForeground(true);
            stopSelf();
            isRunning = false;
        } else if (intent!=null && ACTION_START.equals(intent.getAction())) {
            notificationTitle = intent.getStringExtra(Keys.ARG_NOTIFICATION_TITLE);
            notificationMsg = intent.getStringExtra(Keys.ARG_NOTIFICATION_MSG);
            String iconNameDefault = "ic_launcher";
 //           var iconName = intent.getStringExtra(ARG_NOTIFICATION_ICON)
 //           if (iconName == null || iconName.isEmpty()) {
 //               iconName = iconNameDefault
 //           }
            icon = getResources().getIdentifier(iconNameDefault, "mipmap", getPackageName());
            wakeLockTime = 60 * 60 * 1000L;
            start();
        }
        return START_STICKY;
    }

    private Class getMainActivityClass(Context context ){
        Class c=null;
        String packageName = context.getPackageName();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        String className = launchIntent.getComponent().getClassName();
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }
}