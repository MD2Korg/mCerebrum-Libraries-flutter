package org.md2k.phonesensor.mcerebrum;

import android.content.Context;
import android.util.Log;

import org.md2k.mcerebrumapi.MCerebrumAPI;
import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.datakitapi.ipc.authenticate.MCConnectionCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource.MCRegistration;
import org.md2k.mcerebrumapi.status.MCStatus;
import org.md2k.mcerebrumapi.utils.DateTime;
import org.md2k.phonesensor.MCSensorManager;
import org.md2k.phonesensor.sensor.SensorType;
import org.md2k.phonesensor.configuration.Configuration;
import org.md2k.phonesensor.sensor.Comparison;
import org.md2k.phonesensor.sensor.EventListener;
import org.md2k.phonesensor.sensor.ISensor;
import org.md2k.phonesensor.sensor.MCAbstractNativeSensor;
import org.md2k.phonesensor.sensor.MCAbstractSensor;
import org.md2k.phonesensor.sensor.WriteType;
import org.md2k.phonesensor.sensor.activity_type.MCActivityType;
import org.md2k.phonesensor.sensor.gps.MCGps;
import org.md2k.phonesensor.sensor.step_count.MCStepCount;

import java.util.HashMap;
import java.util.Map;
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
final public class PhoneSensorManager {
    private static PhoneSensorManager instance;
    private Context context;
    private long startTimestamp;
    private PhoneSensorDataSource phoneSensorDataSource;
    private HashMap<String, Object> config;
    private HashMap<String, EventListener> iSensorEvents;
    private MCConnectionCallback connectionCallback = new MCConnectionCallback() {
        @Override
        public void onSuccess() {
            config = MCerebrumAPI.getConfiguration("phonesensor");
            for(SensorType sensorType: SensorType.values()){
                if(Configuration.isEnable(sensorType, config)){
                    ISensor iSensor = setConfig(sensorType, config);
                    final MCRegistration r = MCerebrumAPI.registerDataSource(phoneSensorDataSource.getDataSource(sensorType));
                    EventListener eventListener = createListener(r);
                    iSensorEvents.put(sensorType.name(), eventListener);
                    iSensor.start(eventListener);
                }
            }
            Log.d("abc","abc");
        }

        @Override
        public void onError(MCStatus status) {
            stopBackground();
        }
    };
    private boolean isRunning(){
        return startTimestamp!=-1;
    }
    public HashMap<String, Object> getSettings(){
        return config;
    }
    public boolean setSettings(HashMap<String, Object> config){
        this.config = config;
        int status = MCerebrumAPI.setConfiguration("phonesensor", config);
        if(isRunning()){
            stopBackground();
            startBackground();
        }
        return status==0;
    }
    public HashMap<String, MCDataSource> getDataSourceInfo(){
        return phoneSensorDataSource.getDataSources();
    }

    public static PhoneSensorManager getInstance(Context context) {
        if (instance == null)
            instance = new PhoneSensorManager(context.getApplicationContext());
        return instance;
    }

    private ISensor setConfig(SensorType sensorType, HashMap<String, Object> config) {
        ISensor iSensor = MCSensorManager.getInstance(context).getSensor(sensorType);
        double sampleRate = Configuration.getSampleRate(sensorType, config);
        TimeUnit sampleRateUnit = Configuration.getSampleRateUnit(sensorType, config);
        WriteType writeType = Configuration.getWriteType(sensorType, config);
        Comparison comparison = Configuration.getWriteOnChangeComparison(sensorType, config);
        if (writeType == WriteType.AS_RECEIVED)
            ((MCAbstractSensor) iSensor).setWriteAsReceived();
        else if (writeType == WriteType.FIXED)
            ((MCAbstractSensor) iSensor).setWriteFixed(sampleRate, sampleRateUnit);
        else if (writeType == WriteType.ON_CHANGE) {
            ((MCAbstractSensor) iSensor).setWriteOnChange(comparison);
        }

        switch (sensorType) {
            case ACCELEROMETER:
            case ACCELEROMETER_LINEAR:
            case GYROSCOPE:
            case GRAVITY:
            case MAGNETOMETER:
            case AMBIENT_LIGHT:
            case AMBIENT_TEMPERATURE:
            case PROXIMITY:
            case RELATIVE_HUMIDITY:
                ((MCAbstractNativeSensor) iSensor).setReadFrequency(sampleRate, sampleRateUnit);
                break;
            case CHARGING_STATUS:
            case CONNECTIVITY_STATUS:
            case BLUETOOTH_STATUS:
            case GPS_STATUS:
            case WIFI_STATUS:
            case SIGNIFICANT_MOTION:
            case BATTERY:
                break;
            case ACTIVITY_TYPE:
                ((MCActivityType) iSensor).setReadFrequency(sampleRate, sampleRateUnit);
                break;
            case GPS:
                ((MCGps) iSensor).setReadFrequency(sampleRate, sampleRateUnit);
                break;
            case STEP_COUNT:
                ((MCStepCount) iSensor).setReadFrequency(sampleRate, sampleRateUnit);
                break;
        }
        return iSensor;
    }

    private PhoneSensorManager(Context context) {
        this.context = context;
        startTimestamp = -1;
        MCerebrumAPI.init(context, PhoneSensorExtension.createExtensionAPI(context));
        iSensorEvents = new HashMap<>();
        phoneSensorDataSource = new PhoneSensorDataSource(context);
    }


    protected void startBackground() {
        if (startTimestamp!=-1) return;
        startTimestamp = DateTime.getCurrentTime();
        MCerebrumAPI.connect(connectionCallback);
    }

    protected void stopBackground() {
        if (startTimestamp==-1) return;
        for (Map.Entry<String, EventListener> entry : iSensorEvents.entrySet()) {
            SensorType s = SensorType.valueOf(entry.getKey());
            MCSensorManager.getInstance(context).getSensor(s).stop(entry.getValue());
        }
        startTimestamp = -1;
        MCerebrumAPI.disconnect(connectionCallback);
    }
    public long getRunningTime(){
        if(startTimestamp==-1) return startTimestamp;
        return DateTime.getCurrentTime()-startTimestamp;
    }

    private EventListener createListener(final MCRegistration r) {
        return new EventListener() {
            @Override
            public void onChange(long timestamp, Object sample) {
                MCData data = MCData.create(r, timestamp, sample);
                    Log.d("abc","create data: "+r.getDataSource().getDataSourceType()+" "+((double[])sample)[0]);//+" "+((double[])sample)[1]+" "+((double[])sample)[2]);
                MCerebrumAPI.insertData(data);
            }
        };
    }


    public HashMap<String, Object> getSummary() {
        HashMap<String, Object> summary = new HashMap<>();
        summary.put("running_time", getRunningTime());
        summary.put("is_running", getRunningTime()>0);

        return summary;
    }
}
