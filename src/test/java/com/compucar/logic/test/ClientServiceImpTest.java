package com.compucar.logic.test;

import com.compucar.builder.ClientBuilder;
import com.compucar.dao.ClientDao;
import com.compucar.exception.EntityNotExistException;
import com.compucar.exception.EntityNullException;
import com.compucar.exception.IdNullException;
import com.compucar.service.ClientService;
import com.compucar.service.ClientServiceImp;
import com.compucar.model.Client;
import com.compucar.model.ClientType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ClientServiceImpTest {

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
        ClientService service = new ClientServiceImp(dao);

        Client toAdd = new ClientBuilder()
                .name("Test Client 1")
                .email("email@email.com")
                .number(30)
                .phone("200003132")
                .type(ClientType.COMPANY)
                .build();

        Client result = service.addClient(toAdd);
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
        ClientService service = new ClientServiceImp(dao);
        service.addClient(null);
    }

    @Test
    public void updateClientOkTest() throws IdNullException, EntityNotExistException, EntityNullException {
        when(dao.findOne(32L)).thenReturn(new ClientBuilder()
                .id(32L)
                .name("Test Client Update")
                .email("email@email.com")
                .number(30)
                .phone("200003132")
                .type(ClientType.COMPANY)
                .build()
        );

        when(dao.save(isA(Client.class))).thenReturn(new ClientBuilder()
                .id(32L)
                .name("New Client Name")
                .email("newemial@email.com")
                .number(30)
                .phone("1234567890")
                .type(ClientType.PERSON)
                .build()
        );

        ClientService service = new ClientServiceImp(dao);
        Client result = service.updateClient(32L, new ClientBuilder()
                .name("New Client Name")
                .email("newemial@email.com")
                .number(0)
                .phone("1234567890")
                .type(ClientType.PERSON)
                .build()
        );

        Assert.assertEquals(32L, result.getId());
        Assert.assertEquals("New Client Name", result.getName());
        Assert.assertEquals("newemial@email.com", result.getEmail());
        Assert.assertEquals(30, result.getNumber());
        Assert.assertEquals("1234567890", result.getPhone());
        Assert.assertEquals(ClientType.PERSON, result.getType());

        verify(dao, times(1)).findOne(32L);
        verify(dao, times(1)).save(isA(Client.class));
    }

    @Test
    public void updateClientIdNotFoundTest() throws IdNullException, EntityNullException {
        boolean exceptionThrown = false;
        when(dao.findOne(100L)).thenReturn(null);
        ClientService service = new ClientServiceImp(dao);
        try {
            service.updateClient(100L, new Client());
        } catch(EntityNotExistException ne) {
            Assert.assertEquals("El cliente con id 100 no existe.", ne.getMessage());
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);

        verify(dao, times(1)).findOne(100L);
        verify(dao, times(0)).save(isA(Client.class));
    }

    @Test(expected = EntityNullException.class)
    public void updateClientNullTest() throws EntityNullException, EntityNotExistException, IdNullException {
        ClientService service = new ClientServiceImp(dao);
        service.updateClient(10L, null);
    }

    @Test(expected = IdNullException.class)
    public void updateIdNullTest() throws EntityNullException, EntityNotExistException, IdNullException {
        ClientService service = new ClientServiceImp(dao);
        service.updateClient(null, new Client());
    }

    @Test
    public void deleteClientOkTest() throws IdNullException {
        doNothing().when(dao).delete(12L);
        ClientService service = new ClientServiceImp(dao);
        service.deleteClient(12L);
        verify(dao, times(1)).delete(12L);
    }

    @Test(expected = IdNullException.class)
    public void deleteClientNullIdTest() throws IdNullException {
        ClientService service = new ClientServiceImp(dao);
        service.deleteClient(null);
    }

    @Test
    public void getAllClientsOkTest() {
        ArrayList<Client> mockList = new ArrayList<Client>();
        for (int i = 0; i < 10; i++) {
            mockList.add(new ClientBuilder()
                    .id((long) i)
                    .name(String.format("Client %s", i))
                    .email(String.format("newemial%s@email.com", i))
                    .number(i)
                    .phone(String.format("1234567890%s", i))
                    .type(ClientType.PERSON)
                    .build());
        }
        when(dao.findAll()).thenReturn(mockList);

        ClientService service = new ClientServiceImp(dao);
        List<Client> result = service.getAllClients();

        Assert.assertEquals(10, result.size());
        Assert.assertEquals(mockList, result);

        verify(dao, times(1)).findAll();
    }

    @Test
    public void getClientOkTest() throws EntityNotExistException, IdNullException {
        when(dao.findOne(50L)).thenReturn(new ClientBuilder()
                .id(50L)
                .name("Test Client Get")
                .email("get@email.com")
                .number(20)
                .phone("987654321")
                .type(ClientType.PERSON)
                .build()
        );

        ClientService service = new ClientServiceImp(dao);
        Client result = service.getClient(50L);

        Assert.assertEquals(50L, result.getId());
        Assert.assertEquals("Test Client Get", result.getName());
        Assert.assertEquals("get@email.com", result.getEmail());
        Assert.assertEquals(20, result.getNumber());
        Assert.assertEquals("987654321", result.getPhone());
        Assert.assertEquals(ClientType.PERSON, result.getType());

        verify(dao, times(1)).findOne(50L);
    }

    @Test
    public void getClientIdNotExistTest() {
        boolean exceptionThrown = false;
        when(dao.findOne(20L)).thenReturn(null);
        ClientService service = new ClientServiceImp(dao);
        try {
            service.getClient(20L);

        } catch(EntityNotExistException | IdNullException ne) {
            Assert.assertEquals("A client with id 20 doesn't exist", ne.getMessage());
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);
        verify(dao, times(1)).findOne(20L);
    }

    @Test(expected = IdNullException.class)
    public void getClientIdNullTest() throws EntityNotExistException, IdNullException {
        ClientService service = new ClientServiceImp(dao);
        service.getClient(null);
    }

    @After
    public void afterTest() {
        verifyNoMoreInteractions(dao);
    }

}
