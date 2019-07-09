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

package org.md2k.mcerebrumapi.datakitapi.datasource.metadata;

import org.md2k.mcerebrumapi.datakitapi.datasource.unit.MCUnit;

import java.util.HashMap;
import java.util.Map;

/**
 * This class defines the <code>DataSourceMetaData</code> object. This object provides a structure
 * for organizing the metadata related to the data source that collects a data point. This class
 * implements <code>Parcelable</code> so that the <code>DataSourceMetaData</code> objects can be
 * parceled with their data points. Metadata of note includes the application title, summary, description,
 * and data collection rate (denoted as data rate). These fields are stored in a hash map of strings.
 */
public class MCDataSourceMetaData {
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String UNIT = "unit";

    private HashMap<String, String> metaData;

    /**
     * Returns the name.
     *
     * @return The name.
     */
    public String getName() {
        return metaData.get(NAME);
    }

    /**
     * Returns the description.
     *
     * @return The description.
     */
    public String getDescription() {
        return metaData.get(DESCRIPTION);
    }

    /**
     * Returns the unit of measurement for the data.
     *
     * @return The unit of measurement.
     */
    public String getUnit() {
        return metaData.get(UNIT);
    }

    /**
     * Returns the metadata field for the given key.
     *
     * @param key Key of the the metadata to return. This key should be the name of the field in all
     *            capital letters, any spaces should be underscores.
     * @return The metadata field for the given key.
     */
    public String asHashMap(String key) {
        return metaData.get(key);
    }

    public HashMap<String, String> asHashMap() {
        return new HashMap<>(metaData);
    }

    /**
     * Constructor
     *
     * @param builder builder object defining how to construct the <code>DataSourceMetaData</code>.
     */
    private MCDataSourceMetaData(DataSourceMetaDataBuilder builder) {
        this.metaData = new HashMap<>(builder.metaData);
    }

    /**
     * Creates a new <code>builder</code> object to define an <code>DataSourceMetaData</code> object.
     *
     * @return A new <code>builder</code>.
     */
    public static DataSourceMetaDataBuilder builder() {
        return new DataSourceMetaDataBuilder();
    }

    /**
     * Embedded class that defines the <code>builder</code> for <code>DataSourceMetaData</code>.
     */
    public static class DataSourceMetaDataBuilder {
        private HashMap<String, String> metaData;

        /**
         * Constructor
         * This constructor initializes a new hash map.
         */
        public DataSourceMetaDataBuilder() {
            metaData = new HashMap<>();
        }

        /**
         * Constructor
         *
         * @param metaData Hash map of metadata to add to the <code>builder</code>.
         */
        DataSourceMetaDataBuilder(HashMap<String, String> metaData) {
            this.metaData = new HashMap<>();
            this.metaData.putAll(metaData);
        }

        /**
         * Sets the <code>NAME</code> key of the hash map.
         *
         * @param name Value to associate <code>NAME</code> to.
         * @return The modified <code>builder</code>.
         */
        public DataSourceMetaDataBuilder setName(String name) {
            if (name != null)
                this.metaData.put(NAME, name);
            return this;
        }


        /**
         * Sets the <code>DESCRIPTION</code> key of the hash map.
         *
         * @param description Value to associate <code>DESCRIPTION</code> to.
         * @return The modified <code>builder</code>.
         */
        public DataSourceMetaDataBuilder setDescription(String description) {
            if (description != null)
                this.metaData.put(DESCRIPTION, description);
            return this;
        }

        /**
         * Sets the <code>unit</code> field, which defines the unit of measure used for the data.
         *
         * @param unit Unit of measure used.
         * @return The modified <code>builder</code>.
         */
        public DataSourceMetaDataBuilder setUnit(MCUnit unit) {
            this.metaData.put(UNIT, unit.name());
            return this;
        }

        /**
         * Sets the <code>unit</code> field, which defines the unit of measure used for the data.
         *
         * @param unit Unit of measure used.
         * @return The modified <code>builder</code>.
         */
        public DataSourceMetaDataBuilder setUnit(String unit) {
            this.metaData.put(UNIT, unit);
            return this;
        }

        /**
         * Puts a custom key and value into the hash map.
         *
         * @param key   Key to add to the hash map.
         * @param value Value to add to the hash map.
         * @return The modified <code>builder</code>.
         */
        public DataSourceMetaDataBuilder setMetaData(String key, String value) {
            this.metaData.put(key, value);
            return this;
        }

        /**
         * Takes an existing hash map and merges it into the <code>builder</code>'s hash map.
         *
         * @param metaData Hash map to add to the <code>builder</code>.
         * @return The modified <code>builder</code>.
         */
        public DataSourceMetaDataBuilder setMetaData(HashMap<String, String> metaData) {
            for (HashMap.Entry<String, String> entry : metaData.entrySet())
                this.metaData.put(entry.getKey(), entry.getValue());
            return this;
        }

        /**
         * Passes the <code>builder</code> to the <code>DataSourceMetaData</code> constructor.
         *
         * @return The resulting <code>DataSourceMetaData</code> object.
         */
        public MCDataSourceMetaData build() {
            return new MCDataSourceMetaData(this);
        }
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
        if (toCompare instanceof MCDataSourceMetaData) {
            if (metaData.size() != ((MCDataSourceMetaData) toCompare).metaData.size()) return false;
            for (Map.Entry<String, String> entry : metaData.entrySet()) {
                if (!((MCDataSourceMetaData) toCompare).metaData.containsKey(entry.getKey()))
                    return false;
                if (!entry.getValue().equals(((MCDataSourceMetaData) toCompare).metaData.get(entry.getKey())))
                    return false;
            }
            return true;
        } else
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
        result = 31 * result + metaData.hashCode();
        return result;
    }

}
