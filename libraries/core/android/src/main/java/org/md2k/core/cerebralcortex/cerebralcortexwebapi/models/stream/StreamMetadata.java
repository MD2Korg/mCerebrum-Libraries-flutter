package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class StreamMetadata {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("data_descriptor")
    @Expose
    private List<DataDescriptor> dataDescriptors;

    @SerializedName("modules")
    @Expose
    private List<Module> modules;

    public StreamMetadata() {
    }

    public StreamMetadata(String name, String description,
                          List<DataDescriptor> dataDescriptors,
                          List<Module> modules) {
        super();
        this.name = name;
        this.description = description;
        this.dataDescriptors = dataDescriptors;
        this.modules = modules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DataDescriptor> getDataDescriptors() {
        return dataDescriptors;
    }

    public void setDataDescriptors(List<DataDescriptor> dataDescriptors) {
        this.dataDescriptors = dataDescriptors;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
}