package org.md2k.motionsense.configuration;

import android.content.Context;
import android.util.Log;

import org.md2k.mcerebrumapi.MCerebrumAPI;
import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.datakitapi.ipc.authenticate.MCConnectionCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource.MCRegistration;
import org.md2k.mcerebrumapi.status.MCStatus;
import org.md2k.mcerebrumapi.utils.DateTime;
import org.md2k.motionsense.Callback;
import org.md2k.motionsense.EventListener;
import org.md2k.motionsense.MotionSenseDataSource;
import org.md2k.motionsenselibrary.device.DeviceInfo;
import org.md2k.motionsenselibrary.device.MotionSenseManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MotionSense {
    private static MotionSense instance;
    private long startTimestamp;
    private HashMap<String, Object> config;
    private HashMap<String, EventListener> iSensorEvents;

    public static void init(Context context) {
        if (instance == null) {
            instance = new MotionSense(context);
        }
    }

    private MotionSense(Context context) {
        startTimestamp = -1;
        MCerebrumAPI.init(context.getApplicationContext());
        MCerebrumAPI.connect(new MCConnectionCallback() {
            @Override
            public void onSuccess() {
                Log.d("abc","connected to mcerebrum");
            }

            @Override
            public void onError(MCStatus status) {

            }
        });
        MotionSenseManager.init(context.getApplicationContext());
        iSensorEvents = new HashMap<>();
    }

    public static boolean isConfigured() {
        HashMap<String, Object> config = MCerebrumAPI.getConfiguration("motionsense");
        if (config.containsKey(ConfigId.DEVICES) && config.get(ConfigId.DEVICES) != null) {
            ArrayList<Object> res = (ArrayList<Object>) config.get(ConfigId.DEVICES);
            return res!=null && res.size() != 0;
        }
        return false;
    }

    public static void setConfiguration(HashMap<String, Object> h) {
        MCerebrumAPI.setConfiguration("motionsense", h);
    }

    public static HashMap<String, Object> getConfiguration() {
        return MCerebrumAPI.getConfiguration("motionsense");
    }

    public static void startBackground() {
        HashMap<String, Object> h = getConfiguration();

    }

    public static void stopBackground() {

    }

    private MCConnectionCallback connectionCallback = new MCConnectionCallback() {
        @Override
        public void onSuccess() {
//            config = MCerebrumAPI.getConfiguration("motionsense");
/*
            DeviceInfo d;
            MotionSenseManager.addDevice(new DeviceInfo)

            for(SensorType sensorType: SensorType.values()){
                if(Configuration.isEnable(sensorType, config)){
                    ISensor iSensor = setConfig(sensorType, config);
                    final MCRegistration r = MCerebrumAPI.registerDataSource(motionSenseDataSource.getDataSource(sensorType));
                    EventListener eventListener = createListener(r);
                    iSensorEvents.put(sensorType.name(), eventListener);
                    iSensor.start(eventListener);
                }
            }
*/
            Log.d("abc", "abc");
        }

        @Override
        public void onError(MCStatus status) {
            stopBackground();
        }
    };
/*
    private boolean isRunning(){
        return startTimestamp!=-1;
    }
    public HashMap<String, Object> getSettings(){
        return config;
    }
    public boolean setSettings(HashMap<String, Object> config){
        this.config = config;
        int status = MCerebrumAPI.setConfiguration("motionsense", config);
        if(isRunning()){
            stopBackground();
            startBackground();
        }
        return status==0;
    }
*/
/*
    public HashMap<String, MCDataSource> getDataSourceInfo(){
        return motionSenseDataSource.getDataSources();
    }
*/

/*
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
*/


/*    protected void startBackground() {
        if (startTimestamp!=-1) return;
        startTimestamp = DateTime.getCurrentTime();
        MCerebrumAPI.connect(connectionCallback);
    }

    protected void stopBackground() {
*//*
        if (startTimestamp==-1) return;
        for (Map.Entry<String, EventListener> entry : iSensorEvents.entrySet()) {
            SensorType s = SensorType.valueOf(entry.getKey());
            MCSensorManager.getInstance(context).getSensor(s).stop(entry.getValue());
        }
        startTimestamp = -1;
        MCerebrumAPI.disconnect(connectionCallback);
*//*
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
    }*/
}
