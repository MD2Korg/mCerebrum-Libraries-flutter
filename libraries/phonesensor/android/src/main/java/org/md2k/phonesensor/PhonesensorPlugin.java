package org.md2k.phonesensor;

import android.content.Context;

import com.google.gson.Gson;

import org.md2k.mcerebrumapi.extensionapi.library.MCExtensionAPILibrary;
import org.md2k.phonesensor.mcerebrum.PhoneSensorExtension;
import org.md2k.phonesensor.mcerebrum.PhoneSensorManager;

import java.util.HashMap;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * PhonesensorPlugin
 */
public class PhonesensorPlugin implements MethodCallHandler {
    /**
     * Plugin registration.
     */
    private static final String RUNNING_TIME = "RUNNING_TIME";
    private static final String SUMMARY = "SUMMARY";
    private static final String GET_SETTINGS = "GET_SETTINGS";
    private static final String SET_SETTINGS = "SET_SETTINGS";
    private static final String GET_SENSOR_INFO = "GET_SENSOR_INFO";
    private static final String BACKGROUND_SERVICE = "BACKGROUND_SERVICE";
    private static Context context;
    private static MCExtensionAPILibrary mcExtension;

    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "phonesensor");
        channel.setMethodCallHandler(new PhonesensorPlugin());
        context = registrar.context();
        mcExtension = PhoneSensorExtension.createExtensionAPI(context);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        Gson gson = new Gson();
        switch (call.method) {
            case RUNNING_TIME:
//                mcExtension.getBackgroundProcess().
                result.success(PhoneSensorManager.getInstance(context).getRunningTime());
                break;
            case BACKGROUND_SERVICE:
                boolean run = call.argument("run");
                if (run)
                    mcExtension.getBackgroundProcess().start(null);
//                    PhoneSensorManager.getInstance(context).startBackground();
                else
                    mcExtension.getBackgroundProcess().stop();
//                    PhoneSensorManager.getInstance(context).stopBackground();
                result.success(true);
                break;
            case GET_SETTINGS:
                result.success(new Gson().toJson(PhoneSensorManager.getInstance(context).getSettings()));
                break;
            case SUMMARY:
                HashMap<String, Object> res = PhoneSensorManager.getInstance(context).getSummary();
                result.success(new Gson().toJson(res));
            case GET_SENSOR_INFO:
                String s = gson.toJson(PhoneSensorManager.getInstance(context).getDataSourceInfo());
                result.success(s);
                break;

            case SET_SETTINGS:
                String x = call.argument("config");
                HashMap<String, Object> h = gson.fromJson(x, HashMap.class);
                PhoneSensorManager.getInstance(context).setSettings(h);
                result.success(true);
                break;
            default:
                result.notImplemented();
                break;

        }
/*
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else {
    }
*/
    }
}
