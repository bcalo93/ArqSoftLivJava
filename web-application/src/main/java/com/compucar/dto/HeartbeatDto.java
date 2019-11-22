package com.compucar.dto;

import lombok.Data;

@Data
public class HeartbeatDto {

    private String applicationName;
    private int port;

    public HeartbeatDto(String applicationName, int port) {
        this.applicationName = applicationName;
        this.port = port;
    }
}
