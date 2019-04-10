package org.md2k.mcerebrumapi.datakitapi.ipc.insert_data;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;

import org.md2k.mcerebrumapi.data.DataArray;
import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.ipc.data.SyncCallback;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource.MCRegistration;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
public class _InsertDataExec {
    private SparseArray<DataArray> dataArrays;
    private _DataBuffer dataBuffer;
    private Handler handler;
    private static final long SYNC_TIME = 1000; //1 second
    private boolean isSyncScheduled;
    private Lock lock;
    private SyncCallback syncCallback;


    public _InsertDataExec(SyncCallback syncCallback) {
        this.dataBuffer = new _DataBuffer();
        this.syncCallback = syncCallback;
        handler = new Handler(Looper.getMainLooper());
        isSyncScheduled = false;
        lock = new ReentrantLock();
        dataArrays = new SparseArray<>();
    }

    public void addData(MCRegistration registration, MCData[] data) {
        Log.d("abc","data="+registration.getDataSource().getDataSourceType());
        lock.lock();
        DataArray d = dataArrays.get(registration.getDsId(), new DataArray());
        d.add(data);
        dataArrays.put(registration.getDsId(), d);
        for (MCData aData : data) dataBuffer.add(registration.getDsId(), aData.getTimestamp());
        if (dataBuffer.isHighFrequency(registration.getDsId())) {
            if (!isSyncScheduled)
                handler.postDelayed(runnable, SYNC_TIME);
            isSyncScheduled = true;
        } else {
            handler.removeCallbacks(runnable);
            isSyncScheduled = false;
            syncCallback.sync();
        }

        lock.unlock();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            lock.lock();
            syncCallback.sync();
            isSyncScheduled = false;
            lock.unlock();
        }
    };

    public void syncIfRequired(int dsId) {
        if (dataArrays.get(dsId) != null) {
            syncCallback.sync();
        }
    }

    public SparseArray<DataArray> getData() {
        SparseArray<DataArray> d = dataArrays;
        dataArrays = new SparseArray<>();
        return d;
    }
}
