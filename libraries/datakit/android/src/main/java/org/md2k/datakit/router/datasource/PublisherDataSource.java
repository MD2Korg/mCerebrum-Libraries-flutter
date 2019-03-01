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

package org.md2k.datakit.router.datasource;

import android.os.RemoteException;

import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.core.datakitapi.ipc.IDataKitRemoteCallback;
import org.md2k.mcerebrumapi.core.datakitapi.ipc._Session;
import org.md2k.mcerebrumapi.core.datakitapi.ipc.subscribe_datasource._SubscribeDataSourceOut;
import org.md2k.mcerebrumapi.core.status.MCStatus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Publishes messages on behalf of a particular <code>DataSource</code>.
 */
public class PublisherDataSource {

    /**
     * List of message subscribers.
     */
    private List<Subscriber> subscribers;

    /**
     * Constructor
     *
     */
    PublisherDataSource() {
        subscribers = new ArrayList<>();
    }

    /**
     * Clears <code>subscribers</code> and sets <code>databaseSubscriber</code> to null.
     */
    public void close() {
        subscribers.clear();
    }

    /**
     * Passes the received data to <code>notifyAllObservers</code> so it can be inserted into the
     * database.
     *
     * @param dataSourceResult Array of data types.
     */
    public void publish(MCDataSourceResult dataSourceResult) {
        notifyAllObservers(dataSourceResult);
    }

    /**
     * Checks if the given subscriber exists in the list.
     *
     * @param subscriber subscriber to queryDataSource.
     * @return Whether the subscriber is in the list or not.
     */
    private boolean isExists(IDataKitRemoteCallback subscriber) {
        for(int i=0;i<subscribers.size();i++){
            if(subscribers.get(i).iDataKitRemoteCallback==subscriber) return true;
        }
        return false;
    }


    /**
     * Adds a subscriber to the subscribers list.
     *
     * @param subscriber Subscriber to addListener.
     */
    public void add(IDataKitRemoteCallback subscriber) {
        if (!isExists(subscriber))
            subscribers.add(new Subscriber(subscriber));
    }

    /**
     * Removes a subscriber from the list.
     *
     * @param subscriber Subscriber to remove.
     */
    public void remove(IDataKitRemoteCallback subscriber) {
        int flag =-1;
        for(int i=0;i<subscribers.size();i++){
            if(subscribers.get(i).iDataKitRemoteCallback==subscriber) {
                flag = i;
                break;
            }
        }
        if(flag!=-1) subscribers.remove(flag);
    }

    /**
     * Notifies all observing message threads if the data was inserted successfully or not.
     *
     * @param dataSourceResult Array of data types to publish in the database.
     */
    private void notifyAllObservers(MCDataSourceResult dataSourceResult) {
        for (Iterator<Subscriber> iterator = subscribers.iterator(); iterator.hasNext(); ) {
            Subscriber subscriber = iterator.next();
            try {
                _Session out =  _SubscribeDataSourceOut.create(MCStatus.SUCCESS, dataSourceResult);
                subscriber.iDataKitRemoteCallback.onReceived(out);
            } catch (RemoteException e) {
                iterator.remove();
            }
        }
    }

    public int size() {
        return subscribers.size();
    }

    class Subscriber{
        IDataKitRemoteCallback iDataKitRemoteCallback;

        Subscriber(IDataKitRemoteCallback iDataKitRemoteCallback) {
            this.iDataKitRemoteCallback = iDataKitRemoteCallback;
        }
    }
}
