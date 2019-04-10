package org.md2k.mcerebrumapi.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
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
public class DataArray implements Parcelable {
    private ArrayList<MCData> data;


    /**
     * Constructor
     * This constructor creates a <code>DataArray</code> object from a <code>Parcel</code>.
     *
     * @param in Parceled <code>DataArray</code> object.
     */
    protected DataArray(Parcel in) {
        data = in.readArrayList(MCData.class.getClassLoader());
    }

    /**
     * Embedded <code>CREATOR</code> class for generating instances of <code>DataArray</code>
     * from a <code>Parcel</code>.
     */
    public static final Creator<DataArray> CREATOR = new Creator<DataArray>() {
        /**
         * Creates an <code>DataArray</code> object from a <code>Parcel</code>.
         *
         * @param in <code>Parcel</code> containing the <code>Data</code>.
         * @return The constructed <code>DataArray</code>.
         */
        @Override
        public DataArray createFromParcel(Parcel in) {
            return new DataArray(in);
        }

        /**
         * Creates an array for <code>Data</code> of the given size.
         *
         * @param size Size of the array to create.
         * @return Returns an array for <code>DataArray</code> objects.
         */
        @Override
        public DataArray[] newArray(int size) {
            return new DataArray[size];
        }
    };

    /**
     * Constructor
     * This constructor initialize the DataArray
     */
    public DataArray() {
        this.data = new ArrayList<>();
    }

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
     * Writes the calling <code>DataArray</code> to the passed <code>Parcel</code>.
     *
     * @param dest <code>Parcel</code> to write to.
     * @param i    This should always be the value returned from <code>describeContents()</code>.
     */
    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeList(data);
    }

    /**
     * adds the <code>Data</code> to the <code>DataArray</code>.
     *
     * @param data Array of Data.
     */
    public void add(MCData[] data) {
        ArrayList<MCData> temp = new ArrayList<MCData>(Arrays.asList(data));
        this.data.addAll(temp);
    }

    public void add(ArrayList<MCData> data) {
        this.data.addAll(data);
    }

    public void add(MCData data) {
        this.data.add(data);
    }

    /**
     * Returns an array of <code>Data</code> objects that the caller contains.
     *
     * @return An array of <code>Data</code> objects.
     */
    public ArrayList<MCData> get() {
        return data;
    }

    /**
     * Returns <code>Data</code> object that the caller contains.
     *
     * @return <code>Data</code> objects.
     */
    public MCData get(int index) {
        return data.get(index);
    }

    /**
     * Returns the <code>size</code>.
     *
     * @return The <code>size</code>.
     */
    public int size() {
        return data.size();
    }

    /**
     * Compares the passed object to the calling object.
     * If the passed object is not an instance of this class, false is returned.
     * Since a <code>DataArray</code> may contain various types of <code>Data</code> objects,
     * this method iterates through the array of data and checks the <code>instanceof</code> the
     * objects for comparison.
     *
     * @param toCompare Object to compare.
     * @return True if the objects are equivalent and false if they are not.
     */
    @Override
    public boolean equals(Object toCompare) {
        if (toCompare instanceof DataArray && ((DataArray) toCompare).data.size() == data.size()) {
            for (int i = 0; i < this.data.size(); i++) {
                if (!this.data.get(i).equals(((DataArray) toCompare).data.get(i)))
                    return false;
            }
        }
        return false;
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
        for (MCData dataPoint : data)
            if (dataPoint != null)
                result = 31 * result + dataPoint.hashCode();
        return result;
    }

}
