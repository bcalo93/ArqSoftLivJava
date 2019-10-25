package com.compucar.service;

import com.compucar.builder.OperationLogBuilder;
import com.compucar.dao.OperationLogDao;
import com.compucar.model.OperationLog;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class OperationLogServiceImpTest {
    private static final String SERVICE_TEST = "Service_Test";
    private static final String USER_STRING = "User";

    private OperationLogDao dao;

    @Before
    public void init() {
        this.dao = mock(OperationLogDao.class);
    }

    @Test
    public void addServiceExecutionOkTest() {
        when(dao.save(isA(OperationLog.class))).thenReturn(isA(OperationLog.class));
        OperationLogService service = new OperationLogServiceImp(dao);

        OperationLog toAdd = new OperationLogBuilder()
                .serviceName(SERVICE_TEST)
                .registerDate(LocalDateTime.of(2019, 3,20,19,0))
                .username("User Test")
                .build();

        service.addOperationLog(toAdd);

        verify(dao, times(1)).save(toAdd);
    }

    @Test
    public void addServiceExecutionRequiredFieldMissingTest() {
        OperationLogService service = new OperationLogServiceImp(dao);
        service.addOperationLog(new OperationLogBuilder()
                .serviceName(SERVICE_TEST)
                .registerDate(LocalDateTime.of(2019, 3,20,19,0))
                .build()
        );
    }

    @Test
    public void getAllOperationLogsOkTest() {
        when(dao.findByOrderByRegisterDateDesc()).thenReturn(createOperationLogs());
        OperationLogService service = new OperationLogServiceImp(dao);

        List<OperationLog> result = service.getAllOperationLogs();
        Assert.assertEquals(20, result.size());
        for(OperationLog log : result) {
            Assert.assertTrue(log.getServiceName().contains(SERVICE_TEST));
            Assert.assertTrue(log.getUsername().contains(USER_STRING));
        }

        verify(dao, times(1)).findByOrderByRegisterDateDesc();
    }

    private List<OperationLog> createOperationLogs() {
        ArrayList<OperationLog> result = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            result.add(new OperationLogBuilder()
                    .registerDate(LocalDateTime.of(2019, 3,i,19,0))
                    .serviceName(SERVICE_TEST + i)
                    .username(USER_STRING + i)
                    .build()
            );
        }
        return result;
    }

    @After
    public void afterTest() {
        verifyNoMoreInteractions(dao);
    }

}
