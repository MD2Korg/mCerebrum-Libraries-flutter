package org.md2k.phonesensor.sensor.step_count;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;

import org.md2k.phonesensor.MetaData;
import org.md2k.phonesensor.SensorType;
import org.md2k.phonesensor.sensor.Comparison;
import org.md2k.phonesensor.sensor.Frequency;
import org.md2k.phonesensor.sensor.MCAbstractSensor;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class MCStepCount extends MCAbstractSensor {
    private final static long MICROSECONDS_IN_ONE_MINUTE = 60000000;

    private Frequency readFrequency;
    private SensorManager mSensorManager;
    private int lastStepCount = 0;

    public MCStepCount(Context context) {
        super(context, SensorType.STEP_COUNT, null);
        this.readFrequency = new Frequency(1, TimeUnit.MINUTES);
    }

    public void setReadFrequency(double frequency, TimeUnit timeUnit) {
        this.readFrequency = new Frequency(frequency, timeUnit);
    }

    public void setWriteAsReceived() {
        super.setWriteAsReceived();
    }

    @Override
    public HashMap<String, String> getSensorInfo() {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        HashMap<String, String> sensorInfo = new HashMap<>();
        if (mSensor == null) return sensorInfo;
        sensorInfo.put(MetaData.VENDOR, mSensor.getVendor());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sensorInfo.put(MetaData.MAXIMUM_DELAY, String.valueOf(mSensor.getMaxDelay()));
            switch (mSensor.getReportingMode()) {
                case Sensor.REPORTING_MODE_CONTINUOUS:
                    sensorInfo.put(MetaData.REPORTING_MODE, "REPORTING_MODE_CONTINUOUS");
                    break;
                case Sensor.REPORTING_MODE_ON_CHANGE:
                    sensorInfo.put(MetaData.REPORTING_MODE, "REPORTING_MODE_ON_CHANGE");
                    break;
                case Sensor.REPORTING_MODE_ONE_SHOT:
                    sensorInfo.put(MetaData.REPORTING_MODE, "REPORTING_MODE_ONE_SHOT");
                    break;
                case Sensor.REPORTING_MODE_SPECIAL_TRIGGER:
                    sensorInfo.put(MetaData.REPORTING_MODE, "REPORTING_MODE_SPECIAL_TRIGGER");
                    break;
                default:
                    break;
            }
        }
        sensorInfo.put(MetaData.MAXIMUM_RANGE, String.valueOf(mSensor.getMaximumRange()));
        sensorInfo.put(MetaData.MIN_DELAY, String.valueOf(mSensor.getMinDelay()));
        sensorInfo.put(MetaData.NAME, mSensor.getName());
        sensorInfo.put(MetaData.POWER, String.valueOf(mSensor.getPower()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sensorInfo.put(MetaData.ID, String.valueOf(mSensor.getId()));
        }
        sensorInfo.put(MetaData.RESOLUTION, String.format(Locale.getDefault(), "%.8f", mSensor.getResolution()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            sensorInfo.put(MetaData.TYPE, mSensor.getStringType());
        }

        sensorInfo.put(MetaData.VERSION, String.valueOf(mSensor.getVersion()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sensorInfo.put(MetaData.IS_DYNAMIC, String.valueOf(mSensor.isDynamicSensor()));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sensorInfo.put(MetaData.IS_WAKEUP, String.valueOf(mSensor.isWakeUpSensor()));
        }

        return sensorInfo;
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.values[0] > Integer.MAX_VALUE) {
                return;
            } else {
                int steps = (int) event.values[0];
                if (lastStepCount == 0) lastStepCount = steps;
                else
                    setSample(event.timestamp, steps - lastStepCount);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Do nothing
        }
    };

    @Override
    public boolean isSupported() {
        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        return mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null;
    }

    @Override
    protected boolean isChanged(Object prevSample, Object curSample, Comparison comparison) {
        if (prevSample == null) return true;
        double[] p = (double[]) prevSample;
        double[] c = (double[]) curSample;
        if (comparison.getComparisonType() == Comparison.ComparisonType.NOT_EQUAL) {
            for (int i = 0; i < p.length; i++)
                if (p[i] != c[i]) return true;
            return false;
        } else {
            for (int i = 0; i < p.length; i++) {
                if (Math.abs(p[i] - c[i]) > comparison.getValue()) return true;
            }
            return false;
        }
    }

    @Override
    protected void startSensing() {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mSensorManager.registerListener(sensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL, (int) (1 * MICROSECONDS_IN_ONE_MINUTE));
    }

    @Override
    protected void stopSensing() {
        if (mSensorManager != null)
            mSensorManager.unregisterListener(sensorEventListener);
    }
}
