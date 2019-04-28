package org.md2k.mcerebrumapi.datakitapi;

import android.annotation.SuppressLint;
import android.content.Context;

import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.datakitapi.ipc.authenticate.MCConnectionCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource.MCRegistration;
import org.md2k.mcerebrumapi.datakitapi.ipc.subscribe_data.MCSubscribeDataCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.subscribe_datasource.MCSubscribeDataSourceCallback;

import java.util.ArrayList;
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


    public static MCRegistration registerDataSource(final MCDataSource dataSourceRegister) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceRegister);
        return mcDataAPI.registerDataSource(dataSourceRegister);
    }

    public static ArrayList<MCDataSourceResult> queryDataSource(MCDataSource dataSourceQuery) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceQuery);
        return mcDataAPI.queryDataSource(dataSourceQuery);
    }


    public static void subscribeDataSource(MCDataSource dataSourceQuery, MCSubscribeDataSourceCallback subscribeDataSourceCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceQuery);
        Preconditions.checkNotNull(subscribeDataSourceCallback);
        mcDataAPI.subscribeDataSource(dataSourceQuery, subscribeDataSourceCallback);
    }

    public static void unsubscribeDataSource(MCSubscribeDataSourceCallback subscribeDataSourceCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(subscribeDataSourceCallback);
        mcDataAPI.unsubscribeDataSource(subscribeDataSourceCallback);
    }

    public static ArrayList<MCData> queryData(MCDataSourceResult dataSourceResult, long startTimestamp, long endTimestamp) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceResult);
        return mcDataAPI.queryDataByTime(dataSourceResult, startTimestamp, endTimestamp);
    }

    public static ArrayList<MCData> queryData(MCDataSourceResult dataSourceResult, int lastN) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceResult);
        return mcDataAPI.queryDataByNumber(dataSourceResult, lastN);
    }
    public static HashMap<String, Object> getConfiguration(String id){
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(id);
        return mcDataAPI.getConfiguration(id);
    }
    public static int setConfiguration(HashMap<String, Object> data){
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(data);
        return mcDataAPI.setConfiguration(data);

    }

    public static int queryDataCount(MCDataSourceResult mcDataSourceResult, long startTimestamp, long endTimestamp) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(mcDataSourceResult);
        return mcDataAPI.queryDataCount(mcDataSourceResult, startTimestamp, endTimestamp);
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
    public static int insertDataIfNew(MCData data){
        Preconditions.checkNotNull(data);
        return insertDataIfNew(new MCData[] { data});
    }

    public static int insertDataIfNew(MCData[] data) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(data);
        return mcDataAPI.insertDataIfNew(data);
    }

    public static void subscribeData(MCDataSourceResult mcDataSourceResult, MCSubscribeDataCallback mcSubscribeDataCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(mcDataSourceResult);
        Preconditions.checkNotNull(mcSubscribeDataCallback);
        mcDataAPI.subscribeData(mcDataSourceResult, mcSubscribeDataCallback);
    }

    public static void unsubscribeData(MCSubscribeDataCallback mcSubscribeDataCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(mcSubscribeDataCallback);
        mcDataAPI.unsubscribeData(mcSubscribeDataCallback);
    }
}
