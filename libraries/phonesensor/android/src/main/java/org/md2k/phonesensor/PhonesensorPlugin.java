package org.md2k.phonesensor;

import android.content.Context;

import com.google.gson.Gson;

import org.md2k.phonesensor.configuration.Configuration;

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

    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "phonesensor");
        channel.setMethodCallHandler(new PhonesensorPlugin());
        context = registrar.context();
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        Gson gson = new Gson();
        Configuration c;
        switch (call.method) {
            case RUNNING_TIME:
                result.success(PhoneSensorManager.getInstance(context).getRunningTime());
                break;
            case BACKGROUND_SERVICE:
                boolean run = call.argument("run");
                if (run)
                    PhoneSensorManager.getInstance(context).startBackground(null);
                else
                    PhoneSensorManager.getInstance(context).stopBackground();

                result.success(true);
                break;
            case GET_SETTINGS:
                c = Configuration.read(context);
                result.success(new Gson().toJson(c.getConfig()));
                break;
            case SUMMARY:
                HashMap<String, Object> res = PhoneSensorManager.getInstance(context).getSummary();
                result.success(new Gson().toJson(res));
            case GET_SENSOR_INFO:
                String s = gson.toJson(PhoneSensorDataSource.getDataSources(context));
                result.success(s);
                break;

            case SET_SETTINGS:
                String x = call.argument("config");
                HashMap<String, Object> h = gson.fromJson(x, HashMap.class);
                Configuration cc = new Configuration(h);
                Configuration.write(context, cc);
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
