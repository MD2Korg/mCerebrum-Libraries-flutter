package org.md2k.phonesensor.sensor.bluetooth_status;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import org.md2k.phonesensor.SensorType;
import org.md2k.phonesensor.enable.EnableCallback;
import org.md2k.phonesensor.enable.Enabler;
import org.md2k.phonesensor.enable.SensorEnabler;
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
public class MCBluetoothStatus extends MCAbstractSensor {
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        setSample(System.currentTimeMillis(), new double[]{0});
                        break;
                    case BluetoothAdapter.STATE_ON:
                        setSample(System.currentTimeMillis(), new double[]{1});
                        break;
                }
            }
        }
    };

    public MCBluetoothStatus(Context context) {
        super(context, SensorType.BLUETOOTH_STATUS,new String[]{
            Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_COARSE_LOCATION
        });
        setWriteAsReceived();
    }
    public boolean isOn(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }
    public void turnOn(){
        turnOn(new EnableCallback() {
            @Override
            public void onSuccess() {
                //TODO:
            }

            @Override
            public void onError() {
                //TODO:
            }
        });
    }
    public void turnOnSilent(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled())
            bluetoothAdapter.enable();
    }
    public void turnOff(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isEnabled())
            bluetoothAdapter.disable();
    }

    public void turnOn(EnableCallback enableCallback){
        Enabler enabler = new Enabler(context, SensorEnabler.BLUETOOTH, enableCallback);
        enabler.requestEnable();
    }
    public void setWriteOnChange(){
        super.setWriteOnChange(Comparison.notEqual());
    }

    @Override
    public void startSensing() {
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    public void stopSensing() {
        context.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public boolean isSupported() {
        return BluetoothAdapter.getDefaultAdapter() != null;
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
                return Math.abs(p[0]-c[0]) > comparison.getValue();
            case NOT_EQUAL:
                return p[0] != c[0];
            default:
                return true;
        }
    }

}
