package com.example.iot.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
public class PeopleCount {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int count;

    @CreatedDate
    private Instant timestamp;

    // Getter và setter
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @PrePersist
    public void setTimestamp() {
        if (this.timestamp == null) {
            // Lấy thời gian hiện tại theo múi giờ Việt Nam
            this.timestamp = Instant.now();
        }
    }

    public String getFormattedTimestamp() {
        // Chuyển đổi timestamp từ UTC sang giờ Việt Nam
        ZonedDateTime vietnamTime = timestamp.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        return vietnamTime.toLocalDateTime().toString();
    }

    @Override
    public String toString() {
        return "PeopleCount{" +
                "count=" + count +
                ", timestamp=" + timestamp +
                '}';
    }
}