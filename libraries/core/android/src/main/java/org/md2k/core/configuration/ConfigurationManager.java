package org.md2k.core.configuration;

import android.content.Context;

import org.md2k.core.Core;
import org.md2k.core.ReceiveCallback;
import org.md2k.core.data.LoginInfo;
import org.md2k.mcerebrumapi.utils.DateTime;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

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
                hC.put(ConfigId.core_login_isLoggedIn, false);
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
            hC.put(ConfigId.core_login_isLoggedIn, false);
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
    public void add(String key, Object value){
        config.add(key, value);
    }
    public void add(HashMap<String, Object> h){
        config.add(h);
    }
    public void removeByKey(String key){
        config.removeByKey(key);
    }
    public void removeById(String id){
        config.removeById(id);
    }

    public Object getByKey(String key) {
        Object value = config.getByKey(key);
        if (value != null) return value;
        return defaultConfig.getByKey(key);
    }
    public boolean hasUpdate(HashMap<String, Object> newVersion){
        String curVersion = (String) defaultConfig.getByKey(ConfigId.core_config_version);
        String newConfig = (String) defaultConfig.getByKey(ConfigId.core_config_version);
        if(curVersion.equals(newVersion)) return false;
        else return true;
    }

/*
    public void hasUpdate(final ReceiveCallback receiveCallback) {
        String from = (String) config.getValue(ConfigId.core_config_from);

        if (!from.equals("cerebral_cortex")) {
            HashMap<String, Object> res = new HashMap<>();
            res.put("update",false);
            receiveCallback.onReceive(res);
            return;
        }
        final LoginInfo loginInfo = getLoginInfo();
        final String filename = (String) config.getValue(ConfigId.core_config_filename);
        Core.cerebralCortex.downloadConfigurationFile(loginInfo, filename, new ReceiveCallback() {
            @Override
            public void onReceive(Object obj) {
                HashMap<String, Object> h = (HashMap<String, Object>) obj;
                HashMap<String, Object> res = new HashMap<>();
                String curVersion = (String) defaultConfig.getValue(ConfigId.core_config_version);
                String serverVersion = (String) h.get(ConfigId.core_config_version);
                if (curVersion.equals(serverVersion)) {
                    res.put("update",false);
                    receiveCallback.onReceive(res);
                }
                else {
                    res.put(ConfigId.core_config_version, serverVersion);
                    res.put("update",true);
                    receiveCallback.onReceive(res);
                }
            }

            @Override
            public void onError(Exception e) {
                receiveCallback.onError(e);
            }
        });

    }
*/
    /*

        public void updateDefaultConfig(final ReceiveCallback receiveCallback) {
            final LoginInfo loginInfo = getLoginInfo();
            final String filename = (String) config.getValue(ConfigId.core_config_filename);
            Core.cerebralCortex.downloadConfigurationFile(loginInfo, filename, new ReceiveCallback() {
                @Override
                public void onReceive(Object obj) {
                    HashMap<String, Object> h = (HashMap<String, Object>) obj;
                    defaultConfig.set(h);
                    receiveCallback.onReceive(true);
                }

                @Override
                public void onError(Exception e) {
                    receiveCallback.onError(e);
                }
            });
        }

        public void setDefaultConfig(HashMap<String, Object> hashMap) {
            defaultConfig.set(hashMap);
            HashMap<String, Object> c = new HashMap<>();
            c.put(ConfigId.core_config_id, defaultConfig.getValue(ConfigId.core_config_id));
            c.put(ConfigId.core_config_from, "storage");
            c.put(ConfigId.core_config_createTime, DateTime.getCurrentTime());
            c.put(ConfigId.core_login_isLoggedIn, false);
            config.set(c);

        }
    */
/*
    public void setDefaultConfigFromServer(HashMap<String, Object> defaultConfig, HashMap<String, Object> config){
        this.defaultConfig.set(defaultConfig);
        this.config.set(config);
    }
    public void updateDefaultConfig(HashMap<String, Object> defaultConfig, HashMap<String, Object> config){
        this.defaultConfig.set(defaultConfig);
        this.config.append(config);
    }
*/
/*
    public void setDefaultConfigFromServer(final String filename, final ReceiveCallback receiveCallback) {
        final LoginInfo loginInfo = getLoginInfo();
        Core.cerebralCortex.downloadConfigurationFile(loginInfo, filename, new ReceiveCallback() {
            @Override
            public void onReceive(Object obj) {
                defaultConfig.set((HashMap<String, Object>) obj);
                HashMap<String, Object> c = new HashMap<>();
                c.put(ConfigId.core_config_id, defaultConfig.getValue(ConfigId.core_config_id));
                c.put(ConfigId.core_config_from, "cerebral_cortex");
                c.put(ConfigId.core_config_createTime, DateTime.getCurrentTime());
                c.put(ConfigId.core_config_filename, filename);

                c.put(ConfigId.core_login_isLoggedIn, loginInfo.isLoggedIn());
                c.put(ConfigId.core_login_serverAddress, loginInfo.getServerAddress());
                c.put(ConfigId.core_login_userId, loginInfo.getUserId());
                c.put(ConfigId.core_login_password, loginInfo.getPassword());
                c.put(ConfigId.core_login_accessToken, loginInfo.getAccessToken());

                config.set(c);
                receiveCallback.onReceive(true);
            }

            @Override
            public void onError(Exception e) {
                receiveCallback.onError(e);
            }
        });
    }
*/

/*
    public void getConfigListFromServer(LoginInfo loginInfo, ReceiveCallback receiveCallback) {
        Core.cerebralCortex.getConfigurationList(loginInfo, receiveCallback);
    }
*/

/*    private LoginInfo getLoginInfo() {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setLoggedIn((Boolean) Core.configuration.getValue(ConfigId.core_login_isLoggedIn));
        loginInfo.setServerAddress((String) Core.configuration.getValue(ConfigId.core_login_serverAddress));
        loginInfo.setUserId((String) Core.configuration.getValue(ConfigId.core_login_userId));
        loginInfo.setPassword((String) Core.configuration.getValue(ConfigId.core_login_password));
        loginInfo.setAccessToken((String) Core.configuration.getValue(ConfigId.core_login_accessToken));
        return loginInfo;
    }

    public void updateConfig(final ReceiveCallback receiveCallback) {
        final LoginInfo loginInfo = getLoginInfo();
        final String filename = (String) Core.configuration.getValue(ConfigId.core_config_filename);
        Core.cerebralCortex.downloadConfigurationFile(loginInfo, filename, new ReceiveCallback() {
            @Override
            public void onReceive(Object obj) {
                defaultConfig.set((HashMap<String, Object>) obj);
                HashMap<String, Object> c = new HashMap<>();
                c.put(ConfigId.core_config_createTime, DateTime.getCurrentTime());
                config.append(c);
                receiveCallback.onReceive(true);
            }

            @Override
            public void onError(Exception e) {
                receiveCallback.onError(e);
            }
        });

    }*/
}