package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A data point consists of a sample and a timeframe (starting and ending timestamps).
 */
public class DataPoints {

    /**
     *<p>
     *     Serialized name: "starttime"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("starttime")
    @Expose
    private String starttime;

    /**
     *<p>
     *     Serialized name: "endtime"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("endtime")
    @Expose
    private String endtime;

    /**
     *<p>
     *     Serialized name: "sample"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("sample")
    @Expose
    private String sample;

    /**
     * No arguments constructor for use in serialization
     */
    public DataPoints() {}

    /**
     * Constructor
     *
     * @param endtime Endtime of the data point.
     * @param starttime Starttime of the data point.
     * @param sample Sampling data for the data point.
     */
    public DataPoints(String starttime, String endtime, String sample) {
        super();
        this.starttime = starttime;
        this.endtime = endtime;
        this.sample = sample;
    }

    /**
     * Returns the starttime of the data point.
     * @return The starttime of the data point.
     */
    public String getStarttime() {
        return starttime;
    }

    /**
     * Sets the starttime of the data point.
     * @param starttime The starttime of the data point.
     */
    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    /**
     * Returns the endtime of the data point.
     * @return The endtime of the data point.
     */
    public String getEndtime() {
        return endtime;
    }

    /**
     * Sets the endtime of the data point.
     * @param endtime The endtime of the data point.
     */
    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    /**
     * Returns the sample of the data point.
     * @return The sample of the data point.
     */
    public String getSample() {
        return sample;
    }

    /**
     * Sets the sample of the data point.
     * @param sample The sample of the data point.
     */
    public void setSample(String sample) {
        this.sample = sample;
    }

}