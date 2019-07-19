package org.md2k.core;

import android.content.Context;
import android.util.Log;

import org.md2k.core.cerebralcortex.CerebralCortex;
import org.md2k.core.cerebralcortex.utils.NetworkUtils;
import org.md2k.core.configuration.ConfigId;
import org.md2k.core.configuration.ConfigurationManager;
import org.md2k.core.datakit.DataKitManager;
import org.md2k.core.datakit.authentication.AuthenticationManager;
import org.md2k.core.datakit.privacy.PrivacyManager;
import org.md2k.core.datakit.router.RouterManager;
import org.md2k.core.datakit.storage.StorageManager;
import org.md2k.mcerebrumapi.MCerebrumAPI;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

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
public class Core {
    private static Core instance;
    private CerebralCortex cerebralCortex;
    private DataKitManager dataKit;
    private ConfigurationManager configuration;
    private Disposable disposableUploader;


    public static void init(Context context) {
        if (instance == null) {
            instance = new Core(context.getApplicationContext());
            long uploadTimeConfig = instance.configuration.getUploadTime();
            long msgPackCreationTime = 15 * 60 * 60 * 1000;
            if (uploadTimeConfig < 15 * 60 * 60 * 1000)
                msgPackCreationTime = uploadTimeConfig;
            instance.dataKit = new DataKitManager(new AuthenticationManager(), new PrivacyManager(), new RouterManager(), new StorageManager(context, msgPackCreationTime));
            Object o = instance.configuration.getByKey(ConfigId.core_datakit_active);
            if (o == null || (Boolean) o) {
                instance.dataKit.start();
            }
            if (instance.configuration.isUploaderEnabled())
                startUploader();
        }
    }

    private Core(Context context) {
        MCerebrumAPI.init(context);
        configuration = new ConfigurationManager(context.getApplicationContext());
        cerebralCortex = new CerebralCortex(ConstantCore.SERVER_ADDRESS);
    }

    public static void startUploader() {
        stopUploader();
        Log.d("core", "uploader starts");
        instance.disposableUploader = Observable.interval(instance.configuration.getUploadTime(), TimeUnit.MILLISECONDS)
                .map(new Function<Long, Boolean>() {
                    @Override
                    public Boolean apply(Long aLong) throws Exception {
                        if (!NetworkUtils.checkInternetConnection()) return false;
                        else if (instance.configuration.isWifiOnly())
                            return NetworkUtils.isInWifi(MCerebrumAPI.getContext());
                        else return true;
                    }
                }).filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        return aBoolean;
                    }
                }).flatMap(new Function<Boolean, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Boolean aLong) throws Exception {
                        Log.d("core", "uploader try");
                        String username = instance.configuration.getUserId();
                        boolean isLoggedIn = instance.configuration.isLoggedIn();

                        if (username == null || !isLoggedIn) {
                            Log.d("core", "uploader try (failed) userid=" + username + " loggedIn=" + isLoggedIn);
                            return Observable.just(false);
                        } else return instance.cerebralCortex.login(username, username);
                    }
                }).filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        return aBoolean;
                    }
                }).flatMap(new Function<Boolean, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Boolean aBoolean) throws Exception {
                        String[] files = instance.dataKit.getUploadFileList();
                        Log.d("core", "uploader try : filecount=" + files.length);
                        return Observable.fromArray(files);
                    }
                }).flatMap(new Function<String, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(final String filename) throws Exception {
                        String filenameDataSourceResult = filename + ".json";
                        String filenameMessagePack = filename + ".gz";
                        MCDataSourceResult mcDataSourceResult = Utils.readJson(filenameDataSourceResult, MCDataSourceResult.class);
                        Log.d("core", "upload try file=" + filename + " datasourceId=" + mcDataSourceResult.getDsId());
                        return instance.cerebralCortex.uploadData(mcDataSourceResult, filenameMessagePack).map(new Function<Boolean, Boolean>() {
                            @Override
                            public Boolean apply(Boolean aBoolean) throws Exception {
                                Log.d("core", "upload status file=" + filename + " = " + aBoolean);
                                if (aBoolean) {
                                    new File(filename + ".json").delete();
                                    new File(filename + ".gz").delete();
                                }
                                return aBoolean;
                            }
                        });
                    }
                }).subscribe();
    }

    public static void stopUploader() {
        if (instance.disposableUploader != null && !instance.disposableUploader.isDisposed()) {
            instance.disposableUploader.dispose();
        }
        instance.disposableUploader = null;
    }

    public static void setConfigurationById(String id, HashMap<String, Object> hashMap) {
        instance.configuration.setById(id, hashMap);
    }

    public static HashMap<String, Object> getConfigurationById(String id) {
        return instance.configuration.getById(id);
    }

    public static HashMap<String, Object> getDefaultConfigurationById(String id) {
        return instance.configuration.getDefaultById(id);
    }

    public static DataKitManager getDataKit() {
        return instance.dataKit;
    }
}
