package com.compucar.logic.test;

import com.compucar.builder.ClientBuilder;
import com.compucar.dao.ClientDao;
import com.compucar.exception.EntityNullException;
import com.compucar.logic.ClientLogic;
import com.compucar.logic.ClientLogicImp;
import com.compucar.model.Client;
import com.compucar.model.ClientType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ClientLogicImpTest {

    private ClientDao dao;

    @Before
    public void initTest() {
        this.dao = mock(ClientDao.class);
    }

    @Test
    public void addClientOkTest() throws EntityNullException {
        when(dao.save(isA(Client.class))).thenReturn(
                new ClientBuilder()
                        .id(20L)
                        .name("Test Client 1")
                        .email("email@email.com")
                        .number(30)
                        .phone("200003132")
                        .type(ClientType.COMPANY)
                        .build()
        );
        ClientLogic logic = new ClientLogicImp(dao);

        Client toAdd = new ClientBuilder()
                .name("Test Client 1")
                .email("email@email.com")
                .number(30)
                .phone("200003132")
                .type(ClientType.COMPANY)
                .build();

        Client result = logic.addClient(toAdd);
        Assert.assertEquals(20L, result.getId());
        Assert.assertEquals("Test Client 1", result.getName());
        Assert.assertEquals("email@email.com", result.getEmail());
        Assert.assertEquals(30, result.getNumber());
        Assert.assertEquals("200003132", result.getPhone());
        Assert.assertEquals(ClientType.COMPANY, result.getType());

        verify(dao, times(1)).save(isA(Client.class));
    }

    @Test(expected = EntityNullException.class)
    public void addClientNullTest() throws EntityNullException {

        ClientLogic logic = new ClientLogicImp(dao);
        logic.addClient(null);
    }

    @After
    public void afterTest() {
        verifyNoMoreInteractions(dao);
    }

}
