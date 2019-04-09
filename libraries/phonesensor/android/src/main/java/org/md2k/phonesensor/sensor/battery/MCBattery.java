package org.md2k.phonesensor.sensor.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import org.md2k.phonesensor.SensorType;
import org.md2k.phonesensor.sensor.Comparison;
import org.md2k.phonesensor.sensor.MCAbstractSensor;

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
public class MCBattery extends MCAbstractSensor {
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Read Battery
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            double[] result = new double[]{100.0 * (double) level / (double) scale};
            setSample(System.currentTimeMillis(), result);
        }
    };
    public void setWriteAsReceived(){
        super.setWriteAsReceived();
    }
    public void setWriteFixed(double writeFrequency, TimeUnit timeUnit){
        super.setWriteFixed(writeFrequency, timeUnit);
    }
    public void setWriteOnChange(Comparison comparison){
        super.setWriteOnChange(comparison);
    }

    public MCBattery(Context context) {
        super(context, SensorType.BATTERY,null);
        setWriteFixed(1, TimeUnit.MINUTES);
    }

    @Override
    public void startSensing() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    public void stopSensing() {
        context.unregisterReceiver(mBroadcastReceiver);
    }


    @Override
    public boolean isSupported() {
        return true;
    }


    @Override
    public HashMap<String, String> getSensorInfo() {
        BatteryManager b;
        HashMap<String, String> h = new HashMap<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            b = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
            int chargeCounter = b.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            h.put("CAPACITY", Integer.toString(chargeCounter));
        }
        return h;
    }

    @Override
    protected boolean isChanged(Object prevSample, Object curSample, Comparison comparison) {
        double[] p = (double[]) prevSample;
        double[] c = (double[]) curSample;
        switch (comparison.getComparisonType()){
            case NOT_EQUAL: return p[0]!=c[0];
            case SAMPLE_DIFFERENCE: return Math.abs(p[0]-c[0])>comparison.getValue();
            default:return true;
        }
    }

}
