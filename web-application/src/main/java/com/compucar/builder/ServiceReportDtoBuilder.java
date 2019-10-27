package com.compucar.builder;

import com.compucar.dto.ServiceReportDto;
import com.compucar.dto.ServiceSummaryDto;
import com.compucar.dto.ServiceTimeAttributeDto;
import com.compucar.dto.ServiceUsageAttributeDto;

import java.util.List;

public class ServiceReportDtoBuilder {
    private ServiceReportDto serviceReportDto;

    public ServiceReportDtoBuilder() {
        this.serviceReportDto = new ServiceReportDto();
    }

    public ServiceReportDtoBuilder serviceSummary(List<ServiceSummaryDto> serviceSummary) {
        this.serviceReportDto.setServiceSummary(serviceSummary);
        return this;
    }

    public ServiceReportDtoBuilder fastestService(ServiceTimeAttributeDto fastestService) {
        this.serviceReportDto.setFastestService(fastestService);
        return this;
    }

    public ServiceReportDtoBuilder slowestService(ServiceTimeAttributeDto slowestService) {
        this.serviceReportDto.setSlowestService(slowestService);
        return this;
    }

    public ServiceReportDtoBuilder mostUsedService(ServiceUsageAttributeDto mostUsedService) {
        this.serviceReportDto.setMostUsedService(mostUsedService);
        return this;
    }

    public ServiceReportDtoBuilder lessUsedService(ServiceUsageAttributeDto lessUsedService) {
        this.serviceReportDto.setLessUsedService(lessUsedService);
        return this;
    }

    public ServiceReportDto build() {
        return this.serviceReportDto;
    }
}
