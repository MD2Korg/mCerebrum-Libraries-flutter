package org.md2k.core.configuration;

import android.content.Context;

import org.md2k.core.Core;
import org.md2k.core.ReceiveCallback;
import org.md2k.core.info.LoginInfo;
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
    private Configuration defaultConfig;
    private Configuration config;

    public ConfigurationManager(Context context) {
        defaultConfig = new Configuration(context.getFilesDir().getAbsolutePath() + File.separator + CONFIG_DIRECTORY, DEFAULT_CONFIG_FILENAME);
        config = new Configuration(context.getFilesDir().getAbsolutePath() + File.separator + CONFIG_DIRECTORY, CONFIG_FILENAME);
        boolean dExist = defaultConfig.exists();
        boolean cExist = config.exists();
        if (!dExist) {
            if (!cExist) {
                HashMap<String, Object> c = new HashMap<>();
                c.put(ConfigId.core_config_id, "core");
                c.put(ConfigId.core_config_title, "Core");
                c.put(ConfigId.core_config_description, "Config file created by Core Library");
                c.put(ConfigId.core_config_version, "1.0.0");
                c.put(ConfigId.core_config_from, "org.md2k.core");
                c.put(ConfigId.core_config_createTime, DateTime.getCurrentTime());
                c.put(ConfigId.core_login_isLoggedIn, false);
                config.set(c);
            }
        } else {
            if (!cExist || !isSame()) {
                HashMap<String, Object> c = new HashMap<>();
                c.put(ConfigId.core_config_id, defaultConfig.getValue(ConfigId.core_config_id));
                c.put(ConfigId.core_config_createTime, DateTime.getCurrentTime());
                c.put(ConfigId.core_config_from, "storage");
                c.put(ConfigId.core_login_isLoggedIn, false);
                config.set(c);
            }
        }
    }

    private boolean isSame() {
        String c = (String) config.getValue(ConfigId.core_config_id);
        String d = (String) defaultConfig.getValue(ConfigId.core_config_id);
        if (!c.equals(d)) return false;
        c = (String) config.getValue(ConfigId.core_config_from);
        d = (String) defaultConfig.getValue(ConfigId.core_config_from);
        if (!c.equals(d)) return false;
        return true;
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

    public void removeValue(String key) {
        config.remove(key);
    }

    public void append(HashMap<String, Object> data) {
        config.append(data);
    }

    public void hasUpdate(final ReceiveCallback receiveCallback) {
        String from = (String) config.getValue(ConfigId.core_config_from);

        if (!from.equals("cerebral_cortex")) {
            receiveCallback.onReceive(false);
            return;
        }
        final LoginInfo loginInfo = getLoginInfo();
        final String filename = (String) config.getValue(ConfigId.core_config_filename);
        Core.cerebralCortex.downloadConfigurationFile(loginInfo, filename, new ReceiveCallback() {
            @Override
            public void onReceive(Object obj) {
                HashMap<String, Object> h = (HashMap<String, Object>) obj;
                String curVersion = (String) defaultConfig.getValue(ConfigId.core_config_version);
                String serverVersion = (String) h.get(ConfigId.core_config_version);
                if (curVersion.equals(serverVersion))
                    receiveCallback.onReceive(true);
                else receiveCallback.onReceive(false);
            }

            @Override
            public void onError(Exception e) {
                receiveCallback.onError(e);
            }
        });

    }

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
                c.put(ConfigId.core_login_lastLoginTime, loginInfo.getLastLoginTime());

                config.set(c);
                receiveCallback.onReceive(true);
            }

            @Override
            public void onError(Exception e) {
                receiveCallback.onError(e);
            }
        });
    }

    public void getConfigListFromServer(LoginInfo loginInfo, ReceiveCallback receiveCallback) {
        Core.cerebralCortex.getConfigurationList(loginInfo, receiveCallback);
    }

    private LoginInfo getLoginInfo() {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setLoggedIn((Boolean) Core.configuration.getValue(ConfigId.core_login_isLoggedIn));
        loginInfo.setServerAddress((String) Core.configuration.getValue(ConfigId.core_login_serverAddress));
        loginInfo.setUserId((String) Core.configuration.getValue(ConfigId.core_login_userId));
        loginInfo.setPassword((String) Core.configuration.getValue(ConfigId.core_login_password));
        loginInfo.setAccessToken((String) Core.configuration.getValue(ConfigId.core_login_accessToken));
        loginInfo.setLastLoginTime((long) Core.configuration.getValue(ConfigId.core_login_lastLoginTime));
        return loginInfo;
    }

}
