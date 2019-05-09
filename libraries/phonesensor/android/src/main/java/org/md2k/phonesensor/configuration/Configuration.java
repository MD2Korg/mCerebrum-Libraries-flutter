package org.md2k.phonesensor.configuration;

import org.md2k.phonesensor.sensor.SensorType;
import org.md2k.phonesensor.sensor.Comparison;
import org.md2k.phonesensor.sensor.WriteType;

import java.util.HashMap;
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
public class Configuration {
    public static boolean isEnable(SensorType sensorType, HashMap<String, Object> config){
        String key = getPrefix(sensorType)+"_enable";
        if(config.get(key)==null) return false;
        return (boolean) config.get(key);
    }
    public static double getSampleRate(SensorType sensorType, HashMap<String, Object> config){
        String key = getPrefix(sensorType)+"_sampleRate";
        if(config.get(key)==null) return 1;
        return (double) config.get(key);
    }
    public static TimeUnit getSampleRateUnit(SensorType sensorType, HashMap<String, Object> config) {
        String key = getPrefix(sensorType)+"_sampleRateUnit";
        if(config.get(key)==null) return TimeUnit.SECONDS;
        return TimeUnit.valueOf((String) config.get(key));
    }
    public static WriteType getWriteType(SensorType sensorType, HashMap<String, Object> config) {
        String key = getPrefix(sensorType)+"_writeType";
        Object x = config.get(key);
        if(x==null) return WriteType.AS_RECEIVED;
        else return WriteType.valueOf(x.toString());
    }
    private static Comparison.ComparisonType getComparisonType(SensorType sensorType, HashMap<String, Object> config){
        String key = getPrefix(sensorType)+"_writeOnChangeType";
        if(config.get(key)==null) return Comparison.ComparisonType.NOT_EQUAL;
        return Comparison.ComparisonType.valueOf((String) config.get(key));
    }
    private static double getComparisonValue(SensorType sensorType, HashMap<String, Object> config){
        String key = getPrefix(sensorType)+"_writeOnChangeValue";
        if(config.get(key)==null) return 0.1;
        return (double) config.get(key);
    }

    public static Comparison getWriteOnChangeComparison(SensorType sensorType, HashMap<String, Object> config) {
        Comparison.ComparisonType comparisonType = getComparisonType(sensorType, config);
        if(comparisonType== Comparison.ComparisonType.NOT_EQUAL) return Comparison.notEqual();
        else{
            return Comparison.sampleDiff(getComparisonValue(sensorType, config));
        }
    }
    private static String getPrefix(SensorType sensorType){
        switch(sensorType){
            case ACCELEROMETER: return "phonesensor_accelerometer";
            case ACCELEROMETER_LINEAR: return "phonesensor_accelerometerLinear";
            case ACTIVITY_TYPE: return "phonesensor_activityType";
            case AIR_PRESSURE: return "phonesensor_airPressure";
            case AMBIENT_LIGHT: return "phonesensor_ambientLight";
            case AMBIENT_TEMPERATURE: return "phonesensor_ambientTemperature";
            case BATTERY: return "phonesensor_battery";
            case BLUETOOTH_STATUS: return "phonesensor_bluetoothStatus";
            case CHARGING_STATUS: return "phonesensor_chargingStatus";
            case CONNECTIVITY_STATUS: return "phonesensor_connectivityStatus";
            case GPS: return "phonesensor_gps";
            case GPS_STATUS: return "phonesensor_gpsStatus";
            case GRAVITY: return "phonesensor_gravity";
            case GYROSCOPE: return "phonesensor_gyroscope";
            case MAGNETOMETER: return "phonesensor_magnetometer";
            case PROXIMITY: return "phonesensor_proximity";
            case RELATIVE_HUMIDITY: return "phonesensor_relativeHumidity";
            case SIGNIFICANT_MOTION: return "phonesensor_significantMotion";
            case STEP_COUNT: return "phonesensor_stepCount";
            case WIFI_STATUS: return "phonesensor_wifiStatus";
        }
        return "";
    }
}
