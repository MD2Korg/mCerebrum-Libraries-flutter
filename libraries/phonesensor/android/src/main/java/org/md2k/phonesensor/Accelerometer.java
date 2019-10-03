package org.md2k.phonesensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.HashMap;

import io.flutter.plugin.common.EventChannel;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;
import pl.charmas.android.reactivelocation2.ReactiveLocationProviderConfiguration;

public class Accelerometer extends ISensor{
    private SensorEventListener sensorEventListener;
    private SensorManager sensorManager;
    private Sensor sensor;

    Accelerometer(Context context){
        super(context);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    void start(final EventChannel.EventSink events){
        sensorEventListener = createSensorEventListener(events);
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    void stop(){
        sensorManager.unregisterListener(sensorEventListener);
    }
    private SensorEventListener createSensorEventListener(final EventChannel.EventSink events) {
        return new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}

            @Override
            public void onSensorChanged(SensorEvent event) {
                double[] sensorValues = new double[event.values.length];
                for (int i = 0; i < event.values.length; i++) {
                    sensorValues[i] = event.values[i];
                }
                events.success(createData(System.currentTimeMillis(), sensorValues));
            }
        };
    }
}
