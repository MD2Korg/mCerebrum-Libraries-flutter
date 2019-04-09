package org.md2k.phonesensor.configuration;

import android.content.Context;

import com.google.gson.Gson;

import org.md2k.mcerebrumapi.core.file.FileReadWrite;
import org.md2k.phonesensor.SensorType;
import org.md2k.phonesensor.sensor.Comparison;
import org.md2k.phonesensor.sensor.WriteType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
public class Configuration {
    private static final String ENABLE = "enable";
    private static final String SAMPLE_RATE = "sample_rate";
    private static final String SAMPLE_RATE_UNIT = "sample_rate_unit";
    private static final String WRITE_TYPE = "write_type";
    private static final String WRITE_ON_CHANGE_TYPE = "write_on_change_type";
    private static final String WRITE_ON_CHANGE_VALUE = "write_on_change_value";

    private HashMap<String, Object> config;
    public Configuration(HashMap<String, Object> config){
        this.config = config;
    }

    public HashMap<String, Object> getConfig() {
        return config;
    }

    public double getSampleRate(SensorType sensorType) {
        String key = getKey(sensorType, SAMPLE_RATE);
        Object x = config.get(key);
        if(x==null) return 0;
        else return Double.valueOf(x.toString());
    }
    public ArrayList<SensorType> getSensorTypes(){
        ArrayList<SensorType> sensorTypes = new ArrayList<>();
        for(Map.Entry<String, Object> entry: config.entrySet()){
            String key = entry.getKey();
            if(!key.endsWith("_enable")) continue;
            key = key.replace("phonesensor_","");
            key = key.replace("_enable", "");
            SensorType s = SensorType.valueOf(key.toUpperCase());
            sensorTypes.add(s);
        }
        return sensorTypes;
    }
    private static String getKey(SensorType sensorType, String varType){
        return "phonesensor_"+sensorType.name().toLowerCase()+"_"+varType;
    }

    public TimeUnit getSampleRateUnit(SensorType sensorType) {
        String key = getKey(sensorType, SAMPLE_RATE_UNIT);
        Object x = config.get(key);
        if(x==null) return TimeUnit.SECONDS;
        else return TimeUnit.valueOf(x.toString());
    }

    public WriteType getWriteType(SensorType sensorType) {
        String key = getKey(sensorType, WRITE_TYPE);
        Object x = config.get(key);
        if(x==null) return WriteType.AS_RECEIVED;
        else return WriteType.valueOf(x.toString());
    }

    public Comparison getWriteOnChangeComparison(SensorType sensorType) {
        String key = getKey(sensorType, WRITE_ON_CHANGE_TYPE);
        if (config.containsKey(key) && config.get(key).equals("SAMPLE_DIFFERENCE")) {
            return Comparison.sampleDiff(Double.valueOf(config.get(getKey(sensorType, WRITE_ON_CHANGE_VALUE)).toString()));
        } else return Comparison.notEqual();
    }

    private static String getDirectory(Context context, String dirName) {
        String result = context.getFilesDir().getAbsoluteFile() + File.separator + dirName + File.separator + "org.md2k.phonesensor";
        File f = new File(result);
        f.mkdirs();
        return result + File.separator;
    }
    private static HashMap<String, Object> merge(HashMap<String, Object> assets, HashMap<String, Object> config){
        HashMap<String, Object> res = new HashMap<>();
        if(config==null) return new HashMap<>(assets);
        for(Map.Entry<String, Object> entry: assets.entrySet()){
            String key = entry.getKey();
            if(config.containsKey(key)){
                res.put(key, config.get(key));
            }
            else res.put(key, entry.getValue());
        }
        return res;
    }

    public static Configuration read(Context context) {
        HashMap<String, Object> c = FileReadWrite.readJson(getDirectory(context, "config") + "config.json", HashMap.class);
        HashMap<String, Object> d = FileReadWrite.readJson(getDirectory(context, "default_config")+"default_config.json", HashMap.class);
        HashMap<String, Object> a = readDefaultFromAsset(context);
        HashMap<String, Object> temp = merge(a, d);
        HashMap<String, Object> res = merge(temp, c);
        return new Configuration(res);
    }

    private static HashMap<String, Object> readDefaultFromAsset(Context context) {
        try {
            String str = null;
            InputStream is = context.getAssets().open("default_config.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            str = new String(buffer);
            Gson gson = new Gson();
            return gson.fromJson(str, HashMap.class);
        } catch (IOException ex) {
        }
        return null;
    }

    public static void write(Context context, Configuration configuration) {
        HashMap<String, Object> d = FileReadWrite.readJson(getDirectory(context, "default_config")+"default_config.json", HashMap.class);
        HashMap<String, Object> a = readDefaultFromAsset(context);
        HashMap<String, Object> temp = merge(a, d);
        HashMap<String, Object> c = configuration.getConfig();
        for(Map.Entry<String, Object> entry: c.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue().toString();
            if(!temp.containsKey(key)) c.remove(key);
            else if(temp.get(key).toString().equals(value)) c.remove(key);
        }
        String fileDir = getDirectory(context, "config");
        FileReadWrite.writeJson(fileDir, "config.json", c);
    }

    public boolean isEnable(SensorType sensorType) {
        String key = getKey(sensorType, ENABLE);
        Object x = config.get(key);
        if(x==null) return false;
        else return Boolean.valueOf(x.toString());
    }
}
