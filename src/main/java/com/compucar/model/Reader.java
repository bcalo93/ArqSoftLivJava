package com.compucar.model;

import lombok.Data;

@Data
public class Reader extends DataEntity {
    private String code;
    private String brand;
    private int batteryLife;
    private int actualTimeUse;
    private Workshop workshop;
}
