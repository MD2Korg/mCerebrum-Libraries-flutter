package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Provides getters and setters for <code>MinioBucket</code> objects.
 */
public class MinioBucket {

    /**
     * Name of the bucket.
     * <p>
     *     Serialized name: "bucket-name"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("bucket-name")
    @Expose
    private Object bucketName;

    /**
     * When the bucket was last modified.
     * <p>
     *     Serialized name: "last_modified"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("last_modified")
    @Expose
    private Object lastModified;

    /**
     * Returns the name of the bucket.
     * @return The name of the bucket.
     */
    public Object getBucketName() {
        return bucketName;
    }

    /**
     * Sets the name of the bucket.
     * @param bucketName
     */
    public void setBucketName(Object bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * Returns the <code>lastModified</code> object.
     * @return The <code>lastModified</code> object.
     */
    public Object getLastModified() {
        return lastModified;
    }

    /**
     * Sets the <code>lastModified</code> field.
     * @param lastModified The new value for <code>lastModified</code>.
     */
    public void setLastModified(Object lastModified) {
        this.lastModified = lastModified;
    }
}