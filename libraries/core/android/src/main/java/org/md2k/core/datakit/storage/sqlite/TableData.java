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
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseArray;

import org.md2k.core.datakit.converter.IByteConverter;
import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.data.MCDataType;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource.MCRegistration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Class for defining a <code>DataSource</code> table within the database.
 */
public class TableData extends AbstractTable {
    private static String TABLE_NAME = "data";
    private static String C_ID = "_id";
    private static String C_DS_ID = "ds_id";
    private static String C_TIMESTAMP = "timestamp";
    private static String CC_SYNC = "cc_sync";

    private static String C_DATA_TYPE = "data_type";

    private static String C_SAMPLE = "sample";

    /**
     * Command string to create an index into the database based on <code>datasource_id</code>.
     */
    private static final String SQL_CREATE_DATA_INDEX = "CREATE INDEX IF NOT EXISTS index_datasource_id on "
            + TABLE_NAME + " (" + C_DS_ID + ");";
    /**
     * Command string to create an index into the database based on <code>datasource_id</code> and <code>cloud_sync_bit</code>.
     */
    private static final String SQL_CREATE_CC_INDEX = "CREATE INDEX IF NOT EXISTS index_cc_datasource_id on "
            + TABLE_NAME + " (" + C_DS_ID + ", " + CC_SYNC + ");";

    /**
     * Database table creation command.
     */
    private static final String SQL_CREATE_DATA = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            C_ID + " INTEGER PRIMARY KEY autoincrement, " +
            C_DS_ID + " INTEGER not null, " +
            CC_SYNC + " INTEGER DEFAULT 0, " +
            C_DATA_TYPE + " INTEGER, " +
            C_TIMESTAMP + " INTEGER, " +
            C_SAMPLE + " BLOB not null);";


    /**
     * Creates a data table in the database if one does not already exist.
     */
    TableData(IByteConverter iByteConverter) {
        super(TABLE_NAME, iByteConverter);
    }


    /**
     * Creates the tables for the database if needed.
     *
     * @param db Database.
     */
    void createTable(SQLiteDatabase db) {
        super.createTable(db, SQL_CREATE_DATA);
        db.execSQL(SQL_CREATE_DATA_INDEX);
        db.execSQL(SQL_CREATE_CC_INDEX);
    }

    /**
     * Updates or inserts the data type accordingly.
     *
     * @param db                   Database.
     * @param sparseArray Data type to insert.
     */
    public void insert(SQLiteDatabase db, SparseArray<ArrayList<MCData>> sparseArray) {
        Log.d("abc","insert data to db");
        ArrayList<ContentValues> cValues = new ArrayList<>();
        int dsId;
        for (int i = 0; i < sparseArray.size(); i++) {
            dsId = sparseArray.keyAt(i);
            ArrayList<MCData> values = sparseArray.get(sparseArray.keyAt(i));
            for (MCData value : values) {
                ContentValues cValue = new ContentValues();
                byte[] dataArray = iByteConverter.toBytes(value.getSample());
                cValue.put(C_DS_ID, dsId);
                cValue.put(C_DATA_TYPE, value.getDataType().getValue());

                cValue.put(C_TIMESTAMP, value.getStartTimestamp());
                cValue.put(C_SAMPLE, dataArray);
                cValues.add(cValue);
            }
        }
        Collections.sort(cValues, new Comparator<ContentValues>() {
            @Override
            public int compare(ContentValues lhs, ContentValues rhs) {
                long l = lhs.getAsLong(C_TIMESTAMP);
                long r = rhs.getAsLong(C_TIMESTAMP);
                if(l==r) return 0;
                else if(l<r) return -1;
                else return 1;
            }
        });
        db.beginTransaction();

        for (ContentValues cValue : cValues) {
            db.insert(TABLE_NAME, null, cValue);
        }

        try {
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }


    /**
     * Queries the database for the given data source during the given time frame.
     *
     * @param dsId           Data source identifier.
     * @param startTimestamp Beginning of the time frame.
     * @param endTimestamp   End of the time frame.
     * @return The result of the queryData.
     */
    ArrayList<MCData> query(SQLiteDatabase db, int dsId, long startTimestamp, long endTimestamp) {
        ArrayList<MCData> data = new ArrayList<>();
        String[] columns = new String[]{C_TIMESTAMP, C_DATA_TYPE, C_SAMPLE};
        String selection = C_DS_ID + "=? AND " + C_TIMESTAMP + " >=? AND " + C_TIMESTAMP + " <=?";
        String[] selectionArgs = new String[]{String.valueOf(dsId), String.valueOf(startTimestamp), String.valueOf(endTimestamp)};
        Cursor mCursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, C_TIMESTAMP +" ASC");
        while (mCursor != null && mCursor.moveToNext()) {
            byte[] bytes = mCursor.getBlob(mCursor.getColumnIndex(C_SAMPLE));
            long sTime = mCursor.getLong(mCursor.getColumnIndex(C_TIMESTAMP));
            int dataType = mCursor.getInt(mCursor.getColumnIndex(C_DATA_TYPE));
            MCData d = createData(dsId, sTime, MCDataType.getDataType(dataType), bytes);
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
     * @param lastNSample Last n samples to return.
     * @return A list of <code>DataType</code>s matching the queryData.
     */
    ArrayList<MCData> query(SQLiteDatabase db, int dsId, int lastNSample) {
        ArrayList<MCData> data = new ArrayList<>();
        String[] columns = new String[]{C_TIMESTAMP, C_DATA_TYPE, C_SAMPLE};
        String selection = C_DS_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(dsId)};
        Cursor mCursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, C_TIMESTAMP + " ASC", String.valueOf(lastNSample));
        while (mCursor != null && mCursor.moveToNext()) {
            byte[] bytes = mCursor.getBlob(mCursor.getColumnIndex(C_SAMPLE));
            long startTimestamp = mCursor.getLong(mCursor.getColumnIndex(C_TIMESTAMP));
            int dataType = mCursor.getInt(mCursor.getColumnIndex(C_DATA_TYPE));
            MCData d = createData(dsId, startTimestamp, MCDataType.getDataType(dataType), bytes);
            data.add(d);
        }
        if(mCursor!=null)
            mCursor.close();
        return data;
    }

    HashMap<String, Object> queryNotSynced(SQLiteDatabase db, int dsId, int maximumLimit) {
        ArrayList<MCData> data = new ArrayList<>();
        long minId = -1;
        long maxId = -1;
        String[] columns = new String[]{C_ID, C_TIMESTAMP, C_DATA_TYPE, C_SAMPLE};
        String selection = C_DS_ID + "=? AND " + CC_SYNC + "=?";
        String[] selectionArgs = new String[]{String.valueOf(dsId), String.valueOf(1)};
        Cursor mCursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, C_ID + " ASC", String.valueOf(maximumLimit));
        while (mCursor != null && mCursor.moveToNext()) {
            byte[] bytes = mCursor.getBlob(mCursor.getColumnIndex(C_SAMPLE));
            long startTimestamp = mCursor.getLong(mCursor.getColumnIndex(C_TIMESTAMP));
            int dataType = mCursor.getInt(mCursor.getColumnIndex(C_DATA_TYPE));
            MCData d = createData(dsId, startTimestamp, MCDataType.getDataType(dataType), bytes);
            data.add(d);
            int _id = mCursor.getInt(mCursor.getColumnIndex(C_ID));
            if (minId == -1 || minId > _id) minId = _id;
            if (maxId == -1 || maxId < _id) maxId = _id;
        }
        if (mCursor != null)
            mCursor.close();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("minId", minId);
        hashMap.put("maxId", maxId);
        hashMap.put("data", data);
        return hashMap;
    }

    void setSyncedBit(SQLiteDatabase db, int dsId, long minId, long maxId) {
        ContentValues values = new ContentValues();
        values.put(CC_SYNC, 1);
        String[] args = new String[]{Long.toString(minId), Long.toString(maxId), Integer.toString(dsId)};
        db.update(TABLE_NAME, values, CC_SYNC + "=0 AND " + C_ID + ">=? AND " + C_ID + " <=? AND " + C_DS_ID + " =?", args);
    }

    private int countById(SQLiteDatabase db, int dsId) {
        String selection;
        String[] selectionArgs;
        selection = C_DS_ID + "=?";
        selectionArgs = new String[]{String.valueOf(dsId)};
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, selection, selectionArgs);
    }

    private int countById(SQLiteDatabase db, int dsId, boolean sync) {
        String selection;
        String[] selectionArgs;
        selection = C_DS_ID + "=? AND " + CC_SYNC + "=?";
        if (sync)
            selectionArgs = new String[]{String.valueOf(dsId), "1"};
        else
            selectionArgs = new String[]{String.valueOf(dsId), "0"};
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, selection, selectionArgs);
    }

    int countByTime(SQLiteDatabase db, int dsId, long startTimestamp, long endTimestamp) {
        String selection = C_DS_ID + "=? AND " + C_TIMESTAMP + " >=? AND " + C_TIMESTAMP + " <=?";
        String[] selectionArgs = new String[]{String.valueOf(dsId), String.valueOf(startTimestamp), String.valueOf(endTimestamp)};
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, selection, selectionArgs);
    }

    private MCData createData(int dsId, long timeStamp, MCDataType dataType, byte[] bytes) {
        MCRegistration m = new MCRegistration(new MCDataSourceResult(dsId, 0, 0, null));
        switch (dataType) {
            case BYTE_ARRAY:
                return MCData.create(m, timeStamp, iByteConverter.fromBytes(bytes, byte[].class));
            case BOOLEAN_ARRAY:
                return MCData.create(m, timeStamp, iByteConverter.fromBytes(bytes, boolean[].class));
            case INT_ARRAY:
                return MCData.create(m, timeStamp, iByteConverter.fromBytes(bytes, int[].class));
            case LONG_ARRAY:
                return MCData.create(m, timeStamp, iByteConverter.fromBytes(bytes, long[].class));
            case DOUBLE_ARRAY:
                return MCData.create(m, timeStamp, iByteConverter.fromBytes(bytes, double[].class));
            case STRING_ARRAY:
                return MCData.create(m, timeStamp, iByteConverter.fromBytes(bytes, String[].class));
            case OBJECT:
            case ANNOTATION:
                //TODO: check object
                return MCData.create(m, timeStamp, iByteConverter.fromBytes(bytes, String.class));
            default:
                return MCData.create(m, timeStamp, (byte[]) iByteConverter.fromBytes(bytes, byte[].class));
        }
    }

    public void pruneDataIfSync(SQLiteDatabase db, int dsId, int notPruneCount) {
        int countAll = countById(db, dsId);
        int pruneCount = countAll - notPruneCount;
        int result;
        if (pruneCount <= 0) return;
        int countSync = countById(db, dsId, true);
        if (countSync < pruneCount)
            result = countSync;
        else result = pruneCount;
        String sqlCommand = "delete from " + TABLE_NAME + " where " + C_ID + "  in (select " + C_ID + " from "
                + TABLE_NAME + " where " + C_DS_ID + " = " + dsId + " AND " + CC_SYNC + " =1 "
                + " order by " + C_ID + " limit " + result + ")";
        db.execSQL(sqlCommand);
    }

    public void pruneData(SQLiteDatabase db, int dsId, int notPruneCount) {
        int countAll = countById(db, dsId);
        if (countAll <= notPruneCount) return;
        String sqlCommand = "delete from " + TABLE_NAME + " where " + C_ID + "  in (select " + C_ID + " from "
                + TABLE_NAME + " where " + C_DS_ID + " = " + dsId
                + " order by " + C_ID + " limit " + (countAll - notPruneCount) + ")";
        db.execSQL(sqlCommand);
    }

}
