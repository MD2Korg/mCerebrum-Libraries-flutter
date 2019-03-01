package org.md2k.datakit;

import android.content.Context;
import android.util.SparseArray;

import org.md2k.datakit.authentication.AuthenticationManager;
import org.md2k.datakit.exception.MCExceptionDataKitNotRunning;
import org.md2k.datakit.privacy.PrivacyManager;
import org.md2k.datakit.router.RouterManager;
import org.md2k.datakit.storage.StorageManager;
import org.md2k.mcerebrumapi.core.data.DataArray;
import org.md2k.mcerebrumapi.core.data.MCData;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.core.datakitapi.ipc.IDataKitRemoteCallback;
import org.md2k.mcerebrumapi.core.status.MCStatus;

import java.util.ArrayList;


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
public class DataKitManager {
    private static DataKitManager instance = null;
    private AuthenticationManager authenticationManager;
    private PrivacyManager privacyManager;
    private RouterManager routerManager;
    private StorageManager storageManager;
    private boolean isRunning;

    public static DataKitManager getInstance(Context context) {
        if (instance == null)
            instance = new DataKitManager(context);
        return instance;
    }

    private DataKitManager(Context context) {
        isRunning = false;
        authenticationManager = new AuthenticationManager();
        privacyManager = new PrivacyManager();
        routerManager = new RouterManager();
        storageManager = new StorageManager(context);
    }

    private void startPrivacyManager() {
        MCDataSourceResult dataSourceResult = null;
        try {
            dataSourceResult = insertDataSource(PrivacyManager.getDataSource());
            ArrayList<MCData> data = queryData(dataSourceResult.getDsId(), 1);
            if (data.size() != 0) {
                privacyManager.start(data.get(0));
            }
        } catch (MCExceptionDataKitNotRunning mcExceptionDataKitNotRunning) {
            mcExceptionDataKitNotRunning.printStackTrace();
        }
    }

    private void startStorageManager() {
        storageManager.start();
    }

    private void startAuthenticationManager() {
    }

    private void startRouterManager() {
        routerManager.start();
    }

    public boolean isRunning(){
       return isRunning;
    }
    public void start() {
        isRunning = true;
        startStorageManager();
        startPrivacyManager();
        startAuthenticationManager();
        startRouterManager();
    }

    public void stop() {
        if(!isRunning) return;
        isRunning = false;
        authenticationManager.stop();
        privacyManager.stop();
        routerManager.stop();
        storageManager.stop();
    }

    private void checkRunning() throws MCExceptionDataKitNotRunning {
        if (!isRunning) throw new MCExceptionDataKitNotRunning();
    }

    void delete() {
        stop();
        storageManager.delete();
    }

    public MCDataSourceResult insertDataSource(MCDataSource dataSource) throws MCExceptionDataKitNotRunning {
        checkRunning();
        boolean exists = storageManager.isDataSourceExist(dataSource);
        MCDataSourceResult dataSourceResult = storageManager.insertOrUpdateDataSource(dataSource);
        if (!exists)
            routerManager.publish(dataSourceResult);
        return dataSourceResult;
    }

    public ArrayList<MCDataSourceResult> queryDataSource(MCDataSource dataSource) throws MCExceptionDataKitNotRunning {
        checkRunning();
        return storageManager.queryDataSource(dataSource);
    }

    public ArrayList<MCData> queryData(int dsId, int count) throws MCExceptionDataKitNotRunning {
        checkRunning();
        return storageManager.queryData(dsId, count);
    }

    public ArrayList<MCData> queryData(int dsId, long startTimestamp, long endTimestamp) throws MCExceptionDataKitNotRunning {
        checkRunning();
        return storageManager.queryData(dsId, startTimestamp, endTimestamp);
    }

    public void subscribeDataSource(MCDataSource dataSource, IDataKitRemoteCallback iDataKitRemoteCallback) {
        try {
            checkRunning();
            routerManager.subscribe(dataSource, iDataKitRemoteCallback);
        } catch (MCExceptionDataKitNotRunning mcExceptionDataKitNotRunning) {
            //TODO:
            mcExceptionDataKitNotRunning.printStackTrace();
        }
    }

    public void unsubscribeDataSource(IDataKitRemoteCallback iDataKitRemoteCallback){
        try {
            checkRunning();
            routerManager.unsubscribe(iDataKitRemoteCallback);
        } catch (MCExceptionDataKitNotRunning ignored) {
        }
    }

    public int queryDataCount(int dsId, long startTimestamp, long endTimestamp) throws MCExceptionDataKitNotRunning {
        checkRunning();
        return storageManager.queryDataCount(dsId, startTimestamp, endTimestamp);
    }

    public void subscribeData(int dsId, IDataKitRemoteCallback iDataKitRemoteCallback) {
        try {
            checkRunning();
            routerManager.subscribe(dsId, iDataKitRemoteCallback);
        } catch (MCExceptionDataKitNotRunning mcExceptionDataKitNotRunning) {
            //TODO:
            mcExceptionDataKitNotRunning.printStackTrace();
        }
    }

    public void unsubscribeData(IDataKitRemoteCallback iDataKitRemoteCallback) {
        try {
            checkRunning();
            routerManager.unsubscribe(iDataKitRemoteCallback);
        } catch (MCExceptionDataKitNotRunning ignored) {
        }
    }


    public void insertData(SparseArray<DataArray> data) throws MCExceptionDataKitNotRunning {
        checkRunning();
//        privacyManager.start();
        storageManager.insertData(data);
        routerManager.publish(data);
    }

    int authenticate(int sessionId, String packageName, IDataKitRemoteCallback iDataKitRemoteCallback) throws MCExceptionDataKitNotRunning{
        checkRunning();
        authenticationManager.addCallback(sessionId, packageName, iDataKitRemoteCallback);
        return MCStatus.SUCCESS;
    }

    public long getSize() {
        return storageManager.getSize();
    }
}
