package com.compucar.dto;

import lombok.Data;

@Data
public class ReaderDto {
    private Long id;
    private String code;
    private String brand;
    private int actualTimeUse;
    private int batteryLife;
    private String workshopCode;
}
