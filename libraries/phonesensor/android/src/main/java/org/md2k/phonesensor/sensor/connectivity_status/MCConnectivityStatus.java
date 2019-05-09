package org.md2k.phonesensor.sensor.connectivity_status;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.md2k.mcerebrumapi.time.DateTime;
import org.md2k.phonesensor.PermissionCallback;
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
public class MCConnectivityStatus extends MCAbstractSensor {
    private static final int NOT_CONNECTED = 0;
    private static final int CONNECTED_WIFI = 1;
    private static final int CONNECTED_MOBILE = 2;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = getConnectivityStatus();
            setSample(DateTime.getCurrentTime(), new double[]{status});
        }
    };

    public MCConnectivityStatus(Context context) {
        super(context, SensorType.CONNECTIVITY_STATUS,null);
        setWriteOnChange(Comparison.notEqual());
    }
    private  int getConnectivityStatus() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return CONNECTED_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return CONNECTED_MOBILE;
        }
        return NOT_CONNECTED;
    }
    @Override
    public void startSensing() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
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
    public boolean hasPermission() {
        return true;
    }

    @Override
    public void getPermission(Activity activity, PermissionCallback permissionCallback) {
        permissionCallback.onSuccess();
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
        switch (comparison.getComparisonType()){
            case NOT_EQUAL: return p[0]!=c[0];
            case SAMPLE_DIFFERENCE: return Math.abs(p[0]-c[0])>comparison.getValue();
            default:return true;
        }
    }

}
