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

import org.md2k.core.cerebralcortex.cerebralcortexwebapi.CCWebAPICalls;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.interfaces.CerebralCortexWebApi;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.metadata.MetadataBuilder;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.AuthResponse;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.RegisterResponse;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.StreamMetadata;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.utils.ApiUtils;
import org.md2k.core.data.LoginInfo;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.exception.MCException;
import org.md2k.mcerebrumapi.status.MCStatus;

import java.net.InetAddress;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CerebralCortex {
    private CCWebAPICalls ccWebAPICalls;
    private LoginInfo loginInfo;
    public CerebralCortex(String server) {
        loginInfo = new LoginInfo();
        loginInfo.setLoggedIn(false);
        loginInfo.setServerAddress(server);
        CerebralCortexWebApi ccService = ApiUtils.getCCService(server);
        ccWebAPICalls = new CCWebAPICalls(ccService);
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public Observable<Boolean> login(final String username, final String password) {
        return Observable.just(true).observeOn(Schedulers.newThread())
                .map(new Function<Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean aBoolean) throws Exception {
                        if (!checkInternetConnection()) return false;
                        if (!checkServerUp(loginInfo.getServerAddress())) return false;
                        AuthResponse authResponse = ccWebAPICalls.authenticateUser(username, password);
                        if (authResponse == null) throw new MCException(MCStatus.INVALID_LOGIN);
                        loginInfo.setUserUuid(authResponse.getUserUuid());
                        loginInfo.setUserId(username);
                        loginInfo.setPassword(password);
                        loginInfo.setAccessToken(authResponse.getAccessToken());
                        loginInfo.setLoggedIn(true);
                        return true;
                    }
                });
    }

    /*
        public Observable<List<FileInfo>> getConfigurationList(){
            return Observable.just(true).observeOn(Schedulers.newThread())
                    .map(new Function<Boolean, List<FileInfo>>() {
                        @Override
                        public List<FileInfo> apply(Boolean aBoolean) throws Exception {
                            checkInternetConnection();
                            checkServerUp(loginInfo.getServerAddress());
                            ArrayList<FileInfo> list = new ArrayList<>();

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
                            return list;
                        }
                    });

        }


        public Observable<HashMap<String, Object>> downloadConfigurationFile(final String filename) {
            return Observable.just(true).observeOn(Schedulers.newThread())
                    .map(new Function<Boolean, HashMap<String, Object>>() {
                        @Override
                        public HashMap<String, Object> apply(Boolean aBoolean) throws Exception {
                            checkInternetConnection();
                            checkServerUp(loginInfo.getServerAddress());
                            HashMap<String, Object> res = ccWebAPICalls.downloadMinioObject(loginInfo.getAccessToken(), "configuration", filename);
                            return res;
                        }
                    });
        }
    */
    public Observable<Boolean> uploadData(final MCDataSourceResult dataSourceResult, final String filenameMessagePack) {
        return Observable.just(true).map(new Function<Boolean, RegisterResponse>() {
            @Override
            public RegisterResponse apply(Boolean aBoolean) throws Exception {
                StreamMetadata streamMetadata = MetadataBuilder.buildStreamMetadata(dataSourceResult);
                return ccWebAPICalls.registerDataStream(loginInfo.getAccessToken(), streamMetadata);
            }
        }).map(new Function<RegisterResponse, Boolean>() {
            @Override
            public Boolean apply(RegisterResponse registerResponse) throws Exception {
                return ccWebAPICalls.putDataStream(registerResponse.getHashId(), filenameMessagePack, loginInfo.getAccessToken());
            }
        });
    }

    private boolean checkInternetConnection() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            if (ipAddr.toString().equals("")) {
                return false;
//                throw new MCException(MCStatus.NO_INTERNET_CONNECTION);
            }
        } catch (Exception e) {
            return false;
//            throw new MCException(MCStatus.NO_INTERNET_CONNECTION);
        }
        return true;
    }

    private boolean checkServerUp(String serverAddress) {
        try {
            serverAddress = serverAddress.replace("https://", "");
            serverAddress = serverAddress.replace("http://", "");
            InetAddress ipAddr = InetAddress.getByName(serverAddress);
            //You can replace it with your name
            if (ipAddr.toString().equals("")) {
                return false;
//                throw new MCException(MCStatus.SERVER_DOWN);
            }

        } catch (Exception e) {
            return false;
            //throw new MCException(MCStatus.SERVER_DOWN);
        }
        return true;
    }

    private void checkLoginStatus(LoginInfo loginInfo) throws MCException {
        if (!loginInfo.isLoggedIn()) throw new MCException(MCStatus.NOT_LOGGED_IN);
        CerebralCortexWebApi ccService = ApiUtils.getCCService(loginInfo.getServerAddress());
        CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);
        AuthResponse authResponse = ccWebAPICalls.authenticateUser(loginInfo.getUserId(), loginInfo.getPassword());
        if (authResponse == null) throw new MCException(MCStatus.INVALID_LOGIN);
    }
}
