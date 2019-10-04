package org.md2k.phonesensor;

import android.content.Context;

import java.util.HashMap;

import io.flutter.plugin.common.EventChannel;

abstract class ISensor {
    Context context;
    ISensor(Context context){
        this.context = context;
    }
    abstract void start(EventChannel.EventSink events);
    abstract void stop();
    HashMap<String, Object> createData(long timestamp, double[] sample) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("timestamp", timestamp);
        data.put("sample", sample);
        return data;
    }
}
