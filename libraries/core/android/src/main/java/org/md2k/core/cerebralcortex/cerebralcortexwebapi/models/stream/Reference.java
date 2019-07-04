package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Reference URL information for the data stream.
 */
public class Reference {

    /**
     *<p>
     *     Serialized name: "url"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("url")
    @Expose
    private String url;

    /**
     * No argument constructor for use in serialization
     */
    public Reference() {
    }

    /**
     * Constructor
     *
     * @param url Reference URL
     */
    public Reference(String url) {
        super();
        this.url = url;
    }

    /**
     * Returns the URL.
     * @return The URL.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL.
     * @param url The URL.
     */
    public void setUrl(String url) {
        this.url = url;
    }
}