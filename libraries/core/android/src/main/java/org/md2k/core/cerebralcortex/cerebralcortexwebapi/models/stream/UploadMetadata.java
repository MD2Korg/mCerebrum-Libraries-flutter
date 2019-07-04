package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadMetadata {

    @SerializedName("checksum")
    @Expose
    private String checksum;

    /**
     * No argument constructor for use in serialization
     */
    public UploadMetadata() {}

    public UploadMetadata(String checksum) {
        super();
        this.checksum = checksum;
    }

    public String setChecksum() {
        return this.checksum;
    }

    public void setChecksum(String Checksum) {
        this.checksum = checksum;
    }
}
