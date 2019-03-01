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
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseArray;

import org.md2k.datakit.converter.IByteConverter;
import org.md2k.mcerebrumapi.core.data.DataArray;
import org.md2k.mcerebrumapi.core.data.MCData;
import org.md2k.mcerebrumapi.core.data.MCDataType;
import org.md2k.mcerebrumapi.core.data.MCSampleType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class for defining a <code>DataSource</code> table within the database.
 */
public class TableData extends AbstractTable {
    private static String TABLE_NAME = "data";
    private static String C_ID = "_id";
    private static String C_DS_ID = "ds_id";
    private static String C_START_TIMESTAMP = "start_timestamp";
    private static String C_END_TIMESTAMP = "end_timestamp";

    private static String C_DATA_TYPE = "data_type";

    private static String C_SAMPLE_TYPE = "sample_type";

    private static String C_SAMPLE = "sample";

    /**
     * Database table creation command.
     */
    private static final String SQL_CREATE_DATA = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            C_ID + " INTEGER PRIMARY KEY autoincrement, " +
            C_DS_ID + " INTEGER not null, " +
            C_DATA_TYPE + " INTEGER, " +
            C_SAMPLE_TYPE + " INTEGER, " +
            C_START_TIMESTAMP + " INTEGER, " +
            C_END_TIMESTAMP + " INTEGER, " +
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
    }

    /**
     * Updates or inserts the data type accordingly.
     *
     * @param db                   Database.
     * @param sparseArray Data type to insert.
     */
    public void insert(SQLiteDatabase db, SparseArray<DataArray> sparseArray) {
        Log.d("abc","insert data to db");
        ArrayList<ContentValues> cValues = new ArrayList<>();
        int dsId;
        for (int i = 0; i < sparseArray.size(); i++) {
            dsId = sparseArray.keyAt(i);
            ArrayList<MCData> values = sparseArray.get(sparseArray.keyAt(i)).get();
            for (MCData value : values) {
                ContentValues cValue = new ContentValues();
                byte[] dataArray = iByteConverter.toBytes(value.getSample());
                cValue.put(C_DS_ID, dsId);
                cValue.put(C_DATA_TYPE, value.getDataType().getValue());
                cValue.put(C_SAMPLE_TYPE, value.getSampleType().getValue());

                cValue.put(C_START_TIMESTAMP, value.getStartTimestamp());
                cValue.put(C_END_TIMESTAMP, value.getEndTimestamp());
                cValue.put(C_SAMPLE, dataArray);
                cValues.add(cValue);
            }
        }
        Collections.sort(cValues, new Comparator<ContentValues>() {
            @Override
            public int compare(ContentValues lhs, ContentValues rhs) {
                long l = lhs.getAsLong(C_START_TIMESTAMP);
                long r = rhs.getAsLong(C_START_TIMESTAMP);
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
        String[] columns = new String[]{C_START_TIMESTAMP, C_END_TIMESTAMP, C_DATA_TYPE, C_SAMPLE_TYPE, C_SAMPLE};
        String selection = C_DS_ID + "=? AND " + C_START_TIMESTAMP + " >=? AND " + C_END_TIMESTAMP + " <=?";
        String[] selectionArgs = new String[]{String.valueOf(dsId), String.valueOf(startTimestamp), String.valueOf(endTimestamp)};
        Cursor mCursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, C_START_TIMESTAMP+" ASC");
        while (mCursor != null && mCursor.moveToNext()) {
            byte[] bytes = mCursor.getBlob(mCursor.getColumnIndex(C_SAMPLE));
            long sTime = mCursor.getLong(mCursor.getColumnIndex(C_START_TIMESTAMP));
            long eTime = mCursor.getLong(mCursor.getColumnIndex(C_END_TIMESTAMP));
            int dataType = mCursor.getInt(mCursor.getColumnIndex(C_DATA_TYPE));
            int sampleType = mCursor.getInt(mCursor.getColumnIndex(C_SAMPLE_TYPE));
            MCData d=null;
            if(dataType == MCDataType.POINT.getValue()){
                d = createPoint(sTime, MCSampleType.getSampleType(sampleType), bytes);
            }
            else if(dataType==MCDataType.ANNOTATION.getValue()){
                d=createAnnotation(sTime, eTime, MCSampleType.getSampleType(sampleType), bytes);
            }
            if (d != null) data.add(d);
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
        String[] columns = new String[]{C_START_TIMESTAMP, C_END_TIMESTAMP, C_DATA_TYPE, C_SAMPLE_TYPE, C_SAMPLE};
        String selection = C_DS_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(dsId)};
        Cursor mCursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, C_START_TIMESTAMP+" ASC", String.valueOf(lastNSample));
        while (mCursor != null && mCursor.moveToNext()) {
            byte[] bytes = mCursor.getBlob(mCursor.getColumnIndex(C_SAMPLE));
            long startTimestamp = mCursor.getLong(mCursor.getColumnIndex(C_START_TIMESTAMP));
            long endTimestamp = mCursor.getLong(mCursor.getColumnIndex(C_END_TIMESTAMP));
            int dataType = mCursor.getInt(mCursor.getColumnIndex(C_DATA_TYPE));
            int sampleType = mCursor.getInt(mCursor.getColumnIndex(C_SAMPLE_TYPE));
            MCData d=null;
            if(dataType == MCDataType.POINT.getValue()){
                d = createPoint(startTimestamp, MCSampleType.getSampleType(sampleType), bytes);
            }
            else if(dataType==MCDataType.ANNOTATION.getValue()){
                d=createAnnotation(startTimestamp, endTimestamp, MCSampleType.getSampleType(sampleType), bytes);
            }
            if (d != null) data.add(d);
        }
        if(mCursor!=null)
            mCursor.close();
        return data;
    }
    int count(SQLiteDatabase db, int dsId, long startTimestamp, long endTimestamp) {
        String selection = C_DS_ID + "=? AND " + C_START_TIMESTAMP + " >=? AND " + C_END_TIMESTAMP + " <=?";
        String[] selectionArgs = new String[]{String.valueOf(dsId), String.valueOf(startTimestamp), String.valueOf(endTimestamp)};
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, selection, selectionArgs);
    }

    private MCData createPoint(long timeStamp, MCSampleType sampleType, byte[] bytes) {
        switch (sampleType) {
            case BYTE_ARRAY:
                return MCData.createPointByteArray(timeStamp, (byte[]) iByteConverter.fromBytes(bytes, byte[].class));
            case BOOLEAN_ARRAY:
                return MCData.createPointBooleanArray(timeStamp, (boolean[]) iByteConverter.fromBytes(bytes, boolean[].class));
            case INT_ARRAY:
                return MCData.createPointIntArray(timeStamp, (int[]) iByteConverter.fromBytes(bytes, int[].class));
            case LONG_ARRAY:
                return MCData.createPointLongArray(timeStamp, (long[]) iByteConverter.fromBytes(bytes, long[].class));
            case DOUBLE_ARRAY:
                return MCData.createPointDoubleArray(timeStamp, (double[]) iByteConverter.fromBytes(bytes, double[].class));
            case STRING_ARRAY:
                return MCData.createPointStringArray(timeStamp, (String[]) iByteConverter.fromBytes(bytes, String[].class));
                //TODO: ENUM
            case OBJECT:
                //TODO: check object
                return MCData.createPointObject(timeStamp, iByteConverter.fromBytes(bytes, String.class));
            default:
                return MCData.createPointByteArray(timeStamp, (byte[]) iByteConverter.fromBytes(bytes, byte[].class));
        }
    }
    private MCData createAnnotation(long startTimestamp, long endTimestamp, MCSampleType sampleType, byte[] bytes) {
        switch (sampleType) {
            case BYTE_ARRAY:
                return MCData.createAnnotationByteArray(startTimestamp, endTimestamp, bytes);
            case BOOLEAN_ARRAY:
                return MCData.createAnnotationBooleanArray(startTimestamp, endTimestamp, (boolean[]) iByteConverter.fromBytes(bytes, boolean[].class));
            case INT_ARRAY:
                return MCData.createAnnotationIntArray(startTimestamp, endTimestamp, (int[]) iByteConverter.fromBytes(bytes, int[].class));
            case LONG_ARRAY:
                return MCData.createAnnotationLongArray(startTimestamp, endTimestamp, (long[]) iByteConverter.fromBytes(bytes, long[].class));
            case DOUBLE_ARRAY:
                return MCData.createAnnotationDoubleArray(startTimestamp, endTimestamp, (double[]) iByteConverter.fromBytes(bytes, double[].class));
            case STRING_ARRAY:
                return MCData.createAnnotationStringArray(startTimestamp, endTimestamp, (String[]) iByteConverter.fromBytes(bytes, String[].class));
            case ENUM:
                return MCData.createAnnotationIntArray(startTimestamp, endTimestamp, (int[]) iByteConverter.fromBytes(bytes, int[].class));
            case OBJECT:
                return MCData.createAnnotationObject(startTimestamp, endTimestamp, iByteConverter.fromBytes(bytes, String.class));
            default:
                return MCData.createAnnotationByteArray(startTimestamp, endTimestamp, (byte[]) iByteConverter.fromBytes(bytes, byte[].class));
        }
    }

}
