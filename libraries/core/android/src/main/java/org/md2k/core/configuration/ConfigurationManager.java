package org.md2k.core.configuration;

import android.content.Context;

import org.md2k.core.Core;
import org.md2k.core.ReceiveCallback;
import org.md2k.mcerebrumapi.time.DateTime;

import java.io.File;
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
public class ConfigurationManager {
    private static final String CONFIG_DIRECTORY = "config";
    private static final String DEFAULT_CONFIG_FILENAME = "default_config.json";
    private static final String CONFIG_FILENAME = "config.json";
    private Context context;
    private Configuration defaultConfig;
    private Configuration config;

    public ConfigurationManager(Context context) {
        defaultConfig = new Configuration(context.getFilesDir().getAbsolutePath() + File.separator + CONFIG_DIRECTORY, DEFAULT_CONFIG_FILENAME);
        config = new Configuration(context.getFilesDir().getAbsolutePath() + File.separator + CONFIG_DIRECTORY, CONFIG_FILENAME);
        this.context = context;
        if (!defaultConfig.exists()) {
            HashMap<String, Object> d = new HashMap<>();
            d.put(ConfigId.core_config_filename, DEFAULT_CONFIG_FILENAME);
            d.put(ConfigId.core_config_from, "asset");
            setDefaultConfig(d, new ReceiveCallback() {
                @Override
                public void onReceive(Object obj) {

                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }

    public HashMap<String, Object> get(String id) {
        HashMap<String, Object> h = config.get(id);
        HashMap<String, Object> h1 = defaultConfig.get(id);
        h1.putAll(h);
        return h1;
    }

    public Object getValue(String key) {
        Object value = config.getValue(key);
        if (value != null) return value;
        return defaultConfig.getValue(key);
    }

    public void setValue(String key, Object value) {
        config.append(key, value);
    }

    public void set(HashMap<String, Object> data) {
        config.append(data);
    }

    public void setDefaultConfig(final HashMap<String, Object> settings, final ReceiveCallback receiveCallback){
        String type= (String) settings.get(ConfigId.core_config_from);
        final String filename = (String) settings.get(ConfigId.core_config_filename);
        switch(type){
            case "asset":
                HashMap<String, Object> def;
                HashMap<String, Object> c =new HashMap<>();
                def = Asset.read(context, CONFIG_DIRECTORY, filename);
                c.put(ConfigId.core_config_createTime, DateTime.getCurrentTime());
                c.put(ConfigId.core_config_from, "asset");
                c.put(ConfigId.core_config_filename, filename);
                File f = new File(context.getApplicationInfo().sourceDir);
                c.put(ConfigId.core_config_publishTime, f.lastModified());
                c.put(ConfigId.core_config_fileSize, Asset.getFileSize(context, CONFIG_DIRECTORY, filename));
                c.put(ConfigId.core_login_isLoggedIn, false);
                c.putAll(config.get(ConfigId.core_login));
                defaultConfig.set(def);
                config.set(c);
                receiveCallback.onReceive(true);
                break;
            case "cerebral_cortex":
                Core.cerebralCortex.downloadConfigurationFile(filename, new ReceiveCallback() {
                    @Override
                    public void onReceive(Object obj) {
                        HashMap<String, Object> def = (HashMap<String, Object>) obj;
                        HashMap<String, Object> c = new HashMap<>(settings);
                        c.put(ConfigId.core_config_createTime, DateTime.getCurrentTime());
                        c.putAll(config.get(ConfigId.core_login));
                        defaultConfig.set(def);
                        config.set(c);
                        receiveCallback.onReceive(true);
                    }

                    @Override
                    public void onError(Exception e) {
                        receiveCallback.onError(e);
                    }
                });

                break;
        }
    }
    public void getDefaultConfigList(String type, ReceiveCallback receiveCallback) {
        if (type.equals("asset")) {
            receiveCallback.onReceive(Asset.getList(context, CONFIG_DIRECTORY));
        } else if (type.equals("cerebral_cortex")) {
            Core.cerebralCortex.getConfigurationList(receiveCallback);
        }
    }

}
