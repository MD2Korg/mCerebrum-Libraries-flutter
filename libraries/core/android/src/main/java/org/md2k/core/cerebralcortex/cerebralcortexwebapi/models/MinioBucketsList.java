package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Provides getters and setters for a list of Minio buckets.
 */
public class MinioBucketsList {

    /**
     * List of Minio buckets.
     * <p>
     *     Serialized name: "buckets-list"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("buckets-list")
    @Expose
    private List<MinioBucket> minioBuckets = null;

    /**
     * Returns a list of Minio buckets.
     * @return The list of Minio buckets.
     */
    public List<MinioBucket> getMinioBuckets() {
        return minioBuckets;
    }

    /**
     * Sets the list of Minio buckets.
     * @param minioBuckets The new list of Minio buckets.
     */
    public void setMinioBuckets(List<MinioBucket> minioBuckets) {
        this.minioBuckets = minioBuckets;
    }
}