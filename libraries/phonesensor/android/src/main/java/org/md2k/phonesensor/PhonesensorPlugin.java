package org.md2k.phonesensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * PhonesensorPlugin
 */
public class PhonesensorPlugin implements EventChannel.StreamHandler {
    private ISensor iSensor;
    private static final String ACTIVITY_TYPE_CHANNEL_NAME = "org.md2k.phonesensor.channel.sensors.activity_type";
    private static final String ACCELEROMETER_CHANNEL_NAME = "org.md2k.phonesensor.channel.sensors.accelerometer";

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final EventChannel activityTypeChannel =
                new EventChannel(registrar.messenger(), ACTIVITY_TYPE_CHANNEL_NAME);
        activityTypeChannel.setStreamHandler(
                new PhonesensorPlugin(registrar.context(), "ACTIVITY_TYPE"));

        final EventChannel accelerometerChannel =
                new EventChannel(registrar.messenger(), ACCELEROMETER_CHANNEL_NAME);
        accelerometerChannel.setStreamHandler(
                new PhonesensorPlugin(registrar.context(), "ACCELEROMETER"));

    }
    private PhonesensorPlugin(Context context, String sensorType) {
        switch (sensorType) {
            case "ACTIVITY_TYPE":
                iSensor = new ActivityType(context);
                break;
            case "ACCELEROMETER":
                iSensor = new Accelerometer(context);
        }
    }

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        iSensor.start(events);
    }

    @Override
    public void onCancel(Object arguments) {
        iSensor.stop();
    }
}
