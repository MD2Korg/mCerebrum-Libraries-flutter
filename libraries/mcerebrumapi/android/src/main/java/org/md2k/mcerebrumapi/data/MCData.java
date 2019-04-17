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
public class MCData extends MCValue implements Parcelable {
    private long startTimestamp;
    private long endTimestamp;
    private MCDataType dataType;

    /**
     * Constructor
     * This constructor to create data from the sample.
     *
     * @param dataType       The data type (POINT/ANNOTATION/...).
     * @param sampleType     The sample type (BOOLEAN_ARRAY/INTEGER_ARRAY/...).
     * @param startTimestamp The start timestamp for when the event was started.
     * @param endTimestamp   The end timestamp for when the event was ended.
     * @param sample         The data  sampled from the data source.
     */
    private MCData(MCDataType dataType, MCSampleType sampleType, long startTimestamp, long endTimestamp,Object sample) {
        super(sampleType, sample);
        this.dataType = dataType;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    /**
     * Returns the type of the data.
     *
     * @return The type of the data.
     */
    public MCDataType getDataType() {
        return dataType;
    }

    /**
     * Returns the value of the start timestamp.
     *
     * @return The the value of the start timestamp.
     */
    public long getStartTimestamp() {
        return startTimestamp;
    }

    /**
     * Returns the value of the end timestamp.
     *
     * @return The the value of the end timestamp.
     */
    public long getEndTimestamp() {
        return endTimestamp;
    }

    /**
     * Returns the value of the timestamp.
     *
     * @return The the value of the timestamp.
     */
    public long getTimestamp() {
        return startTimestamp;
    }


    /**
     * Creates a new <code>Data</code> object with the fields of the calling object.
     *
     * @return A new <code>Data</code>.
     */
    public MCData clone() {
        //todo:
        return new MCData(dataType, sampleType, startTimestamp, endTimestamp, sample);
    }

    /**
     * Compares the passed object to the calling object.
     * If the passed object is not an instance of this class, false is returned.
     *
     * @param toCompare Object to compare.
     * @return True if the objects are equivalent and false if they are not.
     */
    @Override
    public boolean equals(Object toCompare) {
        if (!equalsIgnoreTimestamp(toCompare)) return false;
        return (this.getDataType().equals(((MCData) toCompare).getDataType())
                && this.getStartTimestamp() == ((MCData) toCompare).getStartTimestamp()
                && this.getEndTimestamp() == ((MCData) toCompare).getEndTimestamp()
        );
    }

    public boolean equalsIgnoreTimestamp(Object toCompare) {
        if (!(toCompare instanceof MCData) || !this.getSampleType().equals(((MCData) toCompare).getSampleType()))
            return false;
        switch (sampleType) {
            case BOOLEAN_ARRAY:
                return Arrays.equals((boolean[]) sample, (boolean[]) (((MCData) toCompare).getSample()));
            case BYTE_ARRAY:
                return Arrays.equals((byte[]) sample, (byte[]) (((MCData) toCompare).getSample()));
            case INT_ARRAY:
                return Arrays.equals((int[]) sample, (int[]) (((MCData) toCompare).getSample()));
            case LONG_ARRAY:
                return Arrays.equals((long[]) sample, (long[]) (((MCData) toCompare).getSample()));
            case DOUBLE_ARRAY:
                return Arrays.equals((double[]) sample, (double[]) (((MCData) toCompare).getSample()));
            case STRING_ARRAY:
                return Arrays.equals((String[]) sample, (String[]) (((MCData) toCompare).getSample()));
            case OBJECT:
                return Arrays.equals((String[]) sample, (String[]) (((MCData) toCompare).getSample()));
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
        result = 31 * result + dataType.hashCode();
        result = 31 * result + sampleType.hashCode();
        result = 31 * result + (int) (startTimestamp ^ (startTimestamp >>> 32));
        result = 31 * result + (int) (endTimestamp ^ (endTimestamp >>> 32));
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
     * Creates a data point where the sample type is boolean array.
     *
     * @param timestamp The timestamp for when the data was collected.
     * @param sample    The sample that was collected.
     */
    public static MCData createPointBooleanArray(long timestamp, boolean sample) {
        return new MCData(MCDataType.POINT, MCSampleType.BOOLEAN_ARRAY, timestamp, timestamp, new boolean[]{sample});
    }

    /**
     * Creates a data point where the sample type is boolean array.
     *
     * @param timestamp The timestamp for when the data was collected.
     * @param sample    The sample that was collected.
     */
    public static MCData createPointBooleanArray(long timestamp, boolean[] sample) {
        return new MCData(MCDataType.POINT, MCSampleType.BOOLEAN_ARRAY, timestamp, timestamp, sample);
    }

    /**
     * Creates a data point where the sample type is byte array.
     *
     * @param timestamp The timestamp for when the data was collected.
     * @param sample    The sample that was collected.
     */
    public static MCData createPointByteArray(long timestamp, byte sample) {
        return new MCData(MCDataType.POINT, MCSampleType.BYTE_ARRAY, timestamp, timestamp, new byte[]{sample});
    }

    /**
     * Creates a data point where the sample type is byte array.
     *
     * @param timestamp The timestamp for when the data was collected.
     * @param sample    The sample that was collected.
     */
    public static MCData createPointByteArray(long timestamp, byte[] sample) {
        return new MCData(MCDataType.POINT, MCSampleType.BYTE_ARRAY, timestamp, timestamp, sample);
    }

    /**
     * Creates a data point where the sample type is double array.
     *
     * @param timestamp The timestamp for when the data was collected.
     * @param sample    The sample that was collected.
     */
    public static MCData createPointDoubleArray(long timestamp, double sample) {
        return new MCData(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY, timestamp, timestamp, new double[]{sample});
    }

    /**
     * Creates a data point where the sample type is double array.
     *
     * @param timestamp The timestamp for when the data was collected.
     * @param sample    The sample that was collected.
     */
    public static MCData createPointDoubleArray(long timestamp, double[] sample) {
        return new MCData(MCDataType.POINT, MCSampleType.DOUBLE_ARRAY, timestamp, timestamp, sample);
    }


    /**
     * Creates a data point where the sample type is integer array.
     *
     * @param timestamp The timestamp for when the data was collected.
     * @param sample    The sample that was collected.
     */
    public static MCData createPointIntArray(long timestamp, int sample) {
        return new MCData(MCDataType.POINT, MCSampleType.INT_ARRAY, timestamp, timestamp, new int[]{sample});
    }

    /**
     * Creates a data point where the sample type is integer array.
     *
     * @param timestamp The timestamp for when the data was collected.
     * @param sample    The sample that was collected.
     */
    public static MCData createPointIntArray(long timestamp, int[] sample) {
        return new MCData(MCDataType.POINT, MCSampleType.INT_ARRAY, timestamp, timestamp, sample);
    }

    /**
     * Creates a data point where the sample type is object.
     *
     * @param timestamp The timestamp for when the data was collected.
     * @param sample    The sample that was collected.
     */
    public static <T> MCData createPointObject(long timestamp, T sample) {
        Gson gson = new Gson();
        String[] str = new String[]{gson.toJson(sample)};
        return new MCData(MCDataType.POINT, MCSampleType.OBJECT, timestamp, timestamp, str);
    }

    /**
     * Creates a data point where the sample type is long array.
     *
     * @param timestamp The timestamp for when the data was collected.
     * @param sample    The sample that was collected.
     */
    public static MCData createPointLongArray(long timestamp, long[] sample) {
        return new MCData(MCDataType.POINT, MCSampleType.LONG_ARRAY, timestamp, timestamp, sample);
    }
    /**
     * Creates a data point where the sample type is long array.
     *
     * @param timestamp The timestamp for when the data was collected.
     * @param sample    The sample that was collected.
     */
    public static MCData createPointLongArray(long timestamp, long sample) {
        return new MCData(MCDataType.POINT, MCSampleType.LONG_ARRAY, timestamp, timestamp, new long[]{sample});
    }


    /**
     * Creates a data point where the sample type is string array.
     *
     * @param timestamp The timestamp for when the data was collected.
     * @param sample    The sample that was collected.
     */
    public static MCData createPointStringArray(long timestamp, String sample) {
        return new MCData(MCDataType.POINT, MCSampleType.STRING_ARRAY, timestamp, timestamp, new String[]{sample});
    }

    /**
     * Creates a data point where the sample type is string array.
     *
     * @param timestamp The timestamp for when the data was collected.
     * @param sample    The sample that was collected.
     */
    public static MCData createPointStringArray(long timestamp, String[] sample) {
        return new MCData(MCDataType.POINT, MCSampleType.STRING_ARRAY, timestamp, timestamp, sample);
    }

    /**
     * Creates a data annotation where the sample type is boolean array.
     *
     * @param startTimestamp The timestamp of the beginning of the data collection.
     * @param endTimestamp   The timestamp of the end of the data collection.
     * @param sample         The sample that was collected.
     */
    public static MCData createAnnotationByteArray(long startTimestamp, long endTimestamp, byte[] sample) {
        return new MCData(MCDataType.ANNOTATION, MCSampleType.BYTE_ARRAY, startTimestamp, endTimestamp, sample);
    }

    /**
     * Creates a data annotation where the sample type is boolean array.
     *
     * @param startTimestamp The timestamp of the beginning of the data collection.
     * @param endTimestamp   The timestamp of the end of the data collection.
     * @param sample         The sample that was collected.
     */
    public static MCData createAnnotationByteArray(long startTimestamp, long endTimestamp, byte sample) {
        return new MCData(MCDataType.ANNOTATION, MCSampleType.BYTE_ARRAY, startTimestamp, endTimestamp, new byte[]{sample});
    }

    /**
     * Creates a data annotation where the sample type is boolean array.
     *
     * @param startTimestamp The timestamp of the beginning of the data collection.
     * @param endTimestamp   The timestamp of the end of the data collection.
     * @param sample         The sample that was collected.
     */
    public static MCData createAnnotationBooleanArray(long startTimestamp, long endTimestamp, boolean[] sample) {
        return new MCData(MCDataType.ANNOTATION, MCSampleType.BOOLEAN_ARRAY, startTimestamp, endTimestamp, sample);
    }

    /**
     * Creates a data annotation where the sample type is boolean array.
     *
     * @param startTimestamp The timestamp of the beginning of the data collection.
     * @param endTimestamp   The timestamp of the end of the data collection.
     * @param sample         The sample that was collected.
     */
    public static MCData createAnnotationBooleanArray(long startTimestamp, long endTimestamp, boolean sample) {
        return new MCData(MCDataType.ANNOTATION, MCSampleType.BOOLEAN_ARRAY, startTimestamp, endTimestamp, new boolean[]{sample});
    }

    /**
     * Creates a data annotation where the sample type is double array.
     *
     * @param startTimestamp The timestamp of the beginning of the data collection.
     * @param endTimestamp   The timestamp of the end of the data collection.
     * @param sample         The sample that was collected.
     */
    public static MCData createAnnotationDoubleArray(long startTimestamp, long endTimestamp, double sample) {
        return new MCData(MCDataType.ANNOTATION, MCSampleType.DOUBLE_ARRAY, startTimestamp, endTimestamp, new double[]{sample});
    }

    /**
     * Creates a data annotation where the sample type is double array.
     *
     * @param startTimestamp The timestamp of the beginning of the data collection.
     * @param endTimestamp   The timestamp of the end of the data collection.
     * @param sample         The sample that was collected.
     */
    public static MCData createAnnotationDoubleArray(long startTimestamp, long endTimestamp, double[] sample) {
        return new MCData(MCDataType.ANNOTATION, MCSampleType.DOUBLE_ARRAY, startTimestamp, endTimestamp, sample);
    }

    /**
     * Creates a data annotation where the sample type is integer array.
     *
     * @param startTimestamp The timestamp of the beginning of the data collection.
     * @param endTimestamp   The timestamp of the end of the data collection.
     * @param sample         The sample that was collected.
     */
    public static MCData createAnnotationIntArray(long startTimestamp, long endTimestamp, int sample) {
        return new MCData(MCDataType.ANNOTATION, MCSampleType.INT_ARRAY, startTimestamp, endTimestamp, new int[]{sample});
    }

    /**
     * Creates a data annotation where the sample type is integer array.
     *
     * @param startTimestamp The timestamp of the beginning of the data collection.
     * @param endTimestamp   The timestamp of the end of the data collection.
     * @param sample         The sample that was collected.
     */
    public static MCData createAnnotationIntArray(long startTimestamp, long endTimestamp, int[] sample) {
        return new MCData(MCDataType.ANNOTATION, MCSampleType.INT_ARRAY, startTimestamp, endTimestamp, sample);
    }

    /**
     * Creates a data annotation where the sample type is long array.
     *
     * @param startTimestamp The timestamp of the beginning of the data collection.
     * @param endTimestamp   The timestamp of the end of the data collection.
     * @param sample         The sample that was collected.
     */
    public static MCData createAnnotationLongArray(long startTimestamp, long endTimestamp, long sample) {
        return new MCData(MCDataType.ANNOTATION, MCSampleType.LONG_ARRAY, startTimestamp, endTimestamp, new long[]{sample});
    }
    /**
     * Creates a data annotation where the sample type is long array.
     *
     * @param startTimestamp The timestamp of the beginning of the data collection.
     * @param endTimestamp   The timestamp of the end of the data collection.
     * @param sample         The sample that was collected.
     */
    public static MCData createAnnotationLongArray(long startTimestamp, long endTimestamp, long[] sample) {
        return new MCData(MCDataType.ANNOTATION, MCSampleType.LONG_ARRAY, startTimestamp, endTimestamp, sample);
    }

    /**
     * Creates a data annotation where the sample type is object.
     *
     * @param startTimestamp The timestamp of the beginning of the data collection.
     * @param endTimestamp   The timestamp of the end of the data collection.
     * @param sample         The sample that was collected.
     */
    public static <T> MCData createAnnotationObject(long startTimestamp, long endTimestamp, T sample) {
        Gson gson = new Gson();
        String str = gson.toJson(sample);
        return new MCData(MCDataType.ANNOTATION, MCSampleType.OBJECT, startTimestamp, endTimestamp, str);
    }

    /**
     * Creates a data annotation where the sample type is string array.
     *
     * @param startTimestamp The timestamp of the beginning of the data collection.
     * @param endTimestamp   The timestamp of the end of the data collection.
     * @param sample         The sample that was collected.
     */
    public static MCData createAnnotationStringArray(long startTimestamp, long endTimestamp, String sample) {
        return new MCData(MCDataType.ANNOTATION, MCSampleType.STRING_ARRAY, startTimestamp, endTimestamp, new String[]{sample});
    }

    /**
     * Creates a data annotation where the sample type is string array.
     *
     * @param startTimestamp The timestamp of the beginning of the data collection.
     * @param endTimestamp   The timestamp of the end of the data collection.
     * @param sample         The sample that was collected.
     */
    public static MCData createAnnotationStringArray(long startTimestamp, long endTimestamp, String[] sample) {
        return new MCData(MCDataType.ANNOTATION, MCSampleType.STRING_ARRAY, startTimestamp, endTimestamp, sample);
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
        super.writeToParcel(dest, flags);
        dest.writeLong(startTimestamp);
        dest.writeLong(endTimestamp);
        dest.writeInt(dataType.getValue());
    }
    /**
     * Constructor
     * This constructor creates an <code>Data</code> object from a <code>Parcel</code>.
     *
     * @param in Parceled <code>Data</code> object.
     */
    protected MCData(Parcel in) {
        super(in);
        startTimestamp = in.readLong();
        endTimestamp = in.readLong();
        dataType = MCDataType.getDataType(in.readInt());
    }

}
