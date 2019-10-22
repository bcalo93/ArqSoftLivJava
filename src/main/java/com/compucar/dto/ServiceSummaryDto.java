package com.compucar.dto;

import lombok.Data;

@Data
public class ServiceSummaryDto {
    private String serviceName;
    private double average;
    private int callCount;
}
