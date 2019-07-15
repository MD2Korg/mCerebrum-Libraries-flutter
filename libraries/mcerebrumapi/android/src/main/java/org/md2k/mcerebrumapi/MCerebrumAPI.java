package org.md2k.mcerebrumapi;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.datakitapi.ipc.authenticate.MCConnectionCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource.MCRegistration;
import org.md2k.mcerebrumapi.datakitapi.ipc.subscribe_data.MCSubscribeDataCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.subscribe_datasource.MCSubscribeDataSourceCallback;
import org.md2k.mcerebrumapi.extensionapi.MCExtensionAPI;
import org.md2k.mcerebrumapi.status.MCStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class MCerebrumAPI{
    @SuppressLint("StaticFieldLeak")
    private static MCerebrumAPI instance = null;
    private Context context;
    private DataKitManager mcDataAPI;

    private MCExtensionAPI mcExtensionAPI;

    public static void init(@NonNull Context context){
        if(instance==null){
            instance = new MCerebrumAPI();
            instance.context = context.getApplicationContext();
            instance.mcDataAPI = new DataKitManager();
        }
        instance.mcExtensionAPI = null;
    }

    public static void init(@NonNull Context context, @NonNull MCExtensionAPI mcExtensionAPI){
        if(instance==null){
            instance = new MCerebrumAPI();
            instance.context = context.getApplicationContext();
            instance.mcDataAPI = new DataKitManager();
        }
        instance.mcExtensionAPI = mcExtensionAPI;
    }

    private MCerebrumAPI(){
    }

    public static MCExtensionAPI getMcExtensionAPI() {
        if(instance==null) return null;
        else return instance.mcExtensionAPI;
    }

    public static Context getContext() {
        return instance.context;
    }

    public static void connect(MCConnectionCallback mcConnectionCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(mcConnectionCallback);
        instance.mcDataAPI.connect(mcConnectionCallback);
    }
    public static boolean isConnected(){
        Preconditions.checkAPIInitialized(instance);
        return instance.mcDataAPI.isConnected();
    }

    public static void disconnect(MCConnectionCallback mcConnectionCallback) {
        instance.mcDataAPI.disconnect(mcConnectionCallback);
    }

/*
    public static void disconnectAll() {
        mcDataAPI.disconnectAll();
    }
*/


    public static MCRegistration registerDataSource(final MCDataSource dataSourceRegister) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceRegister);
        return instance.mcDataAPI.registerDataSource(dataSourceRegister);
    }

    public static ArrayList<MCDataSourceResult> queryDataSource(MCDataSource dataSourceQuery) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceQuery);
        return instance.mcDataAPI.queryDataSource(dataSourceQuery);
    }


    public static void subscribeDataSource(MCDataSource dataSourceQuery, MCSubscribeDataSourceCallback subscribeDataSourceCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceQuery);
        Preconditions.checkNotNull(subscribeDataSourceCallback);
        instance.mcDataAPI.subscribeDataSource(dataSourceQuery, subscribeDataSourceCallback);
    }

    public static void unsubscribeDataSource(MCSubscribeDataSourceCallback subscribeDataSourceCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(subscribeDataSourceCallback);
        instance.mcDataAPI.unsubscribeDataSource(subscribeDataSourceCallback);
    }

    public static ArrayList<MCData> queryData(MCDataSourceResult dataSourceResult, long startTimestamp, long endTimestamp) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceResult);
        return instance.mcDataAPI.queryDataByTime(dataSourceResult, startTimestamp, endTimestamp);
    }

    public static ArrayList<MCData> queryData(MCDataSourceResult dataSourceResult, int lastN) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(dataSourceResult);
        return instance.mcDataAPI.queryDataByNumber(dataSourceResult, lastN);
    }
    public static HashMap<String, Object> getConfiguration(String id){
        Preconditions.checkAPIInitialized(instance);
        return instance.mcDataAPI.getConfiguration(id);
    }
    public static HashMap<String, Object> getDefaultConfiguration(String id){
        Preconditions.checkAPIInitialized(instance);
        return instance.mcDataAPI.getDefaultConfiguration(id);
    }
    public static int setConfiguration(String id, HashMap<String, Object> data){
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(data);
        return instance.mcDataAPI.setConfiguration(id, data);

    }

    public static int queryDataCount(MCDataSourceResult mcDataSourceResult, long startTimestamp, long endTimestamp) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(mcDataSourceResult);
        return instance.mcDataAPI.queryDataCount(mcDataSourceResult, startTimestamp, endTimestamp);
    }

    public static MCStatus insertData(MCData data) {
        Preconditions.checkNotNull(data);
        return insertData(new MCData[]{data});
    }

    public static MCStatus insertData(MCData[] data) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(data);
        return instance.mcDataAPI.insertData(data);
    }

    public static void subscribeData(MCDataSourceResult mcDataSourceResult, MCSubscribeDataCallback mcSubscribeDataCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(mcDataSourceResult);
        Preconditions.checkNotNull(mcSubscribeDataCallback);
        instance.mcDataAPI.subscribeData(mcDataSourceResult, mcSubscribeDataCallback);
    }

    public static void unsubscribeData(MCSubscribeDataCallback mcSubscribeDataCallback) {
        Preconditions.checkAPIInitialized(instance);
        Preconditions.checkNotNull(mcSubscribeDataCallback);
        instance.mcDataAPI.unsubscribeData(mcSubscribeDataCallback);
    }
}
