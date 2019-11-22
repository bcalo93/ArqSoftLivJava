package com.compucar.model;

import lombok.Data;

@Data
public class Heartbeat {
    private String applicationName;
    private int port;

    public Heartbeat(String applicationName, int port) {
        this.applicationName = applicationName;
        this.port = port;
    }
}
