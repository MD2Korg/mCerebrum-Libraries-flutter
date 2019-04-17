package org.md2k.mcerebrumapi.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.Arrays;

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
public class MCValue implements Parcelable {
    protected MCSampleType sampleType;
    protected Object sample;
    
    /**
     * Constructor
     * This constructor to create data from the sample.
     *
     * @param sampleType     The sample type (BOOLEAN_ARRAY/INTEGER_ARRAY/...).
     * @param sample         The data  sampled from the data source.
     */
    protected MCValue(MCSampleType sampleType, Object sample) {
        this.sampleType = sampleType;
        this.sample = sample;
    }

    /**
     * Returns the type of the sample.
     *
     * @return The type of the sample.
     */
    public MCSampleType getSampleType() {
        return sampleType;
    }

    /**
     * Returns the sample (converted to assigned type).
     *
     * @return The sample  (converted to assigned type).
     */
    public <T extends Object> T getSample() {
        return (T) sample;
    }

    /**
     * Creates a data point where the sample type is object.
     *
     * @param sample    The sample that was collected.
     */
    public static <T> MCValue create(T sample) {
        Gson gson = new Gson();
        String[] str = new String[]{gson.toJson(sample)};
        return new MCValue(MCSampleType.OBJECT, str);
    }
    public MCValue clone() {
        //todo:
        return new MCValue(sampleType, sample);
    }

    public boolean equals(Object toCompare) {
        if (!(toCompare instanceof MCValue) || !this.getSampleType().equals(((MCValue) toCompare).getSampleType()))
            return false;
        switch (sampleType) {
            case BOOLEAN_ARRAY:
                return Arrays.equals((boolean[]) sample, (boolean[]) (((MCValue) toCompare).getSample()));
            case BYTE_ARRAY:
                return Arrays.equals((byte[]) sample, (byte[]) (((MCValue) toCompare).getSample()));
            case INT_ARRAY:
                return Arrays.equals((int[]) sample, (int[]) (((MCValue) toCompare).getSample()));
            case LONG_ARRAY:
                return Arrays.equals((long[]) sample, (long[]) (((MCValue) toCompare).getSample()));
            case DOUBLE_ARRAY:
                return Arrays.equals((double[]) sample, (double[]) (((MCValue) toCompare).getSample()));
            case STRING_ARRAY:
                return Arrays.equals((String[]) sample, (String[]) (((MCValue) toCompare).getSample()));
            case OBJECT:
                return Arrays.equals((String[]) sample, (String[]) (((MCValue) toCompare).getSample()));
            default:
                return false;
        }
    }

    /**
     * Calculates and returns a hash code for the calling object.
     * The hash code is calculated using the method denoted in "Effective Java" and described in this Medium
     * <a href="https://medium.com/codelog/overriding-hashcode-method-effective-java-notes-723c1fedf51c">post</a>.
     *
     * @return The hash code of the calling object.
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + sampleType.hashCode();
        switch (sampleType) {
            case INT_ARRAY:
                result = 31 * result + Arrays.hashCode((int[]) sample);
                break;
            case LONG_ARRAY:
                result = 31 * result + Arrays.hashCode((long[]) sample);
                break;
            case DOUBLE_ARRAY:
                result = 31 * result + Arrays.hashCode((double[]) sample);
                break;
            case BYTE_ARRAY:
                result = 31 * result + Arrays.hashCode((byte[]) sample);
                break;
            case BOOLEAN_ARRAY:
                result = 31 * result + Arrays.hashCode((boolean[]) sample);
                break;
            case STRING_ARRAY:
                result = 31 * result + Arrays.hashCode((String[]) sample);
                break;
            case OBJECT:
                result = 31 * result + sample.hashCode();
                break;
            default:
                break;
        }
//        result = 31 * result + sample.hashCode();
        return result;
    }

    /**
     * Constructor
     * This constructor creates an <code>Data</code> object from a <code>Parcel</code>.
     *
     * @param in Parceled <code>Data</code> object.
     */
    protected MCValue(Parcel in) {
        sampleType = MCSampleType.getSampleType(in.readInt());
        switch (sampleType) {
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
            case LONG_ARRAY:
                sample = in.createLongArray();
                break;
            case STRING_ARRAY:
                sample = in.createStringArray();
                break;
            default:
                break;
        }
    }

    /**
     * Embedded <code>CREATOR</code> class for generating instances of <code>Data</code>
     * from a <code>Parcel</code>.
     */
    public static final Creator<MCValue> CREATOR = new Creator<MCValue>() {

        /**
         * Creates an <code>Data</code> object from a <code>Parcel</code>.
         *
         * @param in <code>Parcel</code> containing the <code>Data</code>.
         * @return The constructed <code>Data</code>.
         */
        @Override
        public MCValue createFromParcel(Parcel in) {
            return new MCValue(in);
        }

        /**
         * Creates an array for <code>Data</code> of the given size.
         *
         * @param size Size of the array to create.
         * @return Returns an array for <code>Data</code> objects.
         */
        @Override
        public MCValue[] newArray(int size) {
            return new MCValue[size];
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
        dest.writeInt(sampleType.getValue());
        switch (sampleType) {
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
            default:
                break;
        }
    }


}
