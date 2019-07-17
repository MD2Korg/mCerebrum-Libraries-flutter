package org.md2k.motionsense.configuration;

import java.util.HashMap;

public class ConfigDeviceSettings {
    private static final String motionsense_device_platformType="motionsense_device_platformType";
    private static final String motionsense_device_platformId="motionsense_device_platformId";
    private static final String motionsense_device_version="motionsense_device_version";
    private static final String motionsense_device_minimumConnectionInterval="motionsense_device_minimumConnectionInterval";
    private static final String motionsense_accelerometer_enable="motionsense_accelerometer_enable";
    private static final String motionsense_accelerometer_frequency="motionsense_accelerometer_frequency";
    private static final String motionsense_accelerometer_sensitivity="motionsense_accelerometer_sensitivity";
    private static final String motionsense_gyroscope_enable="motionsense_gyroscope_enable";
    private static final String motionsense_gyroscope_frequency="motionsense_gyroscope_frequency";
    private static final String motionsense_gyroscope_sensitivity="motionsense_gyroscope_sensitivity";
    private static final String motionsense_sequenceNumberMotion_enable="motionsense_sequenceNumberMotion_enable";
    private static final String motionsense_sequenceNumberMotion_frequency="motionsense_sequenceNumberMotion_frequency";
    private static final String motionsense_rawMotion_enable="motionsense_rawMotion_enable";
    private static final String motionsense_rawMotion_frequency="motionsense_rawMotion_frequency";
    private static final String motionsense_battery_enable="motionsense_battery_enable";
    private static final String motionsense_led_enable="motionsense_led_enable";
    private static final String motionsense_led_frequency="motionsense_led_frequency";
    private static final String motionsense_ledFiltered_enable="motionsense_ledFiltered_enable";
    private static final String motionsense_ledRed_enable="motionsense_ledRed_enable";
    private static final String motionsense_ledGreen_enable="motionsense_ledGreen_enable";
    private static final String motionsense_ledInfrared_enable="motionsense_ledInfrared_enable";
    private static final String motionsense_rawLed_enable="motionsense_rawLed_enable";
    private static final String motionsense_quaternion_enable="motionsense_quaternion_enable";
    private static final String motionsense_quaternion_frequency="motionsense_quaternion_frequency";
    private static final String motionsense_magnetometer_enable="motionsense_magnetometer_enable";
    private static final String motionsense_magnetometer_frequency="motionsense_magnetometer_frequency";
    private static final String motionsense_magnetometerSensitivity_enable="motionsense_magnetometerSensitivity_enable";
    private static final String motionsense_magnetometerSensitivity_frequency="motionsense_magnetometerSensitivity_frequency";
    private static final String motionsense_rawMagnetometer_enable="motionsense_rawMagnetometer_enable";
    private static final String motionsense_rawMagnetometer_frequency="motionsense_rawMagnetometer_frequency";
    private static final String motionsense_sequenceNumberMagnetometer_enable="motionsense_sequenceNumberMagnetometer_enable";
    private static final String motionsense_sequenceNumberMagnetometer_frequency="motionsense_sequenceNumberMagnetometer_frequency";

    private HashMap<String, Object> map;
    ConfigDeviceSettings(HashMap<String, Object> map){
        this.map = map;
    }
    String getPlatformType(){
        return (String) map.get(motionsense_device_platformType);
    }
    String getPlatformId(){
        return (String) map.get(motionsense_device_platformId);
    }
    String getVersion(){
        return (String) map.get(motionsense_device_version);
    }
    int getMinimumConnectionInterval(){
        return (int) map.get(motionsense_device_minimumConnectionInterval);
    }
    boolean isAccelerometerEnable(){
        return (boolean) map.get(motionsense_accelerometer_enable);
    }
    double getAccelerometerFrequency(){
        return (double) map.get(motionsense_accelerometer_frequency);
    }
    double getAccelerometerSensitivity(){
        return (double) map.get(motionsense_accelerometer_sensitivity);
    }
    boolean isGyroscopeEnable(){
        return (boolean) map.get(motionsense_gyroscope_enable);
    }
    double getGyroscopeFrequency(){
        return (double) map.get(motionsense_gyroscope_frequency);
    }
    double getGyroscopeSensitivity(){
        return (double) map.get(motionsense_gyroscope_sensitivity);
    }

    boolean isEqual(String platformType, String platformId, String version) {
        if(getPlatformType()==null || !getPlatformType().equals(platformType)) return false;
        if(getPlatformId()==null || !getPlatformId().equals(platformId)) return false;
        return getVersion() != null && getVersion().equals(version);
    }

    public boolean isEqual(String platformType, String version) {
        if(getPlatformType()==null || !getPlatformType().equals(platformType)) return false;
        return getVersion() != null && getVersion().equals(version);
    }
}
