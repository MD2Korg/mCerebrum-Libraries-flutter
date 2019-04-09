package org.md2k.phonesensor.sensor;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.md2k.phonesensor.PermissionCallback;
import org.md2k.phonesensor.SensorType;
import org.md2k.phonesensor.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;


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
public abstract class MCAbstractSensor implements ISensor {
    private SensorType sensorType;
    protected Context context;
    private ArrayList<EventListener> eventListeners;
    private Handler handlerWriteFixed;
    private Object lastSample;
    private boolean running;
    private long writeInMillis;
    private WriteType writeType;
    private Frequency writeFrequency;
    private Comparison writeOnChangeComparison;
    private String[] requiredPermissions;
    private long startTimestamp;
    private long lastTimestamp;
    private int sampleNo;

    //Actual sensor sensing
    protected abstract void startSensing();
    protected abstract void stopSensing();
    //Sensor Information
    @Override
    public SensorType getSensorType() {
        return sensorType;
    }

    public abstract HashMap<String, String> getSensorInfo();

    //Permission
    public boolean hasPermission(){
        if(requiredPermissions==null) return true;
        for (String requirePermission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(context, requirePermission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @Override
    public void getPermission(Activity activity, final PermissionCallback permissionCallback) {
        if(requiredPermissions==null || requiredPermissions.length==0) {
            permissionCallback.onSuccess();
            return;
        }
        Dexter.withActivity(activity)
                .withPermissions(requiredPermissions).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted())
                    permissionCallback.onSuccess();
                else permissionCallback.onDenied();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                Log.d("abc","abc");
            }
        }).check();

    }

    public abstract boolean isSupported();
    protected abstract boolean isChanged(Object prevSample, Object curSample, Comparison comparison);

    protected void setWriteAsReceived(){
        this.writeType = WriteType.AS_RECEIVED;
    }
    protected void setWriteFixed(double writeFrequency, TimeUnit timeUnit){
        this.writeType = WriteType.FIXED;
        this.writeFrequency = new Frequency(writeFrequency, timeUnit);
    }
    protected void setWriteOnChange(Comparison comparison){
        this.writeType = WriteType.ON_CHANGE;
        this.writeOnChangeComparison = comparison;
    }

    protected MCAbstractSensor(Context context, SensorType sensorType, String[] requiredPermissions) {
        this.context = context;
        this.sensorType = sensorType;
        running = false;
        eventListeners=new ArrayList<>();
        handlerWriteFixed = new Handler(Looper.getMainLooper());
        this.writeType = WriteType.AS_RECEIVED;
        this.requiredPermissions = requiredPermissions;
    }
    private int checkStatus() {
        if(!isSupported()) return Status.NOT_SUPPORTED;
        if(!hasPermission()) return Status.PERMISSION_DENIED;
        return Status.SUCCESS;
    }

    public int start(EventListener eventListener) {
        int status = checkStatus();
        if(status!=Status.SUCCESS) return status;
        eventListeners.add(eventListener);
        if (running) return Status.SUCCESS;
        startTimestamp = System.currentTimeMillis();
        lastTimestamp=startTimestamp;
        running = true;
        lastSample = null;
        if(writeType== WriteType.FIXED) {
            writeInMillis = (long) Math.ceil(1000.0/ writeFrequency.as(TimeUnit.SECONDS).getFrequency());
            handlerWriteFixed.postDelayed(runnable, writeInMillis);
        }
        startSensing();
        return Status.SUCCESS;
    }

    public void stop(EventListener eventListener) {
        eventListeners.remove(eventListener);
        if (!running) return;
        if (eventListeners.size() == 0) {
            running = false;
            if(writeType== WriteType.FIXED)
                handlerWriteFixed.removeCallbacks(runnable);
            stopSensing();
        }
    }
    public void stopAll(){
        for(int i =0;i<eventListeners.size();i++){
            stop(eventListeners.get(i));
        }
    }
    public boolean isRunning() {
        return running;
    }


    protected void setSample(long timestamp, Object sample){
        switch(writeType){
            case FIXED:
                this.lastSample = sample;
//                this.lastTimestamp = timestamp;
                break;
            case AS_RECEIVED:
                broadcastSample(timestamp, sample);
                break;
            case ON_CHANGE:
                if(lastSample ==null || isChanged(lastSample, sample, writeOnChangeComparison)){
                    broadcastSample(timestamp, sample);
                    this.lastSample = sample;
                    this.lastTimestamp = timestamp;
                }
                break;
            default:
                break;
        }
    }

    private void broadcastSample(long timestamp, Object sample) {
        sampleNo++;
        for (int i = 0; i < eventListeners.size(); i++) {
            eventListeners.get(i).onChange(timestamp, sample);
        }
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long curTimestamp = System.currentTimeMillis();
            while(curTimestamp-lastTimestamp>writeInMillis) {
                lastTimestamp += writeInMillis;
                if (lastSample != null) {
                    broadcastSample(lastTimestamp, lastSample);
                }
            }
//                long nextTriggerTime = startTimestamp+(lastTimestamp+1)*sampleReceived;
//                handlerWriteFixed.postAtTime(runnable, SystemClock.uptimeMillis()+(lastTimestamp-startTimestamp)*(sampleReceived+1));
                handlerWriteFixed.postDelayed(runnable, writeInMillis);
        }
    };


    @Override
    public long getLastSampleTimestamp() {
        return lastTimestamp;
    }

    @Override
    public long getStartTimestamp() {
        return startTimestamp;
    }

    @Override
    public int getSampleNo() {
        return sampleNo;
    }
    @Override
    public Object getLastSample(){
        return lastSample;
    }
    @Override
    public String[] getPermissionList(){
        return requiredPermissions;
    }
}
