package com.compucar.service;

import com.compucar.builder.ServiceExecutionBuilder;
import com.compucar.dao.ServiceExecutionDao;
import com.compucar.dto.ServiceAttributeDto;
import com.compucar.dto.ServiceSummaryDto;
import com.compucar.dto.ServiceReportDto;
import com.compucar.model.ServiceExecution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ServiceExecutionServiceImpTest {
    private static final String SERVICE_A = "ServiceA";
    private static final String SERVICE_B = "ServiceB";

    private ServiceExecutionDao dao;

    @Before
    public void init() {
        this.dao = mock(ServiceExecutionDao.class);
    }

    @Test
    public void getUsageReportOkTest() {
        LocalDate paramDate = LocalDate.of(2019,7,30);
        LocalDateTime expectedStartDate = LocalDateTime.of(2019, 7, 30, 0, 0, 0);
        LocalDateTime expectedEndDate = LocalDateTime.of(2019, 7, 30, 23, 59, 59);

        when(dao.findByRegisterDateBetween(expectedStartDate,expectedEndDate))
                .thenReturn(createListServiceExecutions());
        ServiceExecutionService service = new ServiceExecutionServiceImp(dao);

        ServiceReportDto result = service.getUsageReport(paramDate);

        List<ServiceSummaryDto> averageDtos = result.getServiceSummary();
        ServiceSummaryDto serviceSummaryA = averageDtos.stream().filter(
                serviceSummaryDto -> serviceSummaryDto.getServiceName().equals(SERVICE_A))
                .findFirst()
                .get();
        Assert.assertEquals(4.5, serviceSummaryA.getAverage(), 0);
        Assert.assertEquals(2, serviceSummaryA.getCallCount());
        Assert.assertEquals(SERVICE_A, serviceSummaryA.getServiceName());

        ServiceSummaryDto serviceSummaryB = averageDtos.stream().filter(
                serviceSummaryDto -> serviceSummaryDto.getServiceName().equals(SERVICE_B))
                .findFirst()
                .get();
        Assert.assertEquals(SERVICE_B, serviceSummaryB.getServiceName());
        Assert.assertEquals(300, serviceSummaryB.getAverage(), 0);
        Assert.assertEquals(3, serviceSummaryB.getCallCount());

        ServiceAttributeDto fastestService = result.getFastestService();
        Assert.assertEquals(SERVICE_A, fastestService.getServiceName());
        Assert.assertEquals(3, fastestService.getValue());

        ServiceAttributeDto slowestService = result.getSlowestService();
        Assert.assertEquals(SERVICE_B, slowestService.getServiceName());
        Assert.assertEquals(500, slowestService.getValue());

        ServiceAttributeDto mostUsedService = result.getMostUsedService();
        Assert.assertEquals(SERVICE_B, mostUsedService.getServiceName());
        Assert.assertEquals(3, mostUsedService.getValue());

        ServiceAttributeDto lessUsedService = result.getLessUsedService();
        Assert.assertEquals(SERVICE_A, lessUsedService.getServiceName());
        Assert.assertEquals(2, lessUsedService.getValue());
    }

    private List<ServiceExecution> createListServiceExecutions() {
        List<ServiceExecution> result = new ArrayList<>();
        result.add(new ServiceExecutionBuilder()
                .serviceName("ServiceA")
                .executionTime(5L)
                .build());

        result.add(new ServiceExecutionBuilder()
                .serviceName("ServiceA")
                .executionTime(3L)
                .build());

        result.add(new ServiceExecutionBuilder()
                .serviceName("ServiceB")
                .executionTime(300L)
                .build());

        result.add(new ServiceExecutionBuilder()
                .serviceName("ServiceB")
                .executionTime(100L)
                .build());

        result.add(new ServiceExecutionBuilder()
                .serviceName("ServiceB")
                .executionTime(500L)
                .build());

        return result;
    }
}
