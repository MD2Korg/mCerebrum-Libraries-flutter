package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DataAttributes {
    @SerializedName("frequency")
    @Expose
    private String frequency;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("min_value")
    @Expose
    private String minValue;

    @SerializedName("max_value")
    @Expose
    private String maxValue;

    @SerializedName("unit")
    @Expose
    private String unit;

    public DataAttributes() {}

    public DataAttributes(String frequency, String description, String minValue,
                          String maxValue, String unit) {
        super();
        this.frequency = frequency;
        this.description = description;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.unit = unit;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}