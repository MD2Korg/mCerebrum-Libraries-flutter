package org.md2k.motionsense;

import android.util.Log;

import com.google.gson.Gson;

import org.md2k.mcerebrumapi.MCerebrumAPI;
import org.md2k.motionsense.configuration.MotionSense;
import org.md2k.motionsenselibrary.device.MotionSenseManager;
import org.md2k.motionsenselibrary.device.ScanCallback;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * MotionsensePlugin
 */
public class MotionsensePlugin implements MethodCallHandler, EventChannel.StreamHandler {
    private static final String RUNNING_TIME = "RUNNING_TIME";
    private static final String SUMMARY = "SUMMARY";
    private static final String GET_SETTINGS = "GET_SETTINGS";
    private static final String SET_SETTINGS = "SET_SETTINGS";
    private static final String GET_SENSOR_INFO = "GET_SENSOR_INFO";
    private static final String BACKGROUND_SERVICE = "BACKGROUND_SERVICE";

    /**
     * Plugin registration.
     */

    public static void registerWith(Registrar registrar) {
        MotionSense.init(registrar.context());
        MotionsensePlugin motionsensePlugin = new MotionsensePlugin();
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "org.md2k.motionsense.channel");
        channel.setMethodCallHandler(motionsensePlugin);
        final EventChannel messageChannel = new EventChannel(registrar.messenger(), "org.md2k.motionsense.scan");
        messageChannel.setStreamHandler(motionsensePlugin);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        Gson gson = new Gson();
        switch (call.method) {
            case GET_SETTINGS:
                HashMap<String, Object> config = MotionSense.getConfiguration();
                result.success(gson.toJson(config));
                break;
            case SET_SETTINGS:
                String x = call.argument("config");
                HashMap<String, Object> h = gson.fromJson(x, HashMap.class);
                MotionSense.setConfiguration(h);
                result.success(true);
                break;
            default:
                result.notImplemented();
        }
    }

    @Override
    public void onListen(Object o, EventChannel.EventSink eventSink) {
        MotionSenseManager.startScan(new ScanCallback() {
            @Override
            public void onReceive(String deviceName, String deviceId) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("deviceName", deviceName);
                hashMap.put("deviceId", deviceId);
                Gson gson = new Gson();
                eventSink.success(gson.toJson(hashMap));
            }
        });
    }

    @Override
    public void onCancel(Object o) {
        MotionSenseManager.stopScan();
    }
}
