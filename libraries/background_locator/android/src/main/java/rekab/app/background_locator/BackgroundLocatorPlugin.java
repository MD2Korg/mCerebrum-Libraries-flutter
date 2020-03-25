package rekab.app.background_locator;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

import static rekab.app.background_locator.Keys.CALLBACK_HANDLE_KEY;
import static rekab.app.background_locator.Keys.NOTIFICATION_CALLBACK_HANDLE_KEY;


public class BackgroundLocatorPlugin
    implements MethodCallHandler, FlutterPlugin, PluginRegistry.NewIntentListener, ActivityAware {
    private FusedLocationProviderClient locatorClient;
    private Context context = null;
    private Activity activity = null;
    private static MethodChannel channel = null;
        private static void registerLocator(Context context, FusedLocationProviderClient client,
                                    Map<String, Object> args,
                                    Result result) {
            if (IsolateHolderService.isRunning) {
                // The service is running already
                Log.d("BackgroundLocatorPlugin", "Locator service is already running");
                return;
            }
            long callbackHandle = -1;
            if(args.containsKey(Keys.ARG_CALLBACK))
            callbackHandle = (long) args.get(Keys.ARG_CALLBACK);
            setCallbackHandle(context, CALLBACK_HANDLE_KEY, callbackHandle);

            long notificationCallback = -1;
            if(args.containsKey(Keys.ARG_NOTIFICATION_CALLBACK))
            notificationCallback  = (long) args.get(Keys.ARG_NOTIFICATION_CALLBACK);
            setCallbackHandle(context, NOTIFICATION_CALLBACK_HANDLE_KEY, notificationCallback);

            Map<String, Object> settings = (Map<String, Object>) args.get(Keys.ARG_SETTINGS);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_DENIED) {

                String msg = "'registerLocator' requires the ACCESS_FINE_LOCATION permission.";
                result.error(msg, null, null);
            }

            startIsolateService(context, settings);

            client.requestLocationUpdates(getLocationRequest(settings),
                    getLocatorPendingIndent(context));
        }

        private static void startIsolateService(Context context, Map<String, Object> settings) {
            Intent intent = new Intent(context, IsolateHolderService.class);
            intent.setAction(IsolateHolderService.ACTION_START);
            intent.putExtra(Keys.ARG_NOTIFICATION_TITLE, (String)settings.get(Keys.ARG_NOTIFICATION_TITLE));
            intent.putExtra(Keys.ARG_NOTIFICATION_MSG, (String)settings.get(Keys.ARG_NOTIFICATION_MSG));
            intent.putExtra(Keys.ARG_NOTIFICATION_ICON, (String)settings.get(Keys.ARG_NOTIFICATION_ICON));

            if (settings.containsKey(Keys.ARG_WAKE_LOCK_TIME)) {
                intent.putExtra(Keys.ARG_WAKE_LOCK_TIME, (int)settings.get(Keys.ARG_WAKE_LOCK_TIME));
            }

            ContextCompat.startForegroundService(context, intent);
        }

        private static void stopIsolateService(Context context) {
            Intent intent = new Intent(context, IsolateHolderService.class);
            intent.setAction(IsolateHolderService.ACTION_SHUTDOWN);
            ContextCompat.startForegroundService(context, intent);
        }

        private static void initializeService(Context context, Map<String, Object> args) {
            long callbackHandle = (long) args.get(Keys.ARG_CALLBACK_DISPATCHER);
            setCallbackDispatcherHandle(context, callbackHandle);
        }

        private static PendingIntent getLocatorPendingIndent(Context context) {
            Intent intent = new Intent(context, LocatorBroadcastReceiver.class);
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }


        private static LocationRequest getLocationRequest(Map<String, Object> settings) {
            LocationRequest locationRequest =new LocationRequest();

            long interval = ((int)(settings.get(Keys.ARG_INTERVAL)) * 1000);
            locationRequest.setInterval(interval);
            locationRequest.setFastestInterval(interval);
            locationRequest.setMaxWaitTime(interval);

            int accuracyKey = (int) settings.get(Keys.ARG_ACCURACY);
            locationRequest.setPriority(getAccuracy(accuracyKey));

            double distanceFilter = (double) settings.get(Keys.ARG_DISTANCE_FILTER);
            locationRequest.setSmallestDisplacement((float) distanceFilter);

            return locationRequest;
        }

        private static int getAccuracy(int key){
            switch(key) {
                case 0: return LocationRequest.PRIORITY_NO_POWER;
                case 1: return LocationRequest.PRIORITY_LOW_POWER;
                case 2: return LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
                case 3: return LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
                case 4: return LocationRequest.PRIORITY_HIGH_ACCURACY;
                default: return LocationRequest.PRIORITY_HIGH_ACCURACY;
            }
        }

        private static void  removeLocator(Context context,
                                           FusedLocationProviderClient client) {
            if (!IsolateHolderService.isRunning) {
                // The service is not running
                Log.d("BackgroundLocatorPlugin", "Locator service is not running, nothing to stop");
                return;
            }

            client.removeLocationUpdates(getLocatorPendingIndent(context));
            stopIsolateService(context);
        }

        private static void isRegisterLocator(Context context,
                                      Result result) {
            if (IsolateHolderService.isRunning) {
                result.success(true);
            } else {
                result.success(false);
            }
            return;
        }

        private static void setCallbackDispatcherHandle(Context context, long handle) {
            context.getSharedPreferences(Keys.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
                    .edit()
                    .putLong(Keys.CALLBACK_DISPATCHER_HANDLE_KEY, handle)
                    .apply();
        }

        public static void setCallbackHandle(Context context, String key, long handle) {
            if (handle == -1) {
                return;
            }

            context.getSharedPreferences(Keys.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
                    .edit()
                    .putLong(key, handle)
                    .apply();
        }

        public static long getCallbackHandle(Context context, String key){
            return context.getSharedPreferences(Keys.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
                    .getLong(key, 0);
        }

    @Override public void onMethodCall(MethodCall call, Result result) {
            if(Keys.METHOD_PLUGIN_INITIALIZE_SERVICE.equals(call.method)) {
                Map<String, Object> args = call.arguments();
                initializeService(context, args);
                result.success(true);
            }else if(Keys.METHOD_PLUGIN_REGISTER_LOCATION_UPDATE.equals(call.method)) {
                Map<String, Object> args = call.arguments();
                registerLocator(context,
                        locatorClient,
                        args,
                        result);
            }else if(Keys.METHOD_PLUGIN_UN_REGISTER_LOCATION_UPDATE.equals(call.method)) {
                removeLocator(context, locatorClient);
            }else if(Keys.METHOD_PLUGIN_IS_REGISTER_LOCATION_UPDATE.equals(call.method)) {
                isRegisterLocator(context, result);
            }else result.notImplemented();
    }

    @Override public void onAttachedToEngine(FlutterPlugin.FlutterPluginBinding binding) {
        onAttachedToEngine(binding.getApplicationContext(), binding.getBinaryMessenger());
    }

    @Override public void onDetachedFromEngine(FlutterPlugin.FlutterPluginBinding binding) {
    }

    private void onAttachedToEngine(Context context, BinaryMessenger messenger) {
        BackgroundLocatorPlugin plugin = new BackgroundLocatorPlugin();
        plugin.context = context;
        plugin.locatorClient = LocationServices.getFusedLocationProviderClient(context);

        channel = new MethodChannel(messenger, Keys.CHANNEL_ID);
        channel.setMethodCallHandler(plugin);
    }

    @Override
    public boolean onNewIntent(Intent intent){
        final long notificationCallback = getCallbackHandle(activity, NOTIFICATION_CALLBACK_HANDLE_KEY);
        if (notificationCallback > 0 && IsolateHolderService._backgroundFlutterView != null) {
            final MethodChannel backgroundChannel = new MethodChannel(IsolateHolderService._backgroundFlutterView,
                    Keys.BACKGROUND_CHANNEL_ID);
            if(activity!=null) {
                new Handler(activity.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put(Keys.ARG_NOTIFICATION_CALLBACK, notificationCallback);
                        backgroundChannel.invokeMethod(Keys.BCM_NOTIFICATION_CLICK, hashMap);

                    }
                });
                }
            }

        return true;
    }

    @Override public void onDetachedFromActivity() {
    }

    @Override public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
    }

    @Override public void onAttachedToActivity(ActivityPluginBinding binding) {
        activity = binding.getActivity();
        binding.addOnNewIntentListener(this);
    }

    @Override public void onDetachedFromActivityForConfigChanges() {
    }
}
