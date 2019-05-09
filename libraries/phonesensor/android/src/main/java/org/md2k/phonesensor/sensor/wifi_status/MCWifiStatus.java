package org.md2k.phonesensor.sensor.wifi_status;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import org.md2k.phonesensor.sensor.SensorType;
import org.md2k.phonesensor.sensor.Comparison;
import org.md2k.phonesensor.sensor.MCAbstractSensor;

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
public class MCWifiStatus extends MCAbstractSensor {
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);

            switch (wifiState) {
                case WifiManager.WIFI_STATE_ENABLED: {
                    setSample(System.currentTimeMillis(), new double[]{1});
                }
                break;
                case WifiManager.WIFI_STATE_DISABLED: {
                    setSample(System.currentTimeMillis(), new double[]{0});

                }
                break;

            }
        }
    };

    public MCWifiStatus(Context context) {
        super(context, SensorType.WIFI_STATUS, new String[]{
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
        });
        setWriteAsReceived();
    }

    public boolean isOn() {
        WifiManager w = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int wifiState = w.getWifiState();
        switch (wifiState) {
            case WifiManager.WIFI_STATE_ENABLED:
                return true;
            case WifiManager.WIFI_STATE_DISABLED:
                return false;
        }
        return false;
    }

    @Override
    public void startSensing() {
        if(isOn()){
            setSample(System.currentTimeMillis(), new double[]{1});
        }else{
            setSample(System.currentTimeMillis(), new double[]{0});
        }

        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        context.registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    public void stopSensing() {
        context.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public boolean isSupported() {
        return context.getApplicationContext().getSystemService(Context.WIFI_SERVICE)!=null;
    }

    @Override
    public HashMap<String, String> getSensorInfo() {
        HashMap<String, String> h = new HashMap<>();
        return h;
    }

    @Override
    protected boolean isChanged(Object prevSample, Object curSample, Comparison comparison) {
        double[] p = (double[]) prevSample;
        double[] c = (double[]) curSample;
        switch (comparison.getComparisonType()) {
            case SAMPLE_DIFFERENCE:
                return Math.abs(p[0] - c[0]) > comparison.getValue();
            case NOT_EQUAL:
                return p[0] != c[0];
            default:
                return true;
        }
    }

}
