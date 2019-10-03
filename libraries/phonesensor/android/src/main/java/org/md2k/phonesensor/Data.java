package org.md2k.phonesensor;

public class Data {
    private long timestamp;
    private double[] values;

    public Data(long timestamp, double[] values) {
        this.timestamp = timestamp;
        this.values = values;
    }
}
