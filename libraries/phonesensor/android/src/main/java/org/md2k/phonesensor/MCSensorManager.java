package org.md2k.phonesensor;

import android.content.Context;

import org.md2k.phonesensor.sensor.ISensor;
import org.md2k.phonesensor.sensor.SensorType;
import org.md2k.phonesensor.sensor.accelerometer.MCAccelerometer;
import org.md2k.phonesensor.sensor.accelerometer_linear.MCAccelerometerLinear;
import org.md2k.phonesensor.sensor.activity_type.MCActivityType;
import org.md2k.phonesensor.sensor.air_pressure.MCAirPressure;
import org.md2k.phonesensor.sensor.ambient_light.MCAmbientLight;
import org.md2k.phonesensor.sensor.ambient_temperature.MCAmbientTemperature;
import org.md2k.phonesensor.sensor.battery.MCBattery;
import org.md2k.phonesensor.sensor.bluetooth_status.MCBluetoothStatus;
import org.md2k.phonesensor.sensor.charging_status.MCChargingStatus;
import org.md2k.phonesensor.sensor.connectivity_status.MCConnectivityStatus;
import org.md2k.phonesensor.sensor.gps.MCGps;
import org.md2k.phonesensor.sensor.gps_status.MCGPSStatus;
import org.md2k.phonesensor.sensor.gravity.MCGravity;
import org.md2k.phonesensor.sensor.gyroscope.MCGyroscope;
import org.md2k.phonesensor.sensor.magnetometer.MCMagnetometer;
import org.md2k.phonesensor.sensor.proximity.MCProximity;
import org.md2k.phonesensor.sensor.relative_humidity.MCRelativeHumidity;
import org.md2k.phonesensor.sensor.significant_motion.MCSignificantMotion;
import org.md2k.phonesensor.sensor.step_count.MCStepCount;
import org.md2k.phonesensor.sensor.wifi_status.MCWifiStatus;

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
public class MCSensorManager {
    private static MCSensorManager instance;
    private HashMap<SensorType, ISensor> sensors;

    public static MCSensorManager getInstance(Context context) {
        if (instance == null)
            instance = new MCSensorManager(context.getApplicationContext());
        return instance;
    }

    private MCSensorManager(Context context) {
        sensors = new HashMap<>();
        sensors.put(SensorType.ACCELEROMETER, new MCAccelerometer(context));
        sensors.put(SensorType.ACCELEROMETER_LINEAR, new MCAccelerometerLinear(context));
        sensors.put(SensorType.ACTIVITY_TYPE, new MCActivityType(context));
        sensors.put(SensorType.AIR_PRESSURE, new MCAirPressure(context));
        sensors.put(SensorType.AMBIENT_LIGHT, new MCAmbientLight(context));
        sensors.put(SensorType.AMBIENT_TEMPERATURE, new MCAmbientTemperature(context));
        sensors.put(SensorType.BATTERY, new MCBattery(context));
        sensors.put(SensorType.BLUETOOTH_STATUS, new MCBluetoothStatus(context));
        sensors.put(SensorType.CHARGING_STATUS, new MCChargingStatus(context));
        sensors.put(SensorType.CONNECTIVITY_STATUS, new MCConnectivityStatus(context));
        sensors.put(SensorType.GPS, new MCGps(context));
        sensors.put(SensorType.GPS_STATUS, new MCGPSStatus(context));
        sensors.put(SensorType.GRAVITY, new MCGravity(context));
        sensors.put(SensorType.GYROSCOPE, new MCGyroscope(context));
        sensors.put(SensorType.MAGNETOMETER, new MCMagnetometer(context));
        sensors.put(SensorType.PROXIMITY, new MCProximity(context));

        sensors.put(SensorType.RELATIVE_HUMIDITY, new MCRelativeHumidity(context));
        sensors.put(SensorType.SIGNIFICANT_MOTION, new MCSignificantMotion(context));
        sensors.put(SensorType.STEP_COUNT, new MCStepCount(context));
        sensors.put(SensorType.WIFI_STATUS, new MCWifiStatus(context));
/*        sensors.put(SensorType.BLUETOOTH, null);
        sensors.put(SensorType.NETWORK_STATUS, null);
        sensors.put(SensorType.NETWORK, null);
        sensors.put(SensorType.TELEPHONY, null);
        sensors.put(SensorType.WIFI_NEARBY, null);
        sensors.put(SensorType.WEATHER, new MCWeather(context));
*/
    }
    public <T extends ISensor> T  getSensor(SensorType sensorType){
        return (T) sensors.get(sensorType);
    }
}
