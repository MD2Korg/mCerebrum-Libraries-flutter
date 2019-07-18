package org.md2k.motionsense;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import org.md2k.motionsenselibrary.device.MotionSenseManager;
import org.md2k.motionsenselibrary.device.ScanCallback;
import org.md2k.motionsenselibrary.plot.ActivityPlot;

import java.util.ArrayList;
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
    private static final String GET_SUMMARY = "GET_SUMMARY";
    private static final String GET_SETTINGS = "GET_SETTINGS";
    private static final String SET_SETTINGS = "SET_SETTINGS";
    private static final String GET_SENSOR_INFO = "GET_SENSOR_INFO";
    private static final String BACKGROUND_SERVICE = "BACKGROUND_SERVICE";
    private static final String PLOT = "PLOT";
    private Activity activity;

    /**
     * Plugin registration.
     */

    public static void registerWith(Registrar registrar) {
        MotionSense.init(registrar.context());
        MotionsensePlugin motionsensePlugin = new MotionsensePlugin();
        motionsensePlugin.activity = registrar.activity();
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
            case BACKGROUND_SERVICE:
                boolean run = call.argument("run");
                if(run)
                MotionSense.startBackground();
                else MotionSense.stopBackground();
                result.success(true);
                break;
            case GET_SUMMARY:
                HashMap<String, Object> m = new HashMap<>();
                m.put("isRunning",MotionSense.getRunningTime()!=-1);
                m.put("runningTime", MotionSense.getRunningTime());
                m.put("devices", MotionSense.getConfiguration().get("motionsense_devices"));
                result.success(gson.toJson(m));
                break;
            case PLOT:
                String platformType = call.argument("platformType");
                String platformId = call.argument("platformId");
                String sensorType = call.argument("sensorType");
                HashMap<String, Object> hConfig = MotionSense.getConfiguration();
                if(hConfig.containsKey("motionsense_devices")) {
                    ArrayList<Object> lists = (ArrayList<Object>) hConfig.get("motionsense_devices");
                    for(int i = 0;i<lists.size();i++){
                        Map map = (Map) lists.get(i);
                        String pType = (String) map.get("platformType");
                        String pId = (String) map.get("platformId");
                        String dId = (String) map.get("deviceId");
                        if(platformType.equals(pType) && platformId.equals(pId)) {
                            Intent intent = new Intent(activity, ActivityPlot.class);
                            intent.putExtra("deviceId",dId);
                            intent.putExtra("sensorType", sensorType);
                            activity.startActivity(intent);
                        }
                    }
                }
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
