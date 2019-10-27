package com.compucar.dto;

import com.compucar.model.ServiceExecution;
import lombok.Data;

@Data
public class ServiceTimeAttributeDto {
    private String serviceName;
    private Long timeValue;

    public ServiceTimeAttributeDto(ServiceExecution serviceExecution) {
        this.serviceName = serviceExecution.getServiceName();
        this.timeValue = serviceExecution.getExecutionTime();
    }
}
