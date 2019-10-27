package com.compucar.dto;

import lombok.Data;

import java.util.List;

@Data
public class ServiceReportDto {
    private List<ServiceSummaryDto> serviceSummary;
    private ServiceTimeAttributeDto fastestService;
    private ServiceTimeAttributeDto slowestService;
    private ServiceUsageAttributeDto mostUsedService;
    private ServiceUsageAttributeDto lessUsedService;
}
