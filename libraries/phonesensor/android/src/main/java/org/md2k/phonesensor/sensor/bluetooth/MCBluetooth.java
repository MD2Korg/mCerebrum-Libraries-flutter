package org.md2k.phonesensor.sensor.bluetooth;

import org.md2k.phonesensor.sensor.Frequency;

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
public class MCBluetooth {/* extends MCAbstractSensor {*/
    private AbstractScan scan;
    private Frequency scanDuration;
    private Frequency scanFrequency;
/*    private BluetoothType bluetoothType;

    public enum BluetoothType {
        BLUETOOTH_CLASSIC,
        BLUETOOTH_LE,
    }

    private HashSet<String> bluetoothAddress;
    private Handler hStopSensing;
    private Handler hSchedule;

    public MCBluetooth(Context context) {
        super(context, SensorType.BLUETOOTH,new String[]{
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
        hStopSensing = new Handler();
        hSchedule = new Handler();
        bluetoothAddress=new HashSet<>();
        bluetoothType = BluetoothType.BLUETOOTH_LE;
        scanDuration=new Frequency(10,TimeUnit.SECONDS);
        scanFrequency = new Frequency(5, TimeUnit.MINUTES);
    }
    public void setBluetoothType(BluetoothType bluetoothType){
        this.bluetoothType = bluetoothType;
    }
    public void setPeriodicScan(double scanDuration, TimeUnit scanDurationTimeUnit, double scanFrequency,TimeUnit scanFrequencyTimeUnit){
        this.scanDuration = new Frequency(scanDuration, scanDurationTimeUnit);
        this.scanFrequency = new Frequency(scanFrequency, scanFrequencyTimeUnit);
    }

    @Override
    public void startSensing() {
        if(bluetoothType==BluetoothType.BLUETOOTH_CLASSIC){
            scan = new ScanClassic(context);
        }
        else scan = new ScanLE(context);
        hSchedule.post(runnableSchedule);
    }
    @Override
    public void stopSensing() {
        hSchedule.removeCallbacks(runnableSchedule);
        stop();
    }
    private Runnable runnableStopSensing =new Runnable() {
        @Override
        public void run() {
            stop();
        }
    };
    private Runnable runnableSchedule=new Runnable() {
        @Override
        public void run() {
            stop();
            start();
            //TODO:
//            hSchedule.postDelayed(this,scanFrequency.);
        }
    };
    private void start(){
            //TODO:
//        hStopSensing.postDelayed(runnableStopSensing, ((BluetoothSettings) settings).getScanDuration());
        scan.start();
    }
    private void stop(){
        //todo
        scan.start();
        bluetoothAddress.clear();
        hStopSensing.removeCallbacks(runnableStopSensing);
    }

    @Override
    protected boolean isChanged(Object prevData, Object curData, Comparison comparison) {
        return true;
    }

    @Override
    public boolean isSupported() {
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    @Override
    public HashMap<String, String> getSensorInfo() {
        return new HashMap<>();
    }*/
}
