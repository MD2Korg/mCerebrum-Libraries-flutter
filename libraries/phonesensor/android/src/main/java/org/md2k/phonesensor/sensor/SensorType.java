package org.md2k.phonesensor.sensor;

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
public enum SensorType {
    ACCELEROMETER,//1
    ACCELEROMETER_LINEAR,//2
    ACTIVITY_TYPE,//3
    AIR_PRESSURE,//4
    AMBIENT_LIGHT,//5
    AMBIENT_TEMPERATURE,//6
    BATTERY,//7
    BLUETOOTH_STATUS,//8
    CHARGING_STATUS,//9
    CONNECTIVITY_STATUS,//10
    GPS,//11
    GPS_STATUS,//12
    GRAVITY,//13
    GYROSCOPE,//14
    MAGNETOMETER,//15
    PROXIMITY,//16
    RELATIVE_HUMIDITY,//17
    SIGNIFICANT_MOTION,//18
    STEP_COUNT,//19
    WIFI_STATUS,//20

/*    WIFI_NEARBY,
    NETWORK_STATUS,
    NETWORK,
    TELEPHONY,
    GEOFENCE,
    WEATHER,
    BLUETOOTH_NEARBY,
*/
}
