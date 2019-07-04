package org.md2k.mcerebrumapi.datakitapi.ipc.query_data_summary;

import android.os.Parcel;
import android.os.Parcelable;

import org.md2k.mcerebrumapi.datakitapi.ipc._Session;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center

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
public class _QueryDataSummaryIn extends _Session {
    private int dsId;
    private long startTimestamp;
    private long endTimestamp;

/*
    public _QueryDataSummaryIn(int session, int dsId, long startTimestamp, long endTimestamp) {
        super(session, OperationType.QUERY_DATA_SUMMARY, Status.SUCCESS);
        this.dsId = dsId;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }
*/

    public int getDsId() {
        return dsId;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public static final Parcelable.Creator<_QueryDataSummaryIn> CREATOR = new Parcelable.Creator<_QueryDataSummaryIn>() {
        @Override
        public _QueryDataSummaryIn createFromParcel(Parcel in) {
            return new _QueryDataSummaryIn(in);
        }

        @Override
        public _QueryDataSummaryIn[] newArray(int size) {
            return new _QueryDataSummaryIn[size];
        }
    };

    public _QueryDataSummaryIn(Parcel in) {
        super(in);
        dsId = in.readInt();
        startTimestamp = in.readLong();
        endTimestamp = in.readLong();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(dsId);
        parcel.writeLong(startTimestamp);
        parcel.writeLong(endTimestamp);
    }
}
