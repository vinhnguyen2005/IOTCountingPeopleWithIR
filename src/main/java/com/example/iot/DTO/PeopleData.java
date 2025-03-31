package com.example.iot.DTO;

import java.time.Instant;
public class PeopleData {
    private int count;
    private Instant timestamp;

    public PeopleData() {}

    public PeopleData(int count, Instant timestamp) {
        this.count = count;
        this.timestamp = (timestamp != null) ? timestamp : Instant.now();
    }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "PeopleData{count=" + count + ", timestamp=" + timestamp + "}";
    }
}
