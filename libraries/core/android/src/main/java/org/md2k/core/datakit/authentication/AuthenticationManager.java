package org.md2k.core.datakit.authentication;

import android.os.RemoteException;

import org.md2k.mcerebrumapi.core.datakitapi.ipc.IDataKitRemoteCallback;
import org.md2k.mcerebrumapi.core.datakitapi.ipc.OperationType;
import org.md2k.mcerebrumapi.core.datakitapi.ipc._Session;
import org.md2k.mcerebrumapi.core.status.MCStatus;

import java.util.HashMap;
import java.util.Map;

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
public class AuthenticationManager {
    private HashMap<AuthData, IDataKitRemoteCallback> iDataKitRemoteCallbackHashMap;
    public AuthenticationManager() {
        iDataKitRemoteCallbackHashMap = new HashMap<>();
    }

    public void start() {
        iDataKitRemoteCallbackHashMap.clear();
    }

    public void stop() {

        for(Map.Entry<AuthData, IDataKitRemoteCallback> entry: iDataKitRemoteCallbackHashMap.entrySet()){
            try {
                AuthData in = entry.getKey();
                _Session out =  new _Session(in.sessionId, OperationType.AUTHENTICATE, MCStatus.DATAKIT_STOPPED);
                entry.getValue().onReceived(out);
            } catch (RemoteException ignored) {
            }
        }
        iDataKitRemoteCallbackHashMap.clear();
    }

    public void addCallback(int sessionId, String packageName, IDataKitRemoteCallback iDataKitRemoteCallback) {
        iDataKitRemoteCallbackHashMap.put(new AuthData(sessionId, packageName), iDataKitRemoteCallback);
    }

/*
    public void removeCallback(String packageName) {
        iDataKitRemoteCallbackHashMap.remove(packageName);
    }
*/

/*

    public ArrayList<String> getPackageList() {
        ArrayList<String> packageNameList = new ArrayList<>();
        for (Map.Entry<_Session, IDataKitRemoteCallback> entry : iDataKitRemoteCallbackHashMap.entrySet())
            packageNameList.add(entry.getKey());
        return packageNameList;
    }

*/
class AuthData{
    int sessionId;
    String packageName;

    AuthData(int sessionId, String packageName) {
        this.sessionId = sessionId;
        this.packageName = packageName;
    }
}
}