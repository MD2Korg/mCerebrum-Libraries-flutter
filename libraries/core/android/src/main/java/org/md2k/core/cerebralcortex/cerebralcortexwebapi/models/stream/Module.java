package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Module {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("authors")
    @Expose
    private List<Author> authors;

    public Module() {}

    public Module(String name, String version, List<Author> authors) {
        super();
        this.name = name;
        this.version = version;
        this.authors = authors;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}