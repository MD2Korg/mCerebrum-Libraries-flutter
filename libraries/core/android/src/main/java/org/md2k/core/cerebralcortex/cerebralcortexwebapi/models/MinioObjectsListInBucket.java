package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Provides getters and setters for listing the Minio objects in a bucket.
 */
public class MinioObjectsListInBucket {

    /**
     * List of <code>MinioObjectStats</code>.
     * <p>
     *     Serialized name: "bucket-objects"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("bucket-objects")
    @Expose
    private List<MinioObjectStats> bucketObjects = null;

    /**
     * Returns the list of <code>MinioObjectStats</code>.
     * @return The list of <code>MinioObjectStats</code>.
     */
    public List<MinioObjectStats> getBucketObjects() {
        return bucketObjects;
    }

    /**
     * Sets a new list of <code>MinioObjectStats</code>.
     * @param bucketObjects The new list of <code>MinioObjectStats</code>.
     */
    public void setBucketObjects(List<MinioObjectStats> bucketObjects) {
        this.bucketObjects = bucketObjects;
    }
}