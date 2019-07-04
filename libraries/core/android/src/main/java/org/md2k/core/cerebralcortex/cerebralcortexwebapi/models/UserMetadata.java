package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Provides getters and setters for <code>UserMetadata</code> objects.
 */
public class UserMetadata {
    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("value")
    @Expose
    private String value;

    public UserMetadata() {}

    public UserMetadata(String key, String value) {
        super();
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}