package com.compucar.monitor.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Heartbeat {
    private String applicationName;
    private int port;
    private long timestamp;

    public Heartbeat() {
        this.timestamp = Instant.now().getEpochSecond();
    }
}
