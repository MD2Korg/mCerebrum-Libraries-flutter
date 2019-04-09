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

package org.md2k.core.datakit.router.datasource;

import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.core.datakitapi.ipc.IDataKitRemoteCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Routes messages and method calls to <code>Publishers</code>, <code>Publisher</code>,
 * <code>MessageSubscriber</code>, and <code>DatabaseSubscriber</code>.
 */
public class RouterDataSource {

    /**
     * List of data source message publishers.
     */
    private HashMap<String, PublisherDataSource> publishers;

    public RouterDataSource(){
        publishers = new HashMap<>();
    }
    public void start() {

    }

    public void stop() {
        publishers.clear();
    }

    /**
     * Inserts the given <code>DataType</code> array into the database.
     *
     * @param dataSourceResult Array of data to publish.
     */
    public void publish(MCDataSourceResult dataSourceResult) {
        for (Map.Entry<String, PublisherDataSource> entry : publishers.entrySet()) {
            MCDataSource key = (MCDataSource) MCDataSource.queryBuilder().fromUUID(entry.getKey()).build();
            PublisherDataSource value = entry.getValue();
            if(key.isSubset(dataSourceResult.getDataSource()))
                value.publish(dataSourceResult);
        }
    }

    /**
     * Subscribes the given data source to <code>MessageSubscriber</code>.
     *
     * @param dataSource                   Data source identifier.
     * @param iDataKitRemoteCallback return callback.
     */
    public void subscribe(MCDataSource dataSource, IDataKitRemoteCallback iDataKitRemoteCallback) {
        String uuid = dataSource.toUUID();
        for (Map.Entry<String, PublisherDataSource> entry : publishers.entrySet()) {
            String key = entry.getKey();
            PublisherDataSource value = entry.getValue();
            if(key.equals(uuid)){
                value.add(iDataKitRemoteCallback);
                return;
            }
        }
        PublisherDataSource publisherDataSource = new PublisherDataSource();
        publisherDataSource.add(iDataKitRemoteCallback);
        publishers.put(uuid, publisherDataSource);
    }

    /**
     * Unsubscribe the given data source.
     *
     * @param iDataKitRemoteCallback return callback
     */
    public void unsubscribe(IDataKitRemoteCallback iDataKitRemoteCallback) {
        String key=null;
        for (Map.Entry<String, PublisherDataSource> entry : publishers.entrySet()) {
            PublisherDataSource value = entry.getValue();
            value.remove(iDataKitRemoteCallback);
            if(value.size()==0) key= entry.getKey();
        }
        if(key!=null)
            publishers.remove(key);
    }

    /**
     * Closes the <code>RoutingManager</code>, <code>publishers</code>, and <code>databaseLogger</code>.
     */
/*
    public void close() {
        for (int i = 0; i < publishers.size(); i++) {
            int key = publishers.keyAt(i);
            publishers.get(key).close();
        }
        publishers.clear();
    }

    private boolean isExist(int dsId) {
        return publishers.indexOfKey(dsId) >= 0;
    }
*/
}
