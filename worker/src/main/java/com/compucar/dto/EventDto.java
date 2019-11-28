package com.compucar.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class EventDto {
    private String serviceCode;
    private String name;
    private String payload;

    public EventDto() {

    }

    public EventDto(String serviceCode, String name, String payload) {
        this.serviceCode = serviceCode;
        this.name = name;
        this.payload = payload;
    }
}
