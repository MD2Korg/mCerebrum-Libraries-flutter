/*
 * Copyright (c) 2018, The University of Memphis, MD2K Center of Excellence
 *
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

package org.md2k.core.datakit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.md2k.core.Core;
import org.md2k.mcerebrumapi.datakitapi.IDataKitRemoteService;
import org.md2k.mcerebrumapi.datakitapi.ipc.IDataKitRemoteCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.OperationType;
import org.md2k.mcerebrumapi.datakitapi.ipc._Session;
import org.md2k.mcerebrumapi.datakitapi.ipc.authenticate._AuthenticateIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.authenticate._AuthenticateOut;
import org.md2k.mcerebrumapi.datakitapi.ipc.get_configuration._GetConfigurationIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.get_configuration._GetConfigurationOut;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_data._InsertDataIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_data._InsertDataOut;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource._InsertDataSourceIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource._InsertDataSourceOut;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_data_by_number._QueryDataByNumberIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_data_by_number._QueryDataByNumberOut;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_data_by_time._QueryDataByTimeIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_data_by_time._QueryDataByTimeOut;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_data_count._QueryDataCountIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_data_count._QueryDataCountOut;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_datasource._QueryDataSourceIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_datasource._QueryDataSourceOut;
import org.md2k.mcerebrumapi.datakitapi.ipc.set_configuration._SetConfigurationIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.set_configuration._SetConfigurationOut;
import org.md2k.mcerebrumapi.datakitapi.ipc.subscribe_data._SubscribeDataIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.subscribe_datasource._SubscribeDataSourceIn;
import org.md2k.mcerebrumapi.exception.MCException;
import org.md2k.mcerebrumapi.status.MCStatus;


/**
 * <code>DataKit</code> background service.
 */
public class ServiceDataKit extends Service {
    private DataKitManager dataKitManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Core.init(this);
        dataKitManager = Core.dataKit;
    }

    private final IDataKitRemoteService.Stub mBinder = new IDataKitRemoteService.Stub() {
        @Override
        public _Session execute(_Session in) {
            switch (in.getOperationType()) {
                case OperationType.INSERT_DATASOURCE:
                    return insertDataSource(in);
                case OperationType.QUERY_DATASOURCE:
                    return queryDataSource(in);
                case OperationType.QUERY_DATA_BY_NUMBER:
                    return queryDataByNumber(in);
                case OperationType.QUERY_DATA_BY_TIME:
                    return queryDataByTime(in);
                case OperationType.QUERY_DATA_COUNT:
                    return queryDataCount(in);
                case OperationType.INSERT_DATA:
                    return insertData(in);
                case OperationType.GET_CONFIGURATION:
                    return getConfiguration(in);
                case OperationType.SET_CONFIGURATION:
                    return setConfiguration(in);
                default:
                    return new _Session(in.getSessionId(), in.getOperationType(), MCStatus.INVALID_OPERATION);
            }
        }

        @Override
        public void executeAsync(_Session in, IDataKitRemoteCallback iDataKitRemoteCallback) {
            try {
                switch (in.getOperationType()) {
                    case OperationType.AUTHENTICATE:
                        iDataKitRemoteCallback.onReceived(authenticate(in, iDataKitRemoteCallback));
                        break;
                    case OperationType.INSERT_DATASOURCE:
                        iDataKitRemoteCallback.onReceived(insertDataSource(in));
                        break;
                    case OperationType.QUERY_DATASOURCE:
                        iDataKitRemoteCallback.onReceived(queryDataSource(in));
                        break;
                    case OperationType.QUERY_DATA_BY_NUMBER:
                        iDataKitRemoteCallback.onReceived(queryDataByNumber(in));
                        break;
                    case OperationType.QUERY_DATA_BY_TIME:
                        iDataKitRemoteCallback.onReceived(queryDataByTime(in));
                        break;
                    case OperationType.QUERY_DATA_COUNT:
                        iDataKitRemoteCallback.onReceived(queryDataCount(in));
                        break;
                    case OperationType.INSERT_DATA:
                        iDataKitRemoteCallback.onReceived(insertData(in));
                        break;
                    case OperationType.SUBSCRIBE_DATA:
                        subscribeData(in, iDataKitRemoteCallback);
                        break;
                    case OperationType.SUBSCRIBE_DATASOURCE:
                        subscribeDataSource(in, iDataKitRemoteCallback);
                        break;
                    case OperationType.UNSUBSCRIBE_DATA:
                        unsubscribeData(in, iDataKitRemoteCallback);
                        break;
                    case OperationType.UNSUBSCRIBE_DATASOURCE:
                        unsubscribeDataSource(in, iDataKitRemoteCallback);
                        break;

                }
            } catch (RemoteException e) {
                Log.d("abc", "Server remote exception e=" + e.getMessage());
                e.printStackTrace();
            }
        }
    };

    _Session authenticate(_Session in, IDataKitRemoteCallback iDataKitRemoteCallback) {
        try {
            int status = 0;
            status = dataKitManager.authenticate(in.getSessionId(), _AuthenticateIn.getPackageName(in.getBundle()), iDataKitRemoteCallback);
            return _AuthenticateOut.create(in.getSessionId(), status);
        } catch (MCException mcExceptionDataKitNotRunning) {
            return new _Session(in.getSessionId(), in.getOperationType(), MCStatus.DATAKIT_STOPPED);
        }

    }

    _Session insertDataSource(_Session in) {
        try {
            return _InsertDataSourceOut.create(in.getSessionId(), dataKitManager.insertDataSource(_InsertDataSourceIn.getDataSource(in.getBundle())));
        } catch (MCException mcExceptionDataKitNotRunning) {
            return new _Session(in.getSessionId(), in.getOperationType(), MCStatus.DATAKIT_STOPPED);
        }
    }

    _Session queryDataSource(_Session in) {
        try {
            return _QueryDataSourceOut.create(in.getSessionId(), dataKitManager.queryDataSource(_QueryDataSourceIn.getDataSource(in.getBundle())));
        } catch (MCException mcExceptionDataKitNotRunning) {
            return new _Session(in.getSessionId(), in.getOperationType(), MCStatus.DATAKIT_STOPPED);
        }
    }

    _Session queryDataByNumber(_Session in) {
        try {
            return _QueryDataByNumberOut.create(in.getSessionId(), dataKitManager.queryData(_QueryDataByNumberIn.getDsId(in.getBundle()), _QueryDataByNumberIn.getLastN(in.getBundle())));
        } catch (MCException mcExceptionDataKitNotRunning) {
            return new _Session(in.getSessionId(), in.getOperationType(), MCStatus.DATAKIT_STOPPED);
        }
    }

    _Session queryDataByTime(_Session in) {
        try {
            return _QueryDataByTimeOut.create(in.getSessionId(), dataKitManager.queryData(_QueryDataByTimeIn.getDsId(in.getBundle()), _QueryDataByTimeIn.startTimestamp(in.getBundle()), _QueryDataByTimeIn.endTimestamp(in.getBundle())));
        } catch (MCException mcExceptionDataKitNotRunning) {
            return new _Session(in.getSessionId(), in.getOperationType(), MCStatus.DATAKIT_STOPPED);
        }
    }

    _Session queryDataCount(_Session in) {
        try {
            return _QueryDataCountOut.create(in.getSessionId(), dataKitManager.queryDataCount(_QueryDataCountIn.getDsId(in.getBundle()), _QueryDataCountIn.startTimestamp(in.getBundle()), _QueryDataCountIn.endTimestamp(in.getBundle())));
        } catch (MCException mcExceptionDataKitNotRunning) {
            return new _Session(in.getSessionId(), in.getOperationType(), MCStatus.DATAKIT_STOPPED);
        }
    }

    _Session insertData(_Session in) {
        try {
            dataKitManager.insertData(_InsertDataIn.getData(in.getBundle()));
            return _InsertDataOut.create(in.getSessionId(), MCStatus.SUCCESS);
        } catch (MCException mcExceptionDataKitNotRunning) {
            return new _Session(in.getSessionId(), in.getOperationType(), MCStatus.DATAKIT_STOPPED);
        }
    }

    void subscribeData(_Session in, IDataKitRemoteCallback iDataKitRemoteCallback) {
        dataKitManager.subscribeData(_SubscribeDataIn.getDataSourceResult(in.getBundle()).getDsId(), iDataKitRemoteCallback);
    }

    void unsubscribeData(_Session in, IDataKitRemoteCallback iDataKitRemoteCallback) {
        dataKitManager.unsubscribeData(iDataKitRemoteCallback);
    }

    void subscribeDataSource(_Session in, IDataKitRemoteCallback iDataKitRemoteCallback) {
        dataKitManager.subscribeDataSource(_SubscribeDataSourceIn.getDataSource(in.getBundle()), iDataKitRemoteCallback);
    }

    void unsubscribeDataSource(_Session in, IDataKitRemoteCallback iDataKitRemoteCallback) {
        dataKitManager.unsubscribeDataSource(iDataKitRemoteCallback);
    }

    _Session getConfiguration(_Session in) {
        return _GetConfigurationOut.create(in.getSessionId(), Core.configuration.get(_GetConfigurationIn.getId(in.getBundle())));
    }

    _Session setConfiguration(_Session in) {
        Core.configuration.append(_SetConfigurationIn.getConfiguration(in.getBundle()));
        return _SetConfigurationOut.create(in.getSessionId(), MCStatus.SUCCESS);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
