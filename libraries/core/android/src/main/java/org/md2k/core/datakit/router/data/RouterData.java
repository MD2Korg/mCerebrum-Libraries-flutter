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

package org.md2k.core.datakit.router.data;

import android.util.SparseArray;

import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.ipc.IDataKitRemoteCallback;

import java.util.ArrayList;

/**
 * Routes messages and method calls to <code>Publishers</code>, <code>Publisher</code>,
 * <code>MessageSubscriber</code>, and <code>DatabaseSubscriber</code>.
 */
public class RouterData {

    /** List of data source message publishers. */
    private SparseArray<PublisherData> publishers;
    public RouterData(){
        publishers = new SparseArray<>();
    }

    public void start(){

    }
    public void stop(){
        publishers.clear();
    }

    public void publish(ArrayList<MCData> data) {
        int key = data.get(0).getDsId();
        if (publishers.get(key) != null)
            publishers.get(key).publish(data);
    }

    /**
     * Subscribes the given data source to <code>MessageSubscriber</code>.
     *
     * @param dsId Data source identifier.
     * @param iDataKitRemoteCallback return callback.
     */
    public void subscribe(int dsId, IDataKitRemoteCallback iDataKitRemoteCallback) {
        PublisherData p = publishers.get(dsId, new PublisherData());
        p.add(iDataKitRemoteCallback);
        publishers.put(dsId, p);
/*
        if(isExist(dsId)) {
            publishers.get(dsId).add(iDataKitRemoteCallback);
        }else {
            PublisherData publisherData = new PublisherData();
            publisherData.add(iDataKitRemoteCallback);
            publishers.put(dsId, publisherData);
        }
*/
    }

    /**
     * Unsubscribe the given data source.
     *
     * @param iDataKitRemoteCallback return callback
     */
    public void unsubscribe(IDataKitRemoteCallback iDataKitRemoteCallback) {
        int curKey=-1;
        for(int i=0;i<publishers.size();i++){
            int key = publishers.keyAt(i);
            PublisherData value = publishers.get(key);
            value.remove(iDataKitRemoteCallback);
            if(value.size()==0)
                curKey = key;
        }
        if(curKey!=-1)
            publishers.remove(curKey);
        /*
        if(isExist(dsId))
            publishers.get(dsId).remove(iDataKitRemoteCallback);
*/
    }

/*
    */
/**
     * Closes the <code>RoutingManager</code>, <code>publishers</code>, and <code>databaseLogger</code>.
     *//*

    public void close() {
        for (int i = 0; i < publishers.size(); i++) {
            int key = publishers.keyAt(i);
            publishers.get(key).close();
        }
        publishers.clear();
    }
*/
    private boolean isExist(int dsId) {
        return publishers.indexOfKey(dsId) >= 0;
    }
}
