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

package org.md2k.datakit.storage.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.md2k.datakit.converter.IByteConverter;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Class for defining a <code>DataSource</code> table within the database.
 */
class TableDataSource extends AbstractTable {

    protected static String TABLE_NAME = "datasources";
    private static String C_DS_ID = "ds_id";
    private static String C_DATASOURCE_ID = "datasource_id";
    private static String C_DATASOURCE_TYPE = "datasource_type";
    private static String C_PLATFORM_ID = "platform_id";
    private static String C_PLATFORM_TYPE = "platform_type";
    private static String C_PLATFORMAPP_ID = "platformapp_id";
    private static String C_PLATFORMAPP_TYPE = "platformapp_type";
    private static String C_APPLICATION_ID = "application_id";
    private static String C_APPLICATION_TYPE = "application_type";
    private static String C_CREATE_DATETIME = "create_datetime";
    private static String C_UPDATE_DATETIME = "update_datetime";
    private static String C_DATASOURCE = "datasource";
    private String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + " ("
            + C_DS_ID + " INTEGER PRIMARY KEY autoincrement, "
            + C_DATASOURCE_TYPE + " TEXT, "
            + C_DATASOURCE_ID + " TEXT, "
            + C_PLATFORM_TYPE + " TEXT,"
            + C_PLATFORM_ID + " TEXT, "
            + C_PLATFORMAPP_TYPE + " TEXT,"
            + C_PLATFORMAPP_ID + " TEXT, "
            + C_APPLICATION_TYPE + " TEXT,"
            + C_APPLICATION_ID + " TEXT, "
            + C_CREATE_DATETIME + " INTEGER, "
            + C_UPDATE_DATETIME + " INTEGER, "
            + C_DATASOURCE + " BLOB not null);";

    TableDataSource(IByteConverter iByteConverter) {
        super(TABLE_NAME, iByteConverter);
    }

    void createTable(SQLiteDatabase db) {
        super.createTable(db, SQL_CREATE_TABLE);
    }

    MCDataSourceResult insertDataSource(SQLiteDatabase db, long createTimestamp, MCDataSource dataSource) {
        HashMap<String, String> h = createHashForSelection(dataSource);
        ContentValues cValues = new ContentValues();
        for (Map.Entry<String, String> me : h.entrySet())
            cValues.put(me.getKey(), me.getValue());
        cValues.put(C_CREATE_DATETIME, createTimestamp);
        cValues.put(C_UPDATE_DATETIME, createTimestamp);
        cValues.put(C_DATASOURCE, iByteConverter.toBytes(dataSource));
        db.insertOrThrow(TABLE_NAME, null, cValues);
        return queryExactDataSource(db, dataSource);
    }
    /**
     * update a row in the database for the given <code>DataSource</code>.
     *
     * @param db         Database to addListener the new row to.
     * @param dataSource <code>DataSource</code> to addListener a row for.
     */

    MCDataSourceResult updateDataSource(SQLiteDatabase db, int dsId, long updateTimestamp, MCDataSource dataSource) {
        HashMap<String, String> h = createHashForSelection(dataSource);
        ContentValues cValues = new ContentValues();
        for (Map.Entry<String, String> me : h.entrySet())
            cValues.put(me.getKey(), me.getValue());
        cValues.put(C_UPDATE_DATETIME, updateTimestamp);
        cValues.put(C_DATASOURCE, iByteConverter.toBytes(dataSource));
        int v = db.update(TABLE_NAME, cValues,C_DS_ID + "=" + dsId,  null);
        return queryExactDataSource(db, dataSource);
    }

    /**
     * Constructs a database queryData for finding the given <code>DataSource</code> rows.
     *
     * @param db         Database to queryData.
     * @param dataSource <code>DataSource</code> to queryData for.
     * @return ArrayList of <code>DataSource</code> table rows that match the queryData.
     */
    MCDataSourceResult queryExactDataSource(SQLiteDatabase db, MCDataSource dataSource) {
        ArrayList<MCDataSourceResult> dataSourceResults = queryDataSource(db, dataSource);
        for (MCDataSourceResult dataSourceResult : dataSourceResults) {
            if (dataSourceResult.getDataSource().toUUID().equals(dataSource.toUUID()))
                return dataSourceResult;
        }
        return null;
    }

    ArrayList<MCDataSourceResult> queryDataSource(SQLiteDatabase db, MCDataSource dataSource) {
        ArrayList<MCDataSourceResult> dataSources = new ArrayList<>();
        HashMap<String, String> h = createHashForSelection(dataSource);
        StringBuilder selection = new StringBuilder();
        String[] selectionArgs = new String[h.size()];
        int i = 0;
        for (Map.Entry<String, String> me : h.entrySet()) {
            if (i != 0) selection.append(" AND ");
            selection.append(me.getKey()).append("=?");
            selectionArgs[i] = me.getValue();
            i++;
        }
        Cursor mCursor=null;
        try {
             mCursor = db.query(TABLE_NAME, new String[]{C_DS_ID, C_DATASOURCE, C_CREATE_DATETIME, C_UPDATE_DATETIME}, selection.toString(), selectionArgs, null, null,null);
        }catch (Exception e){
            Log.e("abc","abc");
        }
        Log.d("abc","abc");
            while (mCursor != null && mCursor.moveToNext()) {
                int dsId = mCursor.getInt(mCursor.getColumnIndex(C_DS_ID));
                long createTime = mCursor.getLong(mCursor.getColumnIndex(C_CREATE_DATETIME));
                long updateTime = mCursor.getLong(mCursor.getColumnIndex(C_UPDATE_DATETIME));
                MCDataSource d = (MCDataSource) iByteConverter.fromBytes(mCursor.getBlob(mCursor.getColumnIndex(C_DATASOURCE)), MCDataSource.class);
                dataSources.add(new MCDataSourceResult(dsId, createTime, updateTime, d));
            }
            if (mCursor != null)
                mCursor.close();
        return dataSources;
    }

    private HashMap<String, String> createHashForSelection(MCDataSource dataSource) {
        HashMap<String, String> h = new HashMap<>();
        h.put(C_DATASOURCE_TYPE, dataSource.getDataSourceType());
        h.put(C_DATASOURCE_ID, dataSource.getDataSourceId());
        h.put(C_PLATFORM_TYPE, dataSource.getPlatformType());
        h.put(C_PLATFORM_ID, dataSource.getPlatformId());
        h.put(C_PLATFORMAPP_TYPE, dataSource.getPlatformAppType());
        h.put(C_PLATFORMAPP_ID, dataSource.getPlatformAppId());
        h.put(C_APPLICATION_TYPE, dataSource.getApplicationType());
        h.put(C_APPLICATION_ID, dataSource.getApplicationId());
        h.values().removeAll(Collections.singleton(null));
        return h;
    }

}
