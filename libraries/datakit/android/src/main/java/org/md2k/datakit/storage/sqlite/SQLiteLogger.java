package org.md2k.datakit.storage.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

import org.md2k.datakit.converter.IByteConverter;
import org.md2k.datakit.converter.KryoConverter;
import org.md2k.datakit.storage.ILogger;
import org.md2k.mcerebrumapi.core.data.DataArray;
import org.md2k.mcerebrumapi.core.data.MCData;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.core.datakitapi.ipc.insert_datasource.MCRegistration;
import org.md2k.mcerebrumapi.core.time.DateTime;

import java.util.ArrayList;

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
public class SQLiteLogger implements ILogger {
    private static final String FILEPATH = "sqlite.db";

    private SQLiteDatabase db;
    private TableDataSource tableDataSource;
    private TableData tableData;
    private TableMetaData tableMetaData;
    private Context context;

    public SQLiteLogger(Context context) {
        this.context = context;
    }

    @Override
    public void start() {
        db = context.openOrCreateDatabase(FILEPATH, Context.MODE_PRIVATE, null);
        IByteConverter iByteConverter = new KryoConverter();
        tableDataSource = new TableDataSource(iByteConverter);
        tableData = new TableData(iByteConverter);
        tableMetaData = new TableMetaData(iByteConverter);
        tableDataSource.createTable(db);
        tableData.createTable(db);
        tableMetaData.createTable(db);
    }

    @Override
    public void stop() {
        try {
            if (db.isOpen())
                db.close();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void delete() {
        stop();
        try {
            context.deleteDatabase(FILEPATH);
        } catch (Exception ignored) {
        }

    }

    @Override
    public long size() {
        return context.getDatabasePath(FILEPATH).length();
    }

    @Override
    public boolean isDataSourceExist(MCDataSource dataSource) {
        MCDataSourceResult d = tableDataSource.queryExactDataSource(db, dataSource);
        return d != null;
    }


    @Override
    public MCDataSourceResult insertOrUpdateDataSource(MCDataSource dataSource) {
        long curTimestamp = DateTime.getCurrentTime();
        MCDataSourceResult d = tableDataSource.queryExactDataSource(db, dataSource);
        if (d == null) {
            MCDataSourceResult dataSourceResult = tableDataSource.insertDataSource(db, curTimestamp, dataSource);
            tableMetaData.insert(db, dataSourceResult.getDsId(), curTimestamp, dataSource);
            return dataSourceResult;
        } else {
            if (d.getDataSource().equals(dataSource)) return d;
            else {
                MCDataSourceResult dataSourceResult = tableDataSource.updateDataSource(db, d.getDsId(), curTimestamp, dataSource);
                tableMetaData.insert(db, dataSourceResult.getDsId(), curTimestamp, dataSource);
                return dataSourceResult;
            }
        }
    }

    @Override
    public ArrayList<MCDataSourceResult> queryDataSource(MCDataSource d) {
        return tableDataSource.queryDataSource(db, d);
    }

    @Override
    public void insertData(SparseArray<DataArray> data) {
        tableData.insert(db, data);
    }

    @Override
    public ArrayList<MCData> queryData(int dsId, int n) {
        return tableData.query(db, dsId, n);
    }

    @Override
    public ArrayList<MCData> queryData(int dsId, long startTimestamp, long endTimestamp) {
        return tableData.query(db, dsId, startTimestamp, endTimestamp);
    }

    @Override
    public int queryDataCount(int dsId, long startTimestamp, long endTimestamp) {
        return tableData.count(db, dsId, startTimestamp, endTimestamp);
    }

    long size(String tableName) {
        if (tableName.equals(tableDataSource.getTableName()))
            return tableDataSource.getSize(db);
        if (tableName.equals(tableData.getTableName()))
            return tableData.getSize(db);
        if (tableName.equals(tableMetaData.getTableName()))
            return tableMetaData.getSize(db);
        return -1;
    }


}
