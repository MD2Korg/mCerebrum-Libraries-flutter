package org.md2k.mcerebrumapi.datakitapi.ipc;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.md2k.mcerebrumapi.status.MCStatus;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center

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
public class _Session implements Parcelable {

    private int sessionId;
    private int operationType;
    private MCStatus status;
    private Bundle bundle;

    public _Session(int sessionId, int operationType, MCStatus status) {

        this.sessionId = sessionId;
        this.operationType = operationType;
        this.status = status;
        this.bundle = null;
    }

    public _Session(int sessionId, int operationType, MCStatus status, Bundle bundle) {

        this.sessionId = sessionId;
        this.operationType = operationType;
        this.status = status;
        this.bundle = bundle;
    }

    public int getSessionId() {
        return sessionId;
    }


    public MCStatus getStatus() {
        return status;
    }

    public int getOperationType() {
        return operationType;
    }

    protected _Session(Parcel in) {
        sessionId = in.readInt();
        this.operationType = in.readInt();
        status = MCStatus.valueOf(in.readInt());
        bundle = in.readBundle(getClass().getClassLoader());
    }

    public Bundle getBundle() {
        return bundle;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sessionId);
        dest.writeInt(operationType);
        dest.writeInt(status.getId());
        dest.writeBundle(bundle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<_Session> CREATOR = new Creator<_Session>() {
        @Override
        public _Session createFromParcel(Parcel in) {
            return new _Session(in);
        }

        @Override
        public _Session[] newArray(int size) {
            return new _Session[size];
        }
    };
}
