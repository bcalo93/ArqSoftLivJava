package com.compucar.dto;

import lombok.Data;

import java.util.List;

@Data
public class ServiceReportDto {
    private List<ServiceSummaryDto> serviceSummary;
    private ServiceAttributeDto fastestService;
    private ServiceAttributeDto slowestService;
    private ServiceAttributeDto mostUsedService;
    private ServiceAttributeDto lessUsedService;
}
