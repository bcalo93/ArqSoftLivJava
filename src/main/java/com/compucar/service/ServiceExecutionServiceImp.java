package com.compucar.service;

import com.compucar.builder.ServiceReportDtoBuilder;
import com.compucar.dao.ServiceExecutionDao;
import com.compucar.dto.ServiceSummaryDto;
import com.compucar.dto.ServiceTimeAttributeDto;
import com.compucar.dto.ServiceReportDto;
import com.compucar.dto.ServiceUsageAttributeDto;
import com.compucar.model.ServiceExecution;
import com.compucar.service.exceptions.RequiredFieldMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@Slf4j
public class ServiceExecutionServiceImp implements ServiceExecutionService {
    private static final int END_HOURS = 23;
    private static final int END_MINUTES = 59;
    private static final int END_SECONDS = 59;

    @Autowired
    private ServiceExecutionDao serviceExecutionDao;

    public ServiceExecutionServiceImp(ServiceExecutionDao serviceExecutionDao) {
        this.serviceExecutionDao = serviceExecutionDao;
    }

    @Override
    public void addServiceExecution(ServiceExecution serviceExecution) {
        if(isValidServiceExecution(serviceExecution)) {
            serviceExecutionDao.save(serviceExecution);

        } else {
            log.warn("Invalid operationLog object {}", serviceExecution);
        }
    }

    @Override
    public ServiceReportDto getUsageReport(LocalDate date) throws RequiredFieldMissingException {
        List<ServiceExecution> serviceExecutions = this.getServiceExecutionByDate(date);
        Map<String, List<ServiceExecution>> groupedServiceExecution = groupServiceExecutionByName(serviceExecutions);
        return new ServiceReportDtoBuilder()
                .serviceSummary(getSummaryPerService(groupedServiceExecution))
                .fastestService(getFastestService(serviceExecutions))
                .slowestService(getSlowestService(serviceExecutions))
                .mostUsedService(getMostUsedService(groupedServiceExecution))
                .lessUsedService(getLessUsedService(groupedServiceExecution))
                .build();
    }

    private boolean isValidServiceExecution(ServiceExecution serviceExecution) {
        return serviceExecution != null && serviceExecution.getExecutionTime() != null &&
                serviceExecution.getRegisterDate() != null && serviceExecution.getServiceName() != null;
    }

    private List<ServiceExecution> getServiceExecutionByDate(LocalDate date) throws RequiredFieldMissingException {
        if(date == null) {
            throw new RequiredFieldMissingException("A date ");
        }
        LocalDateTime startTime = date.atStartOfDay();
        LocalDateTime endTime = date.atTime(END_HOURS, END_MINUTES, END_SECONDS);
        return serviceExecutionDao.findByRegisterDateBetween(startTime, endTime);
    }

    private Map<String, List<ServiceExecution>> groupServiceExecutionByName(List<ServiceExecution> serviceExecutions) {
        return serviceExecutions.stream().collect(Collectors.groupingBy(se -> se.getServiceName()));
    }

    private List<ServiceSummaryDto> getSummaryPerService(Map<String, List<ServiceExecution>> groupedServiceExecution) {
        return groupedServiceExecution.keySet().stream()
                .map(gs -> new ServiceSummaryDto(gs,
                        groupedServiceExecution.get(gs).stream()
                        .collect(Collectors.averagingDouble(se -> se.getExecutionTime())),
                        groupedServiceExecution.get(gs).size()
                ))
                .collect(Collectors.toList());
    }

    private ServiceTimeAttributeDto getFastestService(List<ServiceExecution> serviceExecutions) {
        return serviceExecutions.stream()
                .min(Comparator.comparing(se -> se.getExecutionTime()))
                .map(se -> new ServiceTimeAttributeDto(se))
                .orElse(null);
    }

    private ServiceTimeAttributeDto getSlowestService(List<ServiceExecution> serviceExecutions) {
        return serviceExecutions.stream()
                .max(Comparator.comparing(se -> se.getExecutionTime()))
                .map(se -> new ServiceTimeAttributeDto(se))
                .orElse(null);
    }

    private ServiceUsageAttributeDto getMostUsedService(Map<String, List<ServiceExecution>> groupedServiceExecution) {
        return groupedServiceExecution.keySet().stream()
                .max(Comparator.comparing(key -> groupedServiceExecution.get(key).size()))
                .map(key -> new ServiceUsageAttributeDto(key, groupedServiceExecution.get(key).size()))
                .orElse(null);
    }

    private ServiceUsageAttributeDto getLessUsedService(Map<String, List<ServiceExecution>> groupedServiceExecution) {
        return groupedServiceExecution.keySet().stream()
                .min(Comparator.comparing(key -> groupedServiceExecution.get(key).size()))
                .map(key -> new ServiceUsageAttributeDto(key, groupedServiceExecution.get(key).size()))
                .orElse(null);
    }
}
