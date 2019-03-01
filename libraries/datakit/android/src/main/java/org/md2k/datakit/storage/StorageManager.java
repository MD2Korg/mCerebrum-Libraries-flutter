package org.md2k.datakit.storage;

import android.content.Context;
import android.os.Handler;
import android.util.SparseArray;

import org.md2k.datakit.storage.sqlite.SQLiteLogger;
import org.md2k.mcerebrumapi.core.data.DataArray;
import org.md2k.mcerebrumapi.core.data.MCData;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    private SparseArray<MCData> lastDataSparseArray;
    private SparseArray<DataArray> tempStorage;
    private ILogger iLogger;
    private Handler handlerSync;

    public StorageManager(Context context) {
        iLogger = new SQLiteLogger(context);
        lastDataSparseArray = new SparseArray<>();
        tempStorage = new SparseArray<>();
        handlerSync = new Handler();
    }
    private Runnable runnableSync= new Runnable() {
        @Override
        public void run() {
            syncData();
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
            if (data != null)
                dataSourceResults.get(i).setLastDataTime(data.getTimestamp());
            else dataSourceResults.get(i).setLastDataTime(-1);
        }
        Collections.sort(dataSourceResults, new Comparator<MCDataSourceResult>() {
            @Override
            public int compare(MCDataSourceResult d1, MCDataSourceResult d2) {
                if(d1.getLastDataTime()>d2.getLastDataTime()) return -1;
                if(d1.getLastDataTime()<d2.getLastDataTime()) return 1;
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

    public void insertData(SparseArray<DataArray> data) {
        for (int i = 0; i < data.size(); i++) {
            int dsId = data.keyAt(i);
            DataArray dataArray = data.valueAt(i);
            if (dataArray.size() > 0) {
                lastDataSparseArray.put(dsId, dataArray.get(dataArray.size() - 1));
                DataArray t = tempStorage.get(dsId, new DataArray());
                t.add(dataArray.get());
                tempStorage.put(dsId, t);
            }
        }
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
