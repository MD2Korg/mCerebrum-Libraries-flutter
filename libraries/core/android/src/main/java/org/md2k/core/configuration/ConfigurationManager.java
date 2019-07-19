package org.md2k.core.configuration;

import android.content.Context;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
public class ConfigurationManager {
    private static final String CONFIG_DIRECTORY = "config";
    private static final String DEFAULT_CONFIG_FILENAME = "default_config.json";
    private static final String CONFIG_FILENAME = "config.json";
    private Configuration defaultConfig;
    private Configuration config;

    public ConfigurationManager(Context context) {
        prepareConfig(context);
    }

    private boolean isDifferent(HashMap<String, Object> hD, HashMap<String, Object> hC){
        if(hD==null || hD.get(ConfigId.core_config_id)==null || hC==null || hC.get(ConfigId.core_config_id)==null) return true;
        String d = (String) hD.get(ConfigId.core_config_id);
        String c = (String) hC.get(ConfigId.core_config_id);
        if(d==null || c==null) return true;
        else
            return !c.equals(d);

    }
    private void prepareConfig(Context context){
        String fileDir = context.getFilesDir().getAbsolutePath() + File.separator + CONFIG_DIRECTORY;
        HashMap <String, Object> hD=Configuration.readFromFile(fileDir, DEFAULT_CONFIG_FILENAME);
        HashMap <String, Object> hC=Configuration.readFromFile(fileDir, CONFIG_FILENAME);
        if(hD!=null){
            defaultConfig = Configuration.create(fileDir, DEFAULT_CONFIG_FILENAME, hD, false);
            if(isDifferent(hD, hC)){
                hC=new HashMap<>();
                hC.put(ConfigId.core_config_id, defaultConfig.getByKey(ConfigId.core_config_id));
                hC.put(ConfigId.core_config_filename, DEFAULT_CONFIG_FILENAME);
                hC.put(ConfigId.core_config_from, "file");
                config = Configuration.create(fileDir, CONFIG_FILENAME, hC, true);
            }else{
                config = Configuration.create(fileDir, CONFIG_FILENAME, hC, false);
            }
        }else{
            hD = Configuration.readFromAsset(context);
            defaultConfig = Configuration.create(fileDir, DEFAULT_CONFIG_FILENAME, hD, true);
            hC=new HashMap<>();
            hC.put(ConfigId.core_config_id, defaultConfig.getByKey(ConfigId.core_config_id));
            hC.put(ConfigId.core_config_filename, DEFAULT_CONFIG_FILENAME);
            hC.put(ConfigId.core_config_from, "asset");
            config = Configuration.create(fileDir, CONFIG_FILENAME, hC, true);
        }
    }

    public HashMap<String, Object> getById(String id) {
        HashMap<String, Object> h = config.getById(id);
        HashMap<String, Object> h1 = defaultConfig.getById(id);
        h1.putAll(h);
        return h1;
    }
    public HashMap<String, Object> getDefaultById(String id) {
        return defaultConfig.getById(id);
    }

    public void setById(String id, HashMap<String, Object> hashMap) {
        config.removeById(id);
        HashMap<String, Object> hNew = new HashMap<>();
        for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            Object object = entry.getValue();
            Object objectDefault = defaultConfig.getByKey(key);
            if (objectDefault == null || !objectDefault.equals(object))
                hNew.put(key, object);
        }
        if (hNew.size() != 0)
            config.add(hNew);
    }

    public Object getByKey(String key) {
        Object value = config.getByKey(key);
        if (value != null) return value;
        return defaultConfig.getByKey(key);
    }

    public int getUploadTime() {
        Object o = getByKey(ConfigId.core_upload_time);
        if (o == null) return 15 * 60 * 60 * 1000;
        Double d = (Double) o;
        return d.intValue();
    }

    public String getUserId() {
        Object o = getByKey(ConfigId.core_login_userId);
        if (o == null) return null;
        else return (String) o;
    }

    public boolean isLoggedIn() {
        Object o = getByKey(ConfigId.core_login_isLoggedIn);
        if (o == null) return false;
        else return (boolean) o;
    }

    public boolean isUploaderEnabled() {
        Object o = getByKey(ConfigId.core_upload_enable);
        if (o == null) return true;
        else return (boolean) o;
    }

    public boolean isWifiOnly() {
        Object o = getByKey(ConfigId.core_upload_wifiOnly);
        if (o == null) return true;
        else return (boolean) o;
    }
}