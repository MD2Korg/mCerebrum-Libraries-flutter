package org.md2k.mcerebrumapi.datakitapi;

import android.annotation.SuppressLint;
import android.content.Context;

import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceQuery;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceRegister;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.datakitapi.ipc.authenticate.MCConnectionCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.data.QueryDataCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource.MCRegistration;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource.RegisterCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_data_count.CountDataCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_datasource.QueryDataSourceCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.subscribe_data.MCSubscribeDataCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.subscribe_datasource.MCSubscribeDataSourceCallback;

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
public final class MCDataKitAPI {
    @SuppressLint("StaticFieldLeak")
    private static MCDataKitAPI instance = null;
    private Context context;
    private static DataKitManager mcDataAPI = null;

    public static void init(Context context) {
        Preconditions.checkNotNull(context);
        if (instance == null) {
            instance = new MCDataKitAPI(context.getApplicationContext());
            mcDataAPI = new DataKitManager();
        }
    }

    private MCDataKitAPI(Context context) {
        this.context = context;
    }

    public static Context getContext() {
        if (instance == null) return null;
        return instance.context;
    }

    public static void connect(MCConnectionCallback mcConnectionCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(mcConnectionCallback);
        mcDataAPI.connect(mcConnectionCallback);
    }
    public static boolean isConnected(){
        Preconditions.checkAPIInitialized(instance);
        return mcDataAPI.isConnected();
    }

    public static void disconnect(MCConnectionCallback mcConnectionCallback) {
        mcDataAPI.disconnect(mcConnectionCallback);
    }

/*
    public static void disconnectAll() {
        mcDataAPI.disconnectAll();
    }
*/

    public static MCRegistration registerDataSource(final MCDataSourceRegister dataSourceRegister) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceRegister);
        return mcDataAPI.registerDataSource(dataSourceRegister);
    }

    public static void registerDataSourceAsync(final MCDataSourceRegister dataSourceRegister, RegisterCallback registerCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceRegister);
        Preconditions.checkNotNull(registerCallback);
        mcDataAPI.registerDataSourceAsync(dataSourceRegister, registerCallback);
    }

    public static ArrayList<MCDataSourceResult> queryDataSource(MCDataSourceQuery dataSourceQuery) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceQuery);
        return mcDataAPI.queryDataSource(dataSourceQuery);
    }

    public static void queryDataSourceAsync(MCDataSourceQuery dataSourceQuery, QueryDataSourceCallback queryDataSourceCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceQuery);
        Preconditions.checkNotNull(queryDataSourceCallback);
        mcDataAPI.queryDataSourceAsync(dataSourceQuery, queryDataSourceCallback);
    }

    public static void subscribeDataSourceAsync(MCDataSourceQuery dataSourceQuery, MCSubscribeDataSourceCallback subscribeDataSourceCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceQuery);
        Preconditions.checkNotNull(subscribeDataSourceCallback);
        mcDataAPI.subscribeDataSourceAsync(dataSourceQuery, subscribeDataSourceCallback);
    }

    public static void unsubscribeDataSourceAsync(MCSubscribeDataSourceCallback subscribeDataSourceCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(subscribeDataSourceCallback);
        mcDataAPI.unsubscribeDataSourceAsync(subscribeDataSourceCallback);
    }

    public static ArrayList<MCData> queryData(MCDataSourceResult dataSourceResult, long startTimestamp, long endTimestamp) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceResult);
        return mcDataAPI.queryDataByTime(dataSourceResult, startTimestamp, endTimestamp);
    }

    public static void queryDataAsync(MCDataSourceResult dataSourceResult, long startTimestamp, long endTimestamp, QueryDataCallback queryCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceResult);
        Preconditions.checkNotNull(queryCallback);
        mcDataAPI.queryDataByTimeAsync(dataSourceResult, startTimestamp, endTimestamp, queryCallback);
    }

    public static ArrayList<MCData> queryData(MCDataSourceResult dataSourceResult, int lastN) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceResult);
        return mcDataAPI.queryDataByNumber(dataSourceResult, lastN);
    }

    public static void queryDataAsync(MCDataSourceResult dataSourceResult, int lastN, QueryDataCallback queryCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceResult);
        Preconditions.checkNotNull(queryCallback);
        mcDataAPI.queryDataByNumberAsync(dataSourceResult, lastN, queryCallback);
    }

    public static int queryDataCount(MCDataSourceResult mcDataSourceResult, long startTimestamp, long endTimestamp) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(mcDataSourceResult);
        return mcDataAPI.queryDataCount(mcDataSourceResult, startTimestamp, endTimestamp);
    }

    public static void queryDataCountAsync(MCDataSourceResult dataSourceResult, long startTimestamp, long endTimestamp, CountDataCallback countDataCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceResult);
        Preconditions.checkNotNull(countDataCallback);
        mcDataAPI.queryDataCountAsync(dataSourceResult, startTimestamp, endTimestamp, countDataCallback);
    }

    public static int insertData(MCData data) {
        Preconditions.checkNotNull(data);
        return insertData(new MCData[]{data});
    }

    public static int insertData(MCData[] data) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(data);
        return mcDataAPI.insertData(data);
    }

    public static void subscribeDataAsync(MCDataSourceResult mcDataSourceResult, MCSubscribeDataCallback mcSubscribeDataCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(mcDataSourceResult);
        Preconditions.checkNotNull(mcSubscribeDataCallback);
        mcDataAPI.subscribeDataAsync(mcDataSourceResult, mcSubscribeDataCallback);
    }

    public static void unsubscribeDataAsync(MCSubscribeDataCallback mcSubscribeDataCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(mcSubscribeDataCallback);
        mcDataAPI.unsubscribeDataAsync(mcSubscribeDataCallback);
    }

/*
    public static Data queryDataSummary(DataSourceResult dataSourceResult, long startTimestamp, long endTimestamp) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceResult);
        return mcDataAPI.queryDataSummary(dataSourceResult, startTimestamp, endTimestamp);
    }

    public static void queryDataSummaryAsync(DataSourceResult dataSourceResult, long startTimestamp, long endTimestamp, DataSummaryCallback queryCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceResult);
        Preconditions.checkNotNull(queryCallback);
        mcDataAPI.queryDataSummaryAsync(dataSourceResult, startTimestamp, endTimestamp, queryCallback);
    }
/*
    public static boolean system(int operation){
        Preconditions.checkAPIInitialized(instance);
        try {
        return instance.connectionAPI.systemExec(operation);
        } catch (MCerebrumException e) {
            return false;
        }
    }
    public static void systemAsync(int operation, SystemCallback systemCallback){
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(systemCallback);
        try {
            instance.connectionAPI.systemExecAsync(operation, systemCallback);
        } catch (MCerebrumException e) {
            systemCallback.onError(e.getStatus());
        }
    }

*/

}
