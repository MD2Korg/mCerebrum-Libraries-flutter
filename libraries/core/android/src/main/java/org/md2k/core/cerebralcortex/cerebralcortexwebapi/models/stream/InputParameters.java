package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Parameters for the <code>ProcessingModule</code> input.
 */
public class InputParameters {

    /**
     *<p>
     *     Serialized name: "window_size"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("window_size")
    @Expose
    private Integer windowSize;

    /**
     *<p>
     *     Serialized name: "window_offset"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("window_offset")
    @Expose
    private Integer windowOffset;

    /**
     *<p>
     *     Serialized name: "low_level_threshold"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("low_level_threshold")
    @Expose
    private Double lowLevelThreshold;

    /**
     *<p>
     *     Serialized name: "high_level_threshold"
     *     Exposed to serialization.
     * </p>
     */
    @SerializedName("high_level_threshold")
    @Expose
    private Double highLevelThreshold;

    /**
     * No argument constructor for use in serialization
     */
    public InputParameters() {}

    /**
     * Constructor
     *
     * @param windowOffset
     * @param highLevelThreshold
     * @param lowLevelThreshold
     * @param windowSize
     */
    public InputParameters(Integer windowSize, Integer windowOffset, Double lowLevelThreshold,
                           Double highLevelThreshold) {
        super();
        this.windowSize = windowSize;
        this.windowOffset = windowOffset;
        this.lowLevelThreshold = lowLevelThreshold;
        this.highLevelThreshold = highLevelThreshold;
    }

    /**
     * Returns the window size.
     * @return The window size.
     */
    public Integer getWindowSize() {
        return windowSize;
    }

    /**
     * Sets the window size.
     * @param windowSize The window size.
     */
    public void setWindowSize(Integer windowSize) {
        this.windowSize = windowSize;
    }

    /**
     * Returns the window offset.
     * @return The window offset.
     */
    public Integer getWindowOffset() {
        return windowOffset;
    }

    /**
     * Sets the window offset.
     * @param windowOffset The window offset.
     */
    public void setWindowOffset(Integer windowOffset) {
        this.windowOffset = windowOffset;
    }

    /**
     * Returns the minimum threshold.
     * @return The minimum threshold.
     */
    public Double getLowLevelThreshold() {
        return lowLevelThreshold;
    }

    /**
     * Sets the minimum threshold.
     * @param lowLevelThreshold The minimum threshold.
     */
    public void setLowLevelThreshold(Double lowLevelThreshold) {
        this.lowLevelThreshold = lowLevelThreshold;
    }

    /**
     * Returns the maximum threshold.
     * @return The maximum threshold.
     */
    public Double getHighLevelThreshold() {
        return highLevelThreshold;
    }

    /**
     * Sets the maximum threshold.
     * @param highLevelThreshold The maximum threshold.
     */
    public void setHighLevelThreshold(Double highLevelThreshold) {
        this.highLevelThreshold = highLevelThreshold;
    }
}