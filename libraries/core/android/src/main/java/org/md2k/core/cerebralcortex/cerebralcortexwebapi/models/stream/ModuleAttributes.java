package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.List;


public class ModuleAttributes {

    @SerializedName("reference")
    @Expose
    private Array references;

    @SerializedName("description")
    @Expose
    private String description;

    public ModuleAttributes() {}

    public ModuleAttributes(Array references, String description) {
        super();
        this.references = references;
        this.description = description;
    }

    public Array getReferences() {
        return references;
    }

    public void setReferences(Array references) {
        this.references = references;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}