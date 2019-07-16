package org.md2k.core.datakit.storage;

import android.content.Context;
import android.os.Handler;
import android.util.SparseArray;

import org.md2k.core.datakit.storage.msgpack.MyMessagePack;
import org.md2k.core.datakit.storage.sqlite.SQLiteLogger;
import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
public class StorageManager {
    private static final long SYNC_TIME = 10000;
    private static final int UPLOADER_MAXIMUM_LIMIT = 25000;
    private static final int PRUNE_LIMIT_IF_SYNC = 1000;
    private static final int PRUNE_LIMIT = 500000;
    private SparseArray<MCData> lastDataSparseArray;
    private SparseArray<ArrayList<MCData>> tempStorage;
    private ILogger iLogger;
    private IUploader iUploader;
    private Handler handlerSync;
    private long lastUploaderSynced;
    private long uploaderSyncTime;

    public StorageManager(Context context, long uploaderSyncTime) {
        iLogger = new SQLiteLogger(context);
        iUploader = new MyMessagePack();
        lastDataSparseArray = new SparseArray<>();
        tempStorage = new SparseArray<>();
        handlerSync = new Handler();
        this.uploaderSyncTime = uploaderSyncTime;
    }

    private Runnable runnableSync= new Runnable() {
        @Override
        public void run() {
            syncData();
            long curTime = System.currentTimeMillis();
            if (curTime - lastUploaderSynced >= uploaderSyncTime) {
                createMessagePack(UPLOADER_MAXIMUM_LIMIT);
                lastUploaderSynced = curTime;
            }
            handlerSync.postDelayed(this, SYNC_TIME);
        }
    };
    private void syncDataIfRequired(int dsId){
        if(tempStorage.get(dsId)!=null && tempStorage.get(dsId).size()!=0){
            handlerSync.removeCallbacks(runnableSync);
            syncData();
            handlerSync.postDelayed(runnableSync, SYNC_TIME);
        }
    }

    public void createMessagePack(int maximumLimit) {
        ArrayList<MCDataSourceResult> mcDataSourceResults = iLogger.queryDataSource(MCDataSource.queryBuilder().build());
        for (int i = 0; i < mcDataSourceResults.size(); i++) {
            int dsId = mcDataSourceResults.get(i).getDsId();
            HashMap<String, Object> obj = iLogger.queryNotSynced(dsId, maximumLimit);
            long minId = (long) obj.get("minId");
            long maxId = (long) obj.get("maxId");
            ArrayList<MCData> mcData = (ArrayList<MCData>) obj.get("data");
            boolean res = iUploader.createMessagePack(mcDataSourceResults.get(i), mcData);
            iLogger.setSyncedBit(dsId, minId, maxId);
            iLogger.pruneDataIfSynced(dsId, PRUNE_LIMIT_IF_SYNC);
        }
    }

    public String[] getUploadFileList() {
        return iUploader.getFileList();
    }

    private void syncData(){
        iLogger.insertData(tempStorage);
        tempStorage.clear();
    }

    public void start() {
        iLogger.start();
        handlerSync.postDelayed(runnableSync, SYNC_TIME);
    }

    public void stop() {
        handlerSync.removeCallbacks(runnableSync);
        iLogger.insertData(tempStorage);
        iLogger.stop();
    }

    public MCDataSourceResult insertOrUpdateDataSource(MCDataSource dataSource) {
        return iLogger.insertOrUpdateDataSource(dataSource);
    }

    public ArrayList<MCDataSourceResult> queryDataSource(MCDataSource dataSource) {
        ArrayList<MCDataSourceResult> dataSourceResults = iLogger.queryDataSource(dataSource);
        for (int i = 0; i < dataSourceResults.size(); i++) {
            MCData data = getLastSample(dataSourceResults.get(i).getDsId());
        }
        Collections.sort(dataSourceResults, new Comparator<MCDataSourceResult>() {
            @Override
            public int compare(MCDataSourceResult d1, MCDataSourceResult d2) {
                if(d1.getLastUpdateTime()>d2.getLastUpdateTime()) return -1;
                if(d1.getLastUpdateTime()<d2.getLastUpdateTime()) return 1;
                if(d1.getCreationTime()>d2.getCreationTime()) return -1;
                if(d1.getCreationTime()<d2.getCreationTime()) return 1;
                return 0;
            }
        });
        return dataSourceResults;
    }

    public ArrayList<MCData> queryData(int dsId, int count) {
        if (count == 1) {
            ArrayList<MCData> data = new ArrayList<>();
            MCData d = getLastSample(dsId);
            if (d != null) data.add(d);
            return data;
        } else {
            syncDataIfRequired(dsId);
            return iLogger.queryData(dsId, count);
        }
    }

    public ArrayList<MCData> queryData(int dsId, long startTimestamp, long endTimestamp) {
        MCData d = getLastSample(dsId);
        if (d.getEndTimestamp() < startTimestamp) return new ArrayList<>();
        else{
            syncDataIfRequired(dsId);
            return iLogger.queryData(dsId, startTimestamp, endTimestamp);
        }
    }

    public int queryDataCount(int dsId, long startTimestamp, long endTimestamp) {
        MCData d = getLastSample(dsId);
        if (d!=null && d.getEndTimestamp() < startTimestamp) return 0;
        else {
            syncDataIfRequired(dsId);
            return iLogger.queryDataCount(dsId, startTimestamp, endTimestamp);
        }
    }

    public void insertData(ArrayList<MCData> data) {
        int dsId = data.get(0).getDsId();
        lastDataSparseArray.put(dsId, data.get(data.size() - 1));
        ArrayList<MCData> t = tempStorage.get(dsId, new ArrayList<MCData>());
        t.addAll(data);
    }

    public boolean isDataSourceExist(MCDataSource dataSource) {
        return iLogger.isDataSourceExist(dataSource);
    }

    public void delete() {
        iLogger.delete();
    }

    private MCData getLastSample(int dsId) {
        MCData d = lastDataSparseArray.get(dsId);
        if (d == null) {
            syncDataIfRequired(dsId);
            ArrayList<MCData> data = iLogger.queryData(dsId, 1);
            if (data.size() == 1) {
                d = data.get(0);
                lastDataSparseArray.put(dsId, d);
            }
        }
        return d;
    }

    public long getSize() {
        return iLogger.size();
    }
}
