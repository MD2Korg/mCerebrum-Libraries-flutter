package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Holds information about the environment the data was collected in.
 * <p>
 * This information includes metadata from the data source, application, platform, and platform app.
 * </p>
 */
public class ExecutionContext {

    /**
     * <p>
     * Serialized name: "processing_module"
     * Exposed to serialization.
     * </p>
     */
    @SerializedName("processing_module")
    @Expose
    private ProcessingModule processingModule;

    /**
     * <p>
     * Serialized name: "datasource_metadata"
     * Exposed to serialization.
     * </p>
     */
    @SerializedName("datasource_metadata")
    @Expose
    private HashMap<String, String> datasource_metadata;

    /**
     * <p>
     * Serialized name: "application_metadata"
     * Exposed to serialization.
     * </p>
     */
    @SerializedName("application_metadata")
    @Expose
    private HashMap<String, String> application_metadata;

    /**
     * <p>
     * Serialized name: "platform_metadata"
     * Exposed to serialization.
     * </p>
     */
    @SerializedName("platform_metadata")
    @Expose
    private HashMap<String, String> platform_metadata;

    /**
     * <p>
     * Serialized name: "platformapp_metadata"
     * Exposed to serialization.
     * </p>
     */
    @SerializedName("platformapp_metadata")
    @Expose
    private HashMap<String, String> platformapp_metadata;

    /**
     * No argument constructor for use in serialization
     */
    public ExecutionContext() {
    }


    /**
     * Constructor
     *
     * @param processingModule
     * @param datasource_metadata
     * @param application_metadata
     * @param platform_metadata
     * @param platformapp_metadata
     */
    public ExecutionContext(ProcessingModule processingModule,
                            HashMap<String, String> datasource_metadata,
                            HashMap<String, String> application_metadata,
                            HashMap<String, String> platform_metadata,
                            HashMap<String, String> platformapp_metadata) {
        super();
        this.processingModule = processingModule;
        this.datasource_metadata = datasource_metadata;
        this.application_metadata = application_metadata;
        this.platform_metadata = platform_metadata;
        this.platformapp_metadata = platformapp_metadata;


    }

    /**
     * Returns the <code>ProcessingModule</code>.
     *
     * @return The <code>ProcessingModule</code>.
     */
    public ProcessingModule getProcessingModule() {
        return processingModule;
    }

    /**
     * Sets the <code>ProcessingModule</code>.
     *
     * @param processingModule The <code>ProcessingModule</code>.
     */
    public void setProcessingModule(ProcessingModule processingModule) {
        this.processingModule = processingModule;
    }

    /**
     * Returns the hashMap of <code>Datasource_metadata</code>.
     *
     * @return The hashMap of <code>Datasource_metadata</code>.
     */
    public HashMap<String, String> getDatasource_metadata() {
        return datasource_metadata;
    }

    /**
     * Sets the hashMap of <code>Datasource_metadata</code>.
     *
     * @param datasource_metadata The hashMap of <code>Datasource_metadata</code>.
     */
    public void setDatasource_metadata(HashMap<String, String> datasource_metadata) {
        this.datasource_metadata = datasource_metadata;
    }

    /**
     * Returns the hashMap of <code>Platformapp_metadata</code>.
     *
     * @return The hashMap of <code>Platformapp_metadata</code>.
     */
    public HashMap<String, String> getPlatformapp_metadata() {
        return platformapp_metadata;
    }

    /**
     * Sets the hashMap of <code>Platformapp_metadata</code>.
     *
     * @param platformapp_metadata The hashMap of <code>Platformapp_metadata</code>.
     */
    public void setPlatformapp_metadata(HashMap<String, String> platformapp_metadata) {
        this.platformapp_metadata = platformapp_metadata;
    }

    /**
     * Returns the hashMap of <code>Platform_metadata</code>.
     *
     * @return The hashMap of <code>Platform_metadata</code>.
     */
    public HashMap<String, String> getPlatform_metadata() {
        return platform_metadata;
    }

    /**
     * Sets the hashMap of <code>Platform_metadata</code>.
     *
     * @param platform_metadata The hashMap of <code>Platform_metadata</code>.
     */
    public void setPlatform_metadata(HashMap<String, String> platform_metadata) {
        this.platform_metadata = platform_metadata;
    }

    /**
     * Returns the hashMap of <code>Application_metadata</code>.
     *
     * @return The hashMap of <code>Application_metadata</code>.
     */
    public HashMap<String, String> getApplication_metadata() {
        return application_metadata;
    }

    /**
     * Sets the hashMap of <code>Application_metadata</code>.
     *
     * @param application_metadata The hashMap of <code>Application_metadata</code>.
     */
    public void setApplication_metadata(HashMap<String, String> application_metadata) {
        this.application_metadata = application_metadata;
    }
}