package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Provides a description for the data collected in the data stream.
 */
public class DataDescriptor {

    /**
     *<p>
     *     Serialized name: "type"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("dataAttributes")
    @Expose
    private DataAttributes dataAttributes;

//    /**
//     *<p>
//     *     Serialized name: "unit"
//     *     Exposed to serialization.
//     * </p>
//     */
//    @SerializedName("unit")
//    @Expose
//    private String unit;

    /**
     * No arguments constructor for use in serialization
     */
    public DataDescriptor() {}

    /**
     * Constructor
     *
     * @param name Name of this <code>DataDescriptor</code>.
     * @param type Type of this <code>DataDescriptor</code>.
     */
    public DataDescriptor(String type, String name) {
        super();
        this.type = type;
        this.name = name;
    }

    /**
     * Returns the type of this <code>DataDescriptor</code>.
     * @return The type of this <code>DataDescriptor</code>.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of this <code>DataDescriptor</code>.
     * @param type The type of this <code>DataDescriptor</code>.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the unit of this <code>DataDescriptor</code>.
     * @return The unit of this <code>DataDescriptor</code>.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the unit of this <code>DataDescriptor</code>.
     * @param name The name of this <code>DataDescriptor</code>.
     */
    public void setName(String name) {
        this.name = name;
    }

    public DataAttributes getDataAttributes() {
        return dataAttributes;
    }

    public void setDataAttributes(DataAttributes dataAttributes) {
        this.dataAttributes = dataAttributes;
    }
}