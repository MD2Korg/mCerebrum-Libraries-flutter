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
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.md2k.core.ReceiveCallback;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.CCWebAPICalls;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.interfaces.CerebralCortexWebApi;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.AuthResponse;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.MinioObjectStats;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.UserMetadata;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.UserSettings;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.utils.ApiUtils;
import org.md2k.core.data.LoginInfo;
import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.exception.MCException;
import org.md2k.mcerebrumapi.status.MCStatus;

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

import okhttp3.ResponseBody;

public class CerebralCortexManager {
    private Context context;
    public CerebralCortexManager(Context context) {
        this.context = context;
    }

    public void login(final String server, final String username, final String password, final ReceiveCallback receiveCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkInternetConnection();
                    checkServerUp(server);
                    CerebralCortexWebApi ccService = ApiUtils.getCCService(server);
                    CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);
                    AuthResponse authResponse = ccWebAPICalls.authenticateUser(username, password);
                    if (authResponse == null) throw new MCException(MCStatus.INVALID_LOGIN);
                    LoginInfo loginInfo = new LoginInfo();
                    loginInfo.setUserId(username);
                    loginInfo.setServerAddress(server);
                    loginInfo.setPassword(password);
                    loginInfo.setAccessToken(authResponse.getAccessToken());
                    loginInfo.setLoggedIn(true);
                    receiveCallback.onReceive(loginInfo);
                } catch (MCException mcException) {
                    receiveCallback.onError(mcException);
                } catch (Exception e) {
                    receiveCallback.onError(new MCException(e.getMessage()));
                }
            }
        }).start();

    }


    public void getConfigurationList(final LoginInfo loginInfo, final ReceiveCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkInternetConnection();
                    checkLoginStatus(loginInfo);
                    checkServerUp(loginInfo.getServerAddress());
                    ArrayList<FileInfo> list = new ArrayList<>();
                    CerebralCortexWebApi ccService = ApiUtils.getCCService(loginInfo.getServerAddress());
                    CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);

                    List<MinioObjectStats> objectList = ccWebAPICalls.getObjectsInBucket(loginInfo.getAccessToken(), "configuration");
                    for (int i = 0; objectList != null && i < objectList.size(); i++) {
                        if (!objectList.get(i).getObjectName().endsWith(".json"))
                            continue;
                        double lastModifiedTime = Double.valueOf(objectList.get(i).getLastModified());
                        FileInfo f = new FileInfo();
                        f.setName(objectList.get(i).getObjectName());
                        f.setModifiedTime(((long) lastModifiedTime) * 1000);
                        f.setSize(Long.valueOf(objectList.get(i).getSize()));
                        list.add(f);
                    }
                    callback.onReceive(list);
                } catch (MCException e) {
                    callback.onError(e);
                }
            }
        }).start();

    }


    public void downloadConfigurationFile(final LoginInfo loginInfo, final String filename, final ReceiveCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    checkInternetConnection();
                    checkLoginStatus(loginInfo);
                    checkServerUp(loginInfo.getServerAddress());
                    final String tempFileDir = context.getCacheDir().getAbsolutePath();
                    File file = new File(tempFileDir);
                    file.mkdirs();
                    CerebralCortexWebApi ccService = ApiUtils.getCCService(loginInfo.getServerAddress());
                    final CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);
                    boolean res = ccWebAPICalls.downloadMinioObject(loginInfo.getAccessToken(), "configuration", filename, tempFileDir, filename);
                    if (!res) {
                        callback.onError(new MCException(MCStatus.CONFIG_FILE_NOT_AVAILABLE));
                        return;
                    }
                    HashMap<String, Object> readData = read(tempFileDir + File.separator + filename);
                    new File(tempFileDir + File.separator + filename).delete();
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

    private void checkServerUp(String serverAddress) throws MCException {
        try {

            serverAddress = serverAddress.replace("https://", "");
            serverAddress = serverAddress.replace("http://", "");
            InetAddress ipAddr = InetAddress.getByName(serverAddress);
            //You can replace it with your name
            if (ipAddr.toString().equals("")) {
                throw new MCException(MCStatus.SERVER_DOWN);
            }

        } catch (Exception e) {
            throw new MCException(MCStatus.SERVER_DOWN);
        }
    }

    private void checkLoginStatus(LoginInfo loginInfo) throws MCException {
        if (!loginInfo.isLoggedIn()) throw new MCException(MCStatus.NOT_LOGGED_IN);
        CerebralCortexWebApi ccService = ApiUtils.getCCService(loginInfo.getServerAddress());
        CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);
        AuthResponse authResponse = ccWebAPICalls.authenticateUser(loginInfo.getUserId(), loginInfo.getPassword());
        if (authResponse == null) throw new MCException(MCStatus.INVALID_LOGIN);
    }



    private void syncData() throws MCException {

    }



    public void uploadData(final LoginInfo loginInfo, MCDataSourceResult dataSourceResult, ArrayList<MCData> data, final ReceiveCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkInternetConnection();
                    checkLoginStatus(loginInfo);
                    checkServerUp(loginInfo.getServerAddress());
                    //TODO: add data uploader
                    callback.onReceive(true);
                } catch (Exception e) {
                    callback.onError(e);
                }
            }
        }).start();

    }

    public void registerUser(final LoginInfo loginInfo, final ReceiveCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CerebralCortexWebApi ccService = ApiUtils.getCCService(loginInfo.getServerAddress());
                CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);

                ResponseBody registerResponse = ccWebAPICalls.registerUser(loginInfo.getUserId(), loginInfo.getPassword(), "default", new ArrayList<UserMetadata>(), new ArrayList<UserSettings>());
                try {
                    Log.d("CC_Debug", registerResponse.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //        if registerResponse == null throw new MCException(MCStatus.INVALID_LOGIN);
            }
        }).start();
    }
}
