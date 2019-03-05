package org.md2k.cerebralcortex;
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

import com.blankj.utilcode.util.ZipUtils;
import com.orhanobut.hawk.Hawk;

import org.md2k.cerebralcortex.cerebralcortexwebapi.CCWebAPICalls;
import org.md2k.cerebralcortex.cerebralcortexwebapi.interfaces.CerebralCortexWebApi;
import org.md2k.cerebralcortex.cerebralcortexwebapi.models.AuthResponse;
import org.md2k.cerebralcortex.cerebralcortexwebapi.models.MinioObjectStats;
import org.md2k.cerebralcortex.cerebralcortexwebapi.utils.ApiUtils;
import org.md2k.cerebralcortex.exception.MCExceptionConfigNotFound;
import org.md2k.cerebralcortex.exception.MCExceptionInternetConnection;
import org.md2k.cerebralcortex.exception.MCExceptionInvalidConfig;
import org.md2k.cerebralcortex.exception.MCExceptionInvalidLogin;
import org.md2k.cerebralcortex.exception.MCExceptionNotLoggedIn;
import org.md2k.cerebralcortex.exception.MCExceptionServerDown;
import org.md2k.mcerebrumapi.core.data.MCData;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.core.exception.MCException;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class CerebralCortexManager {
    private Context context;
    private static CerebralCortexManager instance;

    public static CerebralCortexManager getInstance(Context context) {
        if (instance == null)
            instance = new CerebralCortexManager(context);
        return instance;
    }

    private CerebralCortexManager(Context context) {
        this.context = context;
        Hawk.init(context).build();
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
                    ServerInfo.setUserName(userName);
                    ServerInfo.setPassword(password);
                    ServerInfo.setServerAddress(serverAddress);
                    ServerInfo.setAccessToken(authResponse.getAccessToken());
                    ServerInfo.setLoggedIn(true);
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
        ServerInfo.setLoggedIn(false);
    }

    public boolean isLoggedIn() {
        return ServerInfo.getLoggedIn();
    }

    public void getConfigurationFiles(final CerebralCortexCallback cerebralCortexCallback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkInternetConnection();
                    checkLoginStatus();
                    checkServerUp(ServerInfo.getServerAddress());
                    ArrayList<FileInfo> list = new ArrayList<>();
                    CerebralCortexWebApi ccService = ApiUtils.getCCService(ServerInfo.getServerAddress());
                    CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);

                    List<MinioObjectStats> objectList = ccWebAPICalls.getObjectsInBucket(ServerInfo.getAccessToken(), "configuration");
                    for (int i = 0; objectList != null && i < objectList.size(); i++) {
                        FileInfo f = new FileInfo();
                        f.setName(objectList.get(i).getObjectName());
                        f.setSize(objectList.get(i).getSize());
                        f.setLastModified(objectList.get(i).getLastModified());
                        list.add(f);
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
        getConfigurationFiles(new CerebralCortexCallback() {
            @Override
            public void onSuccess(Object obj) {
                ArrayList<FileInfo> fileInfos = (ArrayList<FileInfo>) obj;
                for (int i = 0; i < fileInfos.size(); i++) {
                    if (fileInfos.get(i).getName().equals(fileName)) {
                        cerebralCortexCallback.onSuccess(fileInfos.get(i));
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkInternetConnection();
                    checkLoginStatus();
                    checkServerUp(ServerInfo.getServerAddress());
                    final String tempFileDir = context.getFilesDir().getAbsolutePath() + File.separator + "temp";
                    final String configFileDir = context.getFilesDir().getAbsolutePath() + File.separator + "config";
                    File file = new File(tempFileDir);
                    file.mkdirs();
                    file = new File(configFileDir);
                    file.mkdirs();
                    CerebralCortexWebApi ccService = ApiUtils.getCCService(ServerInfo.getServerAddress());
                    final CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);
                    getConfigurationFile(filename, new CerebralCortexCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            FileInfo fileInfo = (FileInfo) obj;
                            boolean res = ccWebAPICalls.downloadMinioObject(ServerInfo.getAccessToken(), "configuration", filename, tempFileDir, filename);
                            if (!res) {
                                cerebralCortexCallback.onError(new MCExceptionConfigNotFound());
                                return;
                            }
                            try {
                                ZipUtils.unzipFile(tempFileDir+File.separator+fileInfo.getName(), configFileDir);
                            } catch (IOException e) {
                                cerebralCortexCallback.onError(new MCExceptionInvalidConfig());
                                return;
                            }
                            ServerInfo.setFileName(fileInfo.getName());
                            ServerInfo.setFileLastModified(fileInfo.getLastModified());
                            cerebralCortexCallback.onSuccess(true);
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
                }  catch (Exception e){
                    cerebralCortexCallback.onError(new MCException(e.getMessage()));
                }
            }
        }).start();
    }
    public void hasConfigurationUpdate(final CerebralCortexCallback cerebralCortexCallback){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getConfigurationFile(ServerInfo.getFileName(), new CerebralCortexCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            FileInfo fileInfo = (FileInfo) obj;
                            if(!fileInfo.getLastModified().equals(ServerInfo.getFileLastModified()))
                                cerebralCortexCallback.onSuccess(true);
                            else cerebralCortexCallback.onSuccess(false);
                        }

                        @Override
                        public void onError(MCException exception) {
                            cerebralCortexCallback.onError(exception);
                        }
                    });
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
        if (!isLoggedIn()) throw new MCExceptionNotLoggedIn();
        CerebralCortexWebApi ccService = ApiUtils.getCCService(ServerInfo.getServerAddress());
        CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);
        AuthResponse authResponse = ccWebAPICalls.authenticateUser(ServerInfo.getUserName(), ServerInfo.getPassword());
        if (authResponse == null) throw new MCExceptionInvalidLogin();
        ServerInfo.setAccessToken(authResponse.getAccessToken());
        ServerInfo.setLoggedIn(true);
    }

    public void uploadData(MCDataSourceResult dataSourceResult, ArrayList<MCData> data, final CerebralCortexCallback cerebralCortexCallback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkInternetConnection();
                    checkLoginStatus();
                    checkServerUp(ServerInfo.getServerAddress());
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
