package com.compucar.dto;

import lombok.Data;

@Data
public class ServiceUsageAttributeDto {
    private String serviceName;
    private int usageCount;

    public ServiceUsageAttributeDto(String serviceName, int usageCount) {
        this.serviceName = serviceName;
        this.usageCount = usageCount;
    }
}
