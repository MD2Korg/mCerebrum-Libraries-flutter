package org.md2k.phonesensor;

import android.content.Context;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * PhoneSensorPlugin
 */
public class PhoneSensorPlugin implements EventChannel.StreamHandler {
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
                new PhoneSensorPlugin(registrar.context(), "ACTIVITY_TYPE"));

        final EventChannel accelerometerChannel =
                new EventChannel(registrar.messenger(), ACCELEROMETER_CHANNEL_NAME);
        accelerometerChannel.setStreamHandler(
                new PhoneSensorPlugin(registrar.context(), "ACCELEROMETER"));

    }
    private PhoneSensorPlugin(Context context, String sensorType) {
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
