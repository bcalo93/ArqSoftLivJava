package com.compucar.service;

import com.compucar.builder.ClientBuilder;
import com.compucar.dao.ClientDao;
import com.compucar.model.Client;
import com.compucar.model.ClientType;
import com.compucar.service.exceptions.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ClientServiceImpTest {

    private ClientDao dao;

    @Before
    public void initTest() {
        this.dao = mock(ClientDao.class);
    }

    @Test
    public void addClientOkTest() throws EntityNullException, DuplicateElementException, RequiredFieldMissingException {
        Long expectedId = 20L;
        when(dao.findByNumber(anyInt())).thenReturn(Optional.empty());
        when(dao.save(isA(Client.class))).thenReturn(
                new ClientBuilder()
                        .id(expectedId)
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
        Assert.assertEquals(expectedId, result.getId());
        Assert.assertEquals("Test Client 1", result.getName());
        Assert.assertEquals("email@email.com", result.getEmail());
        Assert.assertEquals(Integer.valueOf(30), result.getNumber());
        Assert.assertEquals("200003132", result.getPhone());
        Assert.assertEquals(ClientType.COMPANY, result.getType());

        verify(dao, times(1)).findByNumber(30);
        verify(dao, times(1)).save(isA(Client.class));
    }

    @Test(expected = EntityNullException.class)
    public void addClientNullTest() throws EntityNullException, DuplicateElementException, RequiredFieldMissingException {
        ClientService service = new ClientServiceImp(dao);
        service.addClient(null);
    }

    @Test
    public void addClientDuplicateTest() throws EntityNullException {
        boolean exceptionThrown = false;
        int existingNumber = 35;
        when(dao.findByNumber(existingNumber)).thenReturn(
                Optional.of(
                        new ClientBuilder()
                                .name("Test Client Exist")
                                .email("exist@email.com")
                                .number(existingNumber)
                                .phone("200003732")
                                .type(ClientType.PERSON)
                                .build()
                )

        );

        ClientService service = new ClientServiceImp(dao);
        try {
            service.addClient(new ClientBuilder()
                    .name("Test Client 1")
                    .email("email@email.com")
                    .number(existingNumber)
                    .phone("200003132")
                    .type(ClientType.COMPANY)
                    .build()
            );
        } catch (DuplicateElementException | RequiredFieldMissingException de) {
            Assert.assertEquals(String.format("Client with number %s already exists.", existingNumber),
                    de.getMessage());
            exceptionThrown = true;
        }

        Assert.assertTrue(exceptionThrown);
        verify(dao, times(1)).findByNumber(existingNumber);
    }

    @Test(expected = RequiredFieldMissingException.class)
    public void addClientNullNumberTest() throws RequiredFieldMissingException, EntityNullException, DuplicateElementException {
        ClientService service = new ClientServiceImp(dao);
        service.addClient(new ClientBuilder()
                .name("Test Client 1")
                .email("email@email.com")
                .phone("200003132")
                .type(ClientType.COMPANY)
                .build()
        );
    }

    @Test
    public void updateClientOkTest() throws IdNullException, NotFoundException, EntityNullException {
        Long expectedId = 32L;
        when(dao.findOne(expectedId)).thenReturn(new ClientBuilder()
                .id(expectedId)
                .name("Test Client Update")
                .email("email@email.com")
                .number(30)
                .phone("200003132")
                .type(ClientType.COMPANY)
                .build()
        );

        when(dao.save(isA(Client.class))).thenReturn(new ClientBuilder()
                .id(expectedId)
                .name("New Client Name")
                .email("newemial@email.com")
                .number(30)
                .phone("1234567890")
                .type(ClientType.PERSON)
                .build()
        );

        ClientService service = new ClientServiceImp(dao);
        Client result = service.updateClient(expectedId, new ClientBuilder()
                .name("New Client Name")
                .email("newemial@email.com")
                .number(0)
                .phone("1234567890")
                .type(ClientType.PERSON)
                .build()
        );

        Assert.assertEquals(expectedId, result.getId());
        Assert.assertEquals("New Client Name", result.getName());
        Assert.assertEquals("newemial@email.com", result.getEmail());
        Assert.assertEquals(Integer.valueOf(30), result.getNumber());
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
        } catch (NotFoundException ne) {
            Assert.assertEquals("Client with id 100 was not found.", ne.getMessage());
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);

        verify(dao, times(1)).findOne(100L);
        verify(dao, times(0)).save(isA(Client.class));
    }

    @Test(expected = EntityNullException.class)
    public void updateClientNullTest() throws EntityNullException, NotFoundException, IdNullException {
        ClientService service = new ClientServiceImp(dao);
        service.updateClient(10L, null);
    }

    @Test(expected = IdNullException.class)
    public void updateIdNullTest() throws EntityNullException, NotFoundException, IdNullException {
        ClientService service = new ClientServiceImp(dao);
        service.updateClient(null, new Client());
    }

    @Test
    public void deleteClientOkTest() throws IdNullException {
        Long expectedId = 12L;
        Client toDelete = new ClientBuilder()
                .id(expectedId)
                .name("To Delete")
                .email("delete@email.com")
                .number(123)
                .phone("1234567890")
                .type(ClientType.COMPANY)
                .build();
        when(dao.findOne(expectedId)).thenReturn(toDelete);
        doNothing().when(dao).delete(toDelete);

        ClientService service = new ClientServiceImp(dao);
        service.deleteClient(expectedId);

        verify(dao, times(1)).findOne(expectedId);
        verify(dao, times(1)).delete(toDelete);
    }

    @Test
    public void deleteClientNotExistTest() throws IdNullException {
        Long expectedId = 10L;
        when(dao.findOne(expectedId)).thenReturn(null);

        ClientService service = new ClientServiceImp(dao);
        service.deleteClient(expectedId);

        verify(dao, times(1)).findOne(expectedId);
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
    public void getClientOkTest() throws NotFoundException, IdNullException {
        Long expectedId = 50L;
        when(dao.findOne(expectedId)).thenReturn(new ClientBuilder()
                .id(expectedId)
                .name("Test Client Get")
                .email("get@email.com")
                .number(20)
                .phone("987654321")
                .type(ClientType.PERSON)
                .build()
        );

        ClientService service = new ClientServiceImp(dao);
        Client result = service.getClient(expectedId);

        Assert.assertEquals(expectedId, result.getId());
        Assert.assertEquals("Test Client Get", result.getName());
        Assert.assertEquals("get@email.com", result.getEmail());
        Assert.assertEquals(Integer.valueOf(20), result.getNumber());
        Assert.assertEquals("987654321", result.getPhone());
        Assert.assertEquals(ClientType.PERSON, result.getType());

        verify(dao, times(1)).findOne(50L);
    }

    @Test
    public void getClientIdNotExistTest() throws IdNullException {
        boolean exceptionThrown = false;
        when(dao.findOne(20L)).thenReturn(null);
        ClientService service = new ClientServiceImp(dao);
        try {
            service.getClient(20L);

        } catch (NotFoundException ne) {
            Assert.assertEquals("Client with id 20 was not found.", ne.getMessage());
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);
        verify(dao, times(1)).findOne(20L);
    }

    @Test(expected = IdNullException.class)
    public void getClientIdNullTest() throws NotFoundException, IdNullException {
        ClientService service = new ClientServiceImp(dao);
        service.getClient(null);
    }

    @After
    public void afterTest() {
        verifyNoMoreInteractions(dao);
    }

}
