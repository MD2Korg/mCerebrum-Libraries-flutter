package org.md2k.core.datakit;

import android.util.SparseArray;

import org.md2k.core.datakit.authentication.AuthenticationManager;
import org.md2k.core.datakit.privacy.PrivacyManager;
import org.md2k.core.datakit.router.RouterManager;
import org.md2k.core.datakit.storage.StorageManager;
import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.datakitapi.ipc.IDataKitRemoteCallback;
import org.md2k.mcerebrumapi.exception.MCException;
import org.md2k.mcerebrumapi.status.MCStatus;

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
    private AuthenticationManager authenticationManager;
    private PrivacyManager privacyManager;
    private RouterManager routerManager;
    private StorageManager storageManager;
    private boolean isRunning;

    public DataKitManager(AuthenticationManager authenticationManager, PrivacyManager privacyManager, RouterManager routerManager, StorageManager storageManager) {
        isRunning = false;
        this.authenticationManager = authenticationManager;
        this.privacyManager = privacyManager;
        this.routerManager = routerManager;
        this.storageManager = storageManager;
    }

    private void startPrivacyManager() {
        MCDataSourceResult dataSourceResult;
        try {
            dataSourceResult = insertDataSource(PrivacyManager.getDataSource());
            ArrayList<MCData> data = queryData(dataSourceResult.getDsId(), 1);
            if (data.size() != 0) {
                privacyManager.start(data.get(0));
            }
        } catch (MCException mcException) {
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

//        Core.configuration.add(ConfigId.core_datakit_active, true);
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

    private void checkRunning() throws MCException {
        if (!isRunning) throw new MCException(MCStatus.DATAKIT_STOPPED);
    }

    public void delete() {
        stop();
        storageManager.delete();
    }

    public MCDataSourceResult insertDataSource(MCDataSource dataSource) throws MCException {
        checkRunning();
        boolean exists = storageManager.isDataSourceExist(dataSource);
        MCDataSourceResult dataSourceResult = storageManager.insertOrUpdateDataSource(dataSource);
        if (!exists)
            routerManager.publish(dataSourceResult);
        return dataSourceResult;
    }

    public ArrayList<MCDataSourceResult> queryDataSource(MCDataSource dataSource) throws MCException {
        checkRunning();
        return storageManager.queryDataSource(dataSource);
    }

    public ArrayList<MCData> queryData(int dsId, int count) throws MCException {
        checkRunning();
        return storageManager.queryData(dsId, count);
    }

    public ArrayList<MCData> queryData(int dsId, long startTimestamp, long endTimestamp) throws MCException {
        checkRunning();
        return storageManager.queryData(dsId, startTimestamp, endTimestamp);
    }

    public void subscribeDataSource(MCDataSource dataSource, IDataKitRemoteCallback iDataKitRemoteCallback) {
        try {
            checkRunning();
            routerManager.subscribe(dataSource, iDataKitRemoteCallback);
        } catch (MCException mcExceptionDataKitNotRunning) {
            //TODO:
            mcExceptionDataKitNotRunning.printStackTrace();
        }
    }

    public void unsubscribeDataSource(IDataKitRemoteCallback iDataKitRemoteCallback){
        try {
            checkRunning();
            routerManager.unsubscribe(iDataKitRemoteCallback);
        } catch (MCException ignored) {
        }
    }

    public int queryDataCount(int dsId, long startTimestamp, long endTimestamp) throws MCException {
        checkRunning();
        return storageManager.queryDataCount(dsId, startTimestamp, endTimestamp);
    }

    public void subscribeData(int dsId, IDataKitRemoteCallback iDataKitRemoteCallback) {
        try {
            checkRunning();
            routerManager.subscribe(dsId, iDataKitRemoteCallback);
        } catch (MCException mcExceptionDataKitNotRunning) {
            //TODO:
            mcExceptionDataKitNotRunning.printStackTrace();
        }
    }

    public void unsubscribeData(IDataKitRemoteCallback iDataKitRemoteCallback) {
        try {
            checkRunning();
            routerManager.unsubscribe(iDataKitRemoteCallback);
        } catch (MCException ignored) {
        }
    }
    public void insertData(MCData data) throws MCException {
        ArrayList<MCData> d = new ArrayList<>();
        d.add(data);
        insertData(d);
    }

    public void insertData(ArrayList<MCData> data) throws MCException {
        checkRunning();
        SparseArray<ArrayList<MCData>> sparseArray = new SparseArray<>();
        for (int i = 0; i < data.size(); i++) {
            ArrayList<MCData> t = sparseArray.get(data.get(i).getDsId(), new ArrayList<MCData>());
            t.add(data.get(i));
            sparseArray.put(data.get(i).getDsId(), t);
        }
        for (int i = 0; i < sparseArray.size(); i++) {
            int key = sparseArray.keyAt(i);
            // get the object by the key.
            ArrayList<MCData> obj = sparseArray.get(key);
            if (obj.size() == 0) continue;
            storageManager.insertData(obj);
            routerManager.publish(obj);

        }
    }

    public String[] getUploadFileList() {
        return storageManager.getUploadFileList();
    }

    MCStatus authenticate(int sessionId, String packageName, IDataKitRemoteCallback iDataKitRemoteCallback) throws MCException {
        checkRunning();
        authenticationManager.addCallback(sessionId, packageName, iDataKitRemoteCallback);
        return MCStatus.SUCCESS;
    }

    public long getSize() {
        return storageManager.getSize();
    }
}
