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

import com.orhanobut.hawk.Hawk;

import org.md2k.core.cerebralcortex.cerebralcortexwebapi.CCWebAPICalls;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.interfaces.CerebralCortexWebApi;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.AuthResponse;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.MinioObjectStats;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.utils.ApiUtils;
import org.md2k.core.configuration.ConfigurationManager;
import org.md2k.core.info.ConfigInfo;
import org.md2k.core.info.LoginInfo;
import org.md2k.core.cerebralcortex.exception.MCExceptionConfigNotFound;
import org.md2k.core.cerebralcortex.exception.MCExceptionInternetConnection;
import org.md2k.core.cerebralcortex.exception.MCExceptionInvalidLogin;
import org.md2k.core.cerebralcortex.exception.MCExceptionNotLoggedIn;
import org.md2k.core.cerebralcortex.exception.MCExceptionServerDown;
import org.md2k.mcerebrumapi.core.data.MCData;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.core.exception.MCException;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class CerebralCortexManager {
    private Context context;

    public CerebralCortexManager(Context context) {
        this.context = context;
    }


    public void login(final String serverAddress, final String userName, final String password, final CerebralCortexCallback cerebralCortexCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkInternetConnection();
                    checkServerUp(serverAddress);
                    CerebralCortexWebApi ccService = ApiUtils.getCCService(serverAddress);
                    CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);
                    AuthResponse authResponse = ccWebAPICalls.authenticateUser(userName, password);
                    if (authResponse == null) throw new MCExceptionInvalidLogin();
                    LoginInfo loginInfo = new LoginInfo(userName, serverAddress, password, authResponse.getAccessToken(), true);
                    loginInfo.save();
                    cerebralCortexCallback.onSuccess(true);
                } catch (MCException mcException) {
                    cerebralCortexCallback.onError(mcException);
                } catch (Exception e) {
                    cerebralCortexCallback.onError(new MCException(e.getMessage()));
                }
            }
        }).start();

    }

    public void logout() {
        LoginInfo l = LoginInfo.get();
        l.setLoggedIn(false);
        l.save();
    }

    public void getConfigListFromServer(final CerebralCortexCallback cerebralCortexCallback) {
        final LoginInfo loginInfo = LoginInfo.get();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkInternetConnection();
                    checkLoginStatus();
                    checkServerUp(loginInfo.getServerAddress());
                    ArrayList<ConfigInfo> list = new ArrayList<>();
                    CerebralCortexWebApi ccService = ApiUtils.getCCService(loginInfo.getServerAddress());
                    CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);

                    List<MinioObjectStats> objectList = ccWebAPICalls.getObjectsInBucket(loginInfo.getAccessToken(), "configuration");
                    for (int i = 0; objectList != null && i < objectList.size(); i++) {
                        double lastModifiedTime = Double.valueOf(objectList.get(i).getLastModified());
                        ConfigInfo configInfo = new ConfigInfo(objectList.get(i).getObjectName(), true, Double.valueOf(objectList.get(i).getSize()), ((long) lastModifiedTime) * 1000);
                        list.add(configInfo);
                    }
                    cerebralCortexCallback.onSuccess(list);
                } catch (MCExceptionInternetConnection mcExceptionInternetConnection) {
                    cerebralCortexCallback.onError(mcExceptionInternetConnection);
                } catch (MCExceptionServerDown mcExceptionServerDown) {
                    cerebralCortexCallback.onError(mcExceptionServerDown);
                } catch (MCExceptionNotLoggedIn mcExceptionNotLoggedIn) {
                    cerebralCortexCallback.onError(mcExceptionNotLoggedIn);
                } catch (Exception e) {
                    cerebralCortexCallback.onError(new MCException(e.getMessage()));
                }
            }
        }).start();

    }


    private void getConfigurationFile(final String fileName, final CerebralCortexCallback cerebralCortexCallback) {
        getConfigListFromServer(new CerebralCortexCallback() {
            @Override
            public void onSuccess(Object obj) {
                ArrayList<ConfigInfo> configInfos = (ArrayList<ConfigInfo>) obj;
                for (int i = 0; i < configInfos.size(); i++) {
                    if (configInfos.get(i).getFileName().equals(fileName)) {
                        cerebralCortexCallback.onSuccess(configInfos.get(i));
                        return;
                    }
                }
                cerebralCortexCallback.onError(new MCExceptionConfigNotFound());
            }

            @Override
            public void onError(MCException exception) {
                cerebralCortexCallback.onError(exception);
            }
        });

    }

    public void downloadConfigurationFile(final String filename, final CerebralCortexCallback cerebralCortexCallback) {
        final LoginInfo loginInfo = LoginInfo.get();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkInternetConnection();
                    checkLoginStatus();
                    checkServerUp(loginInfo.getServerAddress());
                    final String tempFileDir = context.getCacheDir().getAbsolutePath();
                    final String configFileDir = context.getFilesDir().getAbsolutePath() + File.separator + "config";
                    File file = new File(tempFileDir);
                    file.mkdirs();
                    file = new File(configFileDir);
                    file.mkdirs();
                    CerebralCortexWebApi ccService = ApiUtils.getCCService(loginInfo.getServerAddress());
                    final CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);
                    getConfigurationFile(filename, new CerebralCortexCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            ConfigInfo configInfo = (ConfigInfo) obj;
                            boolean res = ccWebAPICalls.downloadMinioObject(loginInfo.getAccessToken(), "configuration", filename, tempFileDir, filename);
                            if (!res) {
                                cerebralCortexCallback.onError(new MCExceptionConfigNotFound());
                                return;
                            }
                            cerebralCortexCallback.onSuccess(tempFileDir+"/"+filename);
                        }

                        @Override
                        public void onError(MCException exception) {
                            cerebralCortexCallback.onError(exception);
                        }
                    });
                } catch (MCExceptionServerDown mcExceptionServerDown) {
                    cerebralCortexCallback.onError(mcExceptionServerDown);
                } catch (MCExceptionNotLoggedIn mcExceptionNotLoggedIn) {
                    cerebralCortexCallback.onError(mcExceptionNotLoggedIn);
                } catch (MCExceptionInternetConnection mcExceptionInternetConnection) {
                    cerebralCortexCallback.onError(mcExceptionInternetConnection);
                } catch (Exception e) {
                    cerebralCortexCallback.onError(new MCException(e.getMessage()));
                }
            }
        }).start();
    }

    private void checkInternetConnection() throws MCExceptionInternetConnection {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            if (ipAddr.toString().equals("")) {
                throw new MCExceptionInternetConnection();
            }
        } catch (Exception e) {
            throw new MCExceptionInternetConnection();
        }

    }

    private void checkServerUp(String serverName) throws MCExceptionServerDown {
        try {

            String s = serverName;
            s = s.replace("https://", "");
            s = s.replace("http://", "");
            InetAddress ipAddr = InetAddress.getByName(s);
            //You can replace it with your name
            if (ipAddr.toString().equals("")) {
                throw new MCExceptionServerDown();
            }

        } catch (Exception e) {
            throw new MCExceptionServerDown();
        }
    }

    private void checkLoginStatus() throws MCExceptionNotLoggedIn, MCExceptionInvalidLogin {
        LoginInfo loginInfo = LoginInfo.get();
        if (loginInfo == null)
            throw new MCExceptionNotLoggedIn();

        if (!loginInfo.isLoggedIn()) throw new MCExceptionNotLoggedIn();
        CerebralCortexWebApi ccService = ApiUtils.getCCService(loginInfo.getServerAddress());
        CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);
        AuthResponse authResponse = ccWebAPICalls.authenticateUser(loginInfo.getUserId(), loginInfo.getPassword());
        if (authResponse == null) throw new MCExceptionInvalidLogin();
        loginInfo.setLoggedIn(true);
        loginInfo.save();
    }

    public void uploadData(MCDataSourceResult dataSourceResult, ArrayList<MCData> data, final CerebralCortexCallback cerebralCortexCallback) {
        final LoginInfo loginInfo = LoginInfo.get();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkInternetConnection();
                    checkLoginStatus();
                    checkServerUp(loginInfo.getServerAddress());
                    //TODO: add data uploader
                    cerebralCortexCallback.onSuccess(true);
                } catch (MCExceptionInternetConnection mcExceptionInternetConnection) {
                    cerebralCortexCallback.onError(mcExceptionInternetConnection);
                } catch (MCExceptionServerDown mcExceptionServerDown) {
                    cerebralCortexCallback.onError(mcExceptionServerDown);
                } catch (MCExceptionNotLoggedIn mcExceptionNotLoggedIn) {
                    cerebralCortexCallback.onError(mcExceptionNotLoggedIn);
                } catch (Exception e) {
                    cerebralCortexCallback.onError(new MCException(e.getMessage()));
                }
            }
        }).start();

    }
}
