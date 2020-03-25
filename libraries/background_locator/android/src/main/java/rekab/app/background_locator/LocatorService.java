package rekab.app.background_locator;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import androidx.core.app.JobIntentService;
import com.google.android.gms.location.LocationResult;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.FlutterCallbackInformation;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterNativeView;
import io.flutter.view.FlutterRunArguments;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class LocatorService extends JobIntentService implements MethodChannel.MethodCallHandler {
    private ArrayDeque<HashMap<String, Object>> queue = new ArrayDeque<HashMap<String, Object>>();
    private MethodChannel backgroundChannel;
    private Context context;
    public static int JOB_ID = (int) UUID.randomUUID().getMostSignificantBits();
        private FlutterNativeView backgroundFlutterView = null;
        private static AtomicBoolean serviceStarted = new AtomicBoolean(false);

        public static void enqueueWork(Context context, Intent work) {
            enqueueWork(context, LocatorService.class, JOB_ID, work);
        }
    @Override
    public void onCreate() {
        super.onCreate();
        startLocatorService(this);
    }

    private void startLocatorService(Context context) {
        // start synchronized block to prevent multiple service instant
        synchronized(serviceStarted) {
            this.context = context;
            if (backgroundFlutterView == null) {
                long callbackHandle = context.getSharedPreferences(
                        Keys.SHARED_PREFERENCES_KEY,
                        Context.MODE_PRIVATE)
                        .getLong(Keys.CALLBACK_DISPATCHER_HANDLE_KEY, 0);
                FlutterCallbackInformation callbackInfo = FlutterCallbackInformation.lookupCallbackInformation(callbackHandle);

                // We need flutter view to handle callback, so if it is not available we have to create a
                // Flutter background view without any view
                backgroundFlutterView = new FlutterNativeView(context, true);

                FlutterRunArguments args = new FlutterRunArguments();
                args.bundlePath = FlutterMain.findAppBundlePath(context);
                args.entrypoint = callbackInfo.callbackName;
                args.libraryPath = callbackInfo.callbackLibraryPath;

                if(backgroundFlutterView!=null) backgroundFlutterView.runFromBundle(args);
                IsolateHolderService.setBackgroundFlutterView(backgroundFlutterView);
            }
        }

        backgroundChannel = new MethodChannel(backgroundFlutterView, Keys.BACKGROUND_CHANNEL_ID);
        backgroundChannel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result ) {
        if (Keys.METHOD_SERVICE_INITIALIZED.equals(call.method)) {
            synchronized(serviceStarted) {
                while (!queue.isEmpty()) {
                    sendLocationEvent(queue.remove());
                }
                serviceStarted.set(true);
            }
        }else result.notImplemented();
        result.success(null);
    }

    @Override public void onHandleWork(Intent intent) {
        if (LocationResult.hasResult(intent)) {
            Location location = LocationResult.extractResult(intent).getLastLocation();

            float speedAccuracy = 0f;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                speedAccuracy = location.getSpeedAccuracyMetersPerSecond();
            }
            HashMap<String, Object> locationMap = new HashMap<>();
            locationMap.put(Keys.ARG_LATITUDE, location.getLatitude());
            locationMap.put(Keys.ARG_LONGITUDE, location.getLongitude());
            locationMap.put(Keys.ARG_ACCURACY, location.getAccuracy());
            locationMap.put(Keys.ARG_ALTITUDE, location.getAltitude());
            locationMap.put(Keys.ARG_SPEED, location.getSpeed());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                locationMap.put(Keys.ARG_SPEED_ACCURACY, location.getSpeedAccuracyMetersPerSecond());
            }else
                locationMap.put(Keys.ARG_SPEED_ACCURACY, -1.0f);
            locationMap.put(Keys.ARG_HEADING, location.getBearing());

            long callback = BackgroundLocatorPlugin.getCallbackHandle(context, Keys.CALLBACK_HANDLE_KEY);
            String directory = BackgroundLocatorPlugin.getDirectoryHandle(context, Keys.DIRECTORY_HANDLE_KEY);
            HashMap<String, Object> result = new HashMap<>();
            result.put(Keys.ARG_CALLBACK, callback);
            result.put(Keys.ARG_DIRECTORY, directory);
            result.put(Keys.ARG_LOCATION, locationMap);
            synchronized(serviceStarted) {
                if (!serviceStarted.get()) {
                    queue.add(result);
                } else {
                    sendLocationEvent(result);
                }
            }
        }
    }

    private void sendLocationEvent(final HashMap<String, Object> result) {
        //https://github.com/flutter/plugins/pull/1641
        //https://github.com/flutter/flutter/issues/36059
        //https://github.com/flutter/plugins/pull/1641/commits/4358fbba3327f1fa75bc40df503ca5341fdbb77d
        // new version of flutter can not invoke method from background thread
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                backgroundChannel.invokeMethod(Keys.BCM_SEND_LOCATION, result);
            }
        });
        }
}