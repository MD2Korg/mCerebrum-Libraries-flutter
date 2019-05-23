package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Provides name and identifier information for a data stream.
 */
public class Annotation {

    /**
     * <p>
     *     Serialized name: "name"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * <p>
     *     Serialized name: "identifier"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("identifier")
    @Expose
    private String identifier;

    /**
     * No argument constructor for use in serialization
     */
    public Annotation() {
    }

    /**
     * Constructor
     *
     * @param name Name of the annotation.
     * @param identifier Annotation identifier.
     */
    public Annotation(String name, String identifier) {
        super();
        this.name = name;
        this.identifier = identifier;
    }

    /**
     * Returns the annotation name.
     * @return The annotation name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the annotation name.
     * @param name The new annotation name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the annotation identifier.
     * @return The annotation identifier.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the annotation identifier.
     * @param identifier The new annotation identifer.
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}