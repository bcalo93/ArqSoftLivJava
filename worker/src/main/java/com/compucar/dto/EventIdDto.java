package com.compucar.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EventIdDto implements Serializable {
    private String serviceCode;
    private String name;

    public EventIdDto() {

    }

    public EventIdDto(String serviceCode, String name) {
        this.serviceCode = serviceCode;
        this.name = name;
    }
}
