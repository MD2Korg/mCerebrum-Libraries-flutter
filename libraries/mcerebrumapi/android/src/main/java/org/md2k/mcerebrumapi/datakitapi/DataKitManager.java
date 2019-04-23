package org.md2k.mcerebrumapi.datakitapi;

import android.os.RemoteException;
import android.util.Log;

import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.datakitapi.ipc.IDataKitRemoteCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc._Session;
import org.md2k.mcerebrumapi.datakitapi.ipc.data.QueryDataCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.data.SyncCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_data._InsertDataExec;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_data._InsertDataIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource.MCRegistration;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource.RegisterCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource._InsertDataSourceIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource._InsertDataSourceOut;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_data_by_number._QueryDataByNumberIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_data_by_number._QueryDataByNumberOut;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_data_by_time._QueryDataByTimeIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_data_by_time._QueryDataByTimeOut;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_data_count.CountDataCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_data_count._QueryDataCountIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_data_count._QueryDataCountOut;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_datasource.QueryDataSourceCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_datasource._QueryDataSourceIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.query_datasource._QueryDataSourceOut;
import org.md2k.mcerebrumapi.datakitapi.ipc.subscribe_data.MCSubscribeDataCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.subscribe_data._SubscribeDataIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.subscribe_data._SubscribeDataOut;
import org.md2k.mcerebrumapi.datakitapi.ipc.subscribe_data._UnsubscribeDataIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.subscribe_datasource.MCSubscribeDataSourceCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.subscribe_datasource._SubscribeDataSourceIn;
import org.md2k.mcerebrumapi.datakitapi.ipc.subscribe_datasource._SubscribeDataSourceOut;
import org.md2k.mcerebrumapi.datakitapi.ipc.subscribe_datasource._UnsubscribeDataSourceIn;
import org.md2k.mcerebrumapi.status.MCStatus;

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
final class DataKitManager extends AbstractDataKitManager {
    private static final String TAG = DataKitManager.class.getName();
    private HashMap<MCSubscribeDataSourceCallback, IDataKitRemoteCallback.Stub> subscriptionDataSourceList;
    private HashMap<MCSubscribeDataCallback, IDataKitRemoteCallback.Stub> subscriptionDataList;
    private _InsertDataExec insertDataExec;

    protected DataKitManager() {
        subscriptionDataSourceList = new HashMap<>();
        subscriptionDataList = new HashMap<>();
        insertDataExec = new _InsertDataExec(new SyncCallback() {
            @Override
            public void sync() {
                _Session in = _InsertDataIn.create(createSessionId(), insertDataExec.getData(), false);
                try {
                    execute(in);
                } catch (RemoteException e) {
                    Log.e(TAG, "insert error e=" + e.getMessage());
                    //TODO:
                }
                _Session in1 = _InsertDataIn.create(createSessionId(), insertDataExec.getDataIfNew(), true);
                try {
                    execute(in);
                } catch (RemoteException e) {
                    Log.e(TAG, "insert error e=" + e.getMessage());
                    //TODO:
                }
            }
        });
    }

    protected MCRegistration registerDataSource(MCDataSource dataSourceRegister) {
        _Session in = _InsertDataSourceIn.create(createSessionId(), dataSourceRegister);
        _Session out;
        try {
            out = execute(in);
        } catch (RemoteException e) {
            //TODO:
            return null;
        }
        return new MCRegistration(_InsertDataSourceOut.getDataSourceResult(out.getBundle()));
    }

    protected void registerDataSourceAsync(MCDataSource dataSourceRegister, final RegisterCallback registerCallback) {
        _Session in = _InsertDataSourceIn.create(createSessionId(),  dataSourceRegister);
        try {
            executeAsync(in, new IDataKitRemoteCallback.Stub() {
                @Override
                public void onReceived(_Session _session) throws RemoteException {
                    if (MCStatus.isSuccessful(_session.getStatus()))
                        registerCallback.onRegister(new MCRegistration(_InsertDataSourceOut.getDataSourceResult(_session.getBundle())), _session.getStatus());
                    else registerCallback.onRegister(null, _session.getStatus());
                }
            });
        } catch (Exception e) {
            registerCallback.onRegister(null, MCStatus.CONNECTION_ERROR);
        }
    }

    protected ArrayList<MCDataSourceResult> queryDataSource(MCDataSource dataSourceQuery) {
        _Session in = _QueryDataSourceIn.create(createSessionId(), dataSourceQuery);
        _Session session;
        try {
            session = execute(in);
        } catch (RemoteException e) {
            Log.d("abc","abc");
            //TODO:
            return null;
        }
        return _QueryDataSourceOut.getDataSourceResults(session.getBundle());
    }

    protected void queryDataSourceAsync(MCDataSource dataSourceQuery, final QueryDataSourceCallback queryDataSourceCallback) {
        _Session in = _QueryDataSourceIn.create(createSessionId(), dataSourceQuery);

        try {
            executeAsync(in, new IDataKitRemoteCallback.Stub() {
                @Override
                public void onReceived(_Session _session) throws RemoteException {
                    if (MCStatus.isSuccessful(_session.getStatus()))
                        queryDataSourceCallback.onReceive(_QueryDataSourceOut.getDataSourceResults(_session.getBundle()), _session.getStatus());
                    else queryDataSourceCallback.onReceive(null, _session.getStatus());
                }
            });
        } catch (Exception e) {
            queryDataSourceCallback.onReceive(null, MCStatus.CONNECTION_ERROR);
        }
    }

    protected void subscribeDataSourceAsync(MCDataSource dataSourceQuery, final MCSubscribeDataSourceCallback subscribeDataSourceCallback) {
        if (subscriptionDataSourceList.containsKey(subscribeDataSourceCallback)) return;
        _Session in = _SubscribeDataSourceIn.create(createSessionId(),  dataSourceQuery);
        IDataKitRemoteCallback.Stub iDataKitRemoteCallback = new IDataKitRemoteCallback.Stub() {
            @Override
            public void onReceived(_Session _session) throws RemoteException {
                subscribeDataSourceCallback.onReceive(_SubscribeDataSourceOut.getDataSourceResult(_session.getBundle()));
            }
        };
        subscriptionDataSourceList.put(subscribeDataSourceCallback, iDataKitRemoteCallback);

        try {
            executeAsync(in, iDataKitRemoteCallback);
        } catch (RemoteException e) {
            //TODO:
            subscribeDataSourceCallback.onError(MCStatus.CONNECTION_ERROR);
        }
    }

    protected void unsubscribeDataSourceAsync(MCSubscribeDataSourceCallback subscribeDataSourceCallback) {
        if (!subscriptionDataSourceList.containsKey(subscribeDataSourceCallback)) return;
        try {
            executeAsync(_UnsubscribeDataSourceIn.create(createSessionId()), subscriptionDataSourceList.get(subscribeDataSourceCallback));
        } catch (RemoteException e) {
            //TODO:
        }
        subscriptionDataSourceList.remove(subscribeDataSourceCallback);
    }


    protected ArrayList<MCData> queryDataByTime(MCDataSourceResult dataSourceResult, long startTimestamp, long endTimestamp) {
        insertDataExec.sync();
        _Session in = _QueryDataByTimeIn.create(createSessionId(), dataSourceResult.getDsId(), startTimestamp, endTimestamp);
        try {
            _Session out = execute(in);
            return _QueryDataByTimeOut.getData(out.getBundle());
        } catch (RemoteException e) {
            //TODO:
            return null;
        }
    }


    protected void queryDataByTimeAsync(MCDataSourceResult dataSourceResult, long startTimestamp, long endTimestamp, final QueryDataCallback queryCallback) {
        insertDataExec.sync();
        _Session in = _QueryDataByTimeIn.create(createSessionId(), dataSourceResult.getDsId(), startTimestamp, endTimestamp);
        try {
            executeAsync(in, new IDataKitRemoteCallback.Stub() {
                @Override
                public void onReceived(_Session _session) throws RemoteException {
                    if (MCStatus.isSuccessful(_session.getStatus()))
                        queryCallback.onReceive(_QueryDataByTimeOut.getData(_session.getBundle()), _session.getStatus());
                    else queryCallback.onReceive(null, _session.getStatus());
                }
            });
        } catch (Exception e) {
            queryCallback.onReceive(null, MCStatus.CONNECTION_ERROR);
        }
    }


    protected ArrayList<MCData> queryDataByNumber(MCDataSourceResult dataSourceResult, int lastNData) {
        insertDataExec.sync();
        _Session in = _QueryDataByNumberIn.create(createSessionId(), dataSourceResult.getDsId(), lastNData);
        try {
            _Session out = execute(in);
            return _QueryDataByNumberOut.getData(out.getBundle());
        } catch (RemoteException e) {
            //TODO:
            return null;
        }
    }


    protected void queryDataByNumberAsync(MCDataSourceResult dataSourceResult, int lastNData, final QueryDataCallback queryCallback) {
        insertDataExec.sync();
        _Session in = _QueryDataByNumberIn.create(createSessionId(), dataSourceResult.getDsId(), lastNData);
        try {
            executeAsync(in, new IDataKitRemoteCallback.Stub() {
                @Override
                public void onReceived(_Session _session) throws RemoteException {
                    if (MCStatus.isSuccessful(_session.getStatus()))
                        queryCallback.onReceive(_QueryDataByNumberOut.getData(_session.getBundle()), _session.getStatus());
                    else queryCallback.onReceive(null, _session.getStatus());
                }
            });
        } catch (Exception e) {
            queryCallback.onReceive(null, MCStatus.CONNECTION_ERROR);
        }
    }

    protected int queryDataCount(MCDataSourceResult dataSourceResult, long startTimestamp, long endTimestamp) {
        insertDataExec.sync();
        _Session in = _QueryDataCountIn.create(createSessionId(), dataSourceResult.getDsId(), startTimestamp, endTimestamp);
        try {
            _Session out = execute(in);
            return _QueryDataCountOut.getCount(out.getBundle());
        } catch (RemoteException e) {
            //TODO:
            return -1;
        }
    }

    protected void queryDataCountAsync(MCDataSourceResult dataSourceResult, long startTimestamp, long endTimestamp, final CountDataCallback callback) {
        insertDataExec.sync();
        _Session in = _QueryDataCountIn.create(createSessionId(), dataSourceResult.getDsId(), startTimestamp, endTimestamp);
        try {
            executeAsync(in, new IDataKitRemoteCallback.Stub() {
                @Override
                public void onReceived(_Session _session) throws RemoteException {
                    if (MCStatus.isSuccessful(_session.getStatus()))
                        callback.onReceive(_QueryDataCountOut.getCount(_session.getBundle()), _session.getStatus());
                    else callback.onReceive(-1, _session.getStatus());
                }
            });
        } catch (Exception e) {
            callback.onReceive(-1, MCStatus.CONNECTION_ERROR);
        }
    }

    protected int insertData(MCData[] data) {
        insertDataExec.addData(data, false);
        return MCStatus.SUCCESS;
    }
    protected int insertDataIfNew(MCData[] data){
        insertDataExec.addData(data, true);
        return MCStatus.SUCCESS;
    }
/*
    protected int insertDataIfNew(MCRegistration registration, MCData[] data) {
        for (MCData aData : data)
            if (registration.getDataSource().getSampleType() != aData.getSampleType() || registration.getDataSource().getDataType() != aData.getDataType())
                return MCStatus.INVALID_DATA;
        insertDataExec.addData(registration, data);
        return MCStatus.SUCCESS;
    }
*/

    protected void subscribeDataAsync(MCDataSourceResult dataSourceResult, final MCSubscribeDataCallback subscribeDataCallback) {
        if (subscriptionDataList.containsKey(subscribeDataCallback)) return;
        _Session in = _SubscribeDataIn.create(createSessionId(), dataSourceResult);
        IDataKitRemoteCallback.Stub iDataKitRemoteCallback = new IDataKitRemoteCallback.Stub() {
            @Override
            public void onReceived(_Session _session) throws RemoteException {
                ArrayList<MCData> data = _SubscribeDataOut.getData(_session.getBundle());
                for (int i = 0; i < data.size(); i++)
                    subscribeDataCallback.onReceive(data.get(i));
            }
        };
        subscriptionDataList.put(subscribeDataCallback, iDataKitRemoteCallback);

        try {
            executeAsync(in, iDataKitRemoteCallback);
        } catch (RemoteException e) {
            //TODO:
            subscribeDataCallback.onError(MCStatus.CONNECTION_ERROR);
        }
    }

    protected void unsubscribeDataAsync(MCSubscribeDataCallback subscribeDataCallback) {
        if (!subscriptionDataList.containsKey(subscribeDataCallback)) return;
        try {
            executeAsync(_UnsubscribeDataIn.create(createSessionId()), subscriptionDataList.get(subscribeDataCallback));
        } catch (RemoteException e) {
            //TODO:
        }
        subscriptionDataList.remove(subscribeDataCallback);
    }


}
