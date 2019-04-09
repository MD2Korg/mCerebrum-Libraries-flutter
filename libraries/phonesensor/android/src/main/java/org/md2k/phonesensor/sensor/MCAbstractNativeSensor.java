package org.md2k.phonesensor.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;

import org.md2k.phonesensor.MetaData;
import org.md2k.phonesensor.SensorType;

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
public abstract class MCAbstractNativeSensor extends MCAbstractSensor {
    private int sensorId;
    private Frequency readFrequency;
    private SensorManager mSensorManager;

    protected MCAbstractNativeSensor(Context context, SensorType sensorType, int sensorId){
        super(context, sensorType, null);
        this.sensorId=sensorId;
        this.readFrequency = new Frequency(6,TimeUnit.SECONDS);
    }
    public void setReadFrequency(double frequency, TimeUnit timeUnit){
        this.readFrequency=new Frequency(frequency, timeUnit);
    }
    public void setWriteAsReceived(){
        super.setWriteAsReceived();
    }
    public void setWriteFixed(double writeFrequency, TimeUnit timeUnit){
        super.setWriteFixed(writeFrequency, timeUnit);
    }
    public void setWriteOnChange(Comparison comparison){
        super.setWriteOnChange(comparison);
    }

    @Override
    public HashMap<String, String> getSensorInfo(){
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor mSensor = mSensorManager.getDefaultSensor(sensorId);
        HashMap<String, String> sensorInfo = new HashMap<>();
        if(mSensor==null) return sensorInfo;
        sensorInfo.put(MetaData.VENDOR,mSensor.getVendor());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sensorInfo.put(MetaData.MAXIMUM_DELAY,String.valueOf(mSensor.getMaxDelay()));
            switch(mSensor.getReportingMode()){
                case Sensor.REPORTING_MODE_CONTINUOUS:
                    sensorInfo.put(MetaData.REPORTING_MODE,"REPORTING_MODE_CONTINUOUS");
                    break;
                case Sensor.REPORTING_MODE_ON_CHANGE:
                    sensorInfo.put(MetaData.REPORTING_MODE,"REPORTING_MODE_ON_CHANGE");
                    break;
                case Sensor.REPORTING_MODE_ONE_SHOT:
                    sensorInfo.put(MetaData.REPORTING_MODE,"REPORTING_MODE_ONE_SHOT");
                    break;
                case Sensor.REPORTING_MODE_SPECIAL_TRIGGER:
                    sensorInfo.put(MetaData.REPORTING_MODE,"REPORTING_MODE_SPECIAL_TRIGGER");
                    break;
                default:
                    break;
            }
        }
        sensorInfo.put(MetaData.MAXIMUM_RANGE,String.valueOf(mSensor.getMaximumRange()));
        sensorInfo.put(MetaData.MIN_DELAY,String.valueOf(mSensor.getMinDelay()));
        sensorInfo.put(MetaData.NAME,mSensor.getName());
        sensorInfo.put(MetaData.POWER,String.valueOf(mSensor.getPower()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sensorInfo.put(MetaData.ID,String.valueOf(mSensor.getId()));
        }
        sensorInfo.put(MetaData.RESOLUTION,String.format(Locale.getDefault(), "%.8f",mSensor.getResolution()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            sensorInfo.put(MetaData.TYPE,mSensor.getStringType());
        }

        sensorInfo.put(MetaData.VERSION,String.valueOf(mSensor.getVersion()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sensorInfo.put(MetaData.IS_DYNAMIC,String.valueOf(mSensor.isDynamicSensor()));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sensorInfo.put(MetaData.IS_WAKEUP,String.valueOf(mSensor.isWakeUpSensor()));
        }

        return sensorInfo;
    }


    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            double[] result=new double[event.values.length];
            for(int i =0;i<event.values.length;i++)
                result[i]=event.values[i];
            setSample(System.currentTimeMillis(), result);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Do nothing
        }
    };
    @Override
    public boolean isSupported(){
        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        return mSensorManager.getDefaultSensor(sensorId)!=null;
    }
    @Override
    protected boolean isChanged(Object prevSample, Object curSample, Comparison comparison) {
        if(prevSample ==null) return true;
        double[] p = (double[]) prevSample;
        double[] c = (double[]) curSample;
        if(comparison.getComparisonType()== Comparison.ComparisonType.NOT_EQUAL) {
            for(int i =0;i<p.length;i++)
                if(p[i]!=c[i]) return true;
            return false;
        }else{
            for(int i=0;i<p.length;i++){
                if(Math.abs(p[i]-c[i])>comparison.getValue()) return true;
            }
            return false;
        }
    }

    @Override
    protected void startSensing(){
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor mSensor = mSensorManager.getDefaultSensor(sensorId);
        mSensorManager.registerListener(sensorEventListener, mSensor, getSensorDelay());
    }
    @Override
    protected void stopSensing(){
        if(mSensorManager!=null)
            mSensorManager.unregisterListener(sensorEventListener);
    }
    private int getSensorDelay(){
        double readFrequencyDouble = readFrequency.as(TimeUnit.SECONDS).getFrequency();
        if(readFrequencyDouble<=6.0)
            return SensorManager.SENSOR_DELAY_NORMAL;
        else if(readFrequencyDouble<=20.0)
            return SensorManager.SENSOR_DELAY_UI;
        else if(readFrequencyDouble<=40.0)
            return SensorManager.SENSOR_DELAY_GAME;
        else return SensorManager.SENSOR_DELAY_FASTEST;
    }
}
