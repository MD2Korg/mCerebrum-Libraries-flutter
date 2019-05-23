package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Data stream input to the <code>ProcessModule</code>.
 */
public class InputStream {

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
     *<p>
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
    public InputStream() {
    }

    /**
     * Constructor
     *
     * @param name Name of the <code>DataStream</code>.
     * @param identifier <code>DataStream</code> identifier.
     */
    public InputStream(String name, String identifier) {
        super();
        this.name = name;
        this.identifier = identifier;
    }

    /**
     * Returns the name of the <code>DataStream</code>.
     * @return The name of the <code>DataStream</code>.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the <code>DataStream</code>.
     * @param name The name of the <code>DataStream</code>.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the <code>DataStream</code> identifier.
     * @return The <code>DataStream</code> identifier.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the <code>DataStream</code> identifier.
     * @param identifier The <code>DataStream</code> identifier.
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}