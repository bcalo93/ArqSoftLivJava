package com.compucar.dto;

import lombok.Data;

@Data
public class EventDto {
    private String serviceCode;
    private String name;
    private String payload;
}
