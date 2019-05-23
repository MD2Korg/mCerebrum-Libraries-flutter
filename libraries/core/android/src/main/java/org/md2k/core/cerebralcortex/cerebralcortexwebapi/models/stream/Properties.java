package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Defines data stream properties
 */
public class Properties {

    /**
     *<p>
     *     Serialized name: "indentifier"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("identifier")
    @Expose
    private String identifier;

    /**
     *<p>
     *     Serialized name: "owner"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("owner")
    @Expose
    private String owner;

    /**
     *<p>
     *     Serialized name: "name"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("name")
    @Expose
    private String name;

    /**
     *<p>
     *     Serialized name: "data_descriptor"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("data_descriptor")
    @Expose
    private List<DataDescriptor> dataDescriptor = null;

    /**
     *<p>
     *     Serialized name: "execution__context"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("execution_context")
    @Expose
    private ExecutionContext executionContext;

    /**
     *<p>
     *     Serialized name: "annotations"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("annotations")
    @Expose
    private List<Annotation> annotations = null;

    /**
     * No argument constructor for use in serialization
     */
    public Properties() {}

    /**
     * Constructor
     *
     * @param identifier <code>DataStream</code> identifier.
     * @param owner <code>DataStream</code> owner.
     * @param name <code>DataStream</code> name.
     * @param dataDescriptor List of descriptions for the data.
     * @param executionContext Execution context of the <code>DataStream</code>.
     * @param annotations Annotations of the <code>DataStream</code>.
     */
    public Properties(String identifier, String owner, String name, List<DataDescriptor> dataDescriptor,
                      ExecutionContext executionContext, List<Annotation> annotations) {
        super();
        this.identifier = identifier;
        this.owner = owner;
        this.name = name;
        this.dataDescriptor = dataDescriptor;
        this.executionContext = executionContext;
        this.annotations = annotations;
    }

    /**
     * Returns the <code>DataStream</code> identifier.
     * @return The <code>DataStream</code> identifier.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the <code>DataStream</code> identifier.
     * @param identifier The <code>DataStream</code> identifier.
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Returns the owner of the <code>DataStream</code>.
     * @return The owner of the <code>DataStream</code>.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the owner of the <code>DataStream</code>.
     * @param owner The owner of the <code>DataStream</code>.
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Returns the name of the <code>DataStream</code>.
     * @return The name of the <code>DataStream</code>.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the <code>DataStream</code>.
     * @param name The name of the <code>DataStream</code>.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the list of data descriptors in the <code>DataStream</code>.
     * @return The list of data descriptors in the <code>DataStream</code>.
     */
    public List<DataDescriptor> getDataDescriptor() {
        return dataDescriptor;
    }

    /**
     * Sets the list of data descriptors in the <code>DataStream</code>.
     * @param dataDescriptor The list of data descriptors in the <code>DataStream</code>.
     */
    public void setDataDescriptor(List<DataDescriptor> dataDescriptor) {
        this.dataDescriptor = dataDescriptor;
    }

    /**
     * Returns the execution context of the <code>DataStream</code>.
     * @return The execution context of the <code>DataStream</code>.
     */
    public ExecutionContext getExecutionContext() {
        return executionContext;
    }

    /**
     * Sets the execution context of the <code>DataStream</code>.
     * @param executionContext The execution context of the <code>DataStream</code>.
     */
    public void setExecutionContext(ExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    /**
     * Returns the list of annotations in the <code>DataStream</code>.
     * @return The list of annotations in the <code>DataStream</code>.
     */
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    /**
     * Sets the list of annotations in the <code>DataStream</code>.
     * @param annotations The list of annotations in the <code>DataStream</code>.
     */
    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }
}