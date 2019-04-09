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

package org.md2k.core.datakit.storage.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.md2k.core.datakit.converter.IByteConverter;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceResult;

import java.util.ArrayList;

/**
 * Class for defining a <code>DataSource</code> table within the database.
 */
class TableMetaData extends AbstractTable {
    protected static String TABLE_NAME = "metadata";
    private static String C_ID = "_id";
    private static String C_DS_ID = "ds_id";
    private static String C_TIMESTAMP = "timestamp";
    private static String C_DATASOURCE = "datasource";

    /**
     * Database table creation command.
     */
    private static final String SQL_CREATE_DATA = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            C_ID + " INTEGER PRIMARY KEY autoincrement, " +
            C_DS_ID + " INTEGER not null, " +
            C_TIMESTAMP + " INTEGER, " +
            C_DATASOURCE + " BLOB not null);";


    /**
     * Creates a data table in the database if one does not already exist.
     */
    TableMetaData(IByteConverter iByteConverter) {
        super(TABLE_NAME, iByteConverter);
    }


    /**
     * Creates the tables for the database if needed.
     *
     * @param db Database.
     */
    void createTable(SQLiteDatabase db) {
        super.createTable(db, SQL_CREATE_DATA);
    }

    public void insert(SQLiteDatabase db, int dsId, long timestamp, MCDataSource dataSource) {
        ContentValues cValues = new ContentValues();
        cValues.put(C_TIMESTAMP, timestamp);
        cValues.put(C_DS_ID, dsId);
        cValues.put(C_DATASOURCE, iByteConverter.toBytes(dataSource));
        db.insertOrThrow(TABLE_NAME, null, cValues);
    }


    /**
     * Queries the database for the given data source during the given time frame.
     *
     * @param dsId           Data source identifier.
     * @return The result of the queryData.
     */
    ArrayList<MCDataSourceResult> query(SQLiteDatabase db, int dsId) {
        ArrayList<MCDataSourceResult> data = new ArrayList<>();
        String[] columns = new String[]{C_TIMESTAMP, C_DATASOURCE};
        String selection = C_DS_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(dsId), String.valueOf(dsId)};
        Cursor mCursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, C_TIMESTAMP +" ASC");
        while (mCursor != null && mCursor.moveToNext()) {
            byte[] bytes = mCursor.getBlob(mCursor.getColumnIndex(C_DATASOURCE));
            long sTime = mCursor.getLong(mCursor.getColumnIndex(C_TIMESTAMP));
            MCDataSourceResult d = new MCDataSourceResult(dsId, sTime, sTime, (MCDataSource) iByteConverter.fromBytes(bytes, MCDataSource.class));
            data.add(d);
        }
        if(mCursor!=null)
            mCursor.close();
        return data;
    }


    /**
     * Queries the database for the last n samples of the given data source.
     *
     * @param db          Database.
     * @param dsId        Data source identifier.
     * @return A list of <code>DataType</code>s matching the queryData.
     */
    MCDataSourceResult query(SQLiteDatabase db, int dsId, long timestamp) {
        MCDataSourceResult dataSourceResult=null;
        String[] columns = new String[]{C_TIMESTAMP, C_DATASOURCE};
        String selection = C_DS_ID + "=? AND " + C_TIMESTAMP + " <=?";
        String[] selectionArgs = new String[]{String.valueOf(dsId), String.valueOf(dsId)};
        Cursor mCursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, C_TIMESTAMP +" DESC");
        if (mCursor != null && mCursor.moveToNext()) {
            byte[] bytes = mCursor.getBlob(mCursor.getColumnIndex(C_DATASOURCE));
            long sTime = mCursor.getLong(mCursor.getColumnIndex(C_TIMESTAMP));
            dataSourceResult = new MCDataSourceResult(dsId, sTime, sTime, (MCDataSource) iByteConverter.fromBytes(bytes, MCDataSource.class));

        }
        if(mCursor!=null)
            mCursor.close();
        return dataSourceResult;
    }
}
