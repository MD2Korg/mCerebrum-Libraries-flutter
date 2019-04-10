package org.md2k.mcerebrumapi.datakitapi.ipc.query_data_summary;

import android.os.Parcel;
import android.os.Parcelable;

import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.ipc._Session;

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
public class _QueryDataSummaryOut extends _Session {
    private MCData data;

    protected _QueryDataSummaryOut(Parcel in) {
        super(in);
        data = in.readParcelable(MCData.class.getClassLoader());
    }

/*
    public _QueryDataSummaryOut(int session, int status, Data data) {
        super(session, OperationType.QUERY_DATA_SUMMARY, status);
        this.data = data;
    }
*/

    public static final Parcelable.Creator<_QueryDataSummaryOut> CREATOR = new Parcelable.Creator<_QueryDataSummaryOut>() {
        @Override
        public _QueryDataSummaryOut createFromParcel(Parcel in) {
            return new _QueryDataSummaryOut(in);
        }

        @Override
        public _QueryDataSummaryOut[] newArray(int size) {
            return new _QueryDataSummaryOut[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeParcelable(data, i);
    }

    public MCData getData() {
        return data;
    }
}
