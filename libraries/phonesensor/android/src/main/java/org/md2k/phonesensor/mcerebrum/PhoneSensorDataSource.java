package org.md2k.phonesensor.mcerebrum;

import android.content.Context;

import org.md2k.mcerebrumapi.data.MCDataType;
import org.md2k.mcerebrumapi.data.MCSampleType;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.datakitapi.datasource.constants.MCPlatformType;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataDescriptor;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataSourceMetaData;
import org.md2k.mcerebrumapi.datakitapi.datasource.unit.MCUnit;
import org.md2k.phonesensor.MCSensorManager;
import org.md2k.phonesensor.sensor.SensorType;

import java.util.HashMap;

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
class PhoneSensorDataSource {
    private HashMap<String, MCDataSource> d;

    PhoneSensorDataSource(Context context) {
        d = new HashMap<>();

        d.put(SensorType.ACCELEROMETER.name(), accelerometer(context));
        d.put(SensorType.ACCELEROMETER_LINEAR.name(), accelerometerLinear(context));
        d.put(SensorType.ACTIVITY_TYPE.name(), activityType(context));
        d.put(SensorType.AIR_PRESSURE.name(), airPressure(context));
        d.put(SensorType.AMBIENT_LIGHT.name(), ambientLight(context));
        d.put(SensorType.AMBIENT_TEMPERATURE.name(), ambientTemperature(context));
        d.put(SensorType.BATTERY.name(), battery(context));
        d.put(SensorType.BLUETOOTH_STATUS.name(), bluetoothStatus(context));
        d.put(SensorType.CHARGING_STATUS.name(), chargingStatus(context));
        d.put(SensorType.CONNECTIVITY_STATUS.name(), connectivityStatus(context));
        d.put(SensorType.GPS.name(), gps(context));
        d.put(SensorType.GPS_STATUS.name(), gpsStatus(context));
        d.put(SensorType.GRAVITY.name(), gravity(context));
        d.put(SensorType.GYROSCOPE.name(), gyroscope(context));
        d.put(SensorType.MAGNETOMETER.name(), magnetometer(context));
        d.put(SensorType.PROXIMITY.name(), proximity(context));
        d.put(SensorType.RELATIVE_HUMIDITY.name(), relativeHumidity(context));
        d.put(SensorType.SIGNIFICANT_MOTION.name(), significantMotion(context));
        d.put(SensorType.STEP_COUNT.name(), stepCount(context));
        d.put(SensorType.WIFI_STATUS.name(), wifiStatus(context));
    }
    MCDataSource getDataSource(SensorType sensorType){
        return d.get(sensorType.name());
    }
    HashMap<String, MCDataSource> getDataSources(){
        return d;
    }


    private static MCDataSource accelerometer(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.ACCELEROMETER).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("x", "y", "z")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Acceleration force along the x axis (including gravity) in m/s^2").setUnit(MCUnit.METER_PER_SECOND_SQUARED).build())
                .setDataDescriptor(1, MCDataDescriptor.builder().setDescription("Acceleration force along the y axis (including gravity) in m/s^2").setUnit(MCUnit.METER_PER_SECOND_SQUARED).build())
                .setDataDescriptor(2, MCDataDescriptor.builder().setDescription("Acceleration force along the z axis (including gravity) in m/s^2").setUnit(MCUnit.METER_PER_SECOND_SQUARED).build())
                .setDataSourceType(SensorType.ACCELEROMETER.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Accelerometer")
                        .setDescription("Measures the acceleration force in m/s^2 that is applied to a device on all three physical axes (x, y, and z), including the force of gravity.")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "Accelerometer")
                        .build())
                .build();
    }

    private static MCDataSource accelerometerLinear(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.ACCELEROMETER_LINEAR).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("x", "y", "z")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Acceleration force along the x axis (excluding gravity) in m/s^2").setUnit(MCUnit.METER_PER_SECOND_SQUARED).build())
                .setDataDescriptor(1, MCDataDescriptor.builder().setDescription("Acceleration force along the y axis (excluding gravity) in m/s^2").setUnit(MCUnit.METER_PER_SECOND_SQUARED).build())
                .setDataDescriptor(2, MCDataDescriptor.builder().setDescription("Acceleration force along the z axis (excluding gravity) in m/s^2").setUnit(MCUnit.METER_PER_SECOND_SQUARED).build())
                .setDataSourceType(SensorType.ACCELEROMETER_LINEAR.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Accelerometer (Linear)")
                        .setDescription("Measures the acceleration force in m/s^2 that is applied to a device on all three physical axes (x, y, and z), excluding the force of gravity.")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "Accelerometer (Linear)")
                        .build())
                .build();
    }

    private static MCDataSource activityType(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.ACTIVITY_TYPE).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("activityType")
                .setDataDescriptor(0, MCDataDescriptor.builder()
                        .setDescription("The detected activity of the device (Detected activity: STILL(0), ON_FOOT(1), WALKING(2), RUNNING(3), ON_BICYCLE(4), IN_VEHICLE(5), TILTING(6), UNKNOWN(7)").build())
                .setDataSourceType(SensorType.ACTIVITY_TYPE.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Activity Type")
                        .setDescription("The detected activity of the device (Detected activity: STILL(0), ON_FOOT(1), WALKING(2), RUNNING(3), ON_BICYCLE(4), IN_VEHICLE(5), TILTING(6), UNKNOWN(7)")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "Activity Type")
                        .build())
                .build();
    }

    private static MCDataSource airPressure(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.AIR_PRESSURE).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("airPressure")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Measures the atmospheric pressure in hectopascal (hPa).").setUnit(MCUnit.HECTOPASCAL).build())
                .setDataSourceType(SensorType.AIR_PRESSURE.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Air Pressure")
                        .setDescription("Measures the atmospheric pressure in hectopascal (hPa).")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "Air Pressure")
                        .build())
                .build();
    }

    private static MCDataSource ambientLight(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.AMBIENT_LIGHT).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("ambientLight")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Measures the current illumination in SI lux units.").setUnit(MCUnit.LUX).build())
                .setDataSourceType(SensorType.AMBIENT_LIGHT.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Ambient Light")
                        .setDescription("Measures the current illumination in SI lux units.")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "Ambient Light")
                        .build())
                .build();
    }

    private static MCDataSource ambientTemperature(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.AMBIENT_TEMPERATURE).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("ambientTemperature")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Measures the ambient (room) temperature in degrees Celsius.").setUnit(MCUnit.CELSIUS).build())
                .setDataSourceType(SensorType.AMBIENT_TEMPERATURE.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Ambient Temperature")
                        .setDescription("Measures the ambient (room) temperature in degrees Celsius.")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "Ambient Temperature")
                        .build())
                .build();
    }

    private static MCDataSource battery(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.BATTERY).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("battery")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Battery Level in percentage").setUnit(MCUnit.PERCENTAGE).build())
                .setDataSourceType(SensorType.BATTERY.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Battery")
                        .setDescription("Measures battery level of the phone in percentage")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "Battery")
                        .build())
                .build();
    }

    private static MCDataSource bluetoothStatus(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.BLUETOOTH_STATUS).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("bluetoothStatus")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Check whether bluetooth is on or off. Values: BLUETOOTH_OFF(0), BLUETOOTH_ON(1)").build())
                .setDataSourceType(SensorType.BLUETOOTH_STATUS.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Bluetooth Status")
                        .setDescription("Check whether bluetooth is on or off. Values: BLUETOOTH_OFF(0), BLUETOOTH_ON(1)")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "Bluetooth Status")
                        .build())
                .build();
    }

    private static MCDataSource chargingStatus(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.CHARGING_STATUS).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("chargingStatus")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Measures the current charge status. Values: NOT_CHARGING(0), BATTERY_PLUGGED_AC(1), BATTERY_PLUGGED_USB(2), BATTERY_PLUGGED_WIRELESS(3)").build())
                .setDataSourceType(SensorType.CHARGING_STATUS.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Charging Status")
                        .setDescription("Measures the current charge status. Values: NOT_CHARGING(0), BATTERY_PLUGGED_AC(1), BATTERY_PLUGGED_USB(2), BATTERY_PLUGGED_WIRELESS(3)")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "Charging Status")
                        .build())
                .build();
    }

    private static MCDataSource connectivityStatus(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.CONNECTIVITY_STATUS).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("status")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Measures the connectivity status. Values: NOT_CONNECTED(0), CONNECTED_WIFI(1), CONNECTED_MOBILE(2)").build())
                .setDataSourceType(SensorType.CONNECTIVITY_STATUS.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Connectivity Status")
                        .setDescription("Measures the connectivity status. Values: NOT_CONNECTED(0), CONNECTED_WIFI(1), CONNECTED_MOBILE(2)")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "Connectivity Status")
                        .build())
                .build();
    }

    private static MCDataSource gpsStatus(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.GPS_STATUS).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("status")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Check whether GPS is on or off. Values: GPS_OFF(0), GPS_ON(1)").build())
                .setDataSourceType(SensorType.GPS_STATUS.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("GPS Status")
                        .setDescription("Check whether GPS is on or off. Values: GPS_OFF(0), GPS_ON(1)")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "GPS Status")
                        .build())
                .build();
    }

    private static MCDataSource gravity(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.GRAVITY).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("x", "y", "z")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Force of gravity along the x axis in m/s^2").setUnit(MCUnit.METER_PER_SECOND_SQUARED).build())
                .setDataDescriptor(1, MCDataDescriptor.builder().setDescription("Force of gravity along the y axis in m/s^2").setUnit(MCUnit.METER_PER_SECOND_SQUARED).build())
                .setDataDescriptor(2, MCDataDescriptor.builder().setDescription("Force of gravity along the z axis in m/s^2").setUnit(MCUnit.METER_PER_SECOND_SQUARED).build())
                .setDataSourceType(SensorType.GRAVITY.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Gravity")
                        .setDescription("Measures the force of gravity in m/s^2 along all three physical axes (x, y, and z).")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "Gravity")
                        .build())
                .build();
    }

    private static MCDataSource gyroscope(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.GYROSCOPE).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("x", "y", "z")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Rate of rotation around the x axis in rad/s").setUnit(MCUnit.RADIAN_PER_SECOND).build())
                .setDataDescriptor(1, MCDataDescriptor.builder().setDescription("Rate of rotation around the y axis in rad/s").setUnit(MCUnit.RADIAN_PER_SECOND).build())
                .setDataDescriptor(2, MCDataDescriptor.builder().setDescription("Rate of rotation around the z axis in rad/s").setUnit(MCUnit.RADIAN_PER_SECOND).build())
                .setDataSourceType(SensorType.GYROSCOPE.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Gyroscope")
                        .setDescription("Measures a device's rate of rotation in rad/s around each of the three physical axes (x, y, and z).")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "Gyroscope")
                        .build())
                .build();
    }

    private static MCDataSource gps(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.GPS).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("latitude", "longitude")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Latitude").setUnit(MCUnit.DEGREE).build())
                .setDataDescriptor(1, MCDataDescriptor.builder().setDescription("Longitude").setUnit(MCUnit.DEGREE).build())
                .setDataSourceType(SensorType.GPS.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Location")
                        .setDescription("Measures latitude and longitude")
                        .setUnit(MCUnit.DEGREE)
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "GPS")
                        .build())
                .build();
    }

    private static MCDataSource magnetometer(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.MAGNETOMETER).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("x", "y", "z")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Geomagnetic field strength along the x axis in uT").setUnit("MICRO_TESLA").build())
                .setDataDescriptor(1, MCDataDescriptor.builder().setDescription("Geomagnetic field strength along the y axis in uT").setUnit("MICRO_TESLA").build())
                .setDataDescriptor(2, MCDataDescriptor.builder().setDescription("Geomagnetic field strength along the z axis in uT").setUnit("MICRO_TESLA").build())
                .setDataSourceType(SensorType.MAGNETOMETER.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Magnetometer")
                        .setDescription("Measures the geomagnetic field strength along the 3 sensor axes.")
                        .setMetaData(sensorInfo)
                        .setUnit("MICRO_TESLA")
                        .setMetaData("TITLE", "Magnetometer")
                        .build())
                .build();
    }

    private static MCDataSource proximity(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.PROXIMITY).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("proximity")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Measures how far away an object is from a device in cm.").setUnit(MCUnit.CENTIMETER).build())
                .setDataSourceType(SensorType.PROXIMITY.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Proximity")
                        .setDescription("Measures how far away an object is from a device in cm.")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "Proximity")
                        .build())
                .build();
    }

    private static MCDataSource relativeHumidity(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.RELATIVE_HUMIDITY).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("relativeHumidity")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Measures the relative humidity in percent.").setUnit(MCUnit.PERCENTAGE).build())
                .setDataSourceType(SensorType.RELATIVE_HUMIDITY.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Relative Humidity")
                        .setDescription("Measures the relative humidity in percent")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "Relative Humidity")
                        .build())
                .build();
    }

    private static MCDataSource significantMotion(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.SIGNIFICANT_MOTION).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("significantMotion")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Triggers an event each time significant motion is detected.").build())
                .setDataSourceType(SensorType.SIGNIFICANT_MOTION.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Significant Motion")
                        .setDescription("Triggers an event each time significant motion is detected. value is always 1.0")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "Significant Motion")
                        .build())
                .build();
    }

    private static MCDataSource stepCount(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.STEP_COUNT).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("stepCount")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Measures the number of steps taken by the user from last sample.").build())
                .setDataSourceType(SensorType.STEP_COUNT.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("Step Count")
                        .setDescription("Measures the number of steps taken by the user from last sample.")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "Step Count")
                        .build())
                .build();
    }

    private static MCDataSource wifiStatus(Context context) {
        HashMap<String, String> sensorInfo = MCSensorManager.getInstance(context).getSensor(SensorType.WIFI_STATUS).getSensorInfo();
        if (sensorInfo == null) sensorInfo = new HashMap<>();
        return MCDataSource.registerBuilder()
                .setDataType(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY)
                .setColumnNames("status")
                .setDataDescriptor(0, MCDataDescriptor.builder().setDescription("Check whether WIFI is on or off. Values: WIFI_OFF(0), WIFI_ON(1)").build())
                .setDataSourceType(SensorType.WIFI_STATUS.name())
                .setPlatformType(MCPlatformType.PHONE)
                .setDataSourceMetaData(MCDataSourceMetaData.builder()
                        .setName("WIFI Status")
                        .setDescription("Check whether WIFI is on or off. Values: WIFI_OFF(0), WIFI_ON(1)")
                        .setMetaData(sensorInfo)
                        .setMetaData("TITLE", "WiFi Status")
                        .build())
                .build();
    }

}
