package org.md2k.mcerebrumapi.data;

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
public enum MCDataType {
    /**
     * This class enumerates the sample types of <code>Data</code> objects that can exist
     * and provides <code> enumeration value</code>s for them.
     * The possible types are:
     * <ul>
     * <li>BOOLEAN_ARRAY</li>
     * <li>BYTE_ARRAY</li>
     * <li>INT_ARRAY</li>
     * <li>LONG_ARRAY</li>
     * <li>DOUBLE_ARRAY</li>
     * <li>STRING_ARRAY</li>
     * <li>ENUM</li>
     * <li>OBJECT</li>
     * </ul>
     */
    BOOLEAN_ARRAY(100),
    BYTE_ARRAY(101),
    INT_ARRAY(102),
    LONG_ARRAY(103),
    DOUBLE_ARRAY(104),
    STRING_ARRAY(105),
    ANNOTATION(106),
    OBJECT(107);
    private int value;

    /**
     * Constructor
     *
     * @param value value for the <code>DataType</code>. This should be the
     *              <code>unique code</code> for the specific type of the sample
     *              as enumerated above.
     */
    MCDataType(int value) {
        this.value = value;
    }

    /**
     * Returns the <code>value</code>.
     *
     * @return The <code>value</code>.
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the <code>DataType</code> given value.
     *
     * @param value value to find <code>DataType</code>
     * @return The <code>DataType</code>.
     */
    public static MCDataType getDataType(int value) {
        for (MCDataType a : MCDataType.values()) {
            if (a.getValue() == value) return a;
        }
        return BYTE_ARRAY;
    }
}
