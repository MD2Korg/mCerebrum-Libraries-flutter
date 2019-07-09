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
 * This class defines the <code>DataDescriptor</code> object. The <code>DataDescriptor</code>
 * provides metadata about what the expected values of a datapoint are. This class
 * implements <code>Parcelable</code> so that the <code>DataDescriptor</code> objects can be
 * parceled with their data points. Metadata of note includes the application title, summary, and
 * description. These fields are stored in a hash map of strings. Other metadata fields, such as
 * minimum value, maximum value, unit, and arrays of possible values as strings and integers are not
 * stored in the hash map.
 */
public class MCDataDescriptor {
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String MIN_VALUE = "min_value";
    public static final String MAX_VALUE = "max_value";
    public static final String UNIT = "unit";

    private HashMap<String, String> descriptor;


    /**
     * Returns the name.
     *
     * @return The name.
     */
    public String getName() {
        return descriptor.get(NAME);
    }


    /**
     * Returns the description.
     *
     * @return The description.
     */
    public String getDescription() {
        return descriptor.get(DESCRIPTION);
    }


    /**
     * Returns the minimum allowable value for the data.
     *
     * @return The minimum value.
     */
    public double getMinValue() {
        if (descriptor.get(MIN_VALUE) == null) return Double.MIN_VALUE;
        else return Double.valueOf(descriptor.get(MIN_VALUE));
    }


    /**
     * Returns the maximum allowable value for the data.
     *
     * @return The maximum value.
     */
    public double getMaxValue() {
        if (descriptor.get(MAX_VALUE) == null) return Double.MAX_VALUE;
        else return Double.valueOf(descriptor.get(MAX_VALUE));
    }


    /**
     * Returns the unit of measurement for the data.
     *
     * @return The unit of measurement.
     */
    public String getUnit() {
        return descriptor.get(UNIT);
    }

    /**
     * Returns the value of a custom key.
     *
     * @param key The key to get the value of.
     * @return The value for the given key.
     */
    public String asHashMap(String key) {
        if (descriptor == null) return null;
        return descriptor.get(key);
    }

    /**
     * Returns the data descriptors as a hash map.
     *
     * @return The hash map of all data descriptors.
     */
    public HashMap<String, String> asHashMap() {
        return new HashMap<>(descriptor);
    }

    /**
     * Constructor
     *
     * @param builder builder object defining how to construct the <code>DataDescriptor</code>.
     */
    private MCDataDescriptor(Builder builder) {
        descriptor = builder.descriptor;
    }

    /**
     * Creates a new <code>builder</code> object to define an <code>DataDescriptor</code> object.
     *
     * @return A new <code>builder</code>.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Embedded class that defines the <code>builder</code> for <code>DataDescriptor</code>.
     */
    public static class Builder {
        private HashMap<String, String> descriptor = new HashMap<>();


        /**
         * Sets the <code>DESCRIPTION</code> key of the hash map.
         *
         * @param description Value to associate <code>DESCRIPTION</code> to.
         * @return The modified <code>builder</code>.
         */
        public Builder setDescription(String description) {
            descriptor.put(DESCRIPTION, description);
            return this;
        }

        /**
         * Sets the <code>minValue</code> and <code>maxValue</code>field of the object.
         *
         * @param minValue Minimum allowable value for the data point.
         * @param maxValue Maximum allowable value for the data point.
         * @return The modified <code>builder</code>.
         */
        public Builder setRange(double minValue, double maxValue) {
            descriptor.put(MIN_VALUE, String.valueOf(minValue));
            descriptor.put(MAX_VALUE, String.valueOf(maxValue));
            return this;
        }

        /**
         * Puts a custom key and value into the hash map.
         *
         * @param key   Key to add to the hash map.
         * @param value Value to add to the hash map.
         * @return The modified <code>builder</code>.
         */
        public Builder setDescriptor(String key, String value) {
            this.descriptor.put(key, value);
            return this;
        }

        /**
         * Sets the <code>unit</code> field, which defines the unit of measure used for the data.
         *
         * @param unit Unit of measure used.
         * @return The modified <code>builder</code>.
         */
        public Builder setUnit(MCUnit unit) {
            this.descriptor.put(UNIT, unit.name());
            return this;
        }

        /**
         * Sets the <code>unit</code> field, which defines the unit of measure used for the data.
         *
         * @param unit Unit of measure used.
         * @return The modified <code>builder</code>.
         */
        public Builder setUnit(String unit) {
            this.descriptor.put(UNIT, unit);
            return this;
        }

        public Builder setDescriptor(HashMap<String, String> descriptor) {
            if (descriptor != null)
                for (HashMap.Entry<String, String> entry : descriptor.entrySet()) {
                    if (entry.getKey() != null && entry.getValue() != null)
                        this.descriptor.put(entry.getKey(), entry.getValue());
                }
            return this;

        }

        /**
         * Passes the <code>builder</code> to the <code>DataDescriptor</code> constructor.
         *
         * @return The resulting <code>DataDescriptor</code> object.
         */
        public MCDataDescriptor build() {
            return new MCDataDescriptor(this);
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
        if (toCompare instanceof MCDataDescriptor) {
            if (descriptor.size() != ((MCDataDescriptor) toCompare).descriptor.size()) return false;
            for (Map.Entry<String, String> entry : descriptor.entrySet()) {
                if (!((MCDataDescriptor) toCompare).descriptor.containsKey(entry.getKey()))
                    return false;
                if (!entry.getValue().equals(((MCDataDescriptor) toCompare).descriptor.get(entry.getKey())))
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
        result = 31 * result + descriptor.hashCode();
        return result;
    }
}
