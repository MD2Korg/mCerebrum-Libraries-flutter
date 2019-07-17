package org.md2k.mcerebrumapi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.md2k.mcerebrumapi.datakitapi.ipc.IDataKitRemoteCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc._Session;
import org.md2k.mcerebrumapi.datakitapi.ipc.authenticate.MCConnectionCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.authenticate._AuthenticateIn;
import org.md2k.mcerebrumapi.status.MCStatus;
import org.md2k.mcerebrumapi.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center

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
abstract class AbstractDataKitManager {
    private Integer sessionId = null;

    private IDataKitRemoteService iDataKitRemoteService;
    private ArrayList<MCConnectionCallback> connectionCallbacks;
    private boolean connected;
    private MCStatus authenticated;
    private Intent serverIntent;

    AbstractDataKitManager() {
        connectionCallbacks = new ArrayList<>();
        iDataKitRemoteService = null;
        connected = false;
        authenticated = MCStatus.AUTHENTICATION_REQUIRED;

    }

    void connect(final MCConnectionCallback connectionCallback) {
        if (!connectionCallbacks.contains(connectionCallback))
            connectionCallbacks.add(connectionCallback);
        if (!connected)
            connectToServer();
        else {
//            if (authenticated == MCStatus.SUCCESS)
                connectionCallback.onSuccess();
//            else connectionCallback.onError(authenticated);
        }
    }

    void disconnect(MCConnectionCallback connectionCallback) {
        connectionCallbacks.remove(connectionCallback);
        if (connectionCallbacks.size() == 0) {
            disconnectFromServer();
        }
    }

    boolean isConnected() {
        return connected;
    }

    private void disconnectAll() {
        connectionCallbacks.clear();

        disconnectFromServer();
    }

    private void disconnectFromServer() {
        if (!connected) return;
        connected = false;
        authenticated = MCStatus.AUTHENTICATION_REQUIRED;
        if (serverIntent != null) {
            assert MCerebrumAPI.getContext() != null;
            MCerebrumAPI.getContext().stopService(serverIntent);
            MCerebrumAPI.getContext().unbindService(mServiceConnection);
        }
    }

    private void sendConnectionError(MCStatus status) {
        for (int i = 0; i < connectionCallbacks.size(); i++)
            connectionCallbacks.get(i).onError(status);
        connectionCallbacks.clear();
    }

    private void sendConnectionSuccess() {
        for (int i = 0; i < connectionCallbacks.size(); i++)
            connectionCallbacks.get(i).onSuccess();
    }


    private void connectToServer() {
        serverIntent = Utils.findServer(MCerebrumAPI.getContext());
        if (serverIntent == null) {
            sendConnectionError(MCStatus.MCEREBRUM_APP_NOT_INSTALLED);
            return;
        }
        try {
            assert MCerebrumAPI.getContext() != null;
            boolean res = MCerebrumAPI.getContext().bindService(serverIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
            if (!res) {
                sendConnectionError(MCStatus.MCEREBRUM_BIND_ERROR);
            }

        } catch (SecurityException e) {
            sendConnectionError(MCStatus.MCEREBRUM_BIND_ERROR);
        }
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("abc", "onServiceConnected()");
            connected = true;
            iDataKitRemoteService = IDataKitRemoteService.Stub.asInterface(service);
            authenticate();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("abc", "onServiceDisconnected()");
            iDataKitRemoteService = null;
            connected = false;
            if (authenticated == MCStatus.SUCCESS)
                connectToServer();
        }
    };

    private void authenticate() {
        try {
            assert MCerebrumAPI.getContext() != null;
            _Session in = _AuthenticateIn.create(createSessionId(), MCerebrumAPI.getContext().getPackageName());
            executeAsync(in, new IDataKitRemoteCallback.Stub() {
                @Override
                public void onReceived(_Session _session) {
//                    MCStatus authenticated = _session.getStatus();
//                    if (authenticated == MCStatus.SUCCESS) {
                        sendConnectionSuccess();
 //                   } else {
 //                       sendConnectionError(authenticated);
 //                       disconnectAll();
 //                   }
                }
            });
        } catch (RemoteException e) {
            sendConnectionError(MCStatus.MCEREBRUM_BIND_ERROR);
        }

    }

    protected _Session execute(_Session _session) throws RemoteException {
        return iDataKitRemoteService.execute(_session);
    }

    void executeAsync(_Session _session, IDataKitRemoteCallback iDataKitRemoteCallback) throws RemoteException {
        iDataKitRemoteService.executeAsync(_session, iDataKitRemoteCallback);
    }



    synchronized int createSessionId() {
        if (sessionId == null) {
            sessionId = new Random().nextInt();
        } else sessionId++;
        return sessionId;
    }

}
