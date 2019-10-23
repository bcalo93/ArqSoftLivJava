package com.compucar.dto;

import lombok.Data;

@Data
public class ServiceSummaryDto {
    private String serviceName;
    private double average;
    private int callCount;

    public ServiceSummaryDto(String serviceName, double average, int callCount) {
        this.serviceName = serviceName;
        this.average = average;
        this.callCount = callCount;
    }
}
