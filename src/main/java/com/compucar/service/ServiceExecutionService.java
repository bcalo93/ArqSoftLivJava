package com.compucar.service;

import com.compucar.dto.ServiceReportDto;
import com.compucar.model.ServiceExecution;

import java.time.LocalDate;

public interface ServiceExecutionService {
    void addServiceExecution(ServiceExecution serviceExecution);
    ServiceReportDto getUsageReport(LocalDate date);
}
