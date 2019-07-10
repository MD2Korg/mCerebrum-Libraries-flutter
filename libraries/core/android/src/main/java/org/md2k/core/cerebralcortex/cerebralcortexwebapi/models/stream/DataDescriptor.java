package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

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

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("attributes")
    @Expose
    private HashMap<String, String> attributes;

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
        this.description = "";
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttributes(HashMap<String, String> attributes) { this.attributes = attributes; }

    public HashMap<String, String> getAttributes() { return attributes; }

}