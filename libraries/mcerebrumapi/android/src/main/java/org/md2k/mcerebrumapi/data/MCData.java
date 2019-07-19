package org.md2k.mcerebrumapi.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


import com.google.gson.Gson;

import org.md2k.mcerebrumapi.datakitapi.ipc.insert_datasource.MCRegistration;

import java.util.Map;

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
public class MCData implements Parcelable {
    private int dsId;
    private MCDataType dataType;
    private long timestamp;
    private Object sample;
    private boolean ifNew;

    /**
     * Constructor
     * This constructor to create data from the sample.
     *
     * @param dsId           The registration id .
     * @param dataType       The sample type (BOOLEAN_ARRAY/INTEGER_ARRAY/...).
     * @param timestamp The start timestamp for when the event was started.
     * @param sample         The data  sampled from the data source.
     */
    public MCData(int dsId, MCDataType dataType, long timestamp,Object sample, boolean ifNew) {
        Gson gson=new Gson();
        this.dataType = dataType;
        this.dsId = dsId;
        this.timestamp = timestamp;
        if (dataType == MCDataType.OBJECT || dataType == MCDataType.ANNOTATION) {
            this.sample = gson.toJson(sample);
        } else
            this.sample = sample;
        this.ifNew = ifNew;
    }

    public int getDsId() {
        return dsId;
    }

    public MCDataType getDataType() {
        return dataType;
    }

    public boolean isIfNew() {
        return ifNew;
    }

    public <T> T getSample(Class<T> t) {
        Gson gson = new Gson();
        return gson.fromJson((String) sample, t);
    }

    public <T> T getSample() {
        if (dataType == MCDataType.OBJECT || dataType == MCDataType.ANNOTATION) {
            Gson gson = new Gson();
            Map o= gson.fromJson((String)sample, Map.class);
            return (T) o;
        } else {
            return (T) sample;
        }
    }


    /**
     * Returns the value of the start timestamp.
     *
     * @return The the value of the start timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }


    public static <T> MCData create(MCRegistration registration, long timestamp, T sample) {
        checkDataType(sample, registration.getDataSource().getDataType(), registration.getDataSource().getDataDescriptors().size(), registration.getDataSource().toUUID());
        return new MCData(registration.getDsId(), registration.getDataSource().getDataType(), timestamp, sample, false);
    }

    private static void checkDataType(Object sample, MCDataType dataType, int length, String source) {
        if (sample instanceof boolean[] && ((boolean[]) sample).length == length && dataType == MCDataType.BOOLEAN_ARRAY)
            return;
        if (sample instanceof byte[] && ((byte[]) sample).length == length && dataType == MCDataType.BYTE_ARRAY)
            return;
        if (sample instanceof int[] && ((int[]) sample).length == length && dataType == MCDataType.INT_ARRAY)
            return;
        if (sample instanceof long[] && ((long[]) sample).length == length && dataType == MCDataType.LONG_ARRAY)
            return;
        if (sample instanceof double[] && ((double[]) sample).length == length && dataType == MCDataType.DOUBLE_ARRAY)
            return;
        if (sample instanceof String[] && ((String[]) sample).length == length && dataType == MCDataType.STRING_ARRAY)
            return;
        if(dataType ==MCDataType.ANNOTATION && sample instanceof MCAnnotation){
            return;
        }
        if (dataType == MCDataType.OBJECT) return;
        Log.e("mcerebrumapi","check datatype error  datatype = "+dataType.name()+" source = "+source);
        throw new IllegalArgumentException();
    }

    public static <T> MCData createIfNew(MCRegistration registration, long timestamp, T sample) {
        return new MCData(registration.getDsId(), registration.getDataSource().getDataType(), timestamp, sample, true);
    }

    /**
     * Embedded <code>CREATOR</code> class for generating instances of <code>Data</code>
     * from a <code>Parcel</code>.
     */
    public static final Creator<MCData> CREATOR = new Creator<MCData>() {

        /**
         * Creates an <code>Data</code> object from a <code>Parcel</code>.
         *
         * @param in <code>Parcel</code> containing the <code>Data</code>.
         * @return The constructed <code>Data</code>.
         */
        @Override
        public MCData createFromParcel(Parcel in) {
            return new MCData(in);
        }

        /**
         * Creates an array for <code>Data</code> of the given size.
         *
         * @param size Size of the array to create.
         * @return Returns an array for <code>Data</code> objects.
         */
        @Override
        public MCData[] newArray(int size) {
            return new MCData[size];
        }
    };

    /**
     * Always returns 0 because this parcel doesn't contain any special objects.
     * From <a href = https://developer.android.com/reference/android/os/Parcelable>Google's Android documentation</a>:
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     * For example, if the object will include a file descriptor in the output of
     * writeToParcel(Parcel, int), the return value of this method must include the CONTENTS_FILE_DESCRIPTOR bit.
     *
     * @return 0.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the calling <code>DataPointBoolean</code> to the passed <code>Parcel</code>.
     *
     * @param dest  <code>Parcel</code> to write to.
     * @param flags This should always be the value returned from <code>describeContents()</code>.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dsId);
        dest.writeInt(dataType.getValue());
        dest.writeInt(dataType.getValue());
        dest.writeLong(timestamp);
        switch (dataType) {
            case BYTE_ARRAY:
                dest.writeByteArray((byte[]) sample);
                break;
            case INT_ARRAY:
                dest.writeIntArray((int[]) sample);
                break;
            case BOOLEAN_ARRAY:
                boolean[] res = (boolean[]) sample;
                dest.writeBooleanArray(res);
                break;
            case LONG_ARRAY:
                dest.writeLongArray((long[]) sample);
                break;
            case DOUBLE_ARRAY:
                dest.writeDoubleArray((double[]) sample);
                break;
            case STRING_ARRAY:
                dest.writeStringArray((String[]) sample);
                break;
            case OBJECT:
                dest.writeStringArray((String[]) sample);
                break;
            case ANNOTATION:
                dest.writeStringArray((String[]) sample);
                break;
            default:
                break;
        }
        dest.writeByte(ifNew ? (byte) 1 : (byte) 0);
    }

    /**
     * Constructor
     * This constructor creates an <code>Data</code> object from a <code>Parcel</code>.
     *
     * @param in Parceled <code>Data</code> object.
     */
    protected MCData(Parcel in) {
        dsId = in.readInt();
        dataType = MCDataType.getDataType(in.readInt());
        timestamp = in.readLong();
        switch (dataType) {
            case BOOLEAN_ARRAY:
                sample = in.createBooleanArray();
                break;
            case BYTE_ARRAY:
                sample = in.createByteArray();
                break;
            case DOUBLE_ARRAY:
                sample = in.createDoubleArray();
                break;
            case INT_ARRAY:
                sample = in.createIntArray();
                break;
            case OBJECT:
                sample = in.createStringArray();
                break;
            case ANNOTATION:
                sample = in.createStringArray();
                break;
            case LONG_ARRAY:
                sample = in.createLongArray();
                break;
            case STRING_ARRAY:
                sample = in.createStringArray();
                break;
            default:
                break;
        }
        ifNew = in.readByte() == 1;
    }

}
