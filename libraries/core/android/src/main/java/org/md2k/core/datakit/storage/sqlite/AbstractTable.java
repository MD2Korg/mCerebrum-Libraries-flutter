package org.md2k.core.datakit.storage.sqlite;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import org.md2k.core.datakit.converter.IByteConverter;

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
abstract class AbstractTable {
    private String tableName;
    protected IByteConverter iByteConverter;

    AbstractTable(String tableName, IByteConverter iByteConverter){
        this.tableName = tableName;
        this.iByteConverter=iByteConverter;
    }
    boolean isExist(SQLiteDatabase db){
        boolean res = false;
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + tableName + "'", null);
        if(cursor!=null && cursor.getCount()>0){
            res=true;
        }
        if(cursor!=null)
            cursor.close();
        return res;
    }
    void createTable(SQLiteDatabase db, String sql){
        db.execSQL(sql);
    }
    void deleteData(SQLiteDatabase db){
        db.execSQL("delete from "+ tableName);
    }
    void deleteTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS "+tableName);
    }

    public String getTableName(){
        return tableName;
    }

    public long getSize(SQLiteDatabase db) {
        return DatabaseUtils.queryNumEntries(db, tableName);
    }
}
