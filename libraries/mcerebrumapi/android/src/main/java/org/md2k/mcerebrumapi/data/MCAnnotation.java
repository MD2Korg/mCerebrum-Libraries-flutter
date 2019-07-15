package org.md2k.mcerebrumapi.data;

abstract public class MCAnnotation {
    private long startTimestamp;
    private long endTimestamp;

    public MCAnnotation(long startTimestamp, long endTimestamp) {
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }
}
