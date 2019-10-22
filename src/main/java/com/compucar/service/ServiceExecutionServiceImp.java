package com.compucar.service;

import com.compucar.dao.ServiceExecutionDao;
import com.compucar.dto.ServiceAttributeDto;
import com.compucar.dto.ServiceReportDto;
import com.compucar.model.ServiceExecution;
import com.compucar.service.exceptions.RequiredFieldMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public ServiceReportDto getUsageReport(LocalDate date) {
        return null;
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

    private List<ServiceAttributeDto> getAveragePerService(List<ServiceExecution> serviceExecutions) {
        Map<String, List<ServiceExecution>> testMap = serviceExecutions.stream().collect(Collectors.groupingBy(serviceExecution ->
                serviceExecution.getServiceName()));
//        return serviceExecutions.stream().collect(Collectors.groupingBy())
        return null;
    }
}
