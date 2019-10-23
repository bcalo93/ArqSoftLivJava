package com.compucar.service;

import com.compucar.builder.ServiceExecutionBuilder;
import com.compucar.dao.ServiceExecutionDao;
import com.compucar.dto.ServiceTimeAttributeDto;
import com.compucar.dto.ServiceSummaryDto;
import com.compucar.dto.ServiceReportDto;
import com.compucar.dto.ServiceUsageAttributeDto;
import com.compucar.model.ServiceExecution;
import com.compucar.service.exceptions.RequiredFieldMissingException;
import org.junit.After;
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

    private LocalDate paramDate = LocalDate.of(2019,7,30);
    private LocalDateTime expectedStartDate = LocalDateTime.of(2019, 7,
            30, 0, 0, 0);
    private LocalDateTime expectedEndDate = LocalDateTime.of(2019, 7,
            30, 23, 59, 59);

    @Before
    public void init() {
        this.dao = mock(ServiceExecutionDao.class);
    }

    @Test
    public void getUsageReportOkTest() throws RequiredFieldMissingException {
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

        Long fastestTime = 4L;
        ServiceTimeAttributeDto fastestService = result.getFastestService();
        Assert.assertEquals(SERVICE_A, fastestService.getServiceName());
        Assert.assertEquals(fastestTime, fastestService.getTimeValue());

        Long slowestTime = 500L;
        ServiceTimeAttributeDto slowestService = result.getSlowestService();
        Assert.assertEquals(SERVICE_B, slowestService.getServiceName());
        Assert.assertEquals(slowestTime, slowestService.getTimeValue());

        ServiceUsageAttributeDto mostUsedService = result.getMostUsedService();
        Assert.assertEquals(SERVICE_B, mostUsedService.getServiceName());
        Assert.assertEquals(3, mostUsedService.getUsageCount());

        ServiceUsageAttributeDto lessUsedService = result.getLessUsedService();
        Assert.assertEquals(SERVICE_A, lessUsedService.getServiceName());
        Assert.assertEquals(2, lessUsedService.getUsageCount());

        verify(dao, times(1)).findByRegisterDateBetween(expectedStartDate, expectedEndDate);
    }

    @Test
    public void getUsageReportEmptyListTest() throws RequiredFieldMissingException {
        when(dao.findByRegisterDateBetween(expectedStartDate,expectedEndDate))
                .thenReturn(new ArrayList<>());
        ServiceExecutionService service = new ServiceExecutionServiceImp(dao);
        ServiceReportDto result = service.getUsageReport(paramDate);
        Assert.assertTrue(result.getServiceSummary().isEmpty());
        Assert.assertNull(result.getFastestService());
        Assert.assertNull(result.getSlowestService());
        Assert.assertNull(result.getMostUsedService());
        Assert.assertNull(result.getLessUsedService());

        verify(dao, times(1)).findByRegisterDateBetween(expectedStartDate, expectedEndDate);
    }

    @Test(expected = RequiredFieldMissingException.class)
    public void getUsageReportNullDateTest() throws RequiredFieldMissingException {
        ServiceExecutionService service = new ServiceExecutionServiceImp(dao);
        service.getUsageReport(null);
    }

    private List<ServiceExecution> createListServiceExecutions() {
        List<ServiceExecution> result = new ArrayList<>();
        result.add(new ServiceExecutionBuilder()
                .serviceName(SERVICE_A)
                .executionTime(5L)
                .build());

        result.add(new ServiceExecutionBuilder()
                .serviceName(SERVICE_A)
                .executionTime(4L)
                .build());

        result.add(new ServiceExecutionBuilder()
                .serviceName(SERVICE_B)
                .executionTime(300L)
                .build());

        result.add(new ServiceExecutionBuilder()
                .serviceName(SERVICE_B)
                .executionTime(100L)
                .build());

        result.add(new ServiceExecutionBuilder()
                .serviceName("ServiceB")
                .executionTime(500L)
                .build());

        return result;
    }

    @After
    public void afterTest() {
        verifyNoMoreInteractions(dao);
    }
}
