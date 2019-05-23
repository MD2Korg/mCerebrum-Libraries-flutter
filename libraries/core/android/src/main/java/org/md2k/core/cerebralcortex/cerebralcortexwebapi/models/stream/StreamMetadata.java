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
    private List<HashMap<String, String>> dataDescriptors;

    @SerializedName("input_streams")
    @Expose
    private List<InputStream> inputStreams;

    @SerializedName("annotation")
    @Expose
    private List<Annotation> annotations;

    @SerializedName("modules")
    @Expose
    private List<Module> modules;

    public StreamMetadata() {
    }

    public StreamMetadata(String name, String description,
                          List<HashMap<String, String>> dataDescriptors,
                          List<InputStream> inputStreams,
                          List<Annotation> annotations,
                          List<Module> modules) {
        super();
        this.name = name;
        this.description = description;
        this.dataDescriptors = dataDescriptors;
        this.inputStreams = inputStreams;
        this.annotations = annotations;
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

    public List<HashMap<String, String>> getDataDescriptors() {
        return dataDescriptors;
    }

    public void setDataDescriptors(List<HashMap<String, String>> dataDescriptors) {
        this.dataDescriptors = dataDescriptors;
    }

    public List<InputStream> getInputStreams() {
        return inputStreams;
    }

    public void setInputStreams(List<InputStream> inputStreams) {
        this.inputStreams = inputStreams;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
}