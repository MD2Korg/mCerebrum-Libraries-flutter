package org.md2k.core.cerebralcortex;
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

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.md2k.core.Core;
import org.md2k.core.ReceiveCallback;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.CCWebAPICalls;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.interfaces.CerebralCortexWebApi;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.AuthResponse;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.MinioObjectStats;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.utils.ApiUtils;
import org.md2k.core.configuration.ConfigId;
import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.exception.MCException;
import org.md2k.mcerebrumapi.status.MCStatus;
import org.md2k.mcerebrumapi.time.DateTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CerebralCortexManager {
    private Context context;

    public CerebralCortexManager(Context context) {
        this.context = context;
    }


    public void login(final String serverAddress, final String userName, final String password, final ReceiveCallback receiveCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkInternetConnection();
                    checkServerUp(serverAddress);
                    CerebralCortexWebApi ccService = ApiUtils.getCCService(serverAddress);
                    CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);
                    AuthResponse authResponse = ccWebAPICalls.authenticateUser(userName, password);
                    if (authResponse == null) throw new MCException(MCStatus.INVALID_LOGIN);
                    HashMap<String, Object> h = new HashMap<>();
                    h.put(ConfigId.core_login_userId, userName);
                    h.put(ConfigId.core_login_password, password);
                    h.put(ConfigId.core_login_serverAddress, serverAddress);
                    h.put(ConfigId.core_login_accessToken, authResponse.getAccessToken());
                    h.put(ConfigId.core_login_isLoggedIn, true);
                    h.put(ConfigId.core_login_lastLoginTime, DateTime.getCurrentTime());
                    Core.configuration.set(h);
                    receiveCallback.onReceive(true);
                } catch (MCException mcException) {
                    receiveCallback.onError(mcException);
                } catch (Exception e) {
                    receiveCallback.onError(new MCException(e.getMessage()));
                }
            }
        }).start();

    }

    public void logout() {
        Core.configuration.setValue(ConfigId.core_login_isLoggedIn, false);
    }

    public void getConfigurationList(final ReceiveCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkInternetConnection();
                    checkLoginStatus();
                    String serverAddress = (String) Core.configuration.getValue(ConfigId.core_login_serverAddress);
                    String accessToken = (String) Core.configuration.getValue(ConfigId.core_login_accessToken);
                    checkServerUp(serverAddress);
                    ArrayList<HashMap<String, Object>> list = new ArrayList<>();
                    CerebralCortexWebApi ccService = ApiUtils.getCCService(serverAddress);
                    CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);

                    List<MinioObjectStats> objectList = ccWebAPICalls.getObjectsInBucket(accessToken, "configuration");
                    for (int i = 0; objectList != null && i < objectList.size(); i++) {
                        if(!objectList.get(i).getObjectName().endsWith(".json"))
                            continue;
                        double lastModifiedTime = Double.valueOf(objectList.get(i).getLastModified());
                        HashMap<String, Object> h = new HashMap<>();
                        h.put(ConfigId.core_config_id, objectList.get(i).getObjectName());
                        h.put(ConfigId.core_config_title, objectList.get(i).getObjectName());
                        h.put(ConfigId.core_config_filename, objectList.get(i).getObjectName());
                        h.put(ConfigId.core_config_publishTime, ((long) lastModifiedTime) * 1000);
                        h.put(ConfigId.core_config_fileSize, Long.valueOf(objectList.get(i).getSize()));
                        h.put(ConfigId.core_config_from, "cerebral_cortex");
                        list.add(h);
                    }
                    callback.onReceive(list);
                } catch (MCException e) {
                    callback.onError(e);
                }
            }
        }).start();

    }


    private void getConfigurationFile(final String fileName, final ReceiveCallback callback) {
        getConfigurationList(new ReceiveCallback() {
            @Override
            public void onReceive(Object obj) {
                ArrayList<HashMap<String, Object>> configInfos = (ArrayList<HashMap<String, Object>>) obj;
                for (int i = 0; i < configInfos.size(); i++) {
                    if (configInfos.get(i).get(ConfigId.core_config_filename).equals(fileName)) {
                        callback.onReceive(obj);
                    }
                }
                callback.onError(new MCException(MCStatus.CONFIG_FILE_NOT_AVAILABLE));

            }

            @Override
            public void onError(Exception exception) {
                callback.onError(exception);
            }
        });
    }

    public void downloadConfigurationFile(final String filename, final ReceiveCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    checkInternetConnection();
                    checkLoginStatus();
                    String serverAddress = (String) Core.configuration.getValue(ConfigId.core_login_serverAddress);
                    final String accessToken = (String) Core.configuration.getValue(ConfigId.core_login_accessToken);
                    checkServerUp(serverAddress);
                    final String tempFileDir = context.getCacheDir().getAbsolutePath();
                    File file = new File(tempFileDir);
                    file.mkdirs();
                    CerebralCortexWebApi ccService = ApiUtils.getCCService(serverAddress);
                    final CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);
//        HashMap<String, Object> h = getConfigurationFile(filename);
//                            HashMap<String, Object> configInfo = (HashMap<String, Object>) obj;
                    boolean res = ccWebAPICalls.downloadMinioObject(accessToken, "configuration", filename, tempFileDir, filename);
                    if (!res) {
                        callback.onError(new MCException(MCStatus.CONFIG_FILE_NOT_AVAILABLE));
                        return;
                    }
                    HashMap<String, Object> readData = read(tempFileDir + File.separator+filename);
                    new File(tempFileDir+File.separator+filename).delete();
                    callback.onReceive(readData);
                } catch (MCException e) {
                    callback.onError(e);
                }
            }
        }).start();

    }

    private HashMap<String, Object> read(String filepath) throws MCException {
        BufferedReader reader = null;
        HashMap<String, Object> res = null;
        try {
            InputStream in = new FileInputStream(filepath);
            reader = new BufferedReader(new InputStreamReader(in));
            Gson gson = new Gson();
            res = gson.fromJson(reader, new TypeToken<HashMap<String, Object>>() {
            }.getType());
        } catch (Exception e) {
            throw new MCException(MCStatus.INVALID_CONFIG_FILE);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return res;
    }


    private void checkInternetConnection() throws MCException {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            if (ipAddr.toString().equals("")) {
                throw new MCException(MCStatus.NO_INTERNET_CONNECTION);
            }
        } catch (Exception e) {
            throw new MCException(MCStatus.NO_INTERNET_CONNECTION);
        }

    }

    private void checkServerUp(String serverName) throws MCException {
        try {

            String s = serverName;
            s = s.replace("https://", "");
            s = s.replace("http://", "");
            InetAddress ipAddr = InetAddress.getByName(s);
            //You can replace it with your name
            if (ipAddr.toString().equals("")) {
                throw new MCException(MCStatus.SERVER_DOWN);
            }

        } catch (Exception e) {
            throw new MCException(MCStatus.SERVER_DOWN);
        }
    }

    private void checkLoginStatus() throws MCException {
        Object l = Core.configuration.getValue(ConfigId.core_login_isLoggedIn);
        if (l == null || !((Boolean) l)) throw new MCException(MCStatus.NOT_LOGGED_IN);
        String serverAddress = (String) Core.configuration.getValue(ConfigId.core_login_serverAddress);
        String userId = (String) Core.configuration.getValue(ConfigId.core_login_userId);
        String password = (String) Core.configuration.getValue(ConfigId.core_login_password);
        CerebralCortexWebApi ccService = ApiUtils.getCCService(serverAddress);
        CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);
        AuthResponse authResponse = ccWebAPICalls.authenticateUser(userId, password);
        if (authResponse == null) throw new MCException(MCStatus.INVALID_LOGIN);
        Core.configuration.setValue(ConfigId.core_login_isLoggedIn, true);
    }

    public void uploadData(MCDataSourceResult dataSourceResult, ArrayList<MCData> data, final ReceiveCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkInternetConnection();
                    checkLoginStatus();
                    String serverAddress = (String) Core.configuration.getValue(ConfigId.core_login_serverAddress);
                    checkServerUp(serverAddress);
                    //TODO: add data uploader
                    callback.onReceive(true);
                } catch (Exception e) {
                    callback.onError(e);
                }
            }
        }).start();

    }
}
