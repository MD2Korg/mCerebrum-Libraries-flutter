package org.md2k.mcerebrumapi.datakitapi.ipc.query_data_count;

import android.os.Bundle;

import org.md2k.mcerebrumapi.datakitapi.ipc.OperationType;
import org.md2k.mcerebrumapi.datakitapi.ipc._Session;
import org.md2k.mcerebrumapi.status.MCStatus;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source value must retain the above copyright notice, this
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
public class _QueryDataCountIn {
    private static final String DS_ID = "ds_id";
    private static final String START_TIMESTAMP = "start_timestamp";
    private static final String END_TIMESTAMP = "end_timestamp";

    public static _Session create(int session, int dsId, long startTimestamp, long endTimestamp) {
        Bundle b = new Bundle();
        b.putInt(DS_ID, dsId);
        b.putLong(START_TIMESTAMP, startTimestamp);
        b.putLong(END_TIMESTAMP, endTimestamp);
        return new _Session(session, OperationType.QUERY_DATA_COUNT, MCStatus.SUCCESS, b);
    }

    public static long startTimestamp(Bundle b) {
        if (b == null) return 0;
        return b.getLong(START_TIMESTAMP);
    }

    public static long endTimestamp(Bundle b) {
        if (b == null) return 0;
        return b.getLong(END_TIMESTAMP);
    }

    public static int getDsId(Bundle b) {
        if (b == null) return 0;
        return b.getInt(DS_ID);
    }
}
