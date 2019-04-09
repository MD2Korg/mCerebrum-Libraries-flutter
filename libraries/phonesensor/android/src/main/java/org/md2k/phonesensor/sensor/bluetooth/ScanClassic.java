package org.md2k.phonesensor.sensor.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

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
public class ScanClassic extends AbstractScan {
    private BluetoothAdapter bluetoothAdapter;
    ScanClassic(Context context){
        super(context);
    }

    @Override
    protected void start(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        registerLocalBroadcastManager();
        bluetoothAdapter.startDiscovery();
    }
    @Override
    protected void stop(){
        unregisterLocalBroadcastManager();
        bluetoothAdapter.cancelDiscovery();
    }
    // Create a BroadcastReceiver for ACTION_FOUND and ACTION_DISCOVERY_FINISHED
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            // Read the action from the intent
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {  // Device Found

                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int rssi = (int) intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                //TODO
                //send(device, rssi);

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {  // Discovery Finished
                bluetoothAdapter.startDiscovery();
            }
        }
    };

    private void registerLocalBroadcastManager() {
        // Register receivers for ACTION_FOUND and ACTION_DISCOVERY_FINISHED
        IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter finishedFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mReceiver, foundFilter);
        context.registerReceiver(mReceiver, finishedFilter);
    }

    private void unregisterLocalBroadcastManager() {
        context.unregisterReceiver(mReceiver);
    }
}
