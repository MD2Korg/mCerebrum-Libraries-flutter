package org.md2k.phonesensor;

import android.content.Context;
import android.util.Log;

import org.md2k.mcerebrumapi.core.data.MCData;
import org.md2k.mcerebrumapi.core.data.MCEnum;
import org.md2k.mcerebrumapi.core.datakitapi.MCDataKitAPI;
import org.md2k.mcerebrumapi.core.datakitapi.ipc.authenticate.MCConnectionCallback;
import org.md2k.mcerebrumapi.core.datakitapi.ipc.insert_datasource.MCRegistration;
import org.md2k.mcerebrumapi.core.time.DateTime;
import org.md2k.phonesensor.configuration.Configuration;
import org.md2k.phonesensor.sensor.EventListener;
import org.md2k.phonesensor.sensor.ISensor;
import org.md2k.phonesensor.sensor.MCAbstractNativeSensor;
import org.md2k.phonesensor.sensor.WriteType;
import org.md2k.phonesensor.sensor.activity_type.MCActivityType;
import org.md2k.phonesensor.sensor.battery.MCBattery;
import org.md2k.phonesensor.sensor.gps.MCGps;

import java.util.ArrayList;
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
final class PhoneSensorManager {
    private static PhoneSensorManager instance;
    private Context context;
    private long startTimestamp;
    private HashMap<String, EventListener> iSensorEvents;
    private MCConnectionCallback connectionCallback = new MCConnectionCallback() {
        @Override
        public void onSuccess() {
            Configuration config = Configuration.read(context);
            Log.d("abc","abc");
/*
            for (int i = 0; i < config.size(); i++) {
                SensorType sensorType = config.getSensorType(i);
                ISensor iSensor = setConfig(config, sensorType);
                final MCRegistration r = MCDataKitAPI.registerDataSource(CreateDataSource.create(context, sensorType));
                EventListener eventListener = createListener(r);
                iSensorEvents.put(sensorType.name(), eventListener);
                iSensor.start(eventListener);
            }
*/
        }

        @Override
        public void onError(int status) {
            Configuration config = Configuration.read(context);
            ArrayList<SensorType> sensorTypes = config.getSensorTypes();
            for(int i =0;i<sensorTypes.size();i++){
                if(config.isEnable(sensorTypes.get(i))){
                    Log.d("abc","abc");
                }else{
                    Log.d("abc","abc");
                }
            }
            stopBackground();
        }
    };

    protected static PhoneSensorManager getInstance(Context context) {
        if (instance == null)
            instance = new PhoneSensorManager(context.getApplicationContext());
        return instance;
    }

    private ISensor setConfig(Configuration sensor, SensorType sensorType) {
        ISensor iSensor = MCSensorManager.getInstance(context).getSensor(sensorType);
        switch (sensorType) {
            case ACCELEROMETER:
            case GYROSCOPE:
            case GRAVITY:
                ((MCAbstractNativeSensor) iSensor).setReadFrequency(sensor.getSampleRate(sensorType), sensor.getSampleRateUnit(sensorType));
                if (sensor.getWriteType(sensorType) == WriteType.AS_RECEIVED)
                    ((MCAbstractNativeSensor) iSensor).setWriteAsReceived();
                else if (sensor.getWriteType(sensorType) == WriteType.FIXED)
                    ((MCAbstractNativeSensor) iSensor).setWriteFixed(sensor.getSampleRate(sensorType), sensor.getSampleRateUnit(sensorType));
                else if (sensor.getWriteType(sensorType) == WriteType.ON_CHANGE) {
                    ((MCAbstractNativeSensor) iSensor).setWriteOnChange(sensor.getWriteOnChangeComparison(sensorType));
                }
                break;
            case ACTIVITY_TYPE:
                ((MCActivityType) iSensor).setReadFrequency(sensor.getSampleRate(sensorType), sensor.getSampleRateUnit(sensorType));
                ((MCActivityType) iSensor).setWriteAsReceived();
                break;
            case GPS:
                ((MCGps) iSensor).setReadFrequency(sensor.getSampleRate(sensorType), sensor.getSampleRateUnit(sensorType));
                ((MCGps) iSensor).setWriteAsReceived();
                break;
            case BATTERY:
                ((MCBattery) iSensor).setWriteFixed(1, TimeUnit.SECONDS);
                break;
        }
        return iSensor;
    }

    private PhoneSensorManager(Context context) {
        this.context = context;
        startTimestamp = -1;
        MCDataKitAPI.init(context);
        iSensorEvents = new HashMap<>();
    }


    protected void startBackground(Object o) {
        if (startTimestamp!=-1) return;
        startTimestamp = DateTime.getCurrentTime();
        MCDataKitAPI.connect(connectionCallback);
    }

    protected void stopBackground() {
        if (startTimestamp==-1) return;
        for (Map.Entry<String, EventListener> entry : iSensorEvents.entrySet()) {
            SensorType s = SensorType.valueOf(entry.getKey());
            MCSensorManager.getInstance(context).getSensor(s).stop(entry.getValue());
        }
        startTimestamp = -1;
        MCDataKitAPI.disconnect(connectionCallback);
    }
    protected long getRunningTime(){
        if(startTimestamp==-1) return startTimestamp;
        return DateTime.getCurrentTime()-startTimestamp;
    }

    private EventListener createListener(final MCRegistration r) {
        return new EventListener() {
            @Override
            public void onChange(long timestamp, Object sample) {
                MCData data = null;
                switch (r.getDataSource().getSampleType()) {
                    case INT_ARRAY:
                        data = MCData.createPointIntArray(timestamp, (int[]) sample);
                        break;
                    case BYTE_ARRAY:
                        data = MCData.createPointByteArray(timestamp, (byte[]) sample);
                        break;
                    case LONG_ARRAY:
                        data = MCData.createPointLongArray(timestamp, (long[]) sample);
                        break;
                    case DOUBLE_ARRAY:
                        data = MCData.createPointDoubleArray(timestamp, (double[]) sample);
                        break;
                    case STRING_ARRAY:
                        data = MCData.createPointStringArray(timestamp, (String[]) sample);
                        break;
                    case BOOLEAN_ARRAY:
                        data = MCData.createPointBooleanArray(timestamp, (boolean[]) sample);
                        break;
                    case ENUM:
                        data = MCData.createPointEnum(timestamp, (MCEnum) sample);
                        break;
                    case OBJECT:
                        data = MCData.createPointObject(timestamp, sample);
                        break;
                    default:
                        data = null;
                        break;
                }
                if (data != null) {
                    Log.d("abc","create data: "+r.getDataSource().getDataSourceType());
                    MCDataKitAPI.insertData(r, data);
                }
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
